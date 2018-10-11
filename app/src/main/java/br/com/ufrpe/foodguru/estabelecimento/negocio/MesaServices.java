package br.com.ufrpe.foodguru.estabelecimento.negocio;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.persistencia.MesaDAO;

public class MesaServices {
    private MesaDAO mesaDAO= new MesaDAO();
    public boolean adicionarMesa(Mesa mesa) {
        return mesaDAO.adicionarMesa(mesa);
    }
    public boolean removerMesa(Mesa mesa) {
        return mesaDAO.removerMesa(mesa);
    }
    public List<Mesa> loadMesas(DataSnapshot dataSnapshot) {
        return mesaDAO.loadMesas(dataSnapshot);
    }
    public boolean editarMesa(Mesa mesa) {
        return mesaDAO.editarMesa(mesa);
    }

}
