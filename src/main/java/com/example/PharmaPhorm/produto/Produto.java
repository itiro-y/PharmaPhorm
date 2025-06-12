package com.example.PharmaPhorm.produto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Produto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome;
    private Double valorCompra;
    private Double valorVenda;
    private Integer quantidadeEstoque;

    public Produto(String nome, Double valorCompra, Double valorVenda, Integer quantidadeEstoque) {
        this.nome = nome;
        this.valorCompra = valorCompra;
        this.valorVenda = valorVenda;
        this.quantidadeEstoque = quantidadeEstoque;
    }
}
