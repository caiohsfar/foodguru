package br.com.ufrpe.foodguru.Consumo.dominio;




import java.util.Date;



public class ItemConsumo {
    private String id;
    private String nomePrato;
    private String idPrato;
    private String idMesa;
    private int numeroMesa;
    private int quantidade;
    private double preco;
    private String idCliente;
    private String idEstabelecimento;
    private Date data;
    private boolean entregue = false;


    public String getNomePrato() {
        return nomePrato;
    }

    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }

    public String getIdEstabelecimento() {
        return idEstabelecimento;
    }

    public void setIdEstabelecimento(String idEstabelecimento) {
        this.idEstabelecimento = idEstabelecimento;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }



    public int getQuantidade() {
        return quantidade;
    }


    public double getPreco() {
        return preco;
    }

    public void setPrecoQuantidade(int quantidade,double preco) {
        this.preco = quantidade*preco;
        this.quantidade = quantidade;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdPrato() {
        return idPrato;
    }

    public void setIdPrato(String idPrato) {
        this.idPrato = idPrato;
    }

    public String getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(String idMesa) {
        this.idMesa = idMesa;
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
}
