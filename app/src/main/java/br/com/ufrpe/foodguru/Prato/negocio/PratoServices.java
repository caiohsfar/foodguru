package br.com.ufrpe.foodguru.Prato.negocio;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import br.com.ufrpe.foodguru.Prato.dominio.Prato;

import br.com.ufrpe.foodguru.Prato.persistencia.PratoDAO;

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
