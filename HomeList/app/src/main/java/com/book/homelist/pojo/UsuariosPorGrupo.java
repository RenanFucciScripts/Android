package com.book.homelist.pojo;


public class UsuariosPorGrupo {
    private String chaveUsuario;
    private boolean admin;

    public UsuariosPorGrupo(String chaveUsuario, boolean admin) {
        this.chaveUsuario = chaveUsuario;
        this.admin = admin;
    }

    public UsuariosPorGrupo() {
    }

    public String getChaveUsuario() {
        return chaveUsuario;
    }

    public void setChaveUsuario(String chaveUsuario) {
        this.chaveUsuario = chaveUsuario;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
}
