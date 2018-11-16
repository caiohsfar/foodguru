package br.com.ufrpe.foodguru.estabelecimento.negocio;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Endereco;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.estabelecimento.persistencia.EstabelecimentoDAO;
import br.com.ufrpe.foodguru.usuario.persistencia.UsuarioDAO;

public class EstabelecimentoServices {
    private EstabelecimentoDAO estDAO = new EstabelecimentoDAO();
    public boolean adicionarEstabelecimento(Estabelecimento estabelecimento) {
        return estDAO.adicionarEstabelecimento(estabelecimento);
    }
    public boolean editarTelefone(String telefone){
        return estDAO.updateTelefone(telefone);
    }
    public boolean editarEndereco(Endereco endereco){
        return estDAO.updateEndereco(endereco);
    }
    public boolean addPagAuthCode(String authCode){
        return estDAO.addPagAuthCode(authCode);
    }


}
