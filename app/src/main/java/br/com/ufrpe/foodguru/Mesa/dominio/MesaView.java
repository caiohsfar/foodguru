package br.com.ufrpe.foodguru.Mesa.dominio;

public class MesaView {
    private Mesa mesa;
    private boolean selecionado = false;

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}
