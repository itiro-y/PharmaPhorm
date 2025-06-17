package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Tipo;

import java.util.List;

// Esta classe representa os dados que o frontend envia para criar/editar um Negócio.
public class NegocioRequestDTO {
    private Tipo tipo;
    private Long transportadoraId;
    private List<Long> participanteIds;
    private List<ItemDTO> items;

    // Getters e Setters
    public Tipo getTipo() { return tipo; }
    public void setTipo(Tipo tipo) { this.tipo = tipo; }
    public Long getTransportadoraId() { return transportadoraId; }
    public void setTransportadoraId(Long transportadoraId) { this.transportadoraId = transportadoraId; }
    public List<Long> getParticipanteIds() { return participanteIds; }
    public void setParticipanteIds(List<Long> participanteIds) { this.participanteIds = participanteIds; }
    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }

    // Classe interna para representar cada item
    public static class ItemDTO {
        private Long produtoId;
        private int quantidade;

        // Getters e Setters
        public Long getProdutoId() { return produtoId; }
        public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
        public int getQuantidade() { return quantidade; }
        public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    }
}