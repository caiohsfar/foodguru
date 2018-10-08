package br.com.ufrpe.foodguru.estabelecimento.persistencia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    public void atualizaEstabelecimento(Estabelecimento estabelecimento){
        return;
        //vai mudar o banco
        //database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO).child(estabelecimento.getId()).setValue(estabelecimento);

    }



}
