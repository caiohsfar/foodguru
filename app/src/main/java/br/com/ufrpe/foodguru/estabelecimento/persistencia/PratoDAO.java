package br.com.ufrpe.foodguru.estabelecimento.persistencia;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Prato;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_PRATO;

public class PratoDAO {
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();

    public boolean adicionarPrato(Prato prato){
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

    public boolean removerPrato(Prato prato){
        boolean sucess = true;
        //deletando a imagem do prato do storage
        FirebaseStorage.getInstance().getReferenceFromUrl(prato.getUrlImagem()).delete();
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_PRATO).child(prato.getIdPrato()).setValue(null);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }

    public List<Prato> loadPratos(DataSnapshot dataSnapshot){
        List<Prato> pratos = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {

            Prato prato = ds.getValue(Prato.class);
            prato.setIdPrato(ds.getKey());
            pratos.add(prato);
        }
        return pratos;
    }

    public boolean editarPrato(Prato prato){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .child(REFERENCIA_PRATO).child(prato.getIdPrato()).setValue(prato);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
}
