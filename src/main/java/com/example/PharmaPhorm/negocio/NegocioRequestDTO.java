package com.example.PharmaPhorm.negocio;

import java.util.ArrayList;
import java.util.List;

public class NegocioRequestDTO {
    private Negocio negocio;
    private List<Long> idProdutos;
    private List<Integer> quantidades;

    // Getters e setters

    public NegocioRequestDTO(){
        this.negocio = new Negocio();
        this.idProdutos = new ArrayList<Long>();
        this.quantidades = new ArrayList<>();
    }

    public NegocioRequestDTO(Negocio negocio, List<Integer> quantidades, List<Long> idProdutos) {
        this.negocio = negocio;
        this.quantidades = quantidades;
        this.idProdutos = idProdutos;
    }

    public List<Long> getIdProdutos() {
        return idProdutos;
    }

    public void setIdProdutos(List<Long> idProdutos) {
        this.idProdutos = idProdutos;
    }

    public List<Integer> getQuantidades() {
        return quantidades;
    }

    public void setQuantidades(List<Integer> quantidades) {
        this.quantidades = quantidades;
    }

    public Negocio getNegocio() {
        return negocio;
    }

    public void setNegocio(Negocio negocio) {
        this.negocio = negocio;
    }
}

