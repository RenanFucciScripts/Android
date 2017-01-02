package com.book.homelist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.book.homelist.R;
import com.book.homelist.pojo.Usuario;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsuariosEmListaHolder extends RecyclerView.ViewHolder {

    private TextView nomeUsuario;
    private TextView emailUsuario;
    private ImageView imgUsuario;

    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    private LinearLayout rootLayout;


    public UsuariosEmListaHolder(View view) {
        super(view);
        nomeUsuario = (TextView) view.findViewById(R.id.nome_usuario);
        emailUsuario = (TextView) view.findViewById(R.id.email_usuario);
        imgUsuario = (ImageView) view.findViewById(R.id.img_usuario);
        rootLayout = (LinearLayout) view.findViewById(R.id.root_layout_user);

        database = FirebaseDatabase.getInstance();

        usersRef = database.getReference("grupos");

        mAuth = FirebaseAuth.getInstance();
    }

    public void setUsuarioEmLista(final Usuario usuario, final Context context, String grupoId) {

        usersRef.child(grupoId).child("usuarios-grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    rootLayout.setVisibility(View.VISIBLE);
                    nomeUsuario.setVisibility(View.VISIBLE);
                    emailUsuario.setVisibility(View.VISIBLE);
                    imgUsuario.setVisibility(View.VISIBLE);

                    if (dataSnapshot.child(usuario.getId()).exists()) {
                        nomeUsuario.setText(usuario.getNomeUsuario());
                        emailUsuario.setText(usuario.getEmailUsuario());

                        Glide.with(context).load(usuario.getPicturePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(imgUsuario) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                imgUsuario.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                    } else {
                        rootLayout.setVisibility(View.GONE);
                        nomeUsuario.setVisibility(View.GONE);
                        emailUsuario.setVisibility(View.GONE);
                        imgUsuario.setVisibility(View.GONE);
                    }
                } else {
                    rootLayout.setVisibility(View.GONE);
                    nomeUsuario.setVisibility(View.GONE);
                    emailUsuario.setVisibility(View.GONE);
                    imgUsuario.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}