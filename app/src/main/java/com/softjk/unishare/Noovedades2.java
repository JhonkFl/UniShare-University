package com.softjk.unishare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.codesgood.views.JustifiedTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.MenuDrawer.FragmentNovedades;

public class Noovedades2 extends AppCompatActivity {
    TextView Titulo, Punto1,Punto2,Punto3,Punto4,Punto5;
    JustifiedTextView Info;
    ImageView Imagen;
    Button BtnNovedades;

    FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_noovedades2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Titulo = findViewById(R.id.TituloNovedades2);
        Info = findViewById(R.id.InfoNovedades2);
        Imagen = findViewById(R.id.ImagenNovedades2);
        Punto1 = findViewById(R.id.InfoPunto12);
        Punto2 = findViewById(R.id.InfoPunto22);
        Punto3 = findViewById(R.id.InfoPunto32);
        Punto4 = findViewById(R.id.InfoPunto42);
        Punto5 = findViewById(R.id.InfoPunto52);
        BtnNovedades = findViewById(R.id.btnNovedades2);
        mFirestore = FirebaseFirestore.getInstance();

        ObtenerDatos();

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
                Punto1.setText(Info1);
                Punto2.setText(Info2);
                Punto3.setText(Info3);
                Punto4.setText(Info4);
                Punto5.setText(Info5);

                try { //Cargar Imagen
                    if (!ImgN.equals("")){
                        Glide.with(Noovedades2.this)
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
                Toast.makeText(Noovedades2.this, "Error al Cargar los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}