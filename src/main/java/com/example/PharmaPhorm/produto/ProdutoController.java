package com.example.PharmaPhorm.produto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class ProdutoController {
    private final ProdutoRepository repository;

    ProdutoController(ProdutoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/produtos")
    public List<Produto> getAll() {
        return repository.findAll();
    }

    @GetMapping("/produtos/{id}")
    public Produto getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "produto nao encontrado")
        );
    }

    @PostMapping("/produtos")
    public Produto create(@RequestBody Produto produto) {
//        return produto;
        return repository.save(produto);
    }

    @DeleteMapping("/produtos/{id}")
    public void delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado");
        }
        repository.deleteById(id);
    }

    @PutMapping("/produtos/{id}")
    public Produto update(@RequestBody Produto produto, @PathVariable Long id) {
        Produto produtoAlvo = repository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );

        produtoAlvo.setNome(produto.getNome());
        produtoAlvo.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        produtoAlvo.setValorCompra(produto.getValorCompra());
        produtoAlvo.setValorVenda(produto.getValorVenda());
        repository.save(produtoAlvo);
        
        return produtoAlvo;
    }
}
