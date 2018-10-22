package br.com.ufrpe.foodguru.cliente.GUI;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumoAdapter;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.cliente.GUI.HomeClienteActivity;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;


public class ContaFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private View inflatedLayout;
    private RecyclerView mRecyclerView;
    private List<ItemConsumo> itemConsumoList;
    private ItemConsumoAdapter adapter;
    private Spinner formaPagamento;
    private Consumo consumoAtual = SessaoConsumo.getInstance().getConsumo();

    public ContaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_conta, container, false);
        // Inflate the layout for this fragment
        inflatedLayout.findViewById(R.id.btn_finalizar_conta).setOnClickListener(this);
        formaPagamento = inflatedLayout.findViewById(R.id.sp_tipo_pagamento);
        iniciarRecyclerView();
        return inflatedLayout;
    }
    public void iniciarRecyclerView(){
        mRecyclerView = (RecyclerView) inflatedLayout.findViewById(R.id.recycler_view_conta);
        LinearLayoutManager layoutManager = new LinearLayoutManager(inflatedLayout.getContext()
                , LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        itemConsumoList = consumoAtual.getListaItens();
        adapter = new ItemConsumoAdapter(itemConsumoList);
        mRecyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finalizar_conta:{
                finalizarConta();
                break;
            }
        }
    }

    private void finalizarConta() {
        if (formaPagamento.getSelectedItemPosition() == 0){
            Helper.criarToast(inflatedLayout.getContext(), "Informe uma forma de pagamento.");
            return;
        }
        SessaoConsumo.getInstance().reset();
        Helper.criarToast(inflatedLayout.getContext(),"Conta finalizada.");
        abrirTelaLogin();
    }
    public void abrirTelaLogin(){
        Intent intent = new Intent(inflatedLayout.getContext(), HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}