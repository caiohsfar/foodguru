package br.com.ufrpe.foodguru.cliente.GUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.usuario.negocio.UsuarioServices;

public class EditarDadosClienteActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseUser currentUser = FirebaseHelper.getFirebaseAuth().getCurrentUser();
    private EditText etNome,etEmail,etSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dados_cliente);
        findViewById(R.id.btnConfirmarEdicao).setOnClickListener(this);
        setUpEditTexts();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirmarEdicao:{
                confirmarEdicao();
                break;
            }
        }
    }

    private void confirmarEdicao() {
        if(!validarCampos()){
            return;
        }
        if(Helper.isConected(this)){
           validarEmailSenha();
        }

    }
    private void setUpEditTexts(){
        etSenha = findViewById(R.id.etSenhaClienteMeusDados);
        etEmail = findViewById(R.id.etEmailClienteMeusDados);
        etNome =  findViewById(R.id.etNomeClienteMeusDados);
        etEmail.setText(currentUser.getEmail());
        etNome.setText(currentUser.getDisplayName());
    }

    private boolean validarCampos(){
        boolean verificador = true;
        if (etSenha.getText().toString().trim().isEmpty()){
            etSenha.setError("Preencha o campo vazio");
            verificador = false;
        }
        if (etNome.getText().toString().trim().isEmpty()) {
            etNome.setError("Preencha o campo vazio.");
            verificador = false;
        }
        if (etEmail.getText().toString().trim().isEmpty()) {
            etEmail.setError("Preencha o campo vazio.");
            verificador = false;
        }
        if (!Helper.verificaExpressaoRegularEmail(etEmail.getText().toString())){
            etEmail.setError("Informe um email válido.");
            verificador = false;
        }

        return verificador;
    }
    private void validarEmailSenha(){
        FirebaseHelper.getFirebaseAuth().signInWithEmailAndPassword(currentUser.getEmail(), etSenha.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            alterarEmail(etEmail.getText().toString());
                            alterarNome(etNome.getText().toString());
                            Helper.criarToast(getApplicationContext(), "Dados editados com sucesso");
                            abrirTelaCliente();
                        } else {
                            etSenha.setError("Senha incorreta.");
                        }
                    }
                });
    }

    private void abrirTelaCliente() {
        Intent intent = new Intent(EditarDadosClienteActivity.this, HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void alterarEmail(String email){
        UsuarioServices usuarioServices = new UsuarioServices();
        if (usuarioServices.alterarEmail(email)) {
            Helper.criarToast(getApplicationContext(), "Email alterado com sucesso.");
            finish();
        } else {
            Helper.criarToast(EditarDadosClienteActivity.this, "Database error");
        }
    }
    private void alterarNome(String nome){
        UsuarioServices usuarioServices = new UsuarioServices();
        if (usuarioServices.alterarNome(nome)) {
            finish();
        } else {
            Helper.criarToast(EditarDadosClienteActivity.this, "Database error");
        }
    }
}
