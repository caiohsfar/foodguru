package br.com.ufrpe.foodguru.estabelecimento.dominio;

public class Mesa {
    private String idMesa;
    private String numeroMesa;
    private String codigoMesa;
    private String uidEstabelecimento;
    public Mesa(){

    }

    public Mesa(String numeroMesa, String codigoMesa, String uidEstabelecimento) {
        this.numeroMesa = numeroMesa;
        this.codigoMesa = codigoMesa;
        this.uidEstabelecimento = uidEstabelecimento;
    }

    public String getUidEstabelecimento() {
        return uidEstabelecimento;
    }

    public void setUidEstabelecimento(String uidEstabelecimento) {
        this.uidEstabelecimento = uidEstabelecimento;
    }
    public String getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(String numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getCodigoMesa() {
        return codigoMesa;
    }

    public void setCodigoMesa(String codigoMesa) {
        this.codigoMesa = codigoMesa;
    }

    public String getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(String idMesa) {
        this.idMesa = idMesa;
    }
}
