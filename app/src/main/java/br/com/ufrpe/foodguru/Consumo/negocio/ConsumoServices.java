package br.com.ufrpe.foodguru.Consumo.negocio;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.persistencia.ConsumoDAO;

public class ConsumoServices {
    private ConsumoDAO consumoDAO = new ConsumoDAO();

    public static String adicionarConsumo(Consumo consumo){
        return ConsumoDAO.adicionarConsumo(consumo);
    }
    public static List<ItemConsumo> getPedidos(DataSnapshot dataSnapshot) {
        return ConsumoDAO.getPedidos(dataSnapshot);
    }
    public boolean setItemComoEntregue(ItemConsumo itemConsumo) {
        return consumoDAO.setItemComoEntregue(itemConsumo);
    }
    public boolean adicionarItemConsumo(ItemConsumo itemConsumo) {
        return consumoDAO.adicionarItemConsumo(itemConsumo);
    }
    public boolean setFormaPagamento(Consumo consumo){
        return consumoDAO.setFormaPagamento(consumo);
    }
    public static List<ItemConsumo> getPedidosMesa(DataSnapshot dataSnapshot) {
        return ConsumoDAO.getPedidosMesa(dataSnapshot);
    }
}
