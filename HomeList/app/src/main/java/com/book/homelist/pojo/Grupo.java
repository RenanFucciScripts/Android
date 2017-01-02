package com.book.homelist.pojo;

import java.io.Serializable;

public class Grupo implements Serializable {
    private String id;
    private String nomeGrupo;
    private long data;
    private String admin;

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public Grupo(String id, String nomeGrupo, long data, String admin) {
        this.id = id;
        this.nomeGrupo = nomeGrupo;
        this.data = data;
        this.admin = admin;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public Grupo() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNomeGrupo() {
        return nomeGrupo;
    }

    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }
}
