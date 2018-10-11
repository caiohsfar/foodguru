package br.com.ufrpe.foodguru.estabelecimento.negocio;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Prato;

import br.com.ufrpe.foodguru.estabelecimento.persistencia.PratoDAO;

public class PratoServices {
    private PratoDAO pratoDAO = new PratoDAO();
    public boolean adicionarPrato(Prato prato) {
        return pratoDAO.adicionarPrato(prato);
    }
    public boolean removerPrato(Prato prato) {
        return pratoDAO.removerPrato(prato);
    }
    public List<Prato> loadPratos(DataSnapshot dataSnapshot) {
        return pratoDAO.loadPratos(dataSnapshot);
    }

    public boolean editarPrato(Prato prato) {
        return pratoDAO.editarPrato(prato);
    }


}
