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

public class UsuariosHolder extends RecyclerView.ViewHolder {

    private TextView nomeUsuario;
    private TextView emailUsuario;
    private ImageView imgUsuario;
    private ImageView btnAddUser;

    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;

    private LinearLayout rootLayout;


    public UsuariosHolder(View view) {
        super(view);
        nomeUsuario = (TextView) view.findViewById(R.id.txt_nome_user);
        emailUsuario = (TextView) view.findViewById(R.id.txt_email_user);
        imgUsuario = (ImageView) view.findViewById(R.id.img_lista_item);
        btnAddUser = (ImageView) view.findViewById(R.id.btn_add_user);

        database = FirebaseDatabase.getInstance();

        usersRef = database.getReference("grupos");

        mAuth = FirebaseAuth.getInstance();
    }

    public void setAddUser(final Usuario usuario, final Context context, final String grupoId) {


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

        usersRef.child(grupoId).child("usuarios-grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                usersRef.child(grupoId).child("admin").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue().toString().equals(usuario.getId())) {
                            btnAddUser.setVisibility(View.GONE);
                        } else {
                            btnAddUser.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                if (!dataSnapshot.child(usuario.getId()).exists()) {
                    btnAddUser.setImageResource(R.drawable.ic_add_circle_black_24dp);
                    //btnAddUser.setEnabled(true);
                } else {
                    btnAddUser.setImageResource(R.drawable.ic_check_circle_black_24dp);
                    //btnAddUser.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}