package br.com.ufrpe.foodguru.Mesa.persistencia;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

public class MesaDAO {
    private FirebaseAuth mAuth = FirebaseHelper.getFirebaseAuth();
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();
    private FirebaseUser currentUser = mAuth.getCurrentUser();

    public boolean adicionarMesa(Mesa mesa){
        boolean sucess = true;

        try {
            DatabaseReference df = database.child(FirebaseHelper.REFERENCIA_MESA).push();
            String id = df.getKey();
            mesa.setIdMesa(id);
            df.setValue(mesa);

        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    public boolean removerMesa(Mesa mesa){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_MESA)
                    .child(mesa.getIdMesa())
                    .setValue(null);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    public boolean editarMesa(Mesa mesa){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_MESA)
                    .child(mesa.getIdMesa())
                    .setValue(mesa);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    public List<Mesa> loadMesas(DataSnapshot dataSnapshot){
        List<Mesa> mesas = new ArrayList<>();
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {

            Mesa mesa = ds.getValue(Mesa.class);
            mesa.setIdMesa(ds.getKey());
            mesas.add(mesa);
        }
        return mesas;

    }


}
