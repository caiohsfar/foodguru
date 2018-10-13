package br.com.ufrpe.foodguru.Prato.dominio;

public class PratoView {
    private Prato prato;
    private boolean selecionado = false;

    public Prato getPrato() {
        return prato;
    }

    public void setPrato(Prato prato) {
        this.prato = prato;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}
