package com.book.homelist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.book.homelist.R;
import com.book.homelist.pojo.Grupo;
import com.book.homelist.pojo.Usuario;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GrupoHolder extends RecyclerView.ViewHolder {

    private TextView nomeLista;
    private TextView emailUsuario;
    private TextView itensCount;
    private TextView data;
    private ImageView admIcon;
    private ImageView adminBadge;
    private FirebaseAuth mAuth;
    private CardView cardLayout;
    private LinearLayout linear;
    private LinearLayout lowerLayout;

    private FirebaseDatabase database;
    private DatabaseReference gruposRef;
    private DatabaseReference usersRef;

    DateFormat df = new SimpleDateFormat("dd MMM", new Locale("pt", "br"));

    public GrupoHolder(View view) {
        super(view);
        nomeLista = (TextView) view.findViewById(R.id.titulo_lista);
        emailUsuario = (TextView) view.findViewById(R.id.admin_email);
        itensCount = (TextView) view.findViewById(R.id.quantidade_itens);
        data = (TextView) view.findViewById(R.id.data);
        admIcon = (ImageView) view.findViewById(R.id.admin_image);
        adminBadge = (ImageView) view.findViewById(R.id.admin_badge);
        cardLayout = (CardView) view.findViewById(R.id.root_layout);
        linear = (LinearLayout) view.findViewById(R.id.linear);
        lowerLayout = (LinearLayout) view.findViewById(R.id.lower_layout);

        database = FirebaseDatabase.getInstance();
        // gruposRef = database.getReference("grupos").child(mAuth.getCurrentUser().getUid());
        gruposRef = database.getReference("grupos");
        usersRef = database.getReference("usuarios");

        mAuth = FirebaseAuth.getInstance();


    }

    public void setGrupo(final Grupo grupo, final Context context) {
        gruposRef.child(grupo.getId()).child("usuarios-grupo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    // if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("chaveUsuario").getValue() != null) {
                    if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).exists()) {
                        if (dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("chaveUsuario").getValue().toString().equals(mAuth.getCurrentUser().getUid())) {

                            cardLayout.setVisibility(View.VISIBLE);
                            nomeLista.setVisibility(View.VISIBLE);
                            emailUsuario.setVisibility(View.VISIBLE);
                            itensCount.setVisibility(View.VISIBLE);
                            data.setVisibility(View.VISIBLE);
                            admIcon.setVisibility(View.VISIBLE);
                            lowerLayout.setVisibility(View.VISIBLE);
                            adminBadge.setVisibility(View.VISIBLE);
                            linear.setVisibility(View.VISIBLE);

                            Date date = new Date(grupo.getData());
                            nomeLista.setText(grupo.getNomeGrupo());
                            //itensCount.setText("3 itens");
                            data.setText(df.format(date));

                            if (gruposRef.child(grupo.getId()).child("itens") != null) {
                                itensCount.setVisibility(View.VISIBLE);
                                gruposRef.child(grupo.getId()).child("itens").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.hasChildren()) {
                                            itensCount.setText(String.valueOf(dataSnapshot.getChildrenCount()).concat(" itens"));
                                        } else {
                                            itensCount.setText("Nenhum item");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                itensCount.setVisibility(View.GONE);
                            }

                            Query queryRef = gruposRef.child(grupo.getId()).child("usuarios-grupo").child(mAuth.getCurrentUser().getUid()).child("admin");

                            if (queryRef != null) {
                                queryRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if (dataSnapshot.getValue() != null) {
                                            boolean isAdmin = (boolean) dataSnapshot.getValue();

                                            if (isAdmin) {
                                                adminBadge.setVisibility(View.VISIBLE);
                                            } else {
                                                adminBadge.setVisibility(View.GONE);
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }


                            usersRef.child(grupo.getAdmin()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Usuario usuario = dataSnapshot.getValue(Usuario.class);

                                    Glide.with(context).load(usuario.getPicturePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(admIcon) {
                                        @Override
                                        protected void setResource(Bitmap resource) {
                                            RoundedBitmapDrawable circularBitmapDrawable =
                                                    RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                            circularBitmapDrawable.setCircular(true);
                                            admIcon.setImageDrawable(circularBitmapDrawable);
                                        }
                                    });

                                    emailUsuario.setText(usuario.getEmailUsuario());
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        } else {
                            cardLayout.setVisibility(View.GONE);
                            nomeLista.setVisibility(View.GONE);
                            emailUsuario.setVisibility(View.GONE);
                            itensCount.setVisibility(View.GONE);
                            data.setVisibility(View.GONE);
                            admIcon.setVisibility(View.GONE);
                            lowerLayout.setVisibility(View.GONE);
                            adminBadge.setVisibility(View.GONE);
                            linear.setVisibility(View.GONE);
                        }


                    } else {
                        cardLayout.setVisibility(View.GONE);
                        nomeLista.setVisibility(View.GONE);
                        emailUsuario.setVisibility(View.GONE);
                        itensCount.setVisibility(View.GONE);
                        data.setVisibility(View.GONE);
                        admIcon.setVisibility(View.GONE);
                        adminBadge.setVisibility(View.GONE);
                        lowerLayout.setVisibility(View.GONE);
                        linear.setVisibility(View.GONE);
                    }

                } else {
                    cardLayout.setVisibility(View.GONE);
                    nomeLista.setVisibility(View.GONE);
                    emailUsuario.setVisibility(View.GONE);
                    itensCount.setVisibility(View.GONE);
                    data.setVisibility(View.GONE);
                    admIcon.setVisibility(View.GONE);
                    adminBadge.setVisibility(View.GONE);
                    lowerLayout.setVisibility(View.GONE);
                    linear.setVisibility(View.GONE);
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
