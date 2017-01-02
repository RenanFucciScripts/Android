package com.book.homelist;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.book.homelist.pojo.Grupo;
import com.book.homelist.pojo.ItensPorGrupo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Date;

import static com.book.homelist.ListItemsActivity.EXTRA_GRUPO;

public class ActivityAddItem extends AppCompatActivity {

    private Grupo grupo;

    private static final int RC_CAMERA = 0;
    private static final String TAG = "UploadFile";

    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference itensListaRef;

    private File picturePath;

    private EditText edtNomeItem;
    private EditText quantidadeItem;
    private EditText marcaItem;
    private ImageView imgItem;
    private ProgressDialog mProgress;
    private ItensPorGrupo itensPorGrupo;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        grupo = (Grupo) getIntent().getSerializableExtra(EXTRA_GRUPO);

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setDisplayShowTitleEnabled(true);
        }

        edtNomeItem = (EditText) findViewById(R.id.edt_nome_item);
        quantidadeItem = (EditText) findViewById(R.id.edt_quantidade_item);
        marcaItem = (EditText) findViewById(R.id.edt_marca);
        imgItem = (ImageView) findViewById(R.id.img_item);


        mAuth = FirebaseAuth.getInstance();

        initFirebase();


    }

    private void initFirebase() {
        database = FirebaseDatabase.getInstance();
        gruposRef = database.getReference("grupos");
        //itensListaRef = gruposRef.child(grupo.getId()).child("itens");
        itensListaRef = gruposRef.child(grupo.getId()).child("itens");

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://homelist-b4559.appspot.com").child("imagens");

        itensListaRef.keepSynced(true);
    }

    private boolean validate() {

        if (edtNomeItem.getText().toString().isEmpty()) {
            edtNomeItem.setError(getString(R.string.txt_required));
            return false;
        } else if (quantidadeItem.getText().toString().isEmpty()) {
            quantidadeItem.setError(getString(R.string.txt_required));
            return false;
        } else {
            return true;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_item) {

            if (validate()) {

                itensPorGrupo = new ItensPorGrupo();

                mProgress = ProgressDialog.show(this, null, getString(R.string.salvando));

                if (picturePath != null && picturePath.exists()) {
                    StorageReference coverRef = storageRef.child(picturePath.getName());

                    UploadTask uploadTask = coverRef.putFile(Uri.fromFile(picturePath));
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            mProgress.hide();
                            Log.d(TAG, "OnFailureUpload");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            itensPorGrupo.setImagemItem(downloadUrl.toString());
                            saveItem();
                        }
                    });
                } else {
                    saveItem();
                }

            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {


        String itensPorGrupoId = itensListaRef.push().getKey();

        itensPorGrupo.setId(itensPorGrupoId);
        itensPorGrupo.setNomeItem(edtNomeItem.getText().toString());
        itensPorGrupo.setQuantidadeItem(Integer.parseInt(quantidadeItem.getText().toString()));
        itensPorGrupo.setCompradoStatus(false);

        if (marcaItem.getText().toString().isEmpty()) {
            itensPorGrupo.setMarcaItem("");
        } else {
            itensPorGrupo.setMarcaItem(marcaItem.getText().toString());
        }

        itensListaRef.child(itensPorGrupoId).setValue(itensPorGrupo);

        Toast.makeText(this, "Item adicionado com sucesso.", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0) {

            Glide.with(this).
                    load(picturePath.getAbsolutePath()).
                    placeholder(R.mipmap.ic_launcher).
                    //error(R.drawable.ic_clear_black_24dp).
                            diskCacheStrategy(DiskCacheStrategy.ALL).
                    // .override(500, 500).
                            into(imgItem);

        }
    }

    public void openCameraClick(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String pictureName = DateFormat.format("yyyy-MM-dd_hhmmss", new Date()).toString();

            picturePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), pictureName);

            Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            it.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picturePath));

            startActivityForResult(it, RC_CAMERA);

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }


    }
}
