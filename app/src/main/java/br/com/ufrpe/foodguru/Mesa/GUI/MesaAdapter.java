package br.com.ufrpe.foodguru.Mesa.GUI;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import br.com.ufrpe.foodguru.Consumo.gui.PedidoAdapter;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Mesa.dominio.MesaView;
import br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum;

public class MesaAdapter extends RecyclerView.Adapter<MesaHolder> {
    private final Context context;
    private List<MesaView> mesas;
    private final MesaOnClickListener onClickListener;

    public interface MesaOnClickListener {
        void onClickMesa(MesaHolder holder, int indexMesa);
        void onLongClickMesa(MesaHolder holder, int indexMesa);
        void onClickMenuMesa(int indexMesa);
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
        Mesa mesa = mesas.get(position).getMesa();
        String numero = mesas.get(position).getMesa().getNumeroMesa();
        String codigo = mesas.get(position).getMesa().getCodigoMesa();
        holder.numero.setText(numero);
        holder.codigo.setText(codigo);
        holder.numero.setTag(holder);
        holder.menuMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.menuMesa,position);
            }
        });
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

        if (mesa.getStatus() == StatusMesaEnum.PENDENTE.getTipo()){
            holder.status.setBackgroundColor(Color.YELLOW);
        }else if (mesa.getStatus() == StatusMesaEnum.VAZIA.getTipo()){
            holder.status.setBackgroundColor(Color.GRAY);
        }else{
            holder.status.setBackgroundColor(Color.RED);
        }


    }

    private void showPopupMenu(View view, int position) {
        // inflate menu
        PopupMenu popup = new PopupMenu(context, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_mesa, popup.getMenu());
        popup.setOnMenuItemClickListener(new MesaAdapter.MyMenuMesaClickListener(position));
        popup.show();
    }

    public void updateData(List<MesaView> viewModels) {
        viewModels.clear();
        viewModels.addAll(viewModels);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return this.mesas != null ? this.mesas.size() : 0;
    }
    class MyMenuMesaClickListener implements PopupMenu.OnMenuItemClickListener {
        public int position;

        public MyMenuMesaClickListener(int position) {
            this.position = position;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.detalhes_mesa_menu:
                    onClickListener.onClickMenuMesa(position);
                    return true;
                default:
            }
            return false;
        }
    }


}