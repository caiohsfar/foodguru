package br.com.ufrpe.foodguru.estabelecimento.GUI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.cliente.GUI.HomeClienteActivity;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Endereco;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.estabelecimento.negocio.EstabelecimentoServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.usuario.GUI.LoginActivity;
import br.com.ufrpe.foodguru.usuario.negocio.UsuarioServices;

public class EditarDadosEstabelecimentoActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseUser currentUser = FirebaseHelper.getFirebaseAuth().getCurrentUser();
    private EditText etNome, etTelefone, etCidade, etRua, etComplemento, etEmail, etSenha;
    private Spinner spEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_dados_estabelecimento);
        recuperarDados();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirmarEdicaoDadosEstabelecimento:{
                confirmarEdicao();
                break;
            }
        }
    }

    private void recuperarDados(){
        FirebaseHelper.getFirebaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseUser usuario = FirebaseHelper.getFirebaseAuth().getCurrentUser();
                Endereco endereco = dataSnapshot.child("Estabelecimentos")
                        .child(FirebaseHelper.getUidUsuario())
                        .child("endereco")
                        .getValue(Endereco.class);
                String telefone = dataSnapshot.child("Estabelecimentos")
                        .child(FirebaseHelper.getUidUsuario())
                        .child("telefone").getValue(String.class);
                if (usuario != null) {/*&& endereco!= null)*/
                    setUpEditTexts(usuario, endereco,telefone);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setUpEditTexts(FirebaseUser usuario, Endereco endereco, String telefone){
        etNome =  findViewById(R.id.etNomeEstabelecimentoed);
        etTelefone = findViewById(R.id.etTelefoneEstabelecimentoed);
        spEstado = findViewById(R.id.spEstadoEstabelecimentoed);
        etCidade = findViewById(R.id.etCidadeEstabelecimentoed);
        etRua = findViewById(R.id.etRuaEstabelecimentoed);
        etComplemento = findViewById(R.id.etComplementoEstabelecimentoed);
        etEmail = findViewById(R.id.etEmailEstabelecimentoed);
        etSenha = findViewById(R.id.etSenhaEstabelecimentoed);

        etNome.setText(currentUser.getDisplayName());
        etTelefone.setText(telefone);
        etCidade.setText(endereco.getCidade());
        etRua.setText(endereco.getRua());
        etComplemento.setText(endereco.getComplemento());
        etEmail.setText(currentUser.getEmail());
    }

    private void confirmarEdicao() {
        if(!validarCampos()){
            return;
        }

        if(Helper.isConected(this)){
            validarEmailSenha();
        }
    }

    private boolean validarCampos(){
        boolean validacao = true;
        if (etNome.getText().toString().trim().isEmpty()){
            etNome.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if ((!Helper.verificaExpressaoRegularEmail(etEmail.getText().toString())) ||
                etEmail.getText().toString().trim().length() == 0) {
            etEmail.setError(getString(R.string.sp_excecao_email));
            validacao = false;
        }
        if (etCidade.getText().toString().isEmpty()) {
            etCidade.setError(getString(R.string.sp_excecao_senha));
            validacao = false;
        }
        if (etTelefone.getText().toString().isEmpty()) {
            etTelefone.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }

        if (etRua.getText().toString().isEmpty()) {
            etRua.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (etTelefone.getText().toString().isEmpty()) {
            etRua.setError(getString(R.string.alerta_campo_vazio));
            validacao = false;
        }
        if (spEstado.getSelectedItemPosition() == 0){
            validacao = false;
            Helper.criarToast(EditarDadosEstabelecimentoActivity.this, "Selecione seu estado.");
        }
        if (etSenha.getText().toString().trim().isEmpty()){
            etSenha.setError("Preencha o campo vazio");
            validacao = false;
        }
        return validacao;
    }

    private void validarEmailSenha(){
        FirebaseHelper.getFirebaseAuth().signInWithEmailAndPassword(currentUser.getEmail(), etSenha.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            alterarEmail(etEmail.getText().toString());
                            alterarNome(etNome.getText().toString());
                            editarEstabelecimento(criarEstabelecimento());
                            Helper.criarToast(getApplicationContext(), "Dados editados com sucesso");
                            abrirTelaEstabelecimento();
                        } else {
                            etSenha.setError("Senha incorreta.");
                        }
                    }
                });
    }

    private void alterarEmail(String email){
        UsuarioServices usuarioServices = new UsuarioServices();
        if (usuarioServices.alterarEmail(email)) {
            finish();
        } else {
            Helper.criarToast(EditarDadosEstabelecimentoActivity.this, "Database error");
        }
    }

    private void alterarNome(String nome){
        UsuarioServices usuarioServices = new UsuarioServices();
        if (usuarioServices.alterarNome(nome)) {
            finish();
        } else {
            Helper.criarToast(EditarDadosEstabelecimentoActivity.this, "Database error");
        }
    }
    private void alterarEndereco(Endereco endereco){
        EstabelecimentoServices estabelecimentoServices = new EstabelecimentoServices();
        if (!estabelecimentoServices.editarEndereco(endereco)){
            Helper.criarToast(this, "Erro ao editar endereço");
        }
    }
    private void alterarTelefone(String telefone){
        EstabelecimentoServices estabelecimentoServices = new EstabelecimentoServices();
        if (!estabelecimentoServices.editarTelefone(telefone)){
            Helper.criarToast(this, "Erro ao editar telefone");
        }
    }

    public void editarEstabelecimento(Estabelecimento estabelecimento){
        alterarTelefone(estabelecimento.getTelefone());
        alterarEndereco(estabelecimento.getEndereco());
    }

    public Estabelecimento criarEstabelecimento(){
        Estabelecimento estabelecimento = new Estabelecimento();
        estabelecimento.setTelefone(etTelefone.getText().toString());
        estabelecimento.setEndereco(new Endereco(
                etCidade.getText().toString()
                ,etRua.getText().toString()
                ,spEstado.getSelectedItem().toString()
                ,etComplemento.getText().toString()));
        return estabelecimento;
    }
    public void abrirTelaEstabelecimento(){
        Intent intent = new Intent(EditarDadosEstabelecimentoActivity.this, HomeEstabelecimentoActivity.class);
        finish();
        startActivity(intent);
    }

}
