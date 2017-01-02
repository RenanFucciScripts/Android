package com.rf.rastreadorem.io;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rf.rastreadorem.activities.DeciderActvity;

/**
 * Classe para controlar a escrita e leitura de arquivos em dispositivos Android.
 *
 * @author RenanFucci
 */
public class IO {

    private final static String TAG = "IO:\n\t ";

    /**
     * @author RenanFucci<br>
     * <strong>Método setarLogin</strong><br>
     * Método setar se o usuário já fez o login.<br>
     * @param context Context do app.
     * @param is_logged booelan se o login foi feito.
     */
    public static void setarLogin(Context context, boolean is_logged) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("Islogin", is_logged).apply();
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método setarInfoUser</strong><br>
     * Método setar os dados de login do usuário.<br>
     * @param context Context do app.
     * @param userInfos String[] com um array dos dados de login, no qual a posição 0 é o email
     *                  e a 1 é a senha.
     */
    public static void setarInfoUser(Context context, String[] userInfos) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("Email", userInfos[0]).apply();
        preferences.edit().putString("Pwd", userInfos[1]).apply();

    }

    /**
     * @author RenanFucci<br>
     * <strong>Método restartApp</strong><br>
     * Método dar um restart no app.<br>
     * @param context Context do app.
     */
    public static void restartApp(Context context) {
        Intent mStartActivity = new Intent(context, DeciderActvity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }

}
