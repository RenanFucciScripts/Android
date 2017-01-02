package com.rf.rastreadorem.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rf.rastreadorem.R;
import com.rf.rastreadorem.dao.DB;
import com.rf.rastreadorem.io.IO;

import java.util.concurrent.ExecutionException;

import static android.view.MotionEvent.*;
/**
 * Activity da Tela de Configurações de conexão do banco de dados.
 *
 * @author RenanFucci
 */
public class BDConfigActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Context ctx;
    private Button mButton_salvar_config_bd;
    private EditText mEditText_nome_bd;
    private EditText mEditText_ip_host;
    private EditText mEditText_user;
    private TextInputEditText mEditText_pwd;
    private DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bdconfig);
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

        mButton_salvar_config_bd = (Button) findViewById(R.id.button_salvar_config_bd);
        mEditText_nome_bd = (EditText) findViewById(R.id.editText_nome_do_bd);
        mEditText_ip_host = (EditText) findViewById(R.id.editText_ip_host);
        mEditText_user    = (EditText) findViewById(R.id.editText_user);
        mEditText_pwd     = (TextInputEditText) findViewById(R.id.editText_pwd);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        mEditText_nome_bd.setText(preferences.getString("nomeBD", ""));
        mEditText_ip_host.setText(preferences.getString("iphost", ""));
        mEditText_user.setText(preferences.getString("user", ""));
        mEditText_pwd.setText(preferences.getString("pwd", ""));

        mEditText_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {

                    case ACTION_DOWN:
                        // PRESSED
                        mEditText_pwd.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEditText_pwd, InputMethodManager.SHOW_IMPLICIT);
                        mEditText_pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        mEditText_pwd.setSelection(mEditText_pwd.length());
                        return true; // if you want to handle the touch event
                    case ACTION_UP:
                        // RELEASED
                        mEditText_pwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mEditText_pwd.setSelection(mEditText_pwd.length());
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        mButton_salvar_config_bd.setOnClickListener((click)->{
            String nome_bd = mEditText_nome_bd.getText().toString();
            String ip_host = mEditText_ip_host.getText().toString();
            String user = mEditText_user.getText().toString();
            String pwd = mEditText_pwd.getText().toString();

            if(nome_bd.contentEquals("") || ip_host.contentEquals("") || user.contentEquals("")){
                Toast.makeText(ctx, "Campos Vazios!", Toast.LENGTH_LONG).show();
            }else{
                IO.setarInfoBD(ctx, new String[]{nome_bd, ip_host, user, pwd});
                new AsyncTask<Void, Void, Boolean>(){
                    @Override
                    protected Boolean doInBackground(Void... voids) {
                        db = new DB();
                        if(db.connect(ctx)){
                            return true;
                        }
                        return false;
                    }

                    @Override
                    protected void onPostExecute(Boolean aBoolean) {
                        if(aBoolean){
                            new AlertDialog.Builder(ctx)
                                    .setTitle("Banco de Dados")
                                    .setMessage(getResources().getString(R.string.configuracoes_validas_e_salvas))
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(ctx, BuscarCriancaActivity.class));
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }else{
                            new AlertDialog.Builder(ctx)
                                    .setTitle("Banco de Dados")
                                    .setMessage(getResources().getString(R.string.configuracoes_nao_validas))
                                    .setNegativeButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                }.execute();
            }
        });
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
        getMenuInflater().inflate(R.menu.bdconfig, menu);
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_busca_crianca) {
            startActivity(new Intent(this, BuscarCriancaActivity.class));
        }
        else if(id == R.id.nav_busca_crianca_php){
            startActivity(new Intent(this, BuscarCriancasPHPActivity.class));
        }
        else if (id == R.id.nav_bd) {
//            startActivity(new Intent(this, BDConfigActivity.class));
        }
        else if (id == R.id.nav_exit) {
            ctx = this;
            IO.setarLogin(this, false);
            new Handler().postDelayed(()->{
                IO.restartApp(ctx);
            }, 2000);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}