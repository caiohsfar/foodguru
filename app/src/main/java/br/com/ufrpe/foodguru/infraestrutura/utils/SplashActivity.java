package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private final int TEMPO_SPLASH = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startHandler();
    }

    private void startHandler() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startLoginActivity();
            }
        }, TEMPO_SPLASH);

    }

    private void startLoginActivity() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }


}

