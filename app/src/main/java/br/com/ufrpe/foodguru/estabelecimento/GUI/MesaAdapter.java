package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.MesaHolder;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.dominio.MesaView;

public class MesaAdapter extends RecyclerView.Adapter<MesaHolder> {
    private final Context context;
    private List<MesaView> mesas;
    private final MesaOnClickListener onClickListener;

    public interface MesaOnClickListener {
        void onClickMesa(MesaHolder holder, int indexMesa);
        void onLongClickMesa(MesaHolder holder, int indexMesa);
    }
    public MesaAdapter(Context context, List<MesaView> mesas,MesaOnClickListener onClickListener) {
        this.mesas = mesas;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @Override
    public MesaHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mesa_list_item, parent, false);

        MesaHolder holder = new MesaHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final MesaHolder holder, final int position) {
        String numero = mesas.get(position).getMesa().getNumeroMesa();
        String codigo = mesas.get(position).getMesa().getCodigoMesa();
        holder.numero.setText(numero);
        holder.codigo.setText(codigo);
        holder.numero.setTag(holder);
        if (onClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onClickMesa(holder, position);

                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onClickListener.onLongClickMesa(holder, position);
                    return true;
                }
            });
        }
        int corFundo = context.getResources().getColor(mesas.get(position).isSelecionado() ? R.color.RoxoFoodGuruTransparente
                : R.color.zxing_transparent);
        holder.itemView.setBackgroundColor(corFundo);


    }



    @Override
    public int getItemCount() {
        return this.mesas != null ? this.mesas.size() : 0;
    }


}