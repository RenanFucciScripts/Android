package com.example.renanfucci.estaganografia;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EsconderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MetodosPDI metodos = new MetodosPDI();
    private int PICK_IMAGE;
    private ImageView mImageView;
    private EditText mEditText;
    private Bitmap image;
    private Context context;
    private File fl ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esconder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.imageViewEsconder);
        mEditText = (EditText) findViewById(R.id.editTextEsconder);

        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mImageView.getDrawable() == null) {
                    Snackbar.make(view, "Insira uma imagem.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (mEditText.getText().toString().contentEquals("")) {
                    Snackbar.make(view, "Insira um texto.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    if (image != null) {
                        Snackbar.make(view, "Processando.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                        String texto = ((EditText) findViewById(R.id.editTextEsconder)).getText().toString();
                        Log.e("Mensagem Secreta: ", "EditText: "+texto);
                        Bitmap imgOut = metodos.esconderMSG(image, texto);
                        if(imgOut==null) {
                            Log.e("Mensagem Secreta: ", "Imagem Nula");
                        }
                        else {
                            Save saveFile = new Save();
                            fl=saveFile.SaveImage(context,imgOut);
                            Log.e("Mensagem Secreta: ", "fl: "+fl.toString());
                        }
                        Intent sendIntent = new Intent(Intent.ACTION_SEND);
                        Uri imageUri = Uri.parse(fl.toURI().toString());
                        Log.e("Mensagem Secreta: ", "uri: "+imageUri);
                        sendIntent.setType("image/*");
                        sendIntent.putExtra(Intent.EXTRA_STREAM,imageUri);
                        Intent intent = Intent.createChooser(sendIntent, "Envie sua imagem");
                        startActivity(intent);
                    } else {
                        Snackbar.make(view, "Tente Novamente.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.esconder, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toolbar_capturar) {
            dispatchTakePictureIntent();
        } else if (id == R.id.action_toolbar_carregar) {
            Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
            getIntent.setType("image/*");

            Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setType("image/*");

            Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

            startActivityForResult(chooserIntent, PICK_IMAGE);

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_esconder) {

        } else if (id == R.id.nav_mostrar) {
            Intent intent = new Intent(this, MostrarActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;


    public void onClickCarregar(View view) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");
        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(getIntent, "Selecione a imagem");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);

    }


    public void onClickCapturar(View view) {
        dispatchTakePictureIntent();
    }

    private File imageFile;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),("IMG_"+getCurrentDateAndTime()+".png"));
        Uri tempUri = Uri.fromFile(imageFile);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(imageFile.exists()){
                image = decodeFile(imageFile);
                mImageView.setImageBitmap(image);
              mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        }

        else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            image = decodeFile(new File(picturePath));
            mImageView.setImageBitmap(image);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

    }

    private Bitmap decodeFile(File f) {
        try {
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            final int REQUIRED_SIZE=90;

            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (Exception e) {

        }
        return null;
    }

    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

}