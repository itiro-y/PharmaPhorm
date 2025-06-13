package com.example.PharmaPhorm.caixa;

import com.example.PharmaPhorm.produto.Produto;
import com.example.PharmaPhorm.produto.ProdutoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CaixaController {
    private final CaixaRepository caixaRepository;
    private final ProdutoRepository produtoRepository;

    public CaixaController(CaixaRepository caixaRepository, ProdutoRepository produtoRepository) {
        this.caixaRepository = caixaRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping("/caixa")
    List<Caixa> getCaixa() {
        return caixaRepository.findAll();
    }

    @PostMapping("/caixa")
    Caixa addCaixa(@RequestBody Caixa caixa) {
        return caixaRepository.save(caixa);
    }

    @GetMapping("/caixa/estimarLucroMensal")
    public double estimarLucroMensal(){
        List<Produto> produtos = produtoRepository.findAll();
        double valorTotalVenda = 0;
        double valorTotalCompra = 0;

        for (Produto produto : produtos) {
            valorTotalVenda += produto.getValorVenda() * produto.getQuantidadeEstoque();
            valorTotalCompra += produto.getValorCompra() * produto.getQuantidadeEstoque();
        }
        return valorTotalVenda - valorTotalCompra;
    }

    @GetMapping("/caixa/estimarLucroAnual")
    double estimarLucroAnual(){
        return estimarLucroMensal() * 12;
    }


}
