package br.com.ufrpe.foodguru.Mesa.GUI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;

public class MesaHolder extends RecyclerView.ViewHolder{
    public TextView numero, codigo;


    public MesaHolder(View view) {
        super(view);
        numero = (TextView) view.findViewById(R.id.numero);
        codigo = (TextView) view.findViewById(R.id.tv_codigo_mesa);
    }
}