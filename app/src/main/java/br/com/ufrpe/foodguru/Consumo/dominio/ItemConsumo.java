package br.com.ufrpe.foodguru.Consumo.dominio;




import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;


public class ItemConsumo implements Comparable<ItemConsumo>{

    private String id;
    private String inicioPreparo;
    private String idCliente;
    private String idConsumo;
    private String uidEstabelecimento;
    private Prato prato;
    private Mesa mesa;
    private int quantidade;
    private double valor;
    private String horaPedido;
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

    public String getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(String horaPedido) {
        this.horaPedido = horaPedido;
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

    @Override
    public int compareTo(@NonNull ItemConsumo outroItemConsumo) {

        long diferencaObjeto1 = stringToLong(this.getHoraPedido());
        long diferencaObjeto2 = stringToLong(outroItemConsumo.getHoraPedido());

        if(diferencaObjeto1>diferencaObjeto2){
            return 1;
        }
        if(diferencaObjeto1<diferencaObjeto2){
            return -1;
        }

        return 0;
    }

    public static long stringToLong(String horaInicial) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(horaInicial));
            long diferenca = cal.getTimeInMillis();
            return diferenca;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
