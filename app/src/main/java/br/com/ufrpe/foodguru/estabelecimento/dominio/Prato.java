package br.com.ufrpe.foodguru.estabelecimento.dominio;

public class Prato {
    private String idPrato;
    private String nomePrato;
    private String descricaoPrato;
    private String idSessao;

    public String getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(String idSessao) {
        this.idSessao = idSessao;
    }

    public Prato(){
    }

    public Prato(String nomePrato, String descricaoPrato,String id) {
        this.nomePrato = nomePrato;
        this.descricaoPrato = descricaoPrato;
        this.idSessao = id;
    }


    public String getNomePrato() {
        return nomePrato;
    }
    public void setNomePrato(String nomePrato) {
        this.nomePrato = nomePrato;
    }
    public String getDescricaoPrato() {
        return descricaoPrato;
    }
    public void setDescricaoPrato(String descricaoPrato) {
        this.descricaoPrato = descricaoPrato;
    }
    public String getIdPrato() {
        return idPrato;
    }
    public void setIdPrato(String idPrato) {
        this.idPrato = idPrato;
    }


}
