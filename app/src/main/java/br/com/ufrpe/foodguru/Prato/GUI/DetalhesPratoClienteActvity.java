package br.com.ufrpe.foodguru.Prato.GUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.UUID;

import br.com.ufrpe.foodguru.Consumo.dominio.ItemConsumo;
import br.com.ufrpe.foodguru.Consumo.dominio.SessaoConsumo;
import br.com.ufrpe.foodguru.Consumo.negocio.ConsumoServices;
import br.com.ufrpe.foodguru.Mesa.dominio.Mesa;
import br.com.ufrpe.foodguru.Mesa.negocio.MesaServices;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;
import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.infraestrutura.persistencia.FirebaseHelper;
import br.com.ufrpe.foodguru.infraestrutura.utils.StatusMesaEnum;

public class DetalhesPratoClienteActvity extends AppCompatActivity implements View.OnClickListener{
    private ImageView imagem;
    private EditText etObservacao, etQuantidade, descricao, nome;
    private Prato pratoSelecionado;
    private Button btnPedir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_prato_cliente);
        pratoSelecionado = getIntent().getExtras().getParcelable("prato");
        setupViews();
    }

    public void setupViews(){
        imagem = findViewById(R.id.ivDetalhesImagemPratoC);
        descricao = findViewById(R.id.etDetalhesDescricaoPratoC);
        nome = findViewById(R.id.etDetalhesNomePratoC);
        descricao.setText(pratoSelecionado.getDescricaoPrato());
        nome.setText(pratoSelecionado.getNomePrato());
        nome.setKeyListener( null );
        descricao.setKeyListener( null );
        etObservacao = findViewById(R.id.etObservacaoPratoC);
        etQuantidade = findViewById(R.id.etQuantidadePratoC);
        findViewById(R.id.btnPedir).setOnClickListener(this);
        downloadImage();
    }

    public void downloadImage(){
        final ProgressBar finalProgressBar = findViewById(R.id.progress_bar_detalhe_prato_cliente);
        finalProgressBar.setVisibility(View.VISIBLE);
        Picasso.get()
                .load(pratoSelecionado.getUrlImagem())
                .into(imagem, new Callback() {
                    @Override
                    public void onSuccess() {
                        finalProgressBar.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPedir:{
                fazerPedido();
                break;
            }
        }
    }
    public ItemConsumo criarItemConsumo(){
        ItemConsumo itemConsumo = new ItemConsumo();
        itemConsumo.setId(UUID.randomUUID().toString());
        Mesa mesa = SessaoConsumo.getInstance().getConsumo().getMesa();
        itemConsumo.setMesa(mesa);
        itemConsumo.setHoraPedido(getHorario());
        itemConsumo.setPrato(pratoSelecionado);
        itemConsumo.setUidEstabelecimento(mesa.getUidEstabelecimento());
        itemConsumo.setIdConsumo(SessaoConsumo.getInstance().getConsumo().getId());
        itemConsumo.setQuantidade(Integer.parseInt(etQuantidade.getText().toString()));
        itemConsumo.setValor(itemConsumo.getQuantidade(), pratoSelecionado.getPreco());
        itemConsumo.setIdCliente(FirebaseHelper.getUidUsuario());
        if (!etObservacao.getText().toString().isEmpty()){
            itemConsumo.setObservacao(etObservacao.getText().toString());
        }
        return itemConsumo;
    }
    public void fazerPedido(){
        MesaServices mesaServices = new MesaServices();
        if (!validarCampos()){
            return;
        }
        ItemConsumo itemConsumo = criarItemConsumo();
        ConsumoServices consumoServices = new ConsumoServices();
        consumoServices.adicionarItemConsumo(itemConsumo);
        SessaoConsumo.getInstance().getConsumo().getListaItens().add(itemConsumo);
        mesaServices.mudarStatus(itemConsumo.getMesa(), StatusMesaEnum.PENDENTE.getTipo());
        finish();
    }
    public boolean validarCampos() {
        boolean validacao = true;
        if (etQuantidade.getText().toString().trim().isEmpty()) {
            etQuantidade.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        return validacao;
    }

    public String  getHorario(){
        Calendar data = Calendar.getInstance();
        String hora = Integer.toString(data.get(Calendar.HOUR_OF_DAY));
        if (hora.length() == 1){
            hora = "0" + hora;
        }
        String min = Integer.toString(data.get(Calendar.MINUTE));
        if(min.length() == 1){
            min = "0" + min;
        }
        return hora + ":" + min;
    }

}
