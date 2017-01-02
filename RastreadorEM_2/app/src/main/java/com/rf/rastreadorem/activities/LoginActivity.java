package com.rf.rastreadorem.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rf.rastreadorem.R;
import com.rf.rastreadorem.io.IO;

/**
 * Activity da Tela de Login
 *
 * @author RenanFucci
 */
public class LoginActivity extends AppCompatActivity {
    private EditText mEditText_email;
    private EditText mEditText_pwd;
    private Button mButton_login;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ctx= this;

        mEditText_email = (EditText) findViewById(R.id.input_email);
        mEditText_pwd = (EditText) findViewById(R.id.input_pwd);
        mButton_login = (Button) findViewById(R.id.button_login);
        mButton_login.setOnClickListener((click)->{
            String email = mEditText_email.getText().toString();
            String pwd =  mEditText_pwd.getText().toString();
            if(email.contentEquals("") || pwd.contentEquals("")){
                Toast.makeText(this, "Campos Vazios!", Toast.LENGTH_SHORT).show();
            }else{
                if(email.contentEquals("root@root") && pwd.contentEquals("root")){
                    IO.setarLogin(ctx, true);
                    finish();
                    startActivity(new Intent(ctx, BuscarCriancasPHPActivity.class));
                }else{
                    Snackbar.make(click, "Usuário ou Senha Inválido", Snackbar.LENGTH_LONG);
                }
            }
        });
    }

}
