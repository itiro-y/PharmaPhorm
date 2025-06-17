package com.example.PharmaPhorm.produto;

import com.example.PharmaPhorm.itemnegocio.ItemNegocio;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Produto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private Double valorCompra;
    private Double valorVenda;
    private Integer quantidadeEstoque;


    @OneToMany(mappedBy = "produto")
    private Set<ItemNegocio> itemsNegocio = new HashSet<>();

    public Produto() {
        this.nome = "";
        this.valorCompra = 0.0;
        this.valorVenda = 0.0;
        this.quantidadeEstoque = 0;
    }

    public Produto(String nome, Double valorCompra, Double valorVenda, Integer quantidadeEstoque) {
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public void addItemNegocio(ItemNegocio itemNegocio) {
        this.itemsNegocio.add(itemNegocio);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(Double valorCompra) {
        this.valorCompra = valorCompra;
    }

    public Double getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(Double valorVenda) {
        this.valorVenda = valorVenda;
    }

    public Integer getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public void setQuantidadeEstoque(Integer quantidadeEstoque) {
        this.quantidadeEstoque = quantidadeEstoque;
    }

    public Integer adicionarEstoque(Integer estoqueAdicional) {
        quantidadeEstoque += estoqueAdicional;
        return quantidadeEstoque;
    }

    public Integer diminuirEstoque(Integer estoque) {
        quantidadeEstoque -= estoque;
        return quantidadeEstoque;
    }

}
