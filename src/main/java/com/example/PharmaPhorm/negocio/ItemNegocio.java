//package com.example.PharmaPhorm.negocio;
//
//import com.example.PharmaPhorm.produto.Produto;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.context.annotation.Bean;
//
//@NoArgsConstructor
//@Entity
//public class ItemNegocio {
//    @Getter private @Id @GeneratedValue long id;
//    @ManyToOne @JoinColumn(name = "produto_id") private Produto produto;// chave estrangeira
//    private int quantidade;
//
//    @ManyToOne
//    @JoinColumn(name = "negocio_id") // chave estrangeira
//    private Negocio negocio;
//
//    public ItemNegocio(Produto produto, int quantidade) {
//        this.produto = produto;
//
//        if(quantidade<=produto.getQuantidadeEstoque()){
//            this.quantidade = quantidade;
//        }else{
//            System.out.println("Erro - existem apenas "+produto.getQuantidadeEstoque()+" unidades deste produto em estoque.");
//        }
//    }
//
//    public Produto getProduto() {
//        return produto;
//    }
//
//    public void setProduto(Produto produto) {
//        this.produto = produto;
//    }
//
//    public int getQuantidade() {
//        return quantidade;
//    }
//
//    public void setQuantidade(int quantidade) {
//        if(quantidade<=produto.getQuantidadeEstoque()){
//            this.quantidade = quantidade;
//        }else{
//            System.out.println("Erro - existem apenas "+produto.getQuantidadeEstoque()+" unidades deste produto em estoque.");
//        }
//    }
//}
