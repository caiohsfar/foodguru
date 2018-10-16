package br.com.ufrpe.foodguru.Consumo.dominio;

import br.com.ufrpe.foodguru.Prato.dominio.Prato;

public class ItemConsumo {
    private String idPrato;
    private String nomePrato;

    public String getIdPrato() {
        return idPrato;
    }

    public void setIdPrato(String idPrato) {
        this.idPrato = idPrato;
    }

    public String getNomePrato() {
        return nomePrato;
    }

    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }

    private int quantidade;
    private double preco;
    private int numeroMesa;
    private String idCliente;
    private String idEstabelecimento;

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
}
