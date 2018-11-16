package br.com.ufrpe.foodguru.estabelecimento.persistencia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Map;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Endereco;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;


public class EstabelecimentoDAO {
    private  DatabaseReference database = FirebaseHelper.getFirebaseReference();
    public boolean adicionarEstabelecimento(Estabelecimento estabelecimento){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .setValue(estabelecimento);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;

    }
    public boolean updateEndereco(Endereco endereco){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child("endereco").setValue(endereco);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;

    }
    public boolean updateTelefone(String telefone){
        boolean sucess = true;
        try {
        database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                .child("telefone").setValue(telefone);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    public boolean addPagAuthCode(String authCode){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child("pagSeguroAuthCode").setValue(authCode);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

}
