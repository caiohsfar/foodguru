package br.com.ufrpe.foodguru.estabelecimento.GUI;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.ufrpe.foodguru.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PedidosFragment extends Fragment {
    private View inflatedLayout;


    public PedidosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_pedidos, container, false);
        // Inflate the layout for this fragment
        return inflatedLayout;
    }


}
