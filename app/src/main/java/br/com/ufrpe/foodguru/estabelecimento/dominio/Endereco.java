package br.com.ufrpe.foodguru.estabelecimento.dominio;

public class Endereco {
    private String rua;
    private String cidade;
    private String estado;
    private String complemento;

    public Endereco(){

    }
    public Endereco(String cidade, String rua, String estado, String complemento){
        this.setCidade(cidade);
        this.rua = rua;
        this.estado = estado;
        this.complemento = complemento;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
}
