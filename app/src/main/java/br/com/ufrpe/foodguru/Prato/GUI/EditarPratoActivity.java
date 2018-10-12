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
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

import br.com.ufrpe.foodguru.R;
import br.com.ufrpe.foodguru.Prato.dominio.Prato;
import br.com.ufrpe.foodguru.Prato.negocio.PratoServices;
import br.com.ufrpe.foodguru.infraestrutura.utils.Helper;

public class EditarPratoActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText nomePrato, descricaoPrato, precoPrato;
    private StorageReference  storageReference = FirebaseStorage.getInstance().getReference();
    private UUID uidImagemPrato = UUID.randomUUID();
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALERY_REQUEST_CODE = 71;
    private Prato pratoSelecionado;
    private ImageView imvPrato;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_prato);
        Helper.verificarPermissaoEscrever(EditarPratoActivity.this,EditarPratoActivity.this);
        pratoSelecionado = getIntent().getExtras().getParcelable("prato");
        setUpViews();
    }

    private void setUpViews(){
        findViewById(R.id.btnConfirmarEditarPrato).setOnClickListener(this);
        findViewById(R.id.btnEditarImagemPrato).setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        descricaoPrato = findViewById(R.id.etEditarDescricaoPrato);
        descricaoPrato.setText(pratoSelecionado.getDescricaoPrato());
        nomePrato = findViewById(R.id.etEditarNomePrato);
        nomePrato.setText(pratoSelecionado.getNomePrato());
        precoPrato = findViewById(R.id.etEditarPrecoPrato);
        precoPrato.setText(pratoSelecionado.getPreco().toString());
        imvPrato = findViewById(R.id.ivImagemPratoEditar);
        Picasso.get()
                .load(pratoSelecionado.getUrlImagem())
                .resize(500,500)
                .into(imvPrato);
    }

    private boolean validarCampos(){
        boolean validacao = true;
        if (nomePrato.getText().toString().trim().isEmpty()){
            nomePrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if (descricaoPrato.getText().toString().trim().isEmpty()){
            descricaoPrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }
        if(imvPrato.getBackground() == null){
            validacao = false;
            Helper.criarToast(this, "Adicione uma foto");
        }
        if (precoPrato.getText().toString().trim().isEmpty()){
            precoPrato.setError(getString(R.string.sp_excecao_campo_vazio));
            validacao = false;
        }

        return validacao;
    }

    public void confirmarEdicaoPrato(View view) {
        if (!validarCampos()){
            return;
        }
        editarPrato();
    }

    public void editarPrato(){
        setPratoSelecionado();
        PratoServices pratoServices = new PratoServices();
        if (pratoServices.editarPrato(pratoSelecionado)){
            Helper.criarToast(this, "Prato editado com sucesso");
            finish();
        }else{
            Helper.criarToast(this, "Erro ao editar prato");
        }
    }

    public void setPratoSelecionado(){
        pratoSelecionado.setNomePrato(nomePrato.getText().toString());
        pratoSelecionado.setDescricaoPrato(descricaoPrato.getText().toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnConfirmarEditarPrato:{
                if(!validarCampos()){
                    return;
                }
                editarPrato();
                break;
            }
            case R.id.btnEditarImagemPrato:{
                showMenuEscolhaEdicao();
                break;
            }
        }
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
                                if (Helper.verificarPermissoesLeitura(EditarPratoActivity.this,EditarPratoActivity.this)){
                                    escolherFoto();
                                    break;
                                }
                                break;
                            case R.id.tirar_foto:
                                if (Helper.verificarPermissaoAcessarCamera(EditarPratoActivity.this,EditarPratoActivity.this)){
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
        FirebaseStorage.getInstance().getReferenceFromUrl(pratoSelecionado.getUrlImagem()).delete();
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
                    pratoSelecionado.setUrlImagem(imageUri.toString());
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
                Helper.criarToast(EditarPratoActivity.this,e.toString());
                fecharProgressDialog();
            }
        });
    }
    private void setFotoImageView(Bitmap bitmap) {
        imvPrato.setImageBitmap(bitmap);
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

