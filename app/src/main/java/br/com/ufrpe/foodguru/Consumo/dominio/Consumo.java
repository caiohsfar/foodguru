package br.com.ufrpe.foodguru.Consumo.dominio;

import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;

public class Consumo {
    private String id;
    private String idCliente;
    private Mesa mesa;
    private List<ItemConsumo> listaItens = new LinkedList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public List<ItemConsumo> getListaItens() {
        return listaItens;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
    public void reset(){
        this.listaItens.clear();
        setMesa(null);
    }
}
