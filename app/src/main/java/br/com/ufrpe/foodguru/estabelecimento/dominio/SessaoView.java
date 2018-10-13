package br.com.ufrpe.foodguru.estabelecimento.dominio;

import br.com.ufrpe.foodguru.estabelecimento.GUI.SessaoActvity;

public class SessaoView {
    private SessaoActvity sessao;
    private boolean selecionado = false;

    public SessaoActvity getSessao() {
        return sessao;
    }

    public void setSessao(SessaoActvity sessao) {
        this.sessao = sessao;
    }

    public boolean isSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }
}
