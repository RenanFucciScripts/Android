package com.example.renanfucci.estaganografia;

/**
 * Created by Renan Fucci on 29/11/2015.
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class Save {
    private Context TheThis;
    private String NameOfFolder = "/MSG_Secreta";
    private String NameOfFile   = "EST_";
    File filePathFinal;

    public File SaveImage(Context context,Bitmap ImageToSave){
        TheThis = context;
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ NameOfFolder;
        String CurrentDateAndTime= getCurrentDateAndTime();
        File dir = new File(file_path);

        if(!dir.exists()){
            dir.mkdirs();
        }

        File file = new File(dir, NameOfFile +CurrentDateAndTime+ ".png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            ImageToSave.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
            return file;
        }
        catch (FileNotFoundException e) {UnableToSave();}
        catch (IOException e){UnableToSave();}
        return null;


    }



    private void MakeSureFileWasCreatedThenMakeAvabile(File file) {
        MediaScannerConnection.scanFile(TheThis,
                new String[]{file.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        Log.e("ExternalStorage", "Scanned " + path + ":");
                        Log.e("ExternalStorage", "-> uri=" + uri);

                    }
                });

    }



    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }


    private void UnableToSave() {
        Toast.makeText(TheThis, "Imagem não pode ser salva.", Toast.LENGTH_SHORT).show();

    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Imagem salva com sucesso! ", Toast.LENGTH_SHORT).show();

    }

}

