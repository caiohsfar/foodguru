package br.com.ufrpe.foodguru.cliente.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.R;

public class CardapioActivity extends AppCompatActivity {
    private TextView tvNumeroMesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardapio);
        Mesa mesa = getIntent().getExtras().getParcelable("mesa");
        tvNumeroMesa = findViewById(R.id.tv_numero_mesa_cardapio);
        tvNumeroMesa.setText(mesa.getNumeroMesa());

    }
}
