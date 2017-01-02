package com.rf.rastreadorem.dao;

/**
 * Classe DAO da para acessar os dados da criança
 *
 * @author RenanFucci
 */
public class Crianca {
    private String file_in_name;
    private String date_in;
    private String date_out;
    private String last_stage;
    private String url_out;

    public Crianca(String file_in_name, String date_in, String date_out, String last_stage, String url_out){
        this.file_in_name = file_in_name;
        this.date_in = date_in;
        this.date_out = date_out;
        this.last_stage = last_stage;
        this.url_out = url_out;
    }

    /*------------------- GETTERS -------------------*/

    public String getUrl_out() {
        return url_out;
    }

    public String getFile_in_name() {
        return file_in_name;
    }

    public String getDate_in() {
        return date_in;
    }

    public String getDate_out() {
        return date_out;
    }

    public String getLast_stage() {
        return last_stage;
    }
}
