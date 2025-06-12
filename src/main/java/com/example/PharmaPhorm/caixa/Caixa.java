package com.example.PharmaPhorm.caixa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Caixa {
    private @Id @GeneratedValue long id;
    private double valor;

    public Caixa(){

    }

    Caixa(double valor){
        this.valor = valor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void adicionarValor(double total) {
        if (total > 0) this.valor += total;
    }

    public void removerValor(double total) {
        if (total > 0) this.valor -= total;
    }
}
