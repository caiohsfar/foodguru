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

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_ESTABELECIMENTO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_MESA;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getUidUsuario;

public class MesaDAO {
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();

    public boolean adicionarMesa(Mesa mesa){
        boolean sucess = true;

        try {
            //mudou para teste!!
            database.child(REFERENCIA_ESTABELECIMENTO).child(getUidUsuario())
                    .child(REFERENCIA_MESA).child(mesa.getCodigoMesa()).setValue(mesa);

            database.child(REFERENCIA_MESA).child(mesa.getCodigoMesa()).setValue(mesa);

        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    public boolean removerMesa(Mesa mesa){
        boolean sucess = true;
        try {
            database.child(REFERENCIA_ESTABELECIMENTO).child(getUidUsuario())
                    .child(REFERENCIA_MESA)
                    .child(mesa.getCodigoMesa())
                    .setValue(null);
            database.child(REFERENCIA_MESA).child(mesa.getCodigoMesa()).setValue(null);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    public boolean editarMesa(Mesa mesa){
        boolean sucess = true;
        try {
            database.child(REFERENCIA_ESTABELECIMENTO).child(getUidUsuario())
                    .child(REFERENCIA_MESA)
                    .child(mesa.getCodigoMesa())
                    .setValue(mesa);
            database.child(REFERENCIA_MESA).child(mesa.getCodigoMesa()).setValue(mesa);
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
            mesas.add(mesa);
        }
        return mesas;

    }
    public boolean mudarStatus(Mesa mesa, int status){
        boolean sucess = true;
        try {
            database.child(REFERENCIA_ESTABELECIMENTO).child(mesa.getUidEstabelecimento())
                    .child(REFERENCIA_MESA)
                    .child(mesa.getCodigoMesa())
                    .child("status").setValue(status);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    /*
    public boolean mudarIdConsumoAtual(Mesa mesa, String idConsumoAtual){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_MESA)
                    .child(mesa.getCodigoMesa())
                    .child("idConsumoAtual").setValue(idConsumoAtual);
            database.child(REFERENCIA_ESTABELECIMENTO).child(mesa.getUidEstabelecimento())
                    .child(REFERENCIA_MESA)
                    .child(mesa.getCodigoMesa())
                    .child("idConsumoAtual").setValue(idConsumoAtual);
            database.child(FirebaseHelper.REFERENCIA_MESA)
                    .child(mesa.getCodigoMesa())
                    .child("idConsumoAtual").setValue(idConsumoAtual);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;
    }
    */

}
