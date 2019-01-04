package br.com.ufrpe.foodguru.cliente.GUI;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Consumo.negocio.ConsumoServices;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getUidUsuario;
import static br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum.OCUPADA;


/**
 * A simple {@link Fragment} subclass.
 */
public class EscanearQrCodeFragment extends Fragment implements View.OnClickListener{
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
        return inflatedLayout;
    }
    public void validarCodigo(String codigo){
        final MesaServices mesaServices = new MesaServices();
        FirebaseHelper.getFirebaseReference()
                .child(FirebaseHelper.REFERENCIA_MESA)
                .orderByKey()
                .equalTo(codigo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Mesa mesa = ds.getValue(Mesa.class);
                        /*
                        if (!mesa.getIdConsumoAtual().equals("ND")){
                            Helper.criarToast(inflatedLayout.getContext(), "Esta mesa está ocupada.");
                            break;
                        }
                        */
                        mesa.setStatus(OCUPADA.getTipo());
                        mesaServices.mudarStatus(mesa,OCUPADA.getTipo());

                        //Inicia o consumo do cliente a partir do momento em que ele entra no cardápio.
                        Consumo consumo = new Consumo();
                        consumo.setMesa(mesa);
                        consumo.setIdCliente(getUidUsuario());
                        //adiciona o id ao consumo logo após adiciona-lo ao firebase;
                        consumo.setId(getIdConsumo(consumo));
                        SessaoConsumo.getInstance().setConsumo(consumo);
                        //adiciona o id consumo ao IdConsumoAtual
                        //mesaServices.mudarIdConsumoAtual(mesa, consumo.getId());

                        abrirTelaCardapio(mesa);
                        break;
                    }
                } else{
                    Helper.criarToast(getContext(), "Este código não pertence a uma mesa.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String getIdConsumo(Consumo consumo){
        return ConsumoServices.adicionarConsumo(consumo);
    }

    private void abrirTelaCardapio(Mesa mesa) {
        Intent intent = new Intent(getContext(),OperacaoActivity.class);
        intent.putExtra("mesa", mesa);
        startActivity(intent);
    }

    public void escanearCodigo(){
        IntentIntegrator.forSupportFragment(EscanearQrCodeFragment.this)
                .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                .setBeepEnabled(false)
                .setOrientationLocked(false)
                .initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult resultData = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (resultData != null && resultCode == Activity.RESULT_OK) {
            validarCodigo(resultData.getContents());
        }else{
            Helper.criarToast(getContext(), "Cancelado");
            super.onActivityResult(requestCode, resultCode, data);
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
