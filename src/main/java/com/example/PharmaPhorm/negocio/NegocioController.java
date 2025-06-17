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
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.PharmaPhorm.negocio.NegocioRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class NegocioController {
    private final NegocioRepository repository;
    private final ProdutoRepository produtoRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ItemNegocioRepository itemNegocioRepository;

    //Injeção de dependência da classe Repository
    private final CaixaRepository caixaRepository;
    private final TransportadoraRepository transportadoraRepository;

    //Injeção de dependência da classe Repository
    NegocioController(NegocioRepository negocioRepository, CaixaRepository caixaRepository,
                      ProdutoRepository produtoRepository,
                      ItemNegocioRepository itemNegocioRepository,
                      FuncionarioRepository funcionarioRepository,
                      TransportadoraRepository transportadoraRepository) {
        this.repository = negocioRepository;
        this.caixaRepository = caixaRepository;
        this.produtoRepository = produtoRepository;
        this.transportadoraRepository = transportadoraRepository;
        this.itemNegocioRepository = itemNegocioRepository;
        this.funcionarioRepository = funcionarioRepository;
    }

    @GetMapping("/negocio")
    List<Negocio> getNegocio() {
        return repository.findAll();
    }

    @PostMapping("/negocio")
    Negocio addNegocio(@RequestBody Negocio negocio) {
        Transportadora t = transportadoraRepository.findById(negocio.getTransportadora().getId())
                .orElseThrow(() -> new RuntimeException("transportadora nao encontrada"));
        negocio.setTransportadora(t);

        // Fetch and set the participantes
        Set<Funcionario> funcionarios = negocio.getParticipantes().stream()
                .map(f -> funcionarioRepository.findById(f.getId())
                        .orElseThrow(() -> new RuntimeException("Funcionario não encontrado: " + f.getId())))
                .collect(Collectors.toSet());
        negocio.setParticipantes(funcionarios);

        // Fetch and set the itemsNegocio
        List<ItemNegocio> items = negocio.getItemsNegocio().stream()
                .map(item -> itemNegocioRepository.findById(item.getId())
                        .orElseThrow(() -> new RuntimeException("ItemNegocio não encontrado: " + item.getId())))
                .collect(Collectors.toList());
        negocio.setItemsNegocio(items);

        return repository.save(negocio);
    }

//    @PostMapping("/negocio")
//    Negocio addNegocio(@RequestBody Negocio negocio) {
//        if (negocio.getTipo() == Tipo.COMPRA) {
//            //iterando por cada um dos itens negociados
//            for (ItemNegocio item : negocio.getItemsNegocio()) {
//                //caso o item ja exista, sera alterado a quantidade de estoque.
//                if (produtoRepository.existsById(item.getId())) {
//                    return repository.save(negocio);
//                } else {
//                    produtoRepository.save(item.getProduto());
//                }
//            }
//        } else if (negocio.getTipo() == Tipo.VENDA) {
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

    @GetMapping("/negocio/{id}")
    Negocio getFNegociosByID(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
    }

    //Metodo para alterar o status de um negócio para concluido
    @PutMapping("/negocio/concluir/{id}")
    Negocio concluirNegocioByID(@PathVariable Long id) {

        Negocio negocio = repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
        Caixa caixa = caixaRepository.findAll().getFirst();
        double difCaixa = 0.0;

        if (negocio.getTipo().equals(Tipo.COMPRA)) {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                difCaixa += item.getProduto().getValorCompra() * item.getQuantidade();
            }
            caixa.removerValor(difCaixa); //Pode lançar a exceção SaldoInsuficienteException

            //Adicionar logica para aumentar a quantidade de produtos no estoque
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));
                produtoNegociado.adicionarEstoque(item.getQuantidade());
            }

        } else {
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                difCaixa += item.getProduto().getValorVenda() * item.getQuantidade();
            }
            caixa.adicionarValor(difCaixa);
        }

        //Alterar o status do negocio para concluido, persistir a alteração no caixa e no negocio
        negocio.setStatus(Status.CONCLUIDO);
        caixaRepository.save(caixa);
        repository.save(negocio);
        return negocio;
    }

    //Metodo para alterar o status de um negócio para cancelado
    @PutMapping("/negocio/cancelar/{id}")
    Negocio cancelarNegocioByID(@PathVariable Long id) {
        Negocio negocio = repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
        negocio.setStatus(Status.CANCELADO);

        //Adicionar logica para devolver produtos ao estoque se o tipo de negocio for venda
        for (ItemNegocio item : negocio.getItemsNegocio()) {
            //caso o item ja exista, sera alterado a quantidade de estoque.
            Produto produtoNegociado = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado"));
            produtoNegociado.adicionarEstoque(item.getQuantidade());
        }
        //Persistir as alterações no negocio
        repository.save(negocio);
        return negocio;
    }

}
