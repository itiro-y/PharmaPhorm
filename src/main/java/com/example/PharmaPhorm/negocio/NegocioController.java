package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.caixa.Caixa;
import com.example.PharmaPhorm.caixa.CaixaRepository;
import com.example.PharmaPhorm.caixa.Exceptions.SaldoInsuficienteException;
import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.funcionario.FuncionarioRepository;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class NegocioController {

    // ✅ CAMPOS (VARIÁVEIS) REORGANIZADOS PARA O TOPO DA CLASSE
    private final NegocioRepository repository;
    private final ProdutoRepository produtoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ItemNegocioRepository itemNegocioRepository;

    //Injeção de dependência da classe Repository
    private final CaixaRepository caixaRepository;
    private final ItemNegocioRepository itemNegocioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final TransportadoraRepository transportadoraRepository;

    // ✅ CONSTRUTOR ÚNICO E CORRETO
    public NegocioController(NegocioRepository negocioRepository,
                             ProdutoRepository produtoRepository,
                             CaixaRepository caixaRepository,
                             ItemNegocioRepository itemNegocioRepository,
                             FuncionarioRepository funcionarioRepository) {
    //Injeção de dependência da classe Repository
    NegocioController(NegocioRepository negocioRepository, CaixaRepository caixaRepository,
                      ProdutoRepository produtoRepository,
                      ItemNegocioRepository itemNegocioRepository,
                      FuncionarioRepository funcionarioRepository,
                      TransportadoraRepository transportadoraRepository) {
        this.repository = negocioRepository;
        this.produtoRepository = produtoRepository;
        this.caixaRepository = caixaRepository;
        this.itemNegocioRepository = itemNegocioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.transportadoraRepository = transportadoraRepository;
        this.itemNegocioRepository = itemNegocioRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping("/negocio")
    public List<Negocio> getNegocio() {
        return repository.findAll();
    }

    @GetMapping("/negocio/{id}")
    public Negocio getNegociosByID(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
    }

    @PostMapping("/negocio")
    Negocio addNegocio(@RequestBody NegocioRequestDTO request) {
        Transportadora t = transportadoraRepository.findById(request.getNegocio().getTransportadora().getId())
                .orElseThrow(() -> new RuntimeException("transportadora nao encontrada"));
        request.getNegocio().setTransportadora(t);

        // Fetch and set the participantes
        Set<Funcionario> funcionarios = request.getNegocio().getParticipantes().stream()
                .map(f -> funcionarioRepository.findById(f.getId())
                        .orElseThrow(() -> new RuntimeException("Funcionario não encontrado: " + f.getId())))
                .collect(Collectors.toSet());
        request.getNegocio().setParticipantes(funcionarios);

        /*
        // Fetch and set the itemsNegocio
        List<ItemNegocio> items = negocio.getItemsNegocio().stream()
                .map(item -> itemNegocioRepository.findById(item.getId())
                        .orElseThrow(() -> new RuntimeException("ItemNegocio não encontrado: " + item.getId())))
                .collect(Collectors.toList());
<<<<<<< kevin
        */

        List<ItemNegocio> items = new ArrayList<>();
        for(int i=0; i<request.getQuantidades().size(); i++){
            Produto p = produtoRepository.findById(request.getIdProdutos().get(i)).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado")
            );
            ItemNegocio item = new ItemNegocio(p, null, request.getQuantidades().get(i));
            itemNegocioRepository.save(item);
            items.add(item);
        }
        request.getNegocio().setItemsNegocio(new HashSet<>(items));

        repository.save(request.getNegocio());

        for(Funcionario f : funcionarios){
            f.addNegociosParticipantes(request.getNegocio());
            funcionarioRepository.save(f);
        }


        for(ItemNegocio i : items){
            i.addNegocio(request.getNegocio());
            itemNegocioRepository.save(i);

            Produto p = produtoRepository.findById(i.getProduto().getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));
            p.addItemNegocio(i);
            produtoRepository.save(p);
        }

        request.getNegocio().getItemsNegocio().clear();
        request.getNegocio().getItemsNegocio().addAll(items);
        repository.save(request.getNegocio());
//
//        if (negocio.getTipo().equals(Tipo.COMPRA)) {
//            //iterando por cada um dos itens negociados
//            for (ItemNegocio item : negocio.getItemsNegocio()) {
//                //caso o item ja exista, sera alterado a quantidade de estoque.
//                if (produtoRepository.existsById(item.getId())) {
//                    return repository.save(negocio);
//                } else {
//                    produtoRepository.save(item.getProduto());
//                }
//            }
//        } else if (negocio.getTipo().equals(Tipo.VENDA)) {
//            //iterando por cada um dos itens negociados
//            for (ItemNegocio item : negocio.getItemsNegocio()) {
//                //caso o item ja exista, sera alterado a quantidade de estoque.
//                Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));
//                produtoNegociado.diminuirEstoque(item.getQuantidade());
//            }
//        }

        //diminuir o estoque, caso seja venda
        if(request.getNegocio().getTipo().equals(Tipo.VENDA))
        for (ItemNegocio item : request.getNegocio().getItemsNegocio()) {
                //caso o item ja exista, sera alterado a quantidade de estoque.
    @Transactional
    public Negocio addNegocio(@RequestBody Negocio negocio) {
        negocio.setStatus(Status.ABERTO);

        // Lógica para associar Funcionários
        Set<Funcionario> participantesOriginais = negocio.getParticipantes();
        negocio.setParticipantes(new HashSet<>());
        Negocio negocioSalvo = repository.save(negocio);

        if (participantesOriginais != null) {
            for (Funcionario p : participantesOriginais) {
                Funcionario participante = funcionarioRepository.findById(p.getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado: " + p.getId()));
                participante.getNegociosParticipantes().add(negocioSalvo);
                negocioSalvo.getParticipantes().add(participante);
            }
        }

        // Lógica para associar Itens
        if (negocio.getItemsNegocio() != null) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                item.setNegocio(negocioSalvo);
            }
            itemNegocioRepository.saveAll(negocio.getItemsNegocio());
        }

        // Lógica de estoque
        if (negocio.getTipo() == Tipo.VENDA) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado: " + item.getProduto().getId()));
                produtoNegociado.diminuirEstoque(item.getQuantidade());
                produtoRepository.save(produtoNegociado);
            }
        }

        return request.getNegocio();
    }

//    @PostMapping("/negocio")
//    Negocio addNegocio(@RequestBody Negocio negocio) {
//        if (negocio.getTipo().equals(Tipo.COMPRA)) {
//            //iterando por cada um dos itens negociados
//            for (ItemNegocio item : negocio.getItemsNegocio()) {
//                //caso o item ja exista, sera alterado a quantidade de estoque.
//                if (produtoRepository.existsById(item.getId())) {
//                    return repository.save(negocio);
//                } else {
//                    produtoRepository.save(item.getProduto());
//                }
//            }
//        } else if (negocio.getTipo().equals(Tipo.VENDA)) {
//            //iterando por cada um dos itens negociados
//            for (ItemNegocio item : negocio.getItemsNegocio()) {
//                //caso o item ja exista, sera alterado a quantidade de estoque.
//                Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));
//                produtoNegociado.diminuirEstoque(item.getQuantidade());
//            }
//        }
//        return repository.save(negocio);
//    }

        return negocioSalvo;
    }

    @PutMapping("/negocio/concluir/{id}")
    @Transactional
    public Negocio concluirNegocioByID(@PathVariable Long id) {
        Negocio negocio = repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
        if (negocio.getStatus() != Status.ABERTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este negócio não está aberto para ser concluído.");
        }
        Caixa caixa = caixaRepository.findAll().stream().findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Caixa não configurado."));
        double difCaixa = 0.0;

        if (negocio.getTipo().equals(Tipo.COMPRA)) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                difCaixa += item.getProduto().getValorCompra() * item.getQuantidade();
                Produto p = item.getProduto();
                p.adicionarEstoque(item.getQuantidade());
                produtoRepository.save(p);
            }
            caixa.removerValor(difCaixa);
        } else { // VENDA
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                difCaixa += item.getProduto().getValorVenda() * item.getQuantidade();
            }
            caixa.adicionarValor(difCaixa);
        }

        negocio.setStatus(Status.CONCLUIDO);
        caixaRepository.save(caixa);
        return repository.save(negocio);
    }

    @PutMapping("/negocio/cancelar/{id}")
    @Transactional
    public Negocio cancelarNegocioByID(@PathVariable Long id) {
        Negocio negocio = repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
        if (negocio.getStatus() != Status.ABERTO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este negócio não está aberto para ser cancelado.");
        }

        if (negocio.getTipo() == Tipo.VENDA) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
                produtoNegociado.adicionarEstoque(item.getQuantidade());
                produtoRepository.save(produtoNegociado);
            }
        }

        negocio.setStatus(Status.CANCELADO);
        return repository.save(negocio);
    }

    @PutMapping("/negocio/{id}")
    @Transactional
    public Negocio atualizarNegocio(@PathVariable Long id, @RequestBody Negocio negocioAtualizado) {
        Negocio negocioExistente = repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));

        negocioExistente.setTipo(negocioAtualizado.getTipo());
        negocioExistente.setTransportadora(negocioAtualizado.getTransportadora());

        // Atualiza participantes
        Set<Funcionario> participantesAtuais = new HashSet<>(negocioExistente.getParticipantes());
        Set<Funcionario> novosParticipantes = new HashSet<>();
        for(Funcionario p : negocioAtualizado.getParticipantes()) {
            Funcionario participante = funcionarioRepository.findById(p.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            novosParticipantes.add(participante);
        }

        // Remove os que não estão mais na lista
        for(Funcionario p : participantesAtuais) {
            if(!novosParticipantes.contains(p)) {
                p.getNegociosParticipantes().remove(negocioExistente);
            }
        }
        // Adiciona os novos
        for(Funcionario p: novosParticipantes) {
            p.getNegociosParticipantes().add(negocioExistente);
        }
        negocioExistente.setParticipantes(novosParticipantes);


        // Atualiza itens do negócio
        itemNegocioRepository.deleteAll(negocioExistente.getItemsNegocio());
        negocioExistente.getItemsNegocio().clear();

        Set<ItemNegocio> novosItens = new HashSet<>();
        if (negocioAtualizado.getItemsNegocio() != null) {
            for (ItemNegocio item : negocioAtualizado.getItemsNegocio()) {
                item.setNegocio(negocioExistente);
                novosItens.add(item);
            }
            itemNegocioRepository.saveAll(novosItens);
            negocioExistente.setItemsNegocio(novosItens);
        }

        return repository.save(negocioExistente);
    }

    @DeleteMapping("/negocio/{id}")
    @Transactional
    public void deleteNegocio(@PathVariable Long id) {
        Negocio negocio = repository.findById(id)
                .orElseThrow(() -> new NegocioNotFoundException(id));

        // Limpa a relação com os funcionários
        if (negocio.getParticipantes() != null) {
            for (Funcionario participante : new HashSet<>(negocio.getParticipantes())) {
                participante.getNegociosParticipantes().remove(negocio);
            }
        }

        // Deleta o negócio (orphanRemoval=true cuidará dos ItemNegocio)
        repository.delete(negocio);
    }
}