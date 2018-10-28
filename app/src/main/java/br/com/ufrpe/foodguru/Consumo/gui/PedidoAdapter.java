package br.com.ufrpe.foodguru.Consumo.gui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoHolder> {

    private List<ItemConsumo> itensConsumo;
    private Context mContext;
    private final PedidoAdapter.MenuClickListener menuClickListener;

    public interface MenuClickListener{
        void marcarComoEntregue(int position);
        void iniciarPreparoPrato(int position);
    }
    public PedidoAdapter(Context context, List<ItemConsumo> itensConsumo, MenuClickListener listener){
        this.itensConsumo = itensConsumo;
        this.mContext = context;
        this.menuClickListener = listener;

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
    public void onBindViewHolder(final PedidoHolder holder, final int position) {
        holder.numMesa.setText("Mesa: " + itensConsumo.get(position).getMesa().getNumeroMesa());
        holder.quantidade.setText("Quantidade: "+ String.valueOf(itensConsumo.get(position).getQuantidade()));
        holder.nomePrato.setText(itensConsumo.get(position).getPrato().getNomePrato());
        holder.observacao.setText(itensConsumo.get(position).getObservacao());
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.menu,position);
            }
        });
    }
    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_pedidos, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
        popup.show();
    }
    public void updateData(List<ItemConsumo> lista){
        this.itensConsumo.clear();
        this.itensConsumo = lista;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return itensConsumo.size();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        public int position;

        public MyMenuItemClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.marcar_entregue:
                    menuClickListener.marcarComoEntregue(position);
                    return true;
                case R.id.inicio_preparo_prato:
                    menuClickListener.iniciarPreparoPrato(position);
                    return true;
                default:
            }
            return false;
        }
    }

}
