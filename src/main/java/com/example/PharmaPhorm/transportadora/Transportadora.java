package com.example.PharmaPhorm.transportadora;

import com.example.PharmaPhorm.negocio.Negocio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Transportadora {
    private @Id @GeneratedValue long id;

    private String nome;

    @ElementCollection(fetch = FetchType.EAGER) // EAGER para facilitar o acesso às regiões
    private List<String> regioes;

    private boolean isAtivo = true;

    // ✅ ANOTAÇÃO @JsonIgnore ADICIONADA
    // Impede um loop infinito quando o sistema for converter o objeto para JSON.
    @OneToMany(mappedBy = "transportadora", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Negocio> negocios;

    public Transportadora() {
        this.regioes = new ArrayList<>();
        this.negocios = new ArrayList<>();
    }

    public Transportadora(String nome, ArrayList<String> regioes) {
        this.nome = nome;
        this.regioes = regioes;
        this.isAtivo = true;
        this.negocios = new ArrayList<>();
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

    public void setRegioes(List<String> regioes) {
        this.regioes = regioes;
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

    // ✅ GETTERS E SETTERS PARA A LISTA DE NEGÓCIOS ADICIONADOS
    public List<Negocio> getNegocios() {
        return negocios;
    }

    public void setNegocios(List<Negocio> negocios) {
        this.negocios = negocios;
    }

    public void addRegioes(String regiao){
        regioes.add(regiao);
    }
}