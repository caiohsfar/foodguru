package br.com.ufrpe.foodguru.Prato.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.SessaoCardapio;

public class SessaoActvity extends AppCompatActivity {
    private RecyclerView recyclerSessao;
    private SessaoAdapter adapter;
    private List<SessaoCardapio> sessoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessao);
        setupViews();

    }
    private void setupViews(){
        // falta buscar a lista de sessões do firebase
        this.sessoes = new ArrayList();
        this.recyclerSessao = findViewById(R.id.recyclerSessao);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        recyclerSessao.setLayoutManager(layoutManager);
        adapter = new SessaoAdapter(sessoes);
        recyclerSessao.setAdapter(adapter);
    }
}
