package com.example.PharmaPhorm.produto;

import com.example.PharmaPhorm.negocio.ItemNegocio;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Produto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private Double valorCompra;
    private Double valorVenda;
    private Integer quantidadeEstoque;

    @OneToMany(mappedBy = "negocio")
    private final List<ItemNegocio> itemsNegocio;

    public Produto(){
        this.nome = "";
        this.valorCompra=0.0;
        this.valorVenda=0.0;
        this.quantidadeEstoque=0;
        this.itemsNegocio=new ArrayList<>();
    }

    public Produto(String nome, Double valorCompra, Double valorVenda, Integer quantidadeEstoque) {
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
        this.itemsNegocio = new ArrayList<>();
    }
}
