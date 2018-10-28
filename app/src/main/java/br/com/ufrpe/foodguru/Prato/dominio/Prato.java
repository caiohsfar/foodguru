package br.com.ufrpe.foodguru.Prato.dominio;


import android.os.Parcel;
import android.os.Parcelable;

public class Prato implements Parcelable {
    private String idPrato;
    private String nomePrato;
    private String descricaoPrato;
    private String idSessao;
    private String urlImagem;
    private Double preco;
    private int estimativa;

    public int getEstimativa() {
        return estimativa;
    }

    public void setEstimativa(int estimativa) {
        this.estimativa = estimativa;
    }

    public Prato(){
    }

    public Prato(String nomePrato, String descricaoPrato) {
        this.nomePrato = nomePrato;
        this.descricaoPrato = descricaoPrato;
    }


    protected Prato(Parcel in) {
        idPrato = in.readString();
        nomePrato = in.readString();
        descricaoPrato = in.readString();
        idSessao = in.readString();
        urlImagem = in.readString();
        if (in.readByte() == 0) {
            preco = null;
        } else {
            preco = in.readDouble();
        }
    }

    public static final Creator<Prato> CREATOR = new Creator<Prato>() {
        @Override
        public Prato createFromParcel(Parcel in) {
            return new Prato(in);
        }

        @Override
        public Prato[] newArray(int size) {
            return new Prato[size];
        }
    };

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

    public String getIdSessao() {
        return idSessao;
    }

    public void setIdSessao(String idSessao) {
        this.idSessao = idSessao;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idPrato);
        dest.writeString(nomePrato);
        dest.writeString(descricaoPrato);
        dest.writeString(idSessao);
        dest.writeString(urlImagem);
        if (preco == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(preco);
        }
    }
}
