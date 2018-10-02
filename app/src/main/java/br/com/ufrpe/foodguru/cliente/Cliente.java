package br.com.ufrpe.foodguru.cliente;

import br.com.ufrpe.foodguru.usuario.Usuario;

public class Cliente {
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private Usuario usuario;
}
