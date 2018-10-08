package br.com.ufrpe.foodguru.usuario.GUI;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import br.com.ufrpe.foodguru.R;

public class RecuperarSenhaActivity extends AppCompatActivity {
    private EditText edtResgatarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        edtResgatarSenha = (EditText) findViewById(R.id.etEmailRecuperar);
    }

    public void enviarEmail(View view){
        if (edtResgatarSenha.getText().length() == 0){
            edtResgatarSenha.setError(getString(R.string.sp_excecao_campo_vazio));
        }else {
            resgatarSenhaViaEmail(edtResgatarSenha.getText().toString());
        }
    }
    public void resgatarSenhaViaEmail(String email){
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RecuperarSenhaActivity.this, R.string.sp_email_enviado_sucesso, Toast.LENGTH_SHORT).show();
                    RecuperarSenhaActivity.this.onBackPressed();
                }else {
                    edtResgatarSenha.setError(getString(R.string.sp_excecao_email));
                }
            }
        });
    }
}
