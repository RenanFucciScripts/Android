package com.example.renanfucci.buscagro.method.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Renan Fucci on 06/01/2016.
 */
public class BackgroundTask extends AsyncTask<String, Void, String> {

    private static final String TAG = BackgroundTask.class.getName();
    Context ctx;
    private String str_result;


    public BackgroundTask(Context context) {
        this.ctx = context;
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
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
        if (method.contentEquals("cadastrar")) {
            String user_nome = params[1];
            String user_email = params[2];
            String user_pwd = params[3];
            String user_tel = params[4];
            String user_endereco = params[5];
            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/cadastro.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_nome", encodeFormat) + "=" + URLEncoder.encode(user_nome, encodeFormat) + "&" +
                        URLEncoder.encode("user_email", encodeFormat) + "=" + URLEncoder.encode(user_email, encodeFormat) + "&" +
                        URLEncoder.encode("user_pwd", encodeFormat) + "=" + URLEncoder.encode(user_pwd, encodeFormat) + "&" +
                        URLEncoder.encode("user_tel", encodeFormat) + "=" + URLEncoder.encode(user_tel, encodeFormat) + "&" +
                        URLEncoder.encode("user_endereco", encodeFormat) + "=" + URLEncoder.encode(user_endereco, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }
        } else if (method.contentEquals("insertoferta")) {
            String oferta_nome = params[1];
            String oferta_qntd = params[2];
            String oferta_validade = params[3];
            String user_id = params[4];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/insertoferta.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("oferta_nome", encodeFormat) + "=" + URLEncoder.encode(oferta_nome, encodeFormat) + "&" +
                        URLEncoder.encode("oferta_qntd", encodeFormat) + "=" + URLEncoder.encode(oferta_qntd, encodeFormat) + "&" +
                        URLEncoder.encode("oferta_validade", encodeFormat) + "=" + URLEncoder.encode(oferta_validade, encodeFormat) + "&" +
                        URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                str_result = "-1";
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                str_result = "-1";
                e.printStackTrace();
            }

        }else if (method.contentEquals("insertdemanda")) {
            String demanda_nome = params[1];
            String demanda_qntd = params[2];
            String demanda_validade = params[3];
            String user_id = params[4];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/insertdemanda.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("demanda_nome", encodeFormat) + "=" + URLEncoder.encode(demanda_nome, encodeFormat) + "&" +
                        URLEncoder.encode("demanda_qntd", encodeFormat) + "=" + URLEncoder.encode(demanda_qntd, encodeFormat) + "&" +
                        URLEncoder.encode("demanda_validade", encodeFormat) + "=" + URLEncoder.encode(demanda_validade, encodeFormat) + "&" +
                        URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                str_result = "-1";
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                str_result = "-1";
                e.printStackTrace();
            }
        }   else if (method.contentEquals("login")) {
            String user_email = params[1];
            String user_pwd = params[2];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/login.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_email", encodeFormat) + "=" + URLEncoder.encode(user_email, encodeFormat) + "&" +
                        URLEncoder.encode("user_pwd", encodeFormat) + "=" + URLEncoder.encode(user_pwd, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                     str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        } else if (method.contentEquals("selectuserdemandas")) {
            String user_id = params[1];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectuserdemandas.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        } else if (method.contentEquals("selectuserofertas")) {
            String user_id = params[1];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectuserofertas.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login false doInBackground: " + sb.toString());

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }
        else if (method.contentEquals("deleteoferta")) {
            String oferta_id = params[1];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/deleteoferta.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("oferta_id", encodeFormat) + "=" + URLEncoder.encode(oferta_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login false doInBackground: " + sb.toString());

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }
        else if (method.contentEquals("deletedemanda")) {
            String oferta_id = params[1];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/deletedemanda.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("demanda_id", encodeFormat) + "=" + URLEncoder.encode(oferta_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }
        else if (method.contentEquals("selectofertas")) {
            String oferta_nome = params[1];
            String oferta_qntd = params[2];
            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectofertas.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("oferta_nome", encodeFormat) + "=" + URLEncoder.encode(oferta_nome, encodeFormat)+"&"+
                        URLEncoder.encode("oferta_qntd", encodeFormat) + "=" + URLEncoder.encode(oferta_qntd, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }
        else if (method.contentEquals("selectdemandas")) {
            String demanda_nome = params[1];
            String demanda_qntd = params[2];
            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectdemandas.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("demanda_nome", encodeFormat) + "=" + URLEncoder.encode(demanda_nome, encodeFormat)+"&"+
                        URLEncoder.encode("demanda_qntd", encodeFormat) + "=" + URLEncoder.encode(demanda_qntd, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }
        else if (method.contentEquals("selectuserinfo")) {

            String user_id = params[1];
            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectuserinfo.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }
        else if (method.contentEquals("selectallofertas")) {

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectallofertas.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                //Log.e("os", httpURLConnection.getURL().getPath());
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode("1", encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    //Log.e(TAG, "login true doInBackground: " + sb.toString());
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }        else if (method.contentEquals("selectalldemandas")) {

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/selectalldemandas.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode("1", encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        } else if (method.contentEquals("updateuser")) {

            String user_nome = params[1];
            Log.e(TAG, "doInBackground: usernome:\t param[0]: "+user_nome);
            String user_endereco = params[2];
            String user_pwd  = params[3];
            String user_tel = params[4];
            String user_id = params[5];
            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/updateuser.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat)+"&"+
                        URLEncoder.encode("user_nome", encodeFormat) + "=" + URLEncoder.encode(user_nome, encodeFormat)+"&"+
                        URLEncoder.encode("user_pwd", encodeFormat) + "=" + URLEncoder.encode(user_pwd, encodeFormat)+"&"+
                        URLEncoder.encode("user_endereco", encodeFormat) + "=" + URLEncoder.encode(user_endereco, encodeFormat)+"&"+
                        URLEncoder.encode("user_tel", encodeFormat) + "=" + URLEncoder.encode(user_tel, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }

        else if (method.contentEquals("cadastrarcomid")) {
                String user_id = params[1];
                String user_nome = params[2];
                String user_email = params[3];
                String user_pwd = params[4];
                String user_tel = params[5];
                String user_endereco = params[6];

            try {
                URL url = new URL("http://inoxtrading.com.br/buscagro/cadastrarcomid.php");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                String encodeFormat = "UTF-8";
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, encodeFormat));
                String data = URLEncoder.encode("user_id", encodeFormat) + "=" + URLEncoder.encode(user_id, encodeFormat)+"&"+
                        URLEncoder.encode("user_nome", encodeFormat) + "=" + URLEncoder.encode(user_nome, encodeFormat)+"&"+
                        URLEncoder.encode("user_email", encodeFormat) + "=" + URLEncoder.encode(user_email, encodeFormat)+"&"+
                        URLEncoder.encode("user_pwd", encodeFormat) + "=" + URLEncoder.encode(user_pwd, encodeFormat)+"&"+
                        URLEncoder.encode("user_tel", encodeFormat) + "=" + URLEncoder.encode(user_tel, encodeFormat)+"&"+
                        URLEncoder.encode("user_endereco", encodeFormat) + "=" + URLEncoder.encode(user_endereco, encodeFormat);
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                BufferedReader br;
                StringBuilder sb;
                String line = "";
                if (200 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 299) {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getInputStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }
                    str_result = sb.toString();
                    return sb.toString();
                } else {
                    br = new BufferedReader(new InputStreamReader((httpURLConnection.getErrorStream())));
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + '\n');
                    }

                    str_result = sb.toString();
                    return sb.toString();
                }
            } catch (MalformedURLException ex) {
                Log.e("MalformedURLException", ex.getMessage());
                ex.printStackTrace();
            } catch (Exception e) {
                Log.e("Exception", " exception");
                e.printStackTrace();
            }

        }




        return "Mal sucedido";
    }


    public String getStr_result() {
        return str_result;
    }
}
