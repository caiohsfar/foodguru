package br.com.ufrpe.foodguru.Prato.GUI;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;


public class SessaoAdapter extends ArrayAdapter<SessaoCardapio> {
    private List<SessaoCardapio> sessoes;
    private Context context;

    private class SessaoHolder {
        TextView nome;
    }

    public SessaoAdapter(Context context, int resource, ArrayList<SessaoCardapio> sessoesCardapio) {
        super(context, resource);
        sessoes = sessoesCardapio;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SessaoHolder sessaoHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.sessao_list_item, parent, false);
            sessaoHolder = new SessaoAdapter.SessaoHolder();
            sessaoHolder.nome = convertView.findViewById(R.id.txtNomeSessao);
            convertView.setTag(sessaoHolder);
        }else{
            sessaoHolder = (SessaoAdapter.SessaoHolder) convertView.getTag();
        }
        SessaoCardapio sessao = sessoes.get(position);

        if (sessao != null) {
            sessaoHolder.nome.setText(sessao.getNome());
        }
        return convertView;

    }

}
