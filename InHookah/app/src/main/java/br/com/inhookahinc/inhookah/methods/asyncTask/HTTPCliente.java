package br.com.inhookahinc.inhookah.methods.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Renan Fucci on 07/02/2016.
 */
public class HTTPCliente extends AsyncTask<String,Void, String> {

    Context ctx;
    public HTTPCliente(Context context){
        this.ctx = context;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String[] params) {
        String method = params[0];
        if (method.contentEquals("cadastrar")){
            String cadast_url = "http://inhookah.com.br/app/cadastro.php";
            String login_url = "http://inhookah.com.br/app/login.php";
            String username = params[1];
            String pwd = params[2];
            Log.e("login", (username + ", " + pwd));
            try{
                URL url =  new URL(cadast_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =  httpURLConnection.getOutputStream();
                Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat="UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user", encodeFormat)+"="+URLEncoder.encode(username,encodeFormat)+"&"+
                        URLEncoder.encode("password", encodeFormat)+"="+URLEncoder.encode(pwd,encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
                Log.e("Login", "bem sucedido");
                return "Cadastro Sucedido!";
            }catch (MalformedURLException ex){
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }


        }

        return "Mal sucedido";
    }
}


