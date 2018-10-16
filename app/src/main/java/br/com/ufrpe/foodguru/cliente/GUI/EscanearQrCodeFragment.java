package br.com.ufrpe.foodguru.cliente.GUI;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.infraestrutura.utils.OrientacaoQrCodeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class EscanearQrCodeFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private Activity activity;
    private View inflatedLayout;
    private final int BARCODE_REQUEST = 0;


    public EscanearQrCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedLayout = inflater.inflate(R.layout.fragment_escanear_qr_code, container, false);
        inflatedLayout.findViewById(R.id.btn_escanear).setOnClickListener(this);
        context = getContext();
        activity = getActivity();
        return inflatedLayout;
    }
    public void validarCodigo(String codigo){
        FirebaseHelper.getFirebaseReference()
                .child(FirebaseHelper.REFERENCIA_MESA)
                .orderByChild("codigoMesa")
                .equalTo(codigo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Mesa mesa = ds.getValue(Mesa.class);
                        abrirTelaCardapio(mesa);
                        break;
                    }
                }else{
                    Helper.criarToast(getContext(), "Este código não pertence a uma mesa.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void abrirTelaCardapio(Mesa mesa) {
        Intent intent = new Intent(getContext(),CardapioActivity.class);
        intent.putExtra("mesa", mesa);
        startActivity(intent);
    }

    public void escanearCodigo(){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        startActivityForResult(intent, BARCODE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_REQUEST && resultCode == getActivity().RESULT_OK){
            String codigo = data.getStringExtra("SCAN_RESULT");
            validarCodigo(codigo);
        }else{
            Helper.criarToast(getContext(), "Cancelado");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_escanear:{
                escanearCodigo();
                break;
            }
        }
    }
}
