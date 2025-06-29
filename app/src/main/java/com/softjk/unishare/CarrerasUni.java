package com.softjk.unishare;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.softjk.unishare.Adapter.AdapterCarrerasUni;
import com.softjk.unishare.Adapter.AdapterDoctUni;
import com.softjk.unishare.Adapter.AdapterEspecialUni;
import com.softjk.unishare.Adapter.AdapterMaestriaUni;
import com.softjk.unishare.Modelo.Carreras;

public class CarrerasUni extends AppCompatActivity {
    RecyclerView ListaCarreras,ListaPosgrados,ListaMaestria,ListaDoctorado;
    Button Carreras, Especialidad,Maestria,Docturado;

    static String TCarreras;
    static String Estad;
    static String ItemCarrer;
    AdapterCarrerasUni Adapter;
    AdapterEspecialUni AdapterPos;
    AdapterMaestriaUni AdapterMaes;
    AdapterDoctUni AdapterDoc;
    ImageView Totu,DialogCar;
    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_carreras);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String idUser = mAuth.getCurrentUser().getUid();

        Carreras = findViewById(R.id.AddCarr);
        Especialidad = findViewById(R.id.AddEsp);
        Maestria = findViewById(R.id.AddMaes);
        Docturado = findViewById(R.id.AddDoc);
        Totu = findViewById(R.id.IMGTutorial2);
        DialogCar = findViewById(R.id.IMGEliminarCar);

        SharedPreferences preferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        Estad = preferences.getString("Estado","");

        setUpRecyclerView(idUser,Estad);
        RecyclerPos(idUser,Estad);
        RecyclerMaes(idUser,Estad);
        RecyclerDoc(idUser,Estad);

        Carreras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCarreras = "Carreras";
                FragAddCarrerasUni fragmentCarreras = new FragAddCarrerasUni();
                Bundle args = new Bundle();
                args.putString("TCarrera","Carreras");
                fragmentCarreras.setArguments(args);
                fragmentCarreras.show(getSupportFragmentManager(),"open fragment");
            }
        });

        Especialidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCarreras = "Especializaciones";
                FragAddCarrerasUni fragmentCarreras = new FragAddCarrerasUni();
                Bundle args = new Bundle();
                args.putString("TCarrera","Especializaciones");
                fragmentCarreras.setArguments(args);
                fragmentCarreras.show(getSupportFragmentManager(),"open fragment");
            }
        });

        Maestria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCarreras = "Maestrias";
                FragAddCarrerasUni fragmentCarreras = new FragAddCarrerasUni();
                Bundle args = new Bundle();
                args.putString("TCarrera","Maestrias");
                fragmentCarreras.setArguments(args);
                fragmentCarreras.show(getSupportFragmentManager(),"open fragment");
            }
        });

        Docturado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TCarreras = "Doctorado";
                FragAddCarrerasUni fragmentCarreras = new FragAddCarrerasUni();
                Bundle args = new Bundle();
                args.putString("TCarrera","Doctorado");
                fragmentCarreras.setArguments(args);
                fragmentCarreras.show(getSupportFragmentManager(),"open fragment");
            }
        });

        Totu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int DURACION1 = 1000;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogCar.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DialogCar.setVisibility(View.GONE);
                            }
                        },DURACION1);
                    }
                },DURACION1);
            }
        });
    }

    private void setUpRecyclerView(String idUni,String Estados) {
        ListaCarreras = findViewById(R.id.ListaCarreras);
        ListaCarreras.setLayoutManager(new GridLayoutManager(this,1));
        query = mFirestore.collection(Estados+"/"+idUni+"/Carreras");

        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras.class).build();

        Adapter = new AdapterCarrerasUni(firestoreRecyclerOptions,this);
        Adapter.notifyDataSetChanged();
        if (ListaCarreras.isClickable()){
            ItemCarrer = "Carreras";
        }
        ListaCarreras.setAdapter(Adapter);
    }

    private void RecyclerPos(String idUni,String Estados) {
        ListaPosgrados = findViewById(R.id.ListaEspecial);
        ListaPosgrados.setLayoutManager(new GridLayoutManager(this,1));
        query = mFirestore.collection(Estados+"/"+idUni+"/Especializaciones");

        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras.class).build();

        AdapterPos = new AdapterEspecialUni(firestoreRecyclerOptions,this);
        AdapterPos.notifyDataSetChanged();
        ListaPosgrados.setAdapter(AdapterPos);
    }

    private void RecyclerMaes(String idUni,String Estados) {
        ListaMaestria = findViewById(R.id.ListaMaestria);
        ListaMaestria.setLayoutManager(new GridLayoutManager(this,1));
        query = mFirestore.collection(Estados+"/"+idUni+"/Maestrias");

        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras.class).build();

        AdapterMaes = new AdapterMaestriaUni(firestoreRecyclerOptions,this);
        AdapterMaes.notifyDataSetChanged();
        ListaMaestria.setAdapter(AdapterMaes);
    }

    private void RecyclerDoc(String idUni,String Estados) {
        ListaDoctorado = findViewById(R.id.ListaDoctorado);
        ListaDoctorado.setLayoutManager(new GridLayoutManager(this,1));
        query = mFirestore.collection(Estados+"/"+idUni+"/Doctorado");

        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras.class).build();

        AdapterDoc = new AdapterDoctUni(firestoreRecyclerOptions,this);
        AdapterDoc.notifyDataSetChanged();
        ListaDoctorado.setAdapter(AdapterDoc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Adapter.startListening();
        AdapterPos.startListening();
        AdapterMaes.startListening();
        AdapterDoc.startListening();
    }

    public static String getTCarreras(){return TCarreras;}
    public static String getEstad(){return Estad;}
}