package br.com.ufrpe.foodguru.Mesa.GUI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MesaHolder extends RecyclerView.ViewHolder{
    public TextView numero, codigo, teste;
    public ImageView menuMesa;
    public CircleImageView status;


    public MesaHolder(View view) {
        super(view);
        numero = (TextView) view.findViewById(R.id.numero);
        teste = (TextView) view.findViewById(R.id.txt_codigo_da_mesa);
        menuMesa = view.findViewById(R.id.imv_menu_mesa);

    }
}