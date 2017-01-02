package com.example.renanfucci.buscagro.method.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.sql.Date;
import java.util.List;

/**
 * Created by _ on 18/09/2016.
 */
public class Cliente {

    private String nome_completo;
    private String telefone;
    private String email;
    private String endereco;
    private int id;
    //private DAO_DB dao_db;


    public Cliente(int id, String nome_completo, String telefone, String email, String endereco){
        this.id = id;
        this.nome_completo = nome_completo;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;

    }

    /*
        public static void setarInfoUser(Context context,String[] userInfos ){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("id", userInfos[0]).apply();
        preferences.edit().putString("Nome", userInfos[1]).apply();
        preferences.edit().putString("Email", userInfos[2]).apply();
        preferences.edit().putString("Pwd", userInfos[3]).apply();
        preferences.edit().putString("Telefone", userInfos[4]).apply();
        preferences.edit().putString("Endereco", userInfos[5]).apply();
    }

    public static String getID(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("id", "");
    }

    * */

    public static String getId(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("id", "");
    }
    public static String getNome_completo(Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("Nome", "");
    }
    public static String getEmail(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("Email", "");

    }
    public static String getPwd(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("Pwd", "");

    }
    public static String getTelefone( Context ctx) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("Telefone", "");
    }
    public static String getEndereco(Context ctx){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        return preferences.getString("Endereco", "");
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }


    public List<Oferta> getMinhasOfertas(){
        return null;
    }

    public List<Demanda> getMinhasDemandas(){
        return null;
    }

    public void cadastrarOferta(Oferta oferta){

    }

    public void cadastrarDemanda(Demanda demanda){

        String demanda_nome = demanda.getNomeproduto();
        int demanda_qntd = demanda.getQuantidade();
        Date demanda_validade = demanda.getValidade();
        //int user_id = getId();

//        dao_db.connect();
//        dao_db.executeStmt_Insert_demanda(demanda_nome, demanda_qntd, demanda_validade, user_id);
//
    }

    public List<Oferta> consultarOfertas (String nomeProduto, int quantidade){

        return null;
    }

    public List<Demanda> consultarDemanda (String nomeProduto, int quantidade){

        return null;
    }

    public void cancelarOfertas(List<Oferta> ofertas){

    }

    public void cancelarDemandas(List<Demanda> demandas){

    }

}
