package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.transportadora.Transportadora;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Negocio {
    private @Id @GeneratedValue long id;

    private final Tipo tipo;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "transportadora_id") // chave estrangeira
    private Transportadora transportadora;

    @OneToMany(mappedBy = "negocio")
    private final List<ItemNegocio> itemsNegocio;

    @ManyToMany(mappedBy = "negociosParticipantes")
    private final List<Funcionario> participantes;


    public Negocio(){
        this.tipo = Tipo.VENDA;
        this.status = Status.ABERTO;
        this.transportadora = null;
        this.itemsNegocio = new ArrayList<>();
        this.participantes = new ArrayList<>();

    }

    Negocio(Tipo tipo, List<ItemNegocio> itemsNegocio, List<Funcionario> participantes, Transportadora transportadora) {
        this.tipo = tipo;
        this.status = Status.ABERTO;
        this.itemsNegocio = itemsNegocio;

        //diminui a quantidade de cada produto em estoque quando a negociação for de venda
        if (tipo.equals(Tipo.VENDA)) {
            for (ItemNegocio itemNegocio : itemsNegocio) {
                itemNegocio.getProduto().setQuantidadeEstoque(itemNegocio.getProduto().getQuantidadeEstoque() - itemNegocio.getQuantidade());
            }
        }

        this.participantes = participantes;
        this.transportadora = transportadora;
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


    public List<ItemNegocio> getItemsNegocio() {
        return itemsNegocio;
    }
}
