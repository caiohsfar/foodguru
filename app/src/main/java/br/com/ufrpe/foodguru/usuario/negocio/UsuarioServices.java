package br.com.ufrpe.foodguru.usuario.negocio;

import br.com.ufrpe.foodguru.usuario.persistencia.UsuarioDAO;

public class UsuarioServices {
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    public boolean alterarNome(String novoNome) {
        return usuarioDAO.setNome(novoNome);
    }

    public boolean alterarEmail(String novoEmail) {
        return usuarioDAO.setEmail(novoEmail);

    }
}
