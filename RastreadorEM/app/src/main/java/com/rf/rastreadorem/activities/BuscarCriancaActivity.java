package com.rf.rastreadorem.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rf.rastreadorem.R;
import com.rf.rastreadorem.dao.Crianca;
import com.rf.rastreadorem.dao.DB;
import com.rf.rastreadorem.io.IO;
import com.rf.rastreadorem.io.Save_File;

import java.util.ArrayList;

/**
 * Activity da Tela de Buscar Crianças somente pelo BD
 *
 * @author RenanFucci
 */
public class BuscarCriancaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = BuscarCriancaActivity.class.getName();
    private ImageButton mImageButton_buscar_crianca;
    private EditText mEditText_cod_crianca;
    private ProgressBar mProgressBar_busca_crianca;
    private Context ctx;
    private LinearLayout mLinearLayout_criancas;
    private DB db;
    private ArrayList<Crianca> criancas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_crianca);
        ctx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mProgressBar_busca_crianca = (ProgressBar) findViewById(R.id.progressBar_buscar_crianca);
        mProgressBar_busca_crianca.setVisibility(View.GONE);

        mLinearLayout_criancas = (LinearLayout) findViewById(R.id.linearLayout_criancas);
        mEditText_cod_crianca = (EditText) findViewById(R.id.editText_cod_crianca);
        mImageButton_buscar_crianca = (ImageButton) findViewById(R.id.imageButton_buscar);
        mImageButton_buscar_crianca.setOnClickListener((click) -> {
            if (click != null) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(click.getWindowToken(), 0);
            }
            String cod_crianca_editText = mEditText_cod_crianca.getText().toString();
            mProgressBar_busca_crianca.setVisibility(View.VISIBLE);
            new Handler().postDelayed(() -> {
                buscarCriancasBD(cod_crianca_editText);
            }, 1000);
        });
        mEditText_cod_crianca.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    String cod_crianca_editText = mEditText_cod_crianca.getText().toString();
                    mProgressBar_busca_crianca.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> {
                        buscarCriancasBD(cod_crianca_editText);
                    }, 500);
                    return true;
                }
                return false;
            }
        });

    }

    /**
     * @author RenanFucci<br>
     * <strong>Método buscarCriancasSERVER</strong><br>
     * Método AsyncTask para buscar, a partir de um código de criança, imagens do banco de dados, pegar os
     * dados dessas imagens e mostrá-los na tela em forma de lista por imagem.<br>
     * @param cod_crianca_editText String com o código da criança;
     */
    private void buscarCriancasBD(String cod_crianca_editText) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                db = new DB();
                Log.e(TAG, "doInBackground: db" );
                boolean connected = db.connect(ctx);
                Log.e(TAG, "doInBackground: connected: "+connected );
                if(connected) {
                    Log.e(TAG, "doInBackground: buscar_criancas: connected");
                    criancas = db.buscar_criancas_por_codigo(ctx, Integer.parseInt(cod_crianca_editText));
                    return ((criancas==null || criancas.isEmpty())?(false):(true));
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                mProgressBar_busca_crianca.setVisibility(View.GONE);
                if(aBoolean){
                    mLinearLayout_criancas.setBackgroundColor(Color.parseColor("#ffffff"));
                    if(((LinearLayout) mLinearLayout_criancas).getChildCount() > 0)
                        ((LinearLayout) mLinearLayout_criancas).removeAllViews();
                    for (Crianca crianca: criancas) {
                        Log.e(TAG, "\n "+crianca.getFile_in_name());

                        LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        RelativeLayout view_item = (RelativeLayout) vi.inflate(R.layout.fragment_crianca_com_layout, null);

                        TextView view_file_in_name = (TextView) view_item.findViewById(R.id.textView_file_in_name);
                        view_file_in_name.setText(crianca.getFile_in_name());
                        view_file_in_name.setTextColor(Color.BLACK);

                        TextView view_date_in = (TextView) view_item.findViewById(R.id.textView_date_in);
                        view_date_in.setText("\tdatein:\n" + crianca.getDate_in().split("\\s")[0]);
                        view_date_in.setTextColor(Color.BLACK);

                        TextView view_date_out = (TextView) view_item.findViewById(R.id.textView_date_out);
                        view_date_out.setText("\tdateout:\n"+crianca.getDate_out().split("\\s")[0]);
                        view_date_out.setTextColor(Color.BLACK);

                        TextView view_last_stage = (TextView) view_item.findViewById(R.id.textView_last_stage);
                        view_last_stage.setText("\tlaststage:\n"+crianca.getLast_stage());
                        view_last_stage.setTextColor(Color.BLACK);

                        ImageButton imageButton = (ImageButton) view_item.findViewById(R.id.imageButton_download);
                        imageButton.setOnClickListener((click) -> {
                            mProgressBar_busca_crianca.setVisibility(View.VISIBLE);
                            new AsyncTask<Void,Void,Boolean>(){

                                @Override
                                protected Boolean doInBackground(Void... voids) {
                                    return new Save_File().Save_File(ctx, "http://harpia.profalexaraujo.com.br/files/"+crianca.getUrl_out());
                                }

                                @Override
                                protected void onPostExecute(Boolean aBoolean) {
                                    mProgressBar_busca_crianca.setVisibility(View.GONE);
                                    if(aBoolean){
                                        Toast.makeText(ctx, ctx.getResources().getString(R.string.salvo_sucesso), Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(ctx, ctx.getResources().getString(R.string.impossivel_fzr_download), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }.execute();
                        });
                        LinearLayout.LayoutParams layouParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                    layouParams.setMargins(24, 24, 24, 24);
                        mLinearLayout_criancas.addView(view_item, layouParams);
                    }
                }else{
                    String aux = ((db.isErroEhCriancaVazia())?(getResources().getString(R.string.cod_crianca_vazia)):
                            (((db.isErroEhIdInvalido())?(getResources().getString(R.string.cod_crianca_inexistente)):("Erro Desconhecido."))));
                    Toast.makeText(ctx, aux, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
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
        getMenuInflater().inflate(R.menu.buscar_crianca, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_busca_crianca) {
            //startActivity(new Intent(this, BuscarCriancaActivity.class));
        } else if (id == R.id.nav_busca_crianca_php) {
            startActivity(new Intent(this, BuscarCriancasPHPActivity.class));
        } else if (id == R.id.nav_bd) {
            startActivity(new Intent(this, BDConfigActivity.class));
        } else if (id == R.id.nav_exit) {
            ctx = this;
            IO.setarLogin(this, false);
            new Handler().postDelayed(() -> {
                IO.restartApp(ctx);
            }, 2000);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
