package br.com.ufrpe.foodguru.cliente.GUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;
import br.com.ufrpe.foodguru.R;

public class PerfilClienteActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();
    private TextView emailCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cliente);
        findViewById(R.id.btn_log_out).setOnClickListener(this);
        emailCliente = findViewById(R.id.user_email);
        emailCliente.setText(mAuth.getCurrentUser().getEmail());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_log_out:
                if (mAuth.getCurrentUser() != null){
                    mAuth.signOut();
                    Helper.criarToast(this,"Deslogado.");
                    startLoginActivity();
                }

        }
    }
    private void startLoginActivity(){
        Intent loginIntent = new Intent(PerfilClienteActivity.this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }
}
