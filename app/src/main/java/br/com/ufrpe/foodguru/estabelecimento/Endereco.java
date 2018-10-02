package br.com.ufrpe.foodguru.estabelecimento;

public class Endereco {
    private String rua;
    private String estado;
    private String complemento;
    public Endereco(String rua, String estado, String complemento){
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
}
