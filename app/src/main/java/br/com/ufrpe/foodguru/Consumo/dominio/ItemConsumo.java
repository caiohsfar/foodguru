package br.com.ufrpe.foodguru.Consumo.dominio;




import java.util.Date;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;


public class ItemConsumo {

    private String id;
    private String inicioPreparo;
    private String idCliente;
    private String idConsumo;
    private String uidEstabelecimento;
    private Prato prato;
    private Mesa mesa;
    private int quantidade;
    private double valor;
    private Date data;
    private String observacao;
    private boolean entregue = false;


    public int getQuantidade() {
        return quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(int quantidade,double preco) {
        this.valor = quantidade*preco;
        this.setQuantidade(quantidade);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public boolean isEntregue() {
        return entregue;
    }

    public void setEntregue(boolean entregue) {
        this.entregue = entregue;
    }

    public Prato getPrato() {
        return prato;
    }

    public void setPrato(Prato prato) {
        this.prato = prato;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public void setMesa(Mesa mesa) {
        this.mesa = mesa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdConsumo() {
        return idConsumo;
    }

    public void setIdConsumo(String idConsumo) {
        this.idConsumo = idConsumo;
    }

    public String getUidEstabelecimento() {
        return uidEstabelecimento;
    }

    public String getInicioPreparo() {
        return inicioPreparo;
    }

    public void setInicioPreparo(String inicioPreparo) {
        this.inicioPreparo = inicioPreparo;
    }

    public void setUidEstabelecimento(String uidEstabelecimento) {
        this.uidEstabelecimento = uidEstabelecimento;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }
}
