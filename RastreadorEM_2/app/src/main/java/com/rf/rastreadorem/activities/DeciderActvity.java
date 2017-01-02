package com.rf.rastreadorem.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.rf.rastreadorem.R;

/**
 * Activity da Tela para decidir se o aplicativo vai para a tela de login (se o login ainda não
 * tiver sido) e para a tela de Busca de Criancas via PHP.
 *
 * @author RenanFucci
 */
public class DeciderActvity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private boolean logged;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decider_actvity);
        handler.postDelayed(new Runnable() {
            public void run() {
                entrarApp();
            }
        }, 3500);
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método entrarApp</strong><br>
     * Método entrar no App e decidir pra qual irá.<br>
     */
    public void entrarApp(){
        logged = isLoggedIn(this);
        if (logged) {
            finish();
            startActivity(new Intent(this, BuscarCriancasPHPActivity.class));
        } else {
            finish();
            startActivity(new Intent(this, LoginActivity.class ));
        }
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método isLoggedIn</strong><br>
     * Método testar se o usuário já fez o login pelo menos uma vez.<br>
     */
    public boolean isLoggedIn(Context context){
        try {
            //testa login
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            boolean loggedIn = prefs.getBoolean("Islogin", false);
            if (loggedIn) {
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
