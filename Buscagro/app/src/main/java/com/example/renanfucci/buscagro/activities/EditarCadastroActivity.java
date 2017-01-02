package com.example.renanfucci.buscagro.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renanfucci.buscagro.R;
import com.example.renanfucci.buscagro.method.asyncTask.BackgroundTask;
import com.example.renanfucci.buscagro.method.dao.Cliente;
import com.example.renanfucci.buscagro.method.io.IO;
import com.example.renanfucci.buscagro.method.io.RoundedImageView;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;

import java.util.concurrent.ExecutionException;

public class EditarCadastroActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private final String TAG = this.getClass().getName();
    private Context ctx;
    private Button button_editar_cadastro;
    private EditText editText_nomecompleto;
    private EditText editText_email;
    private EditText editText_endereco;
    private EditText editText_telefone;
    private EditText editText_senhaUm;
    private EditText editText_senhaDois;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_cadastro);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Drawable drawable = IO.getNavHeaderImg(this);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_editar_cadastro);
        RoundedImageView roundedImageView = (RoundedImageView) headerView.findViewById(R.id.nav_header_image);
        if(drawable!=null){
            roundedImageView.setImageDrawable(drawable);
        }

        ctx = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ((TextView) headerView.findViewById(R.id.textView2)).setText(preferences.getString("Nome", " "));
        ((TextView) headerView.findViewById(R.id.textView)).setText(preferences.getString("Email"," "));

        editText_nomecompleto = ((EditText) findViewById(R.id.editar_cadastro_editText_NomeCompleto));
        editText_nomecompleto.setHint(preferences.getString("Nome", ""));

        editText_email = ((EditText) findViewById(R.id.editar_cadastro_editText_Email));
        editText_email.setHint(preferences.getString("Email",""));
        
        editText_endereco = ((EditText) findViewById(R.id.editar_cadastro_editText_endereco));
        editText_endereco.setHint(preferences.getString("Endereco",""));

        editText_telefone = ((EditText) findViewById(R.id.editar_cadastro_editText_telefone));
        editText_telefone.setHint(preferences.getString("Telefone",""));
        
        editText_senhaUm = ((EditText) findViewById(R.id.editar_cadastro_editText_SenhaUm));
        editText_senhaUm.setHint(preferences.getString("Pwd",""));
        
        editText_senhaDois =((EditText) findViewById(R.id.editar_cadastro_editText_SenhaDois));
        editText_senhaDois.setHint(preferences.getString("Pwd",""));

        button_editar_cadastro = (Button) findViewById(R.id.editar_cadastro_button_Cadastrar);
        button_editar_cadastro.setOnClickListener(new View.OnClickListener() {
            String nomecomple, email, endereco, telefone, senhaum, senhadois;

            @Override
            public void onClick(View v) {
                nomecomple = editText_nomecompleto.getText().toString();
                email = editText_email.getText().toString();
                endereco = editText_endereco.getText().toString();
                telefone = editText_telefone.getText().toString();
                senhaum = editText_senhaUm.getText().toString();
                senhadois = editText_senhaDois.getText().toString();

                if(nomecomple.contentEquals("") && email.contentEquals("") && endereco.contentEquals("")
                        && senhaum.contentEquals("") && senhadois.contentEquals("") && telefone.contentEquals("") ){
                    Toast.makeText(ctx, "Nenhum Campo foi Alterado.", Toast.LENGTH_LONG).show();
                }else if (!senhaum.contentEquals(senhadois)) {
                    Toast.makeText(ctx, "Senhas não coincidem.", Toast.LENGTH_LONG).show();
                }else{
                    try {

                        nomecomple = (nomecomple.contentEquals("")?(Cliente.getNome_completo(ctx)):(nomecomple));
                        email = (email.contentEquals("")?(Cliente.getEmail(ctx)):(email));
                        endereco = (endereco.contentEquals("")?(Cliente.getEndereco(ctx)):(endereco));
                        telefone = (telefone.contentEquals("")?(Cliente.getTelefone(ctx)):(telefone));
                        senhaum = (senhaum.contentEquals("")?(Cliente.getPwd(ctx)):(senhaum));

                        String[] params = {"updateuser", nomecomple,  endereco,  senhaum.hashCode() + "", telefone, Cliente.getId(ctx)};
//                        String[] params = {"updateuser", nomecomple, endereco,  senhaum.hashCode() + "", telefone, id};
                        BackgroundTask backgroundTask = new BackgroundTask(ctx);
                        backgroundTask.execute(params);
                        backgroundTask.get();
                        String str_result = backgroundTask.getStr_result();
                        str_result = str_result.trim();
                        Log.e(TAG, "onClick: strresult:"+str_result);
                        if (str_result.contentEquals("0")) {
                            String[] userInfo = {Cliente.getId(ctx), nomecomple, email, senhaum.hashCode() + "", telefone, endereco};

                            IO.setarInfoUser(ctx, userInfo);
                            IO.setarLogin(ctx, true);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    startActivity(new Intent(ctx, TelaInicialActivity.class));

                                }
                            }, 2000);

                        } else if (str_result.contentEquals("-2")) {
                            Toast.makeText(ctx, "Email já existe :(", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ctx, "Sem Acesso à Internet :(", Toast.LENGTH_LONG).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


                /*
                *  preferences.edit().putString("id", userInfos[0]).apply();
        preferences.edit().putString("Nome", userInfos[1]).apply();
        preferences.edit().putString("Email", userInfos[2]).apply();
        preferences.edit().putString("Pwd", userInfos[3]).apply();
        preferences.edit().putString("Telefone", userInfos[4]).apply();
        preferences.edit().putString("Endereco", userInfos[5]).apply();
                * */



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



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, TelaInicialActivity.class));

        } else if (id == R.id.nav_cadastrar) {
            startActivity(new Intent(this, TelaCadastroActivity.class));

        } else if (id == R.id.nav_consultar) {
            startActivity(new Intent(this, TelaConsultaActivity.class));
        } else if (id == R.id.nav_cancelar) {
            startActivity(new Intent(this, TelaCancelamentoActivity.class));
        } else if (id == R.id.nav_editar_cadastro) {
        } else if (id == R.id.nav_log_off){

            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            if (accessToken != null) {
                LoginManager.getInstance().logOut();
            }

            IO.setarLogin(this, false);
            ctx =this;
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    IO.restartApp(ctx);

                }
            }, 2000);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
