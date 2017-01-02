package com.book.homelist.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.book.homelist.R;
import com.book.homelist.pojo.Grupo;
import com.book.homelist.pojo.UsuariosPorGrupo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CriarGrupoDialog {

    private AlertDialog.Builder builder;
    private Context mContext;

    private ProgressDialog mProgress;

    private DatabaseReference gruposRef;

    private FirebaseAuth auth;

    private EditText edtNomeGrupo;

    private CoordinatorLayout mCoordinatorLayout;

    private View view;

    LayoutInflater inflater;

    private long time = System.currentTimeMillis();

    public CriarGrupoDialog(Context context) {
        this.mContext = context;
        builder = new AlertDialog.Builder(mContext);

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        view = inflater.inflate(R.layout.dialog_group, null);

        auth = FirebaseAuth.getInstance();

        gruposRef = FirebaseDatabase.getInstance().getReference("grupos");
    }

    private String getUID() {
        return auth.getCurrentUser().getUid();
    }

    private void salvarGrupo(String nomeGrupo, View via) {

        mProgress = ProgressDialog.show(mContext, null, mContext.getString(R.string.txt_criando_grupo));

        if (validate()) {
            final Grupo grupo = new Grupo();
            grupo.setNomeGrupo(nomeGrupo);
            grupo.setData(time);

            String pushKey = gruposRef.push().getKey();
            //String pushKey = gruposRef.child("usuarios").child(auth.getCurrentUser().getUid()).child("listas").push().getKey();

            String adminId = auth.getCurrentUser().getUid();

            grupo.setId(pushKey);
            grupo.setAdmin(adminId);
            gruposRef.child(pushKey).setValue(grupo);


            DatabaseReference usuariosPorListaRef = gruposRef.child(pushKey).child("usuarios-grupo").child(auth.getCurrentUser().getUid());

            UsuariosPorGrupo usuariosPorGrupo = new UsuariosPorGrupo();
            usuariosPorGrupo.setChaveUsuario(adminId);
            usuariosPorGrupo.setAdmin(true);

            //usuarios.setValue(grupo);

            usuariosPorListaRef.setValue(usuariosPorGrupo);
        }

        mProgress.dismiss();

        //Snackbar.make(mCoordinatorLayout, "Add", Snackbar.LENGTH_LONG).show();

        Toast.makeText(mContext, "Grupo criado.", Toast.LENGTH_LONG).show();


    }

    private boolean validate() {

        if (edtNomeGrupo.getText().toString().isEmpty()) {
            edtNomeGrupo.setError(mContext.getString(R.string.txt_required));
            return false;
        } else {
            return true;
        }
    }

    public void criarGrupoDialog() {
        edtNomeGrupo = (EditText) view.findViewById(R.id.editNomeGrupo);

        builder.setView(view);

        builder.setPositiveButton("salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (validate()) {
                    salvarGrupo(edtNomeGrupo.getText().toString(), view);
                }
            }
        });

        builder.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }
}
