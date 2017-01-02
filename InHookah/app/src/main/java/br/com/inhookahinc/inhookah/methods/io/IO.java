package br.com.inhookahinc.inhookah.methods.io;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import com.facebook.Profile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import br.com.inhookahinc.inhookah.R;
import br.com.inhookahinc.inhookah.activities.DeciderMainActivity;

/**
 * Created by Renan Fucci on 09/02/2016.
 */
public class IO {

    private final static String TAG="IO:\n\t ";

    public static boolean copyDefaultNavIMG(Context context, String picturePath){

        Bitmap image = BitmapFactory.decodeFile(picturePath);
        File mydir = context.getDir("imgs", Context.MODE_PRIVATE); //Creating an internal dir;
        mydir.mkdir();
        File fl =  new File(mydir,"img.jpg");
        Log.e(TAG,fl.getAbsolutePath());
        try{
            FileOutputStream fOut = new FileOutputStream(fl);
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Log.e(TAG, "Salva com sucesso"+fl.getAbsolutePath());
            return true;
        }catch (Exception ex){
            Log.e(TAG, (ex.getMessage().contentEquals(""))? ("exception"): ex.getMessage());
        }
        return false;
    }

    public static boolean copyDefaultNavIMG(Context context, Bitmap image){
        File mydir = context.getDir("imgs", Context.MODE_PRIVATE); //Creating an internal dir;
        mydir.mkdir();
        File fl =  new File(mydir,"img.jpg");
        Log.e(TAG,fl.getAbsolutePath());
        try{
            FileOutputStream fOut = new FileOutputStream(fl);
            image.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Log.e(TAG, "Salva com sucesso"+fl.getAbsolutePath());
            return true;

        } catch (FileNotFoundException e) {
            Log.e(TAG, ("exception"));
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, ("exception"));
            e.printStackTrace();
        }
        return false;
    }


    public static Drawable getNavHeaderImg(Context context) {
        final String NAME_OF_FILE = context.getDir("imgs", Context.MODE_PRIVATE) + "/img.jpg";
        Drawable drawable = null;
        drawable = Drawable.createFromPath(NAME_OF_FILE);
        return drawable;
    }

    public static void setarLogin(Context context, boolean is_logged){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean("Islogin", is_logged).apply();
    }

    public static void setarInfoUserFB(Context context,Profile fbprofile){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("Name", fbprofile.getName()).apply();
        //preferences.edit().putString("Email", fbprofile. TODO pegar email do FB;

    }

    public static void setarInfoUser(Context context,String[] userInfos ){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("Name", userInfos[0]).apply();
        preferences.edit().putString("Email", userInfos[1]).apply();
        preferences.edit().putString("Pwd", userInfos[3]).apply();


    }

    public static void restartApp(Context context){
        Intent mStartActivity = new Intent(context, DeciderMainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(context, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }



}
