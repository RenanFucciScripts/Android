package com.rf.rastreadorem.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rf.rastreadorem.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe DAO para acessar as informações de um banco de dados;
 *
 * @author RenanFucci
 */
public class DB {
    private final static String TAG = DB.class.getName();
    private Connection con; //Conexao com BD
    private Statement st; //Requisicao de conexao
    private int last_id_inserted;//Ultimo id inserido no banco
    private PreparedStatement prepStmt_insert;//Obj de Requisicao para INSERT
    private PreparedStatement prepStmt_update;//Obj de Requisicao para UPDATE

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PWD;

    public final static String TESTAR_CONEXAO = "TESTAR_CONEXAO";
    public final static String BUSCAR_CRIANCAS = "BUSCAR_CRIANCAS";


    private boolean erroEhIdInvalido = false;
    private boolean erroEhCriancaVazia = false;

    public boolean isErroEhCriancaVazia() {
        return erroEhCriancaVazia;
    }
    public boolean isErroEhIdInvalido() {
        return erroEhIdInvalido;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo connect</b><br>
     * Metodo para fazer a conexao com o BD .
     *
     * @return o resultado booleano da conexao.
     */
    public boolean connect(Context ctx) {
        try {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ctx);
            String nomeBD = preferences.getString("nomeBD", "");
            String iphost = preferences.getString("iphost", "");
            String user = preferences.getString("user", "");
            String pwd = preferences.getString("pwd", "");

            DB_URL = "jdbc:mysql://" + iphost + ":3306/" + nomeBD + "?autoReconnect=true&useSSL=false";
            DB_USER = user;
            DB_PWD = pwd;

            Log.e(TAG, "connect: DB_URL: "+DB_URL);
            Log.e(TAG, "connect: DB_USER: "+DB_USER);
            Log.e(TAG, "connect: DB_PWD: "+DB_PWD);
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PWD);
            st = con.createStatement();
            Log.e(TAG, "connect: return true");
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "connect: ex: "+e.getLocalizedMessage() );
            e.printStackTrace();
        } catch (Exception ex) {
            Log.e(TAG, "connect: ex: "+ex.getLocalizedMessage() );
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo isConnected</b><br>
     * Metodo para testar se esta connectado com o BD .
     *
     * @return o resultado booleano da teste.
     */
    public boolean isConnected() {
        try {
            if (!con.isClosed()) {
                return true;
            }
        } catch (Exception ex) {
            Log.e(TAG, "isConnected: ex: "+ex.getLocalizedMessage() );
        }
        return false;
    }

    /**
     * <b>@author Renan Fucci<br></b>
     * <b>Metodo disconnect</b><br>
     * Metodo para fazer a desconexao com o BD .
     *
     * @return o resultado booleano da desconexao.
     */
    public boolean disconnect() {
        try {
            con.close();
            return true;
        } catch (SQLException e) {
            Log.e(TAG, "disconnect: ex: "+e.getLocalizedMessage() );
        }
        return false;
    }

    /**
     * @author RenanFucci<br>
     * <strong>Método buscarCriancasSERVER</strong><br>
     * Método para buscar, a partir de um código de criança, imagens do do banco de dados;
     * @param ctx Context do app;
     * @param idchild int com o código da criança;
     * @return {@literal ArrayList<Crianca>} lista de criancas;
     */
    public ArrayList<Crianca> buscar_criancas_por_codigo(Context ctx, int idchild){
        ArrayList<Crianca> criancas = new ArrayList<>();
        int rows = 0;
        PreparedStatement stmt;
        ResultSet rs;
        if (con != null) {
            try {
                String sql_get_id = "SELECT " + EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_CHILD.TABLE_NAME.getAsString_ComCrase() + ""
                        + " WHERE " + EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_ComCrase() + " = " + idchild + "";
                stmt = con.prepareStatement(sql_get_id);
                rs = stmt.executeQuery();
                int id_crianca = -1;
                Log.e(TAG, "buscar_criancas_por_codigo: sql_get_id: "+sql_get_id );
                while (rs.next()) {
                    id_crianca = (rs.getInt(EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_SemCrase()) != 0)
                            ? (rs.getInt(EM_HARPIA.EM_CHILD.FIELD_ID_CHILD.getAsString_SemCrase())) : (id_crianca);
                }
                Log.e(TAG, "buscar_criancas_por_codigo: id_crianca: "+id_crianca );
                if (id_crianca == -1) {
                    erroEhIdInvalido = true;
                    return null;
                }
                erroEhIdInvalido = false;
                String sql = "SELECT " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_FILE_IN_NAME.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_IN.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_ComCrase() + ","
                        + " " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_ComCrase() + ""
                        + " FROM " + EM_HARPIA.EM_HARPIA_IMAGESIN.TABLE_NAME.getAsString_ComCrase() + " "
                        + "WHERE " + EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_ID_CHILD.getAsString_ComCrase() + " = " + idchild + "";
                stmt = con.prepareStatement(sql);
                rs = stmt.executeQuery();

                Log.e(TAG, "buscar_criancas_por_codigo: sql: "+sql );
                if(!rs.isBeforeFirst()){
                    Log.e(TAG, "buscar_criancas_por_codigo: vazio");
                    erroEhCriancaVazia = true;
                    return null;
                }
                erroEhCriancaVazia=false;
                while (rs.next()) {
                    Log.e(TAG, "buscar_criancas_por_codigo: rs.next: true");

                    String file_in_name = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_FILE_IN_NAME.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_FILE_IN_NAME.getAsString_SemCrase())) : ("");
                    Log.e(TAG, "buscar_criancas_por_codigo: file_in_name: "+file_in_name);
                    String date_in = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_IN.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_IN.getAsString_SemCrase())) : ("");
                    Log.e(TAG, "buscar_criancas_por_codigo: date_in: "+date_in);

                    String date_out = (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString() != null) ? (rs.getTimestamp(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_DATE_OUT.getAsString_SemCrase()).toString()) : ("");
                    Log.e(TAG, "buscar_criancas_por_codigo: date_out: "+date_out);

                    String last_stage = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_LAST_STAGE.getAsString_SemCrase())) : ("");
                    Log.e(TAG, "buscar_criancas_por_codigo: \n\t"+file_in_name+"\t"+date_in+"\t"+date_out+"\t"+last_stage);

                    String url_out = (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase()) != null) ? (rs.getString(EM_HARPIA.EM_HARPIA_IMAGESIN.FIELD_URL_OUT.getAsString_SemCrase())) : ("");
                    Log.e(TAG, "buscar_criancas_por_codigo: \n\t"+file_in_name+"\t"+date_in+"\t"+date_out+"\t"+last_stage);
                    criancas.add(new Crianca(file_in_name, date_in, date_out, last_stage, url_out));

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(ctx, "Não foi possível conectar-se ao banco de dados.", Toast.LENGTH_LONG).show();
        }
        return criancas;
    }


}
