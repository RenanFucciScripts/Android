package com.book.homelist.pojo;


import java.io.Serializable;

public class Usuario implements Serializable {
    String id;
    String nomeUsuario;
    String emailUsuario;
    String picturePath;


    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Usuario(String id, String nomeUsuario, String emailUsuario, String picturePath) {
        this.id = id;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
        this.picturePath = picturePath;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }
}
