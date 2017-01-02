package com.example.renanfucci.buscagro.method.dao;

import java.sql.Date;

/**
 * Created by _ on 18/09/2016.
 */
public class Demanda extends  MetodosDemanda_e_Oferta{

    public Demanda(String nomeproduto, int quantidade, Date validade){
        super(nomeproduto, quantidade, validade);
    }
}
