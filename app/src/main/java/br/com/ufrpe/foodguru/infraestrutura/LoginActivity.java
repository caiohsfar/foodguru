package br.com.ufrpe.foodguru.infraestrutura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import br.com.ufrpe.foodguru.EscanearQrCodeFragment;
import br.com.ufrpe.foodguru.LerQrCodeActivity;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.PerfilEstabelecimentoActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static FirebaseAuth mAuth;
    private EditText edtLoginEmail, edtLoginSenha;
    private ProgressDialog mProgressDialog;
    private final DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();
    private final String TIPO_CONTA = "tipoConta";


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseHelper.getFirebaseAuth();
        checkUser(mAuth.getCurrentUser());
        setUpViews();

    }

    private void setUpViews(){
        edtLoginSenha = findViewById(R.id.edtLoginSenha);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        findViewById(R.id.btLogin).setOnClickListener(this);
        findViewById(R.id.tv_recuperar_senha).setOnClickListener(this);
        findViewById(R.id.tvCadastre_se).setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Entrando...");

    }

    private void login(String email, String senha) {
        if (!validarCampos()){
            return;
        }
        mProgressDialog.show();
        mAuth.signInWithEmailAndPassword(email,senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            verificarTipoConta(currentUser);
                        }else {
                            mProgressDialog.dismiss();
                            Helper.criarToast(LoginActivity.this,"Digite uma combinação válida.");
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseHelper.getFirebaseAuth();
        checkUser(mAuth.getCurrentUser());
    }
    private void checkUser(FirebaseUser user) {
        if (user != null) {
            verificarTipoConta(user);
        }

    }
    private void verificarTipoConta(FirebaseUser user) {
        firebaseReference.child(FirebaseHelper.REFERENCIA_USUARIOS)
                .child(user.getUid())
                .child(TIPO_CONTA)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String tipoConta = dataSnapshot.getValue(String.class);
                if (dataSnapshot.getValue(String.class) != null){
                    mProgressDialog.dismiss();
                    if(tipoConta.equals(TipoContaEnum.CLIENTE.getTipo())) {
                        abrirTelaCliente();
                    } else {
                        abrirTelaEstabelecimento();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private boolean validarCampos(){
        boolean validacao = true;

        if (edtLoginEmail.getText().toString().trim().length() == 0){
            edtLoginEmail.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        if (edtLoginSenha.getText().toString().trim().length() == 0){
            edtLoginSenha.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    private void abrirTelaTipoCadastro(){
        Intent intentAbrirTelaRegistro = new Intent(LoginActivity.this, TipoCadastroActivity.class);
        startActivity(intentAbrirTelaRegistro);
        cleanViews();
    }

    private void abrirTelaCliente(){
        Intent intentAbrirTelaCliente = new Intent(LoginActivity.this, LerQrCodeActivity.class);
        startActivity(intentAbrirTelaCliente);
        finish();
        cleanViews();
    }

    private void abrirTelaEstabelecimento() {
        Intent intentAbrirTelaEstabelecimento = new Intent(LoginActivity.this, PerfilEstabelecimentoActivity.class);
        startActivity(intentAbrirTelaEstabelecimento);
        finish();
        cleanViews();
    }

    public void telaRecuperarSenha() {
        Intent intent = new Intent(this, RecuperarSenhaActivity.class);
        cleanViews();
        startActivity(intent);
    }
    private void cleanViews(){
        edtLoginEmail.setText(null);
        edtLoginSenha.setText(null);
    }
    @Override
    public void onClick(View v) {
        if (!Helper.isConected(LoginActivity.this)) {
            Helper.criarToast(LoginActivity.this, "Sem conexão.");
            return;
        }
        switch (v.getId()) {
            case R.id.tv_recuperar_senha:
                telaRecuperarSenha();
                break;
            case R.id.btLogin:
                login(edtLoginEmail.getText().toString().trim()
                        , edtLoginSenha.getText().toString().trim());
                break;
            case R.id.tvCadastre_se:
                abrirTelaTipoCadastro();
                break;
            default:
                break;
            }
    }
}
