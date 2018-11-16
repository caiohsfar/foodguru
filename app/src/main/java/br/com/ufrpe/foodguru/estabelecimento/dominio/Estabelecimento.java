package br.com.ufrpe.foodguru.estabelecimento.dominio;

public class Estabelecimento {
    private String telefone;
    private String pagSeguroAuthCode = "ND";
    private Endereco endereco;

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getPagSeguroAuthCode() {
        return pagSeguroAuthCode;
    }

}
