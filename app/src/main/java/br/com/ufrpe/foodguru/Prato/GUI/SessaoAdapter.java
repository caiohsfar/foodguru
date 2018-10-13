package br.com.ufrpe.foodguru.Prato.GUI;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import br.com.ufrpe.foodguru.Prato.GUI.SessaoHolder;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;


public class SessaoAdapter extends RecyclerView.Adapter<SessaoHolder> {
    private List<SessaoCardapio> sessoes;




    public SessaoAdapter(List<SessaoCardapio> sessoes){
        this.sessoes = sessoes;
    }

    @Override
    public SessaoHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sessao_list_item, parent, false);

        SessaoHolder holder = new SessaoHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(final SessaoHolder holder, final  int position) {
        holder.nome.setText(sessoes.get(position).getNome());



    }


    @Override
    public int getItemCount() {

        return sessoes.size();
    }
}
