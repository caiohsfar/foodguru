package br.com.ufrpe.foodguru.Consumo.persistencia;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.*;

public class ConsumoDAO {
    private final DatabaseReference database = getFirebaseReference();

    public boolean adicionarItemConsumo(ItemConsumo itemConsumo) {
        boolean sucess = true;

        try {
            database.child(REFERENCIA_ITEM_CONSUMO)
                    .push().setValue(itemConsumo);

        } catch (DatabaseException e) {
            sucess = false;
        }
        return sucess;
    }

    public boolean setItemComoEntregue(ItemConsumo itemConsumo) {
        boolean sucess = true;
        try {
            database.child(REFERENCIA_ITEM_CONSUMO)
                    .child(itemConsumo.getId())
                    .child("entregue").setValue(true);
        } catch (DatabaseException e) {
            sucess = false;
        }
        return sucess;
    }

    public static List<ItemConsumo> getPedidos(DataSnapshot dataSnapshot) {
        List<ItemConsumo> itensConsumo = new LinkedList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            ItemConsumo item = ds.getValue(ItemConsumo.class);
            if (item.isEntregue() == false) {
                item.setId(ds.getKey());
                itensConsumo.add(item);
            }
        }
        return itensConsumo;
    }
    public static String adicionarConsumo(Consumo consumo){
        String id = null;
        try {
            DatabaseReference df = FirebaseHelper.getFirebaseReference().child(REFERENCIA_CONSUMO).push();
            id = df.getKey();
            df.setValue(consumo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return id;

    }
    public boolean setFormaPagamento(Consumo consumo) {
        boolean sucess = true;
        try {
            database.child(REFERENCIA_CONSUMO)
                    .child(consumo.getId())
                    .child("formaPagamento")
                    .setValue(consumo.getFormaPagamento());
        } catch (Exception e) {
            e.printStackTrace();
            sucess = false;
        }
        return sucess;
    }

    /*
    query para listar os Pedidos na activity
        getFirebaseReference().child(REFERENCIA_ITEM_CONSUMO).child("mesa)
        .orderByChild("uidEstabelecimento")
                .equalTo(FirebaseHelper.getUidUsuario()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()){
                            lista = ConsumoDAO.getPedidos(dataSnapshot);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    */
}
