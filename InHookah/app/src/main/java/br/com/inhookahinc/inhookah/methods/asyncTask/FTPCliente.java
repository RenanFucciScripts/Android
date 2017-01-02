package br.com.inhookahinc.inhookah.methods.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;

/**
 * Created by Renan Fucci on 04/02/2016.
 */
public class FTPCliente extends AsyncTask<Void,Void, String> {

    final private static String HOST= "ftp.inhookah.com.br";
    final private static String USER= "user.inhookah.com.br";
    final private static String PWD= "Root@123";
    final private static String TAG= "FTP_CL";

    private String calledMethod;
    private JSONObject jsonObject;
    private Context ctx;

    public FTPCliente(Context ctx, String calledMethod, JSONObject jsonObject) {
        this.ctx = ctx;
        this.calledMethod = calledMethod;
        this.jsonObject = jsonObject;
    }

    @Override
    protected String doInBackground(Void... params) {
        if(calledMethod.contentEquals("upload")){
            boolean b = uploadJSON(jsonObject);
            return ((b)? ("uploaded"): "not uploaded");
        }
        return "null";
    }

    @Override
    protected void onPostExecute(String result) {
        Log.e(TAG,  calledMethod+" "+result);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    private  boolean uploadJSON(JSONObject jsonObject){
        FTPClient con = null;
        boolean concluido=false;

        try{
            con = new FTPClient();
            con.connect(HOST);

            if (con.login(USER, PWD)){

                con.enterLocalPassiveMode(); // importante !!!!
                con.setFileType(FTP.BINARY_FILE_TYPE);

                File fl =  new File(ctx.getFilesDir()+ "user.json");//TODO:Pegar o nome de usuario dinamicamente.
                FileWriter fileWriter = new FileWriter(fl);
                fileWriter.write(jsonObject.toString(1));
                fileWriter.close();

                FileInputStream in = new FileInputStream(fl);
                String dirServidor = "add_local/";
                boolean result = con.storeFile(dirServidor+"user.json", in);

                in.close();

                if (result){
                    Log.e("upload result", "succeeded");
                    concluido=true;
                }
                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e){
            Log.e(TAG, "uploadJSON: " + ((e.equals(null)) ? e.getMessage() + "" : "null"));
            e.printStackTrace();
        }

        return concluido;
    }


    /*
     * TODO: Metodo para fazer donwload do arquivo de configuração.
     * */
    public void donwloadJSON(){
        FTPClient con = null;
        try{
            con = new FTPClient();
            con.connect("192.168.2.57");
            if (con.login("Administrator", "KUjWbk")){
                con.enterLocalPassiveMode(); // importante !!!!
                con.setFileType(FTP.BINARY_FILE_TYPE);
                String data = "/sdcard/vivekm4a.m4a";
                OutputStream out = new FileOutputStream(new File(data));
                boolean result = con.retrieveFile("vivekm4a.m4a", out);
                out.close();

                if (result)
                    Log.v("download result", "succeeded");

                con.logout();
                con.disconnect();
            }
        }
        catch (Exception e){
            Log.v("download result","failed");
            e.printStackTrace();
        }
    }


}
