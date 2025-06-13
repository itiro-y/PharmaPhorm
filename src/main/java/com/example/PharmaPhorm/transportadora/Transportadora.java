package com.example.PharmaPhorm.transportadora;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Transportadora {
    private @Id @GeneratedValue long id;

    private String nome;

    @ElementCollection
    private List<String> regioes;

    private boolean isAtivo;

//    @OneToMany(mappedBy = "transportadora")
//    private List<Negocio> negocios;

    public Transportadora() {

    }

    public Transportadora(String nome, ArrayList<String> regioes) {
        this.nome = nome;
        this.regioes = regioes;
        isAtivo = true;
        //negocios = new ArrayList<>();
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<String> getRegioes() {
        return regioes;
    }

    public long getId() {
        return id;
    }

    public boolean isAtivo() {
        return isAtivo;
    }

    public void setAtivo(boolean ativo) {
        isAtivo = ativo;
    }

    public void addRegioes(String regiao){
        regioes.add(regiao);
    }

    public void setRegioes(List<String> regioes) {
        this.regioes = regioes;
    }
}
