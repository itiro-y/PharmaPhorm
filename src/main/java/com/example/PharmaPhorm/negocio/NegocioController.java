package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.negocio.Exceptions.NegocioNotFoundException;
import com.example.PharmaPhorm.produto.Produto;
import com.example.PharmaPhorm.produto.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.PharmaPhorm.negocio.NegocioRepository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class NegocioController {
    private final NegocioRepository repository;
    private final ProdutoRepository produtoRepository;

    //Injeção de dependência da classe Repository
    NegocioController(NegocioRepository negocioRepository,
                      ProdutoRepository produtoRepository) {
        this.repository = negocioRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/negocio")
    List<Negocio> getNegocio() {
        return repository.findAll();
    }

    @PostMapping("/negocio")
    Negocio addNegocio(@RequestBody Negocio negocio) {
        if (negocio.getTipo() == Tipo.COMPRA) {
            //iterando por cada um dos itens negociados
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                //caso o item ja exista, sera alterado a quantidade de estoque.
                if (produtoRepository.existsById(item.getId())) {
                    Produto produtoNegociado = produtoRepository.getById(item.getId());
                } else {
                    produtoRepository.save(item.getProduto());
                }
            }
        } else if (negocio.getTipo() == Tipo.VENDA) {
            //iterando por cada um dos itens negociados
            for (ItemNegocio item : negocio.getItemsNegocio()) {
                //caso o item ja exista, sera alterado a quantidade de estoque.
                Produto produtoNegociado = produtoRepository.getById(item.getId());
                produtoNegociado.diminuirEstoque(item.getQuantidade());
            }
        }
        return repository.save(negocio);
    }

    @GetMapping("/negocio/{id}")
    Negocio getFNegociosByID(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
    }

}
