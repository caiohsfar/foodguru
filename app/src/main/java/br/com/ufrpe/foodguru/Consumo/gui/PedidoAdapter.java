package br.com.ufrpe.foodguru.Consumo.gui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.R;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoHolder> {

    private List<ItemConsumo> itensConsumo;

    public PedidoAdapter(List<ItemConsumo> itensConsumo){
        this.itensConsumo = itensConsumo;

    }
    @Override
    public PedidoHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pedidos_list_item, parent, false);

        PedidoHolder holder = new PedidoHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(PedidoHolder holder, int position) {
        holder.numMesa.setText(itensConsumo.get(position).getNumeroMesa());
        holder.quantidade.setText(itensConsumo.get(position).getQuantidade());
        holder.nomePrato.setText(itensConsumo.get(position).getNomePrato());

    }


    @Override
    public int getItemCount() {

        return itensConsumo.size();
    }
}
