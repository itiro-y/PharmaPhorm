package com.example.PharmaPhorm.produto;

import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    // ✅ CORREÇÃO APLICADA AQUI
    // Adicionamos a relação com ItemNegocio e a configuração de cascata.
    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Evita loops infinitos ao converter para JSON
    private Set<ItemNegocio> itemsNegocio;


    public Produto() {
    }

    public Produto(String nome, Double valorCompra, Double valorVenda, Integer quantidadeEstoque) {
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }

    // Getters e Setters
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

    public Set<ItemNegocio> getItemsNegocio() {
        return itemsNegocio;
    }

    public void setItemsNegocio(Set<ItemNegocio> itemsNegocio) {
        this.itemsNegocio = itemsNegocio;
    }


    // Métodos de Lógica
    public Integer adicionarEstoque(Integer estoqueAdicional) {
        this.quantidadeEstoque += estoqueAdicional;
        return this.quantidadeEstoque;
    }

    public Integer diminuirEstoque(Integer estoque) {
        if (this.quantidadeEstoque >= estoque) {
            this.quantidadeEstoque -= estoque;
        }
        return this.quantidadeEstoque;
    }
}