package br.com.ufrpe.foodguru.usuario.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import br.com.ufrpe.foodguru.cliente.GUI.HomeClienteActivity;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.GUI.HomeEstabelecimentoActivity;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.estabelecimento.GUI.ConfigSeuPSActivity;
import br.com.ufrpe.foodguru.infraestrutura.utils.SPUtil;

import static br.com.ufrpe.foodguru.infraestrutura.utils.TipoContaEnum.*;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private final FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();;
    private EditText edtLoginEmail, edtLoginSenha;
    private ProgressDialog mProgressDialog;
    private SharedPreferences preferences;
    private final DatabaseReference firebaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = SPUtil.getSharedPreferences(LoginActivity.this);
        setUpViews();

    }


    private void setUpViews(){
        edtLoginSenha = findViewById(R.id.edtLoginSenha);
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        findViewById(R.id.btLogin).setOnClickListener(this);
        findViewById(R.id.tv_recuperar_senha).setOnClickListener(this);
        findViewById(R.id.tvCadastre_se).setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
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
                            mProgressDialog.setCanceledOnTouchOutside(true);
                            Helper.criarToast(LoginActivity.this,"Digite uma combinação válida.");
                        }
                    }
                });
    }
    @Override
    protected void onStart() {
        if (FirebaseHelper.getFirebaseAuth().getCurrentUser()!= null){
            FirebaseHelper.getFirebaseAuth().signOut();
        }
        super.onStart();

    }
    private void verificarTipoConta(FirebaseUser cUser){
        final DatabaseReference databaseReference = firebaseReference.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(cUser.getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Estabelecimento estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                            if (estabelecimento.getPagSeguroAuthCode().equals("ND")){
                                abrirTelaPagSeguro();
                            }else{
                                abrirTelaEstabelecimento();
                                finish();
                            }
                        }else{
                            abrirTelaCliente();
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void abrirTelaPagSeguro() {
        Intent intent = new Intent(LoginActivity.this, ConfigSeuPSActivity.class);
        startActivity(intent);
        finish();
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
        Intent intentAbrirTelaCliente = new Intent(LoginActivity.this, HomeClienteActivity.class);
        SPUtil.putString(preferences, getString(R.string.acc_type_key), CLIENTE.getTipo());
        startActivity(intentAbrirTelaCliente);
        finish();
        cleanViews();
    }

    private void abrirTelaEstabelecimento() {
        Intent intentAbrirTelaEstabelecimento = new Intent(LoginActivity.this, HomeEstabelecimentoActivity.class);
        SPUtil.putString(preferences, getString(R.string.acc_type_key), ESTABELECIMENTO.getTipo());
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
