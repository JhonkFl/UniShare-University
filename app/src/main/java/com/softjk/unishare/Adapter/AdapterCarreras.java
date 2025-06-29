package com.softjk.unishare.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.CarrerasUni;
import com.softjk.unishare.Modelo.Carreras;
import com.google.firebase.firestore.FieldValue;
import com.softjk.unishare.Modelo.Universidades;
import com.softjk.unishare.R;

import java.util.HashMap;
import java.util.Map;

public class AdapterCarreras extends FirestoreRecyclerAdapter<Carreras, AdapterCarreras.ViewHolder> {
    Activity activity;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore mfirestore;
    SharedPreferences preferences;
    public AdapterCarreras(@NonNull FirestoreRecyclerOptions<Carreras> options, Activity activity) {
        super(options);
        this.activity = activity;
        this.preferences = activity.getSharedPreferences("id", Context.MODE_PRIVATE);
        mfirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(activity);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterCarreras.ViewHolder viewHolder, int i, @NonNull Carreras carreras) {
        viewHolder.Nombre.setText(carreras.getNombre());
        String Categoria = carreras.getCategoria();
        String Foto = carreras.getIMG();
        String Carrera = carreras.getNombre();
        String ABC = carreras.getABC();
        String idUni = mAuth.getCurrentUser().getUid();
        String Estado = CarrerasUni.getEstad();
        String TCarrera = CarrerasUni.getTCarreras();
        System.out.println("ver idUni "+idUni+" y estado "+Estado);

        try {
            if (!Foto.equals("")){
                Glide.with(activity.getApplicationContext())
                        .load(Foto)
                        .into(viewHolder.Logo);
            }
        }catch (Exception e){
            Log.d("Exception","e: "+e);
        }

        viewHolder.BtnAdd.setVisibility(View.VISIBLE);
        viewHolder.BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Guardando Datos...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                if (TCarrera.equals("Carreras")){
                    //AddTC(Estado,idUni,TCarrera,Carrera,ABC,Categoria);
                    AddTC(Estado,idUni,TCarrera,Carrera,ABC,Categoria);
                }
                if (TCarrera.equals("Especializaciones") || TCarrera.equals("Maestrias") || TCarrera.equals("Doctorado")){
                    ActualizarTC(Estado,idUni,TCarrera,Carrera,ABC,Categoria);
                }

            }
        });

    }

    private void ActualizarTC(String Estado,String idUni,String TCarrera,String Nombre,String ABC,String Categori) {
        progressDialog.setMessage("Preparando...");
        progressDialog.show();
        Map<String, Object> map = new HashMap<>();
        map.put(TCarrera, "Si");

        mfirestore.collection(Estado).document(idUni).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity.getApplicationContext(), "Guardando", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                AddTC(Estado,idUni,TCarrera,Nombre,ABC,Categori);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AddTC(String Estado,String idUni,String TCarrera,String Nombre,String ABC,String Cate) {
        Map<String, Object> map = new HashMap<>();
        map.put("Nombre", Nombre);
        map.put("Categoria",Cate);

        mfirestore.collection(Estado+"/"+idUni+"/"+TCarrera).document(ABC).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity.getApplicationContext(), TCarrera + " Agregado", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                //actualizarMapCarr(ABC,Cate,Estado,idUni);
                if (TCarrera.equals("Carreras")){
                    AgregarUniCollec(Estado,idUni,ABC);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "Error al Agregar La Carrera", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AgregarUniCollec(String estado, String idUni,String idCarrera) {

        String NombreUni = preferences.getString("Nombre","");
        String LogoUni = preferences.getString("Logo","");
        Map<String, Object> map = new HashMap<>();
        map.put("Nombre",NombreUni);
        map.put("Logo",LogoUni);
        map.put("Estado",estado);

        mfirestore.collection("Carreras/"+idCarrera+"/Uni").document(idUni).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Agregado...", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void actualizarMapCarr(String ABC, String Categoria, String Estado,String idUni) {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put(String.valueOf(FieldPath.of("Carreras", ABC)),
                FieldValue.arrayUnion(Categoria));

        mfirestore.collection(Estado).document(idUni).update(updateData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity.getApplicationContext(), Categoria + " Agregado", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity.getApplicationContext(), "Error al Agregar La Carrera", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @NonNull
    @Override
    public AdapterCarreras.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carreras,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre;
        ImageView Logo;
        LinearLayoutCompat Carrera;
        Button BtnAdd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.NombreCarreraItem);
            Carrera = itemView.findViewById(R.id.LinerCarreras);
            BtnAdd = itemView.findViewById(R.id.addCarrera);
        }
    }
}
