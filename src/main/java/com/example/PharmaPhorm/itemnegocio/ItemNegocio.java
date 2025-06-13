package com.example.PharmaPhorm.itemnegocio;

import com.example.PharmaPhorm.negocio.Negocio;
import com.example.PharmaPhorm.produto.Produto;
import jakarta.persistence.*;

@Entity
public class ItemNegocio {

    private @Id @GeneratedValue long id;
    private int quantidade;

    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    public ItemNegocio(Produto produto, Negocio negocio, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.negocio = negocio;
    }

    public ItemNegocio() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Negocio getNegocio() {
        return negocio;
    }

    public void setNegocio(Negocio negocio) {
        this.negocio = negocio;
    }

// Controller
//    public void setQuantidade(int quantidade) {
//        if(quantidade<=produto.getQuantidadeEstoque()){
//            this.quantidade = quantidade;
//        }else{
//            System.out.println("Erro - existem apenas "+produto.getQuantidadeEstoque()+" unidades deste produto em estoque.");
//        }
//    }
}
