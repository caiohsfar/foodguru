package br.com.ufrpe.foodguru.cliente.GUI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.ufrpe.foodguru.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class EscanearQrCodeFragment extends Fragment {


    public EscanearQrCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_escanear_qr_code, container, false);
    }

}
