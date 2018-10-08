package br.com.ufrpe.foodguru.estabelecimento.GUI;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.estabelecimento.dominio.Mesa;
import br.com.ufrpe.foodguru.estabelecimento.dominio.MesaHolder;
import br.com.ufrpe.foodguru.estabelecimento.dominio.MesaView;
import br.com.ufrpe.foodguru.estabelecimento.negocio.MesaServices;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MesasFragment extends Fragment{
    private MesaAdapter adapter;
    private RecyclerView mRecyclerView;
    private MesaServices mesaServices = new MesaServices();
    private ProgressDialog mProgressDialog;
    private List<MesaView> mesasViews;
    private ActionMode actionMode;
    private View viewInflado;
    private FloatingActionButton button;

    private  ActionMode.Callback getActionModeCallback(){
        return new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                menu.findItem(R.id.acao_editar).setVisible(false);
                return true;
            }

            @Override
            //quando aparece o menu e as opções de menu exibidas para serem clicadas
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.selecionar_todos:{
                        selecionarTodos();
                        break;
                    }
                    case R.id.deletar:{
                        exibirAlertDialog();
                        break;
                    }
                    case R.id.acao_editar:{
                        // quando abrir tem que passar o id da mesa pra poder editar na utra tela
                        abrirTelaEditarMesa();
                        actionMode.finish();
                        break;
                    }
                }

                return true;
            }


            @Override
            public void onDestroyActionMode(ActionMode mode) {
                actionMode = null;
                for (MesaView mesa : mesasViews) {
                    mesa.setSelecionado(false);
                }
                mRecyclerView.getAdapter().notifyDataSetChanged();

            }
        };
    }
    private void deletarMesas(){
        for (MesaView mesaView : mesasViews){
            if (mesaView.isSelecionado()){
                mesaServices.removerMesa(mesaView.getMesa());
            }
        }
    }

    private void selecionarTodos(){
        for(MesaView mesaView : mesasViews){
            if (!mesaView.isSelecionado()){
                updateActionModeTitle();
                mesaView.setSelecionado(true);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }
    }
    public MesasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflado = inflater.inflate(R.layout.fragment_mesas, container, false);

        mProgressDialog = new ProgressDialog(viewInflado.getContext());
        iniciarRecyclerView();
        FirebaseHelper.getFirebaseReference().child(FirebaseHelper.REFERENCIA_MESA)
                .orderByChild("uidEstabelecimento").equalTo(FirebaseHelper.getUidUsuario())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mesasViews = mesaToMesaView((ArrayList<Mesa>)mesaServices.loadMesas(dataSnapshot));
                        adapter = new MesaAdapter(getContext(),mesasViews,onClickMesa());
                        mRecyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        return viewInflado;

    }
    private void iniciarActionMode(int indexMesa) {
        actionMode = getActivity().startActionMode(getActionModeCallback());
        MesaView mesa = mesasViews.get(indexMesa);
        mesa.setSelecionado(true);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        updateActionModeTitle();
        updateIconeEditar();
    }
    private MesaAdapter.MesaOnClickListener onClickMesa() {
        return new MesaAdapter.MesaOnClickListener() {
            @Override
            public void onClickMesa(MesaHolder holder, int indexMesa) {
                Mesa mesa = mesasViews.get(indexMesa).getMesa();
                List<MesaView> mesasSelecionadas = getMesasSelecionadas();
                if (actionMode == null) {
                    detalharMesa(mesa);
                } else if (!mesasViews.get(indexMesa).isSelecionado()){
                    selecionarItem(indexMesa);
                }else{
                    desSelecionarItem(indexMesa);
                }
            }
            @Override
            public void onLongClickMesa(MesaHolder holder, int indexMesa) {
                if (actionMode != null) {
                    return;
                }
                iniciarActionMode(indexMesa);
            }

            };
    }

    private void detalharMesa(Mesa mesa) {
        Intent intent = new Intent(getContext(),DetalhesMesaActivity.class);
        intent.putExtra("NUMERO_MESA",mesa.getNumeroMesa());
        intent.putExtra("CODIGO_MESA", mesa.getCodigoMesa());
        startActivity(intent);
    }

    private void desSelecionarItem(int indexMesa) {
        mesasViews.get(indexMesa).setSelecionado(false);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        if (mesasViews.size() == 0){
            actionMode.finish();
        }else{
            updateIconeEditar();
            updateActionModeTitle();
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }

    }

    private void selecionarItem(int indexMesa) {
        mesasViews.get(indexMesa).setSelecionado(true);
        mRecyclerView.getAdapter().notifyDataSetChanged();
        updateIconeEditar();
        updateActionModeTitle();
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
    private void updateIconeEditar(){
        List<MesaView> mesasSelecionadas = getMesasSelecionadas();
        if (mesasSelecionadas.size() == 1){
            actionMode.getMenu().findItem(R.id.acao_editar).setEnabled(true);
            actionMode.getMenu().findItem(R.id.acao_editar).setVisible(true);
        }else if (mesasSelecionadas.size() > 1){
            actionMode.getMenu().findItem(R.id.acao_editar).setEnabled(false);
            actionMode.getMenu().findItem(R.id.acao_editar).setVisible(false);
        }
    }
    private void updateActionModeTitle() {
        List<MesaView> mesasSelecionadas = getMesasSelecionadas();
        if (actionMode != null) {
            if (mesasSelecionadas.size() == 0){
                actionMode.finish();
            }else{
                actionMode.setTitle(String.valueOf(mesasSelecionadas.size()));
            }
        }
    }
    private List<MesaView> getMesasSelecionadas() {
        List<MesaView> list = new ArrayList<MesaView>();
        for (MesaView mesa : this.mesasViews) {
            if (mesa.isSelecionado()) {
                list.add(mesa);
            }
        }
        return list;
    }
    private List<MesaView> mesaToMesaView(ArrayList<Mesa> mesas){
        ArrayList<MesaView> mesasViews = new ArrayList<>();
        for (Mesa mesa : mesas) {
            MesaView mesaView = new MesaView();
            mesaView.setMesa(mesa);
            mesasViews.add(mesaView);
        }
        return mesasViews;
    }


    public void iniciarRecyclerView(){
        mRecyclerView = (RecyclerView) viewInflado.findViewById(R.id.recyclerv_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(viewInflado.getContext()
                , LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new MesaAdapter(getContext(),mesasViews, onClickMesa());
        mRecyclerView.setAdapter(adapter);

    }

    public void abrirTelaEditarMesa(){
        Mesa mesaSelecionada = getMesaSelecionada();
        Intent intent = new Intent(viewInflado.getContext(),EditarMesaActivity.class);
        intent.putExtra("CODIGO_MESA",mesaSelecionada.getCodigoMesa());
        intent.putExtra("ID_MESA",mesaSelecionada.getIdMesa());
        intent.putExtra("NUMERO_MESA", mesaSelecionada.getNumeroMesa());
        startActivity(intent);
    }
    public Mesa getMesaSelecionada() {
        for (MesaView mesaView : mesasViews) {
            if (mesaView.isSelecionado()) {
                return mesaView.getMesa();
            }
        }
        return null;
    }
    public void exibirAlertDialog(){
        AlertDialog.Builder builderDeletar = setarMensagemDeletar();
        setarBotaoNegativoDeletar(builderDeletar);
        setarBotaoPositivoDeletar(builderDeletar);
        exibirTextoConfirmarDelecao(builderDeletar);

    }
    public AlertDialog.Builder setarMensagemDeletar(){
        AlertDialog.Builder  builder = new AlertDialog.Builder(
                getContext());
        builder.setMessage("Você deseja mesmo deletar as mesas?");
        return builder;
    }
    public void setarBotaoNegativoDeletar(AlertDialog.Builder builderDeletar){
        builderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    public void exibirTextoConfirmarDelecao(AlertDialog.Builder builderDeletar){
        AlertDialog alert =  builderDeletar.create();
        alert.setTitle("Confirmação");
        alert.show();
    }
    public void setarBotaoPositivoDeletar(AlertDialog.Builder builderDeletar){
        builderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                deletarMesas();
                actionMode.finish();
            }
        });
    }

}
