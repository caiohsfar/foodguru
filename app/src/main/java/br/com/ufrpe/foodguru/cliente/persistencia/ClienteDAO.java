package br.com.ufrpe.foodguru.cliente.persistencia;

import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import br.com.ufrpe.foodguru.cliente.dominio.Cliente;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

public class ClienteDAO {
    private DatabaseReference database = FirebaseHelper.getFirebaseReference();
    public boolean adicionarCliente(Cliente cliente){
        boolean sucess = true;
        try {
            database.child(FirebaseHelper.REFERENCIA_CLIENTE)
                    .child(FirebaseHelper.getFirebaseAuth().getCurrentUser().getUid())
                    .setValue(cliente);
        }catch(DatabaseException e){
            sucess = false;
        }
        return sucess;

    }
}
