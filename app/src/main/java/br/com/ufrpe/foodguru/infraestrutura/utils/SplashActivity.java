package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.cliente.GUI.CardapioFragment;
import br.com.ufrpe.foodguru.cliente.GUI.HomeClienteActivity;
import br.com.ufrpe.foodguru.cliente.GUI.OperacaoActivity;
import br.com.ufrpe.foodguru.estabelecimento.GUI.HomeEstabelecimentoActivity;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getFirebaseAuth;
import static br.com.ufrpe.foodguru.infraestrutura.utils.TipoContaEnum.*;

public class SplashActivity extends AppCompatActivity {
    private final int TEMPO_SPLASH = 1000;
    private SharedPreferences preferences;
    private String tipoConta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = SPUtil.getSharedPreferences(this);
        setContentView(R.layout.activity_splash);
        tipoConta = preferences.getString(getString(R.string.acc_type_key), "");
        startHandler();
    }

    private void startHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (getFirebaseAuth().getCurrentUser() != null
                        && tipoConta.equals(ESTABELECIMENTO.getTipo()))
                {
                   abrirTelaEstabelecimento();
                }else if (getFirebaseAuth().getCurrentUser() != null
                        && tipoConta.equals(CLIENTE.getTipo()))
                {
                    abrirTelaCliente();
                }else{
                    startLoginActivity();
                }
            }
        }, TEMPO_SPLASH);
    }

    private void abrirTelaCliente(){
        String json = preferences.getString(getString(R.string.consumo_atual), null);
        Log.d("CONSUMO", "jsonAbrirTelaCliente:" + json);
        if (json != null){
            SessaoConsumo.getInstance().setConsumo(jsonToConsumo(json));
            abrirTelaCardapio();
            return;
        }
        Intent intentAbrirTelaCliente = new Intent(SplashActivity.this, HomeClienteActivity.class);
        startActivity(intentAbrirTelaCliente);
        finish();
    }

    public Consumo jsonToConsumo(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Consumo.class);
    }
    private void abrirTelaCardapio(){
        Intent abrirTelaCardapio = new Intent(SplashActivity.this, OperacaoActivity.class);
        startActivity(abrirTelaCardapio);
        finish();

    }

    private void abrirTelaEstabelecimento() {
        Intent intentAbrirTelaEstabelecimento = new Intent(SplashActivity.this, HomeEstabelecimentoActivity.class);
        startActivity(intentAbrirTelaEstabelecimento);
        finish();
    }

    private void startLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


}

