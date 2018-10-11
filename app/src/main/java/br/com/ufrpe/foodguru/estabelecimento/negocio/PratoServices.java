package br.com.ufrpe.foodguru.estabelecimento.negocio;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Prato;

import br.com.ufrpe.foodguru.estabelecimento.persistencia.PratoDAO;

public class PratoServices {
    private PratoDAO pratoDAO = new PratoDAO();
    public boolean adicionarPrato(Prato prato) {
        return pratoDAO.adicionarMesa(prato);
    }
}
