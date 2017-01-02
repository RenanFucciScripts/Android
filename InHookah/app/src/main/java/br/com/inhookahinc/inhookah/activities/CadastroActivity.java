package br.com.inhookahinc.inhookah.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.methods.asyncTask.HTTPCliente;
import br.com.inhookahinc.inhookah.methods.io.IO;


public class CadastroActivity extends AppCompatActivity {

    private static final String TAG = "Cadastro:\n\t ";
    private int PICK_IMAGE;
    private Bitmap image;
    private ImageView mImageView;
    private TextView mTextView;
    private String picturePathIn;
    private ProgressBar mprogressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogressBar = (ProgressBar) findViewById(R.id.cadastro_progress_bar);
        mprogressBar.setVisibility(View.GONE);

        mImageView = (ImageView) findViewById(R.id.ImageView_userImg);
        mTextView = (TextView) findViewById(R.id.textView_insiraImg);


        TextView textViewTermos = (TextView) findViewById(R.id.textView_TermosCompromisso);
        String declaro = "Clicando em CADASTRAR, você ";
        declaro += "<font color='#03AEEB'>concorda  </font>";
        declaro += "com todos os termos de uso ";
        declaro += "<font color='#03AEEB'><u>aqui</u></font> ";
        declaro += "descritos.";
        textViewTermos.setText(Html.fromHtml(declaro));
        textViewTermos.setTextSize(14);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            NavUtils.navigateUpTo(this, intent);
        }
        return super.onOptionsItemSelected(item);

    }

    public void onClickCancelar(View view) {
        if (view != null) {
            Intent intent = new Intent(this, MainActivity.class);
            NavUtils.navigateUpTo(this, intent);
        }
    }

    /**
     * TODO:server
     *
     */
    public void onClickCadastrar(View view) {
        mprogressBar.setVisibility(View.VISIBLE);

        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        String nome = ((EditText) findViewById(R.id.editText_NomeCompleto)).getText().toString();
        String username = ((EditText) findViewById(R.id.editText_Email)).getText().toString();
        String pwd = ((EditText) findViewById(R.id.editText_SenhaUm)).getText().toString();
        String pwd2 = ((EditText) findViewById(R.id.editText_SenhaDois)).getText().toString();

        if(nome.contentEquals("")|| username.contentEquals("") || pwd.contentEquals("") || pwd2.contentEquals("")){
            Snackbar.make(view, "Preencha os campos obrigatórios (*)",Snackbar.LENGTH_LONG).setAction("Action", null).show();
            mprogressBar.setVisibility(View.GONE);
        }
        else{
            if(!pwd.contentEquals(pwd2)){
                Snackbar.make(view, "Senhas devem ser iguais",Snackbar.LENGTH_LONG).setAction("Action", null).show();
                mprogressBar.setVisibility(View.GONE);
            }
            else {
                if (image != null) {
                    Log.e(TAG, IO.copyDefaultNavIMG(this, picturePathIn) + "");
                    IO.setarLogin(this,true);
                    finish();
                    startActivity(new Intent(this, MenuActivity.class));
                }
                else {
                    finish();
                    IO.setarLogin(this, true);
                    startActivity(new Intent(this, MenuActivity.class));
                }
            }
        }

    /*
        String method = "cadastrar";
        HTTPCliente backgroundTask = new HTTPCliente(this);
        backgroundTask.execute(method, username, pwd);

        finish();
    */

    }

    /**
     * Carregar Imagem do dispositivo e setar numa imageView
     * */
    public void onClickCarregar(View view){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Selecione a Imagem");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data !=null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor=  getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            picturePathIn = picturePath;
            image = BitmapFactory.decodeFile(picturePath);
            mImageView.setImageBitmap(image);
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mTextView.setText("");
        }
    }
}