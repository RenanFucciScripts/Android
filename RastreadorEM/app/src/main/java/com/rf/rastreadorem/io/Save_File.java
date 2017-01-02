package com.rf.rastreadorem.io;

/**
 * Classe para a salvar e deixar os arquivos disponiveis para visibilidade e compartilhamento
 * no Android.
 *
 * @author RenanFucci
 */
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

public class Save_File {

    private static final String TAG = Save_File.class.getName();
    private Context ctx;
    private String NameOfFolder = "/RastreadorEM";
    private String NameOfFile   = "EST_";
    File fl;

    public File getFl() {
        return fl;
    }

    public boolean Save_File(Context ctx, String url_localFile){
        try {
            this.ctx = ctx;
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + NameOfFolder);
            if (dir.exists() == false) {
                dir.mkdirs();
            }

            URL url = new URL(url_localFile);
            String fileName = url_localFile.substring(url_localFile.lastIndexOf("/"));
            File file = new File(dir, fileName);

            Log.e("DownloadManager", "download url:" + url);
            Log.e("DownloadManager", "download file name:" + fileName);

            URLConnection uconn = url.openConnection();

            InputStream is = uconn.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[4096];
            int current = 0;

            while((current = bis.read(data,0,data.length)) != -1){
                buffer.write(data,0,current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer.toByteArray());
            fos.close();

            tornarFileDisponivel(file);
            fl = file;
            return true;
        }catch(Exception e) {
            Log.e(TAG, "SaveFile: "+e.getLocalizedMessage());
        }
        return false;
    }


    /**
     * @author RenanFucci<br>
     * <strong>Método tornarFileDisponivel</strong><br>
     * Método tornar o arquivo disponível, ou seja, visivel e compartilhavel.<br>
     */
    private void tornarFileDisponivel(File file) {
        MediaScannerConnection.scanFile(ctx,
                new String[]{file.toString()}, null, (path, uri) -> {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                });
    }
}

