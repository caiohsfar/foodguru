package br.com.ufrpe.foodguru.estabelecimento;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.SessaoCardapio;

public class SessaoCardapioActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessao_cardapio);
        SessaoCardapio sessaoCardapio1 = new SessaoCardapio();
        sessaoCardapio1.setNome("Bebidas");
        SessaoCardapio sessaoCardapio2 = new SessaoCardapio();
        sessaoCardapio2.setNome("Pizzas");
        SessaoCardapio sessaoCardapio3 = new SessaoCardapio();
        sessaoCardapio3.setNome("Sobremesas");

        ArrayList<SessaoCardapio> listaSessao = new ArrayList();
        listaSessao.add(sessaoCardapio1);
        listaSessao.add(sessaoCardapio2);
        listaSessao.add(sessaoCardapio3);

        final Spinner nomeSessao = (Spinner) findViewById(R.id.spnSessaoCardapio);
        ArrayAdapter<SessaoCardapio> adapter = new ArrayAdapter<SessaoCardapio>(this, android.R.layout.simple_spinner_dropdown_item,listaSessao);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        nomeSessao.setAdapter(adapter);

        AdapterView.OnItemSelectedListener escolhaSessao = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String itemEscolhido = nomeSessao.getSelectedItem().toString();
                // itemEscolhido é a string com o nome da sessão que o usuário escolheu

                // pega essa String e faz uma consulta par retornar de qual sessão é o prato
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

    }
}
