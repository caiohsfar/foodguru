package br.com.ufrpe.foodguru.Mesa.negocio;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.persistencia.MesaDAO;

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

    public boolean mudarStatus(Mesa mesa, int status){
        return mesaDAO.mudarStatus(mesa, status);
    }
    public boolean mudarIdConsumoAtual(Mesa mesa, String consumoAtual){
        return mesaDAO.mudarIdConsumoAtual(mesa, consumoAtual);
    }

}
