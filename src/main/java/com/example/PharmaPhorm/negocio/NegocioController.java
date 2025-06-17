package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.caixa.Caixa;
import com.example.PharmaPhorm.caixa.CaixaRepository;
import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.funcionario.FuncionarioRepository;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.negocio.Exceptions.NegocioNotFoundException;
import com.example.PharmaPhorm.produto.Produto;
import com.example.PharmaPhorm.produto.ProdutoRepository;
import com.example.PharmaPhorm.transportadora.Transportadora;
import com.example.PharmaPhorm.transportadora.TransportadoraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/negocio")
@CrossOrigin(origins = "*")
public class NegocioController {

    // CAMPOS (VARIÁVEIS) DA CLASSE - SEM DUPLICATAS
    private final NegocioRepository negocioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final TransportadoraRepository transportadoraRepository;
    private final CaixaRepository caixaRepository;

    // CONSTRUTOR ÚNICO E CORRETO
    public NegocioController(NegocioRepository negocioRepository,
                             FuncionarioRepository funcionarioRepository,
                             ProdutoRepository produtoRepository,
                             TransportadoraRepository transportadoraRepository,
                             CaixaRepository caixaRepository) {
        this.negocioRepository = negocioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.produtoRepository = produtoRepository;
        this.transportadoraRepository = transportadoraRepository;
        this.caixaRepository = caixaRepository;
    }

    @GetMapping
    public List<Negocio> listarTodos() {
        return negocioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Negocio buscarPorId(@PathVariable Long id) {
        return negocioRepository.findById(id)
                .orElseThrow(() -> new NegocioNotFoundException(id));
    }

    @PostMapping
    @Transactional
    public Negocio criarNegocio(@RequestBody NegocioRequestDTO request) {
        Negocio novoNegocio = new Negocio();
        novoNegocio.setTipo(request.getTipo());
        novoNegocio.setStatus(Status.ABERTO);

        Transportadora transportadora = transportadoraRepository.findById(request.getTransportadoraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora não encontrada"));
        novoNegocio.setTransportadora(transportadora);

        Set<Funcionario> participantes = new HashSet<>(funcionarioRepository.findAllById(request.getParticipanteIds()));
        if (participantes.size() != request.getParticipanteIds().size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Um ou mais funcionários não foram encontrados.");
        }
        novoNegocio.setParticipantes(participantes);

        Set<ItemNegocio> itens = request.getItems().stream().map(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + itemDto.getProdutoId()));

            if (novoNegocio.getTipo() == Tipo.VENDA) {
                if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente para o produto: " + produto.getNome());
                }
                produto.diminuirEstoque(itemDto.getQuantidade());
            }
            return new ItemNegocio(produto, novoNegocio, itemDto.getQuantidade());
        }).collect(Collectors.toSet());
        novoNegocio.setItemsNegocio(itens);

        Negocio negocioSalvo = negocioRepository.save(novoNegocio);

        for (Funcionario f : participantes) {
            f.getNegociosParticipantes().add(negocioSalvo);
        }
        funcionarioRepository.saveAll(participantes);

        return negocioSalvo;
    }

    @PutMapping("/{id}")
    @Transactional
    public Negocio atualizarNegocio(@PathVariable Long id, @RequestBody NegocioRequestDTO request) {
        Negocio negocioExistente = buscarPorId(id);

        // Atualiza campos simples
        negocioExistente.setTipo(request.getTipo());
        Transportadora transportadora = transportadoraRepository.findById(request.getTransportadoraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora não encontrada"));
        negocioExistente.setTransportadora(transportadora);

        // --- LÓGICA PARA SINCRONIZAR FUNCIONÁRIOS ---
        Set<Funcionario> participantesAtuais = negocioExistente.getParticipantes();
        Set<Funcionario> novosParticipantes = new HashSet<>(funcionarioRepository.findAllById(request.getParticipanteIds()));

        participantesAtuais.stream()
                .filter(p -> !novosParticipantes.contains(p))
                .forEach(p -> p.getNegociosParticipantes().remove(negocioExistente));

        novosParticipantes.stream()
                .filter(p -> !participantesAtuais.contains(p))
                .forEach(p -> p.getNegociosParticipantes().add(negocioExistente));

        negocioExistente.setParticipantes(novosParticipantes);

        // --- LÓGICA PARA ATUALIZAR ITENS (INCLUINDO ESTOQUE) ---
        // 1. Devolve ao estoque os itens que estão sendo removidos (se for venda)
        if (negocioExistente.getTipo() == Tipo.VENDA) {
            negocioExistente.getItemsNegocio().forEach(itemExistente -> {
                Produto p = itemExistente.getProduto();
                p.adicionarEstoque(itemExistente.getQuantidade());
            });
        }

        // 2. Limpa a lista antiga de itens
        negocioExistente.getItemsNegocio().clear();

        // 3. Cria o novo conjunto de itens
        Set<ItemNegocio> novosItens = request.getItems().stream().map(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + itemDto.getProdutoId()));

            // 4. Diminui o estoque para os novos itens (se for venda)
            if (negocioExistente.getTipo() == Tipo.VENDA) {
                if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente para o produto: " + produto.getNome());
                }
                produto.diminuirEstoque(itemDto.getQuantidade());
            }
            return new ItemNegocio(produto, negocioExistente, itemDto.getQuantidade());
        }).collect(Collectors.toSet());

        negocioExistente.getItemsNegocio().addAll(novosItens);

        return negocioRepository.save(negocioExistente);
    }

    @PutMapping("/concluir/{id}")
    @Transactional
    public Negocio concluirNegocio(@PathVariable Long id) {
        Negocio negocio = buscarPorId(id);
        if (negocio.getStatus() != Status.ABERTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este negócio não pode ser concluído.");
        }

        Caixa caixa = caixaRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Caixa não configurado."));

        if (negocio.getTipo() == Tipo.COMPRA) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                double valorTotalCompra = item.getProduto().getValorCompra() * item.getQuantidade();
                caixa.removerValor(valorTotalCompra);
                Produto p = item.getProduto();
                p.adicionarEstoque(item.getQuantidade());
            }
        } else { // VENDA
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                double valorTotalVenda = item.getProduto().getValorVenda() * item.getQuantidade();
                caixa.adicionarValor(valorTotalVenda);
            }
        }

        negocio.setStatus(Status.CONCLUIDO);
        return negocioRepository.save(negocio);
    }

    @PutMapping("/cancelar/{id}")
    @Transactional
    public Negocio cancelarNegocio(@PathVariable Long id) {
        Negocio negocio = buscarPorId(id);
        if (negocio.getStatus() != Status.ABERTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este negócio não pode ser cancelado.");
        }

        // Devolve produtos ao estoque se for uma VENDA que estava aberta
        if (negocio.getTipo() == Tipo.VENDA) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                Produto produto = item.getProduto();
                produto.adicionarEstoque(item.getQuantidade());
            }
        }

        negocio.setStatus(Status.CANCELADO);
        return negocioRepository.save(negocio);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletarNegocio(@PathVariable Long id) {
        Negocio negocio = buscarPorId(id);

        // Devolve ao estoque se estiver deletando uma venda que estava aberta
        if (negocio.getTipo() == Tipo.VENDA && negocio.getStatus() == Status.ABERTO) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                item.getProduto().adicionarEstoque(item.getQuantidade());
            }
        }

        for (Funcionario participante : negocio.getParticipantes()) {
            participante.getNegociosParticipantes().remove(negocio);
        }

        negocioRepository.delete(negocio);
    }
}