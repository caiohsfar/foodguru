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

import br.com.ufrpe.foodguru.Prato.dominio.Prato;
import br.com.ufrpe.foodguru.R;

public class DetalhesPratoClienteActvity extends AppCompatActivity {
    private ImageView imagem;
    private TextView descricao,nome;
    private Prato pratoSelecionado;
    private Button btnPedir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_prato_cliente);
        setupViews();
    }

    public void setupViews(){
        imagem = findViewById(R.id.ivDetalhesImagemPratoC);
        descricao= findViewById(R.id.etDetalhesDescricaoPratoC);
        nome = findViewById(R.id.etDetalhesNomePratoC);

        String nomePrato = getIntent().getStringExtra("nome");
        String descricaoPrato = getIntent().getStringExtra("descricao");
        String imagem = getIntent().getStringExtra("imagem");
        pratoSelecionado = new Prato();
        pratoSelecionado.setDescricaoPrato(descricaoPrato);
        pratoSelecionado.setUrlImagem(imagem);
        pratoSelecionado.setNomePrato(nomePrato);
        descricao.setText(pratoSelecionado.getDescricaoPrato());
        nome.setText(pratoSelecionado.getNomePrato());
        downloadImage();


    }

    public void downloadImage(){
        final ImageView finalProgressBar = findViewById(R.id.ivDetalhesImagemPratoC);
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

    public void fazerPedido(View view){

    }


}
