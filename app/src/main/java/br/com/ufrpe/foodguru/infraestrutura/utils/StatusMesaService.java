package br.com.ufrpe.foodguru.infraestrutura.utils;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Mesa.GUI.MesasFragment;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

public class StatusMesaService extends Service {
    private Mesa mesaAtual;
    private ValueEventListener valueEventListener;
    private DatabaseReference databaseReference;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mesaAtual = SessaoConsumo.getInstance().getConsumo().getMesa();
        Log.d("ServiceStatusMesa", "O service de status da mesa startou!");
        addListenerStatusMesa();
        return super.onStartCommand(intent, flags, startId);
    }
    public void addListenerStatusMesa(){
        this.databaseReference = FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(mesaAtual.getUidEstabelecimento())
                .child(FirebaseHelper.REFERENCIA_MESA)
                .child(mesaAtual.getCodigoMesa())
                .child("status");

        this.valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //verificar aqui
                Log.d("ServiceStatusMesa",dataSnapshot.getValue().toString());
                if (dataSnapshot.getValue().toString().equals(String.valueOf(StatusMesaEnum.VAZIA.getTipo()))){
                    Log.d("ServiceStatusMesa", "Status é igual a VAZIO (2)");
                    databaseReference.setValue(StatusMesaEnum.OCUPADA.getTipo());
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        databaseReference.addValueEventListener(this.valueEventListener);
    }

    @Override
    public void onDestroy() {
        //Essencial para o status mudar para vazio. Assim ele não mudará para ocupado após feita a mudança para vazio.
        if (this.databaseReference != null){
            this.databaseReference.removeEventListener(this.valueEventListener);
            mudarStatusMesa();
            Log.d("ServiceStatusMesa", "Service Status Mesa destruído!");
        }
        Log.d("ServiceStatusMesa", "Service Status Mesa destruído! DATABASE NULL");


        super.onDestroy();
    }

    private void mudarStatusMesa(){
        //Se a mesa não estiver pendente, mude para VAZIA
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.getValue().toString().equals(String.valueOf(StatusMesaEnum.PENDENTE.getTipo()))){
                    Log.d("ServiceStatusMesa", "Mudando status para VAZIO!");
                    databaseReference.setValue(StatusMesaEnum.VAZIA.getTipo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("ServiceStatusMesa", "Service Status Mesa destruído! task removed");
        this.databaseReference.removeEventListener(this.valueEventListener);
        mudarStatusMesa();
        super.onTaskRemoved(rootIntent);
    }
    */
}
