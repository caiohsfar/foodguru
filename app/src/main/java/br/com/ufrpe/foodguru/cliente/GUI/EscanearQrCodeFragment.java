package br.com.ufrpe.foodguru.cliente.GUI;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.infraestrutura.utils.OrientacaoQrCodeActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class EscanearQrCodeFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private Activity activity;
    private View inflatedLayout;
    private final int CAMERA_TRASEIRA = 0;


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
    public void escanearCodigo(){
        IntentIntegrator integratorQrCode = new IntentIntegrator(activity);
        setUpIntegratorQrCode(integratorQrCode);
        integratorQrCode.initiateScan();
    }
    public void setUpIntegratorQrCode(IntentIntegrator integrator){
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Mire no QrCode para conectar-se à mesa.");
        integrator.setCameraId(CAMERA_TRASEIRA);
        integrator.setCaptureActivity(OrientacaoQrCodeActivity.class);
        integrator.setOrientationLocked(false);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if (resultado != null){
            if  (resultado.getContents() != null){
                String codigoMesa = resultado.getContents();
                //validarCodigoMesa(codigoMesa);
            }else{
                Helper.criarToast(context,
                        "Cancelado.");
            }
        }else{
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
