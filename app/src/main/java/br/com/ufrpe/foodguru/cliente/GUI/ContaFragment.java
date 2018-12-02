package br.com.ufrpe.foodguru.cliente.GUI;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import br.com.ufrpe.foodguru.Consumo.dominio.Consumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumoAdapter;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Estabelecimento;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;
import br.com.ufrpe.foodguru.infraestrutura.utils.SPUtil;
import br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum;
import br.com.ufrpe.foodguru.pagseguro.PagSeguroCheckout;
import br.com.ufrpe.foodguru.pagseguro.PagSeguroFactory;
import br.com.ufrpe.foodguru.pagseguro.PagSeguroItem;
import br.com.ufrpe.foodguru.pagseguro.PagSeguroPayment;
import br.com.ufrpe.foodguru.pagseguro.util.PagSeguroUtil;

import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.REFERENCIA_ESTABELECIMENTO;
import static br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper.getFirebaseReference;


public class ContaFragment extends android.support.v4.app.Fragment implements View.OnClickListener{
    private View inflatedLayout;
    private RecyclerView mRecyclerView;
    private List<ItemConsumo> itemConsumoList;
    private List<ItemConsumo> pedidosTotaisList = new LinkedList<>();
    private ItemConsumoAdapter adapter;
    private Spinner formaPagamento;
    private Consumo consumoAtual = SessaoConsumo.getInstance().getConsumo();
    private MesaServices mesaServices = new MesaServices();
    private Mesa mesa;
    private Context mContext;
    private String checkoutAuthCode;
    private final int PAG_SEGURO_OPTION = 1;
    private final int SPINNER_DEFAULT_OPTION = 0;

    public ContaFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflatedLayout = inflater.inflate(R.layout.fragment_conta, container, false);
        // Inflate the layout for this fragment
        mesa = SessaoConsumo.getInstance().getConsumo().getMesa();
        inflatedLayout.findViewById(R.id.btn_finalizar_conta).setOnClickListener(this);
        formaPagamento = inflatedLayout.findViewById(R.id.sp_tipo_pagamento);
        mContext = inflatedLayout.getContext();
        iniciarRecyclerView();
        getCheckoutAuthCode();
        return inflatedLayout;
    }

    private ItemConsumoAdapter.ItemConsumoOnClickListener onClickListener() {
        return new ItemConsumoAdapter.ItemConsumoOnClickListener() {
            @Override
            public void onClickCronometro(ItemConsumoAdapter.ItemConsumoHolder itemConsumoHolder, int indexPedido) {
                abrirTelaCronometro(itemConsumoList.get(indexPedido).getId(), itemConsumoList.get(indexPedido).getPrato().getNomePrato());
            }
        };
    }

    private void abrirTelaCronometro(String id, String nome) {
        Intent intent = new Intent(getContext(), CronometroActivity.class);
        Log.d("ID", id);
        intent.putExtra("ID_ITEM",id);
        intent.putExtra("NOME_PRATO", nome);
        startActivity(intent);
    }

    public void iniciarRecyclerView(){
        mRecyclerView = (RecyclerView) inflatedLayout.findViewById(R.id.recycler_view_conta);
        LinearLayoutManager layoutManager = new LinearLayoutManager(inflatedLayout.getContext()
                , LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        itemConsumoList = consumoAtual.getListaItens();
        adapter = new ItemConsumoAdapter(inflatedLayout.getContext(), itemConsumoList, onClickListener());
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
        if (formaPagamento.getSelectedItemPosition() == SPINNER_DEFAULT_OPTION){
            Helper.criarToast(inflatedLayout.getContext(), "Informe uma forma de pagamento.");
            return;
        }else if (formaPagamento.getSelectedItemPosition() == PAG_SEGURO_OPTION){
            if (!NetworkUtils.isConnected()){
                Helper.criarToast(getActivity(), "Sem conexão com a internet");
                return;
            }
            //Deixar o codigo de altorização setado para quando ele resolva fechar a conta com pag seguro;
            iniciarPagSeguro();
            return;
        }
        //idConsumoAtualNull();
        SessaoConsumo.getInstance().reset();
        //se a conta estiver vazia ou tiver sido finalizada, o consumo atual será setado como nulo.. sem itens na conta.
        Helper.criarToast(inflatedLayout.getContext(),"Conta finalizada.");
        exibirTelaHomeCliente();
    }



    public void addSingleValueEventStatus(){
        final DatabaseReference database = getFirebaseReference().child(REFERENCIA_ESTABELECIMENTO)
                .child(consumoAtual.getMesa().getUidEstabelecimento())
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
                //idConsumoAtualNull();
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
        SharedPreferences sharedPreferences = SPUtil.getSharedPreferences(inflatedLayout.getContext());
        SPUtil.putString(sharedPreferences, getString(R.string.consumo_atual), null);
        Intent intent = new Intent(getContext(), HomeClienteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    /*
    private void idConsumoAtualNull() {
        MesaServices mesaServices = new MesaServices();
        mesaServices.mudarIdConsumoAtual(mesa, "ND");
    }
    */
    public void iniciarPagSeguro(){
        final PagSeguroFactory pagseguro = PagSeguroFactory.instance();
        List<PagSeguroItem> shoppingCart = itemConsListToPagSegList(consumoAtual.getListaItens());
        //shoppingCart.add(pagseguro.item("123", "PlayStation", BigDecimal.valueOf(3.50), 1, 300));
        //PagSeguroPhone buyerPhone = pagseguro.phone(PagSeguroAreaCode.DDD81, "998187427");
        //PagSeguroBuyer buyer = pagseguro.buyer("Ricardo Ferreira", "14/02/1978", "15061112000", "test@email.com.br", buyerPhone);
        //PagSeguroAddress buyerAddress = pagseguro.address("Av. Boa Viagem", "51", "Apt201", "Boa Viagem", "51030330", "Recife", PagSeguroBrazilianStates.PERNAMBUCO);
        //PagSeguroShipping buyerShippingOption = pagseguro.shipping(PagSeguroShippingType.PAC, buyerAddress);
        PagSeguroCheckout checkout = pagseguro.checkout("Ref0001", shoppingCart);
        // starting payment process
        PagSeguroPayment payment = new PagSeguroPayment(getActivity());
        payment.pay(checkout.buildCheckoutXml(), checkoutAuthCode);
    }
    private void getCheckoutAuthCode(){
        final DatabaseReference databaseReference = FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_ESTABELECIMENTO)
                .child(SessaoConsumo.getInstance().getConsumo().getMesa().getUidEstabelecimento());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Estabelecimento estabelecimento = dataSnapshot.getValue(Estabelecimento.class);
                    Log.d("PAG", "ESTABELECIMENTO" + estabelecimento.getEndereco().getCidade() + ", " + estabelecimento.getPagSeguroAuthCode());
                    checkoutAuthCode = estabelecimento.getPagSeguroAuthCode();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public List<PagSeguroItem> itemConsListToPagSegList(List<ItemConsumo> itemConsumoList){
        List<PagSeguroItem> pagSegList = new ArrayList<>();
        for (ItemConsumo itemConsumo : itemConsumoList){
            PagSeguroItem pagItem =
                    new PagSeguroItem(itemConsumo.getId()
                            ,itemConsumo.getPrato().getNomePrato()
                            ,BigDecimal.valueOf(itemConsumo.getPrato().getPreco())
                            ,itemConsumo.getQuantidade());

            pagSegList.add(pagItem);
        }
        return pagSegList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_CANCELED) {
            // se foi uma tentativa de pagamento
            if(requestCode==PagSeguroPayment.PAG_SEGURO_REQUEST_CODE){
                // exibir confirmação de cancelamento
                final String msg = getString(R.string.transaction_cancelled);
                PagSeguroUtil.showConfirmDialog(mContext, msg, null);
            }
        } else if (resultCode == getActivity().RESULT_OK) {
            // se foi uma tentativa de pagamento
            if(requestCode==PagSeguroPayment.PAG_SEGURO_REQUEST_CODE){
                // exibir confirmação de sucesso
                final String msg = getString(R.string.transaction_succeded);
                PagSeguroUtil.showConfirmDialog(this.mContext, msg, null);
            }
        }
        else if(resultCode == PagSeguroPayment.PAG_SEGURO_REQUEST_CODE){
            switch (data.getIntExtra(PagSeguroPayment.PAG_SEGURO_EXTRA, 0)){
                case PagSeguroPayment.PAG_SEGURO_REQUEST_SUCCESS_CODE:{
                    final String msg =getString(R.string.transaction_succeded);
                    PagSeguroUtil.showConfirmDialog(this.mContext,msg,null);
                    zerarSessao();
                    break;
                }
                case PagSeguroPayment.PAG_SEGURO_REQUEST_FAILURE_CODE:{
                    final String msg = getString(R.string.transaction_error);
                    PagSeguroUtil.showConfirmDialog(this.mContext,msg,null);
                    break;
                }
                case PagSeguroPayment.PAG_SEGURO_REQUEST_CANCELLED_CODE:{
                    final String msg = getString(R.string.transaction_cancelled);
                    PagSeguroUtil.showConfirmDialog(this.mContext,msg,null);
                    break;
                }
            }
        }
    }
    public void zerarSessao(){
        //idConsumoAtualNull();
        SessaoConsumo.getInstance().reset();
        Helper.criarToast(inflatedLayout.getContext(),"Transação concluída com sucesso. Conta finalizada.");
        exibirTelaHomeCliente();
    }

}


