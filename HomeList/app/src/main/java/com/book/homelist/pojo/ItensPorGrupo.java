package com.book.homelist.pojo;

public class ItensPorGrupo {
    String id;
    String nomeItem;
    int quantidadeItem;
    String marcaItem;
    boolean compradoStatus;
    String imagemItem;


    public ItensPorGrupo(String id, String nomeItem, int quantidadeItem, String marcaItem, boolean compradoStatus, String imagemItem) {
        this.id = id;
        this.nomeItem = nomeItem;
        this.quantidadeItem = quantidadeItem;
        this.marcaItem = marcaItem;
        this.compradoStatus = compradoStatus;
        this.imagemItem = imagemItem;
    }


    public String getImagemItem() {
        return imagemItem;
    }

    public void setImagemItem(String imagemItem) {
        this.imagemItem = imagemItem;
    }

    public ItensPorGrupo() {
    }

    public boolean isCompradoStatus() {
        return compradoStatus;
    }

    public void setCompradoStatus(boolean compradoStatus) {
        this.compradoStatus = compradoStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public int getQuantidadeItem() {
        return quantidadeItem;
    }

    public void setQuantidadeItem(int quantidadeItem) {
        this.quantidadeItem = quantidadeItem;
    }

    public String getMarcaItem() {
        return marcaItem;
    }

    public void setMarcaItem(String marcaItem) {
        this.marcaItem = marcaItem;
    }
}
