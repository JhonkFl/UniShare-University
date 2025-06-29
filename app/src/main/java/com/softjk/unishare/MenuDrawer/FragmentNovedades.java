package com.softjk.unishare.MenuDrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.R;


public class FragmentNovedades extends Fragment {

    TextView Titulo, Punto1,Punto2,Punto3,Punto4,Punto5;
    JustifiedTextView Info;
    ImageView Imagen;
    Button BtnNovedades;

    FirebaseFirestore mFirestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_novedades, container, false);
        getActivity().setTitle("Novedades");

        Titulo = view.findViewById(R.id.TituloNovedades);
        Info = view.findViewById(R.id.InfoNovedades);
        Imagen = view.findViewById(R.id.ImagenNovedades);
        Punto1 = view.findViewById(R.id.InfoPunto1);
        Punto2 = view.findViewById(R.id.InfoPunto2);
        Punto3 = view.findViewById(R.id.InfoPunto3);
        Punto4 = view.findViewById(R.id.InfoPunto4);
        Punto5 = view.findViewById(R.id.InfoPunto5);
        BtnNovedades = view.findViewById(R.id.btnNovedades);
        mFirestore = FirebaseFirestore.getInstance();

        ObtenerDatos();
        return view;
    }

    private void ObtenerDatos() {
        mFirestore.collection("Novedades").document("Universidades").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String TituloN = documentSnapshot.getString("Titulo");
                String InfoN = documentSnapshot.getString("Informacion");
                String Info1 = documentSnapshot.getString("Punto1");
                String Info2 = documentSnapshot.getString("Punto2");
                String Info3 = documentSnapshot.getString("Punto3");
                String Info4 = documentSnapshot.getString("Punto4");
                String Info5 = documentSnapshot.getString("Punto5");
                String ImgN = documentSnapshot.getString("IMG");
                String Actualizar = documentSnapshot.getString("Actualizar");

                Titulo.setText(TituloN);
                Info.setText(InfoN);

                if (Info1.equals("")){Punto1.setVisibility(View.GONE);}
                if (Info2.equals("")){Punto2.setVisibility(View.GONE);}
                if (Info3.equals("")){Punto3.setVisibility(View.GONE);}
                if (Info4.equals("")){Punto4.setVisibility(View.GONE);}
                if (Info5.equals("")){Punto5.setVisibility(View.GONE);}

                Punto1.setText(Info1);
                Punto2.setText(Info2);
                Punto3.setText(Info3);
                Punto4.setText(Info4);
                Punto5.setText(Info5);

                try { //Cargar Imagen
                    if (!ImgN.equals("")){
                        Glide.with(FragmentNovedades.this)
                                .load(ImgN)
                                .into(Imagen);
                    }
                }catch (Exception e){
                    Log.v("Error","e"+e);
                }

                if (Actualizar.equals("Si")){  //Mostrar Boton y dar clic
                    BtnNovedades.setVisibility(View.VISIBLE);

                    BtnNovedades.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String URL = "https://play.google.com/store/apps/details?id=com.softjk.uni";
                            Uri Link = Uri.parse(URL);
                            Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                            startActivity(intent);
                        }
                    });
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al Cargar los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}