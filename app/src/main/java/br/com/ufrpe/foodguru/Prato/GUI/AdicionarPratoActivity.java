package br.com.ufrpe.foodguru.Prato.GUI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kennyc.bottomsheet.BottomSheet;
import com.kennyc.bottomsheet.BottomSheetListener;

import java.io.IOException;
import java.util.UUID;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;
import br.com.ufrpe.foodguru.Prato.negocio.PratoServices;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class AdicionarPratoActivity extends AppCompatActivity implements View.OnClickListener {
    private StorageReference  storageReference = FirebaseStorage.getInstance().getReference();
    private EditText etNomePrato, etDescricaoPrato, etPrecoPrato;
    private ProgressDialog progressDialog;
    private ImageView imvFoto;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALERY_REQUEST_CODE = 71;
    private UUID uidImagemPrato = UUID.randomUUID();
    private String urlImagemAdicionada;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_prato);
        Helper.verificarPermissaoEscrever(this,AdicionarPratoActivity.this);
        setUpViews();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirmarAdicionarPrato:{
                if(!validarCampos()){
                    return;
                }
                adicionarPrato();
                break;
            }
            case R.id.editar_foto_adicionar_prato:{
                showMenuEscolhaEdicao();
                break;
            }
        }
    }

    private boolean validarCampos(){
        boolean validacao = true;
        if (etNomePrato.getText().toString().trim().isEmpty()){
            etNomePrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (etDescricaoPrato.getText().toString().trim().isEmpty()){
            etDescricaoPrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if(imvFoto.getDrawable() == null){
            validacao = false;
            Helper.criarToast(this, "Adicione uma foto");
        }
        if (etPrecoPrato.getText().toString().trim().isEmpty()){
            etPrecoPrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        return validacao;
    }
    public void showMenuEscolhaEdicao() {
        BottomSheet.Builder builder = new BottomSheet.Builder(this);
        builder.setTitle("Escolha uma opção")
                .setSheet(R.menu.menu_editar_foto)
                .setListener(new BottomSheetListener() {
                    @Override
                    public void onSheetShown(@NonNull BottomSheet bottomSheet, @Nullable Object o) {

                    }

                    @Override
                    public void onSheetItemSelected(@NonNull BottomSheet bottomSheet, MenuItem menuItem, @Nullable Object o) {
                        switch (menuItem.getItemId()) {
                            case R.id.escolher_foto:
                                if (Helper.verificarPermissoesLeitura(AdicionarPratoActivity.this,AdicionarPratoActivity.this)){
                                    escolherFoto();
                                    break;
                                }
                                break;
                            case R.id.tirar_foto:
                                if (Helper.verificarPermissaoAcessarCamera(AdicionarPratoActivity.this,AdicionarPratoActivity.this)){
                                    tirarFoto();
                                    break;
                                }
                            default:
                                break;
                        }
                    }

                    @Override
                    public void onSheetDismissed(@NonNull BottomSheet bottomSheet, @Nullable Object o, int i) {

                    }
                }).show();

    }

    private void setUpViews(){
        findViewById(R.id.btnConfirmarAdicionarPrato).setOnClickListener(this);
        findViewById(R.id.editar_foto_adicionar_prato).setOnClickListener(this);
        etNomePrato = findViewById(R.id.etNomePrato);
        imvFoto = findViewById(R.id.iv_adicionar_prato);
        etPrecoPrato = findViewById(R.id.etPrecoPrato);
        etDescricaoPrato = findViewById(R.id.etDescricaoPrato);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Adicionando...");
    }

    private Prato criarPrato() {
        Prato prato = new Prato();
        prato.setNomePrato(etNomePrato.getText().toString());
        prato.setDescricaoPrato(etDescricaoPrato.getText().toString());
        prato.setUrlImagem(urlImagemAdicionada);
        prato.setPreco(Double.parseDouble(etPrecoPrato.getText().toString()));
        //spinner
        prato.setIdCategoria("idCategoria");
        return prato;
    }

    private void adicionarPrato() {
        PratoServices pratoServices = new PratoServices();
        if (pratoServices.adicionarPrato(criarPrato())){
            Helper.criarToast(this, "Prato adicionado com sucesso.");
            finish();
        }else{
            Helper.criarToast(this, "Erro ao adicionar prato");
            finish();
        }
    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void escolherFoto(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Selecione uma imagem"), GALERY_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    tirarFoto();
                    break;
                }
                break;
            }
            case GALERY_REQUEST_CODE:{
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    escolherFoto();
                    break;
                }
                break;
            }
            default:
                break;
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GALERY_REQUEST_CODE:
                Uri uriFoto;
                if (requestCode == GALERY_REQUEST_CODE && resultCode == RESULT_OK) {
                    uriFoto = data.getData();
                    try{
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriFoto);
                        setFotoImageView(bitmap);
                        inserirFoto(uriFoto);
                    }catch(IOException e ){
                        Log.d("IOException upload", e.getMessage());
                    }
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            Bitmap bitmap = (Bitmap) extras.get("data");
                            setFotoImageView(bitmap);
                            uriFoto = Helper.getImageUri(this, bitmap);
                            inserirFoto(uriFoto);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }
    private void inserirFoto(Uri uriFoto){
        iniciarProgressDialog();
        final StorageReference ref = storageReference
                .child("images/pratos/" + uidImagemPrato.toString() + ".jpg");

        ref.putFile(uriFoto).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri imageUri = task.getResult();
                    System.out.println(imageUri.toString());
                    urlImagemAdicionada = imageUri.toString();
                    fecharProgressDialog();
                }
            }
        }).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                fecharProgressDialog();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Helper.criarToast(AdicionarPratoActivity.this,e.toString());
                fecharProgressDialog();
            }
        });
    }
    private void setFotoImageView(Bitmap bitmap) {
        imvFoto.setImageBitmap(bitmap);
    }
    private void fecharProgressDialog(){
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.dismiss();
    }
    private void iniciarProgressDialog() {
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Atualizando...");
        progressDialog.show();
    }
}
