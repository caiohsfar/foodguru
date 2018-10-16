package br.com.ufrpe.foodguru.Consumo.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.R;

public class PedidosActvity extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        recyclerView =findViewById(R.id.recyclerViewPedidos);

        //Lista que vem do firebase
        LinkedList<ItemConsumo> livros = new LinkedList();
        recyclerView.setAdapter(new PedidoAdapter(livros));
    }
}
