package br.com.ufrpe.foodguru.estabelecimento.persistencia;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Prato;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_PRATO;

public class PratoDAO {
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();

    public boolean adicionarMesa(Prato prato){
        boolean sucess = true;

        try {
            DatabaseReference df = database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_PRATO).push();
            df.setValue(prato);

        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
}
