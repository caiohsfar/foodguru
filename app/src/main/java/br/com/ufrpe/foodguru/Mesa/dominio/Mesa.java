package br.com.ufrpe.foodguru.Mesa.dominio;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Mesa implements Parcelable {
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

    protected Mesa(Parcel in) {
        idMesa = in.readString();
        numeroMesa = in.readString();
        codigoMesa = in.readString();
        uidEstabelecimento = in.readString();
    }

    public static final Creator<Mesa> CREATOR = new Creator<Mesa>() {
        @Override
        public Mesa createFromParcel(Parcel in) {
            return new Mesa(in);
        }

        @Override
        public Mesa[] newArray(int size) {
            return new Mesa[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMesa);
        dest.writeString(numeroMesa);
        dest.writeString(codigoMesa);
        dest.writeString(uidEstabelecimento);
    }
}
