package br.com.ufrpe.foodguru.Mesa.GUI;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.ufrpe.foodguru.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class MesaHolder extends RecyclerView.ViewHolder{
    public TextView numero, codigo;
    public ImageView menuMesa;
    public CircleImageView status;


    public MesaHolder(View view) {
        super(view);
        numero = (TextView) view.findViewById(R.id.numero);
        codigo = (TextView) view.findViewById(R.id.tv_codigo_mesa);
        menuMesa = view.findViewById(R.id.imv_menu_mesa);
        status = view.findViewById(R.id.imv_status_mesa);

    }
}