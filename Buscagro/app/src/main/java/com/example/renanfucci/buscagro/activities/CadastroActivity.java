package com.example.renanfucci.buscagro.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.renanfucci.buscagro.R;
import com.example.renanfucci.buscagro.method.asyncTask.BackgroundTask;
import com.example.renanfucci.buscagro.method.io.IO;

import java.util.concurrent.ExecutionException;

public class CadastroActivity extends AppCompatActivity {

    private String nomeCompleto, email, pwd, pwd2, telefone, endereco;
    private CheckBox checkbox;
    private static final String TAG = CadastroActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
/*
        TextView textViewComoPretende = (TextView) findViewById(R.id.textView_ComoPretende);
        textViewComoPretende.setText(Html.fromHtml("Como pretende usar o Busc<font color='#309f36'>agro</font>?"));
*/
        TextView textViewTermos = (TextView) findViewById(R.id.textView_TermosCompromisso);
        String declaro = "Declaro que ";
        declaro += "<font color='#03AEEB'>li  </font>";
        declaro += "e ";
        declaro += "<font color='#03AEEB'>concordo </font>";
        declaro += "com todos os termos de uso ";
        declaro += "<font color='#03AEEB'><u>aqui</u></font> ";
        declaro += "descritos.";
        textViewTermos.setText(Html.fromHtml(declaro));


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


    public void onClickCadastrar(View view) {
        nomeCompleto = ((EditText) findViewById(R.id.editar_cadastro_editText_NomeCompleto)).getText().toString();
        email = ((EditText) findViewById(R.id.editar_cadastro_editText_Email)).getText().toString();
        pwd = ((EditText) findViewById(R.id.editar_cadastro_editText_SenhaUm)).getText().toString();
        pwd2 = ((EditText) findViewById(R.id.editar_cadastro_editText_SenhaDois)).getText().toString();
        telefone = ((EditText) findViewById(R.id.editar_cadastro_editText_telefone)).getText().toString();
        endereco = ((EditText) findViewById(R.id.editar_cadastro_editText_endereco)).getText().toString();
        checkbox = (CheckBox) findViewById(R.id.cadastro_checkBox_termos);

        if (nomeCompleto.contentEquals("") || email.contentEquals("") || pwd.contentEquals("")
                || pwd2.contentEquals("") || telefone.contentEquals("") || endereco.contentEquals("")) {
            Toast.makeText(this, "Campos vazios.", Toast.LENGTH_LONG).show();
        } else {
            if (!pwd.contentEquals(pwd2)) {
                Toast.makeText(this, "Senhas não coincidem.", Toast.LENGTH_LONG).show();
            } else if (!checkbox.isChecked()) {
                Toast.makeText(this, "Termos de uso não foram aceitos.", Toast.LENGTH_LONG).show();
            } else {
                try {

                    String[] params = {"cadastrar", nomeCompleto, email, pwd.hashCode() + "", telefone, endereco};

                    BackgroundTask backgroundTask = new BackgroundTask(view.getContext());
                    backgroundTask.execute(params);
                    backgroundTask.get();
                    String str_result = backgroundTask.getStr_result();
                    str_result = str_result.trim();
                    if (!str_result.contentEquals("-1")) {
                        String[] userInfo = {str_result + "", nomeCompleto, email, pwd.hashCode() + "", telefone, endereco};

                        IO.setarInfoUser(this, userInfo);
                        IO.setarLogin(this, true);
                        finish();
                        startActivity(new Intent(this, TelaInicialActivity.class));

                    } else if (str_result.contentEquals("-2")) {
                        Toast.makeText(this, "Email já existe :(", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Sem Acesso à Internet :(", Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
