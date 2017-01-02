package com.example.renanfucci.buscagro.method.asyncTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.renanfucci.buscagro.method.io.IO;
import com.facebook.Profile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Renan Fucci on 09/02/2016.
 */
public class FBConnection extends AsyncTask<String, Void, Boolean> {

    private static final String TAG = "FBConnection";
    private Profile profile;
    private Context context;

    public FBConnection (Context context, Profile profile){
        this.profile = profile;
        this.context = context;

    }


    @Override
    protected Boolean doInBackground(String... params) {
        try {
            URL url = new URL("https://graph.facebook.com/"+profile.getId()+"/picture?type=large");
            Log.e(TAG,url.toString());
            Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            boolean b=false;
            if(bitmap!=null) {
                Log.e(TAG, "if"+bitmap.getWidth()+", "+bitmap.getHeight());
                 b= IO.copyDefaultNavIMG(context, bitmap);

            }
            Log.e(TAG, b + ", "+url.toString());
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
