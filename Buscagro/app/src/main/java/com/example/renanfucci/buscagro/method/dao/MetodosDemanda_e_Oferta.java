package com.example.renanfucci.buscagro.method.dao;

import java.sql.Date;

/**
 * Created by _ on 18/09/2016.
 */
public class MetodosDemanda_e_Oferta {

    private String nomeproduto;
    private int quantidade;
    private Date validade;
    private int id;

    public MetodosDemanda_e_Oferta(int id) {
        this.id = id;
    }

    public MetodosDemanda_e_Oferta(String nomeproduto, int quantidade, Date validade) {
        this.nomeproduto = nomeproduto;
        this.quantidade = quantidade;
        this.validade = validade;
    }

    public String getNomeproduto() {
        return nomeproduto;
    }

    public int getQuantidade() {
        return quantidade;
    }


    public Date getValidade() {
        return validade;
    }

    public int getId() {
        return id;
    }
}
