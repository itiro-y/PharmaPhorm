package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.transportadora.Transportadora;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Negocio {
    private @Id @GeneratedValue Long id;

    private Tipo tipo = Tipo.VENDA;
    private Status status;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "transportadora_id") // chave estrangeira
    private Transportadora transportadora;

    @ManyToMany(mappedBy = "negociosParticipantes")
    private List<Funcionario> participantes = null;

    @OneToMany(mappedBy = "negocio")
    private  List<ItemNegocio> itemsNegocio = null;



    public Negocio(String tipo, List<Funcionario> participantes, Transportadora transportadora) {
        this.tipo = Tipo.valueOf(tipo.toUpperCase());
        this.status = Status.ABERTO;

        this.participantes = participantes;
        this.transportadora = transportadora;
        //this.itemsNegocio = itemsNegocio;
//        //diminui a quantidade de cada produto em estoque quando a negociação for de venda
//        if (tipo.equals(Tipo.VENDA)) {
//            for (ItemNegocio itemNegocio : itemsNegocio) {
//                itemNegocio.getProduto().setQuantidadeEstoque(itemNegocio.getProduto().getQuantidadeEstoque() - itemNegocio.getQuantidade());
//            }
//        }
    }

    public Negocio(Tipo tipo, List<Funcionario> participantes){
        this.tipo = tipo;
        this.participantes = participantes;
    }

    public Negocio(){

    }

    // Getters e Setters
    public long getId() {
        return id;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Funcionario> getParticipantes() {
        return participantes;
    }

    public Transportadora getTransportadora() {
        return transportadora;
    }

//    public List<ItemNegocio> getItemsNegocio() {
//        return itemsNegocio;
//    }
}
