package br.com.ufrpe.foodguru.Prato.GUI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;

public class SessaoHolder extends RecyclerView.ViewHolder {
    public TextView nome;


    public SessaoHolder(View view) {
        super(view);
        nome = (TextView) view.findViewById(R.id.txtNomeSessao);
        // restante das buscas
    }
}
