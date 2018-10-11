package br.com.ufrpe.foodguru.estabelecimento.dominio;

public class Prato {
    private String idPrato;
    private String nomePrato;
    private String descricaoPrato;

    public Prato(){
    }

    public Prato(String nomePrato, String descricaoPrato) {
        this.nomePrato = nomePrato;
        this.descricaoPrato = descricaoPrato;
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
