package com.example.PharmaPhorm.negocio;


import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.caixa.Caixa;
import com.example.PharmaPhorm.caixa.CaixaRepository;
import com.example.PharmaPhorm.caixa.Exceptions.SaldoInsuficienteException;
import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.funcionario.FuncionarioRepository;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.itemnegocio.ItemNegocioRepository;
import com.example.PharmaPhorm.negocio.Exceptions.NegocioNotFoundException;
import com.example.PharmaPhorm.produto.Produto;
import com.example.PharmaPhorm.produto.ProdutoRepository;
import com.example.PharmaPhorm.transportadora.Transportadora;
import com.example.PharmaPhorm.transportadora.TransportadoraRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.PharmaPhorm.negocio.NegocioRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/negocio") // Mapeamento base para todos os endpoints da classe
@CrossOrigin(origins = "*")
public class NegocioController {

    private final NegocioRepository negocioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ProdutoRepository produtoRepository;
    private final TransportadoraRepository transportadoraRepository;
    private final CaixaRepository caixaRepository;

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
        // 1. Cria uma nova instância de Negocio
        Negocio novoNegocio = new Negocio();
        novoNegocio.setTipo(request.getTipo());
        novoNegocio.setStatus(Status.ABERTO);

        // 2. Busca e associa a transportadora
        Transportadora transportadora = transportadoraRepository.findById(request.getTransportadoraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora não encontrada"));
        novoNegocio.setTransportadora(transportadora);

        // 3. Busca e associa os funcionários participantes
        Set<Funcionario> participantes = new HashSet<>(funcionarioRepository.findAllById(request.getParticipanteIds()));
        if(participantes.size() != request.getParticipanteIds().size()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Um ou mais funcionários não foram encontrados.");
        }
        novoNegocio.setParticipantes(participantes);

        // 4. Cria e associa os itens do negócio
        Set<ItemNegocio> itens = request.getItems().stream().map(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + itemDto.getProdutoId()));

            // Lógica de estoque para venda na criação
            if (novoNegocio.getTipo() == Tipo.VENDA) {
                if (produto.getQuantidadeEstoque() < itemDto.getQuantidade()) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estoque insuficiente para o produto: " + produto.getNome());
                }
                produto.diminuirEstoque(itemDto.getQuantidade());
                produtoRepository.save(produto);
            }

            return new ItemNegocio(produto, novoNegocio, itemDto.getQuantidade());
        }).collect(Collectors.toSet());
        novoNegocio.setItemsNegocio(itens);

        // 5. Salva o negócio (com Cascade, salvará os itens juntos)
        Negocio negocioSalvo = negocioRepository.save(novoNegocio);

        // 6. Atualiza o lado inverso da relação Many-to-Many nos funcionários
        for (Funcionario f : participantes) {
            f.getNegociosParticipantes().add(negocioSalvo);
        }
        funcionarioRepository.saveAll(participantes);

        return negocioSalvo;
    }

// Em NegocioController.java

    @PutMapping("/{id}")
    @Transactional
    public Negocio atualizarNegocio(@PathVariable Long id, @RequestBody NegocioRequestDTO request) {
        // 1. Busca o negócio existente que será atualizado
        Negocio negocioExistente = buscarPorId(id);

        // 2. Atualiza os dados simples do negócio
        negocioExistente.setTipo(request.getTipo());
        Transportadora transportadora = transportadoraRepository.findById(request.getTransportadoraId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora não encontrada"));
        negocioExistente.setTransportadora(transportadora);

        // --- LÓGICA CORRETA PARA SINCRONIZAR FUNCIONÁRIOS ---

        // 3. Pega a lista de funcionários que já estavam no negócio
        Set<Funcionario> participantesAtuais = negocioExistente.getParticipantes();

        // 4. Busca as entidades completas dos novos funcionários a partir dos IDs enviados
        Set<Funcionario> novosParticipantes = new HashSet<>(funcionarioRepository.findAllById(request.getParticipanteIds()));

        // 5. REMOVER: Itera sobre os funcionários atuais. Se um deles não está na nova lista, remove a associação.
        // Usamos new HashSet<>(...) para evitar ConcurrentModificationException ao modificar a lista durante a iteração.
        for (Funcionario participanteAtual : new HashSet<>(participantesAtuais)) {
            if (!novosParticipantes.contains(participanteAtual)) {
                // Remove o negócio da lista de participações do funcionário
                participanteAtual.getNegociosParticipantes().remove(negocioExistente);
        //diminuir o estoque, caso seja venda
        if(request.getNegocio().getTipo().equals(Tipo.VENDA)) {
//            for (ItemNegocio item : request.getNegocio().getItemsNegocio()) {
//                //caso o item ja exista, sera alterado a quantidade de estoque.
//                Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));
//                produtoNegociado.diminuirEstoque(item.getQuantidade());
//
//            }
            for (int i = 0; i < request.getIdProdutos().size(); i++) {
                Long produtoId = request.getIdProdutos().get(i);
                int quantidade = request.getQuantidades().get(i);

                Produto produtoNegociado = produtoRepository.findById(produtoId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));

                produtoNegociado.diminuirEstoque(quantidade);
                produtoRepository.save(produtoNegociado);
            }
        }

        // 6. ADICIONAR: Itera sobre os novos funcionários. Se um deles não estava na lista antiga, adiciona a associação.
        for (Funcionario novoParticipante : novosParticipantes) {
            if (!participantesAtuais.contains(novoParticipante)) {
                // Adiciona o negócio na lista de participações do funcionário
                novoParticipante.getNegociosParticipantes().add(negocioExistente);
            }
        }

        // 7. Atualiza a lista de participantes no próprio negócio
        negocioExistente.setParticipantes(novosParticipantes);

        // --- LÓGICA CORRETA PARA ATUALIZAR ITENS DO NEGÓCIO ---

        // 8. Limpa a lista antiga de itens. Graças ao 'orphanRemoval=true', o JPA vai deletar os registros antigos do banco.
        negocioExistente.getItemsNegocio().clear();

        // 9. Cria o novo conjunto de itens a partir dos dados da requisição
        Set<ItemNegocio> novosItens = request.getItems().stream().map(itemDto -> {
            Produto produto = produtoRepository.findById(itemDto.getProdutoId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + itemDto.getProdutoId()));
            // Cria um novo ItemNegocio já associado ao negócio existente
            return new ItemNegocio(produto, negocioExistente, itemDto.getQuantidade());
        }).collect(Collectors.toSet());

        // 10. Adiciona todos os novos itens à lista do negócio
        negocioExistente.getItemsNegocio().addAll(novosItens);

        // 11. Salva o negócio. A transação garantirá que todas as alterações (no negócio, nos funcionários e nos itens) sejam persistidas.
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
                produtoRepository.save(p);
            }
        } else { // VENDA
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                double valorTotalVenda = item.getProduto().getValorVenda() * item.getQuantidade();
                caixa.adicionarValor(valorTotalVenda);
            }
        }

        negocio.setStatus(Status.CONCLUIDO);
        caixaRepository.save(caixa);
        return negocioRepository.save(negocio);
    }

    @PutMapping("/cancelar/{id}")
    @Transactional
    public Negocio cancelarNegocio(@PathVariable Long id) {
        Negocio negocio = buscarPorId(id);
        if (negocio.getStatus() != Status.ABERTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este negócio não pode ser cancelado.");
        }

        // Devolve produtos ao estoque se for uma VENDA cancelada
        if (negocio.getTipo() == Tipo.VENDA) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                Produto produto = item.getProduto();
                produto.adicionarEstoque(item.getQuantidade());
                produtoRepository.save(produto);
            }
        }

        negocio.setStatus(Status.CANCELADO);
        return negocioRepository.save(negocio);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletarNegocio(@PathVariable Long id) {
        Negocio negocio = buscarPorId(id);

        // Remove a associação dos funcionários antes de deletar
        for (Funcionario participante : negocio.getParticipantes()) {
            participante.getNegociosParticipantes().remove(negocio);
        }
        funcionarioRepository.saveAll(negocio.getParticipantes());

        // orphanRemoval=true na entidade Negocio cuidará de deletar os ItemNegocio
        negocioRepository.delete(negocio);
    }
}
