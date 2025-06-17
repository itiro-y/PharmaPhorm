package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.produto.Produto;
import com.example.PharmaPhorm.transportadora.Transportadora;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Negocio {
    private @Id
    @GeneratedValue Long id;

    private Tipo tipo = Tipo.VENDA;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "transportadora_id") // chave estrangeira
    private Transportadora transportadora;

    @ManyToMany(mappedBy = "negociosParticipantes")
    private Set<Funcionario> participantes;

    @OneToMany(mappedBy = "negocio", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Set<ItemNegocio> itemsNegocio = null;


    // String, List<Long>, Long, List<Long>
    public Negocio(String tipo, Set<Funcionario> participantes, Transportadora transportadora) {
        this.tipo = Tipo.valueOf(tipo.toUpperCase());
        this.status = Status.ABERTO;

        this.participantes = participantes;
        this.transportadora = transportadora;
    }

    public Negocio() {
        this.status = Status.ABERTO;
        this.tipo = Tipo.VENDA;
        this.transportadora = new Transportadora();
        this.participantes = new HashSet<>();
        this.itemsNegocio = new HashSet<>();
    }

    // Getters e Setters

    public long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    // ✅ SETTER ADICIONADO
    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<Funcionario> getParticipantes() {
        return participantes;
    }

    // ✅ SETTER ADICIONADO
    public void setParticipantes(Set<Funcionario> participantes) {
        this.participantes = participantes;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

    // ✅ SETTER ADICIONADO
    public void setTransportadora(Transportadora transportadora) {
        this.transportadora = transportadora;
    }

    public Set<ItemNegocio> getItemsNegocio() {
        return itemsNegocio;
    }

    public void setItemsNegocio(Set<ItemNegocio> itemsNegocio) {

        this.itemsNegocio = itemsNegocio;
    }

}

