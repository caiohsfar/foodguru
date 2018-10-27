package br.com.ufrpe.foodguru.cliente.GUI;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumoAdapter;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_ESTABELECIMENTO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getFirebaseReference;


public class ContaFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private View inflatedLayout;
    private RecyclerView mRecyclerView;
    private List<ItemConsumo> itemConsumoList;
    private ItemConsumoAdapter adapter;
    private Spinner formaPagamento;
    private Consumo consumoAtual = SessaoConsumo.getInstance().getConsumo();
    private MesaServices mesaServices = new MesaServices();
    private Mesa mesa;

    public ContaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        setStatusOcupada();
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_conta, container, false);
        // Inflate the layout for this fragment
        mesa = getActivity().getIntent().getExtras().getParcelable("mesa");
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
        if (consumoAtual.getListaItens().isEmpty()){
            exibirConfirmacaoSair();
            return;
        }
        if (formaPagamento.getSelectedItemPosition() == 0){
            Helper.criarToast(inflatedLayout.getContext(), "Informe uma forma de pagamento.");
            return;
        }
        idConsumoAtualNull();
        SessaoConsumo.getInstance().reset();
        Helper.criarToast(inflatedLayout.getContext(),"Conta finalizada.");
        exibirTelaHomeCliente();
    }



    public void addSingleValueEventStatus(){
        final DatabaseReference database = getFirebaseReference().child(REFERENCIA_ESTABELECIMENTO).child(consumoAtual.getMesa().getUidEstabelecimento())
                .child("Mesas").child(consumoAtual.getMesa().getCodigoMesa())
                .child("status");
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                database.setValue(StatusMesaEnum.VAZIA.getTipo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void exibirConfirmacaoSair() {
        AlertDialog.Builder msgBox = new AlertDialog.Builder(getContext());
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setTitle("Sair");
        msgBox.setMessage("Você não pediu nada, deseja mesmo sair?");
        setBtnPositivoSair(msgBox);
        setBtnNegativoSair(msgBox);
        msgBox.show();
    }

    public void setBtnPositivoSair(AlertDialog.Builder msgBox){
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                idConsumoAtualNull();
                SessaoConsumo.getInstance().reset();
                exibirTelaHomeCliente();
            }
        });
    }

    public void setBtnNegativoSair(AlertDialog.Builder msgBox){
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }

    public void exibirTelaHomeCliente(){
        addSingleValueEventStatus();
        Intent intent = new Intent(getContext(), HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void idConsumoAtualNull() {
        MesaServices mesaServices = new MesaServices();
        mesaServices.mudarIdConsumoAtual(mesa, "ND");
    }

    public static void setStatusOcupada(){
        Mesa mesa = SessaoConsumo.getInstance().getConsumo().getMesa();
        final DatabaseReference database = getFirebaseReference().child(REFERENCIA_ESTABELECIMENTO).child(mesa.getUidEstabelecimento())
                .child("Mesas").child(mesa.getCodigoMesa())
                .child("status");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int status = dataSnapshot.getValue(Integer.class);
                if(status != StatusMesaEnum.PENDENTE.getTipo()){
                    database.setValue(StatusMesaEnum.OCUPADA.getTipo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

