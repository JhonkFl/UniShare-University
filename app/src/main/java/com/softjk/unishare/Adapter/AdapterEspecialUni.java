package com.softjk.unishare.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.CarrerasUni;
import com.softjk.unishare.Modelo.Carreras;
import com.softjk.unishare.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AdapterEspecialUni extends FirestoreRecyclerAdapter<Carreras, AdapterEspecialUni.ViewHolder> {
    Activity activity;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    ProgressDialog progressDialog;
    public AdapterEspecialUni(@NonNull FirestoreRecyclerOptions<Carreras> options, Activity activity) {
        super(options);
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(activity);
    }

    @Override
    protected void onBindViewHolder(@NonNull AdapterEspecialUni.ViewHolder viewHolder, int i, @NonNull Carreras carreras) {
        viewHolder.Nombre.setText(carreras.getNombre());

        viewHolder.LinerCarrera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(viewHolder.getAdapterPosition());
                String id = documentSnapshot.getId();
                String Nomb = carreras.getNombre();
                VentanaMsgDialog(Nomb,id);
                return true;
            }
        });

    }

    private void VentanaMsgDialog( String Nombre, String id) {

        new SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE).setTitleText("Aviso")
                .setContentText("Eliminar Carrera: "+Nombre)
                .setCancelText("No").setConfirmText("Si")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();

                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    System.out.println("Ver id: " + id);
                    EliminarCarr(id);
                }).show();

    }

    private void EliminarCarr(String id) {
        String idUser = mAuth.getCurrentUser().getUid();
        String Estado = CarrerasUni.getEstad();

        progressDialog.setMessage("Eliminando Carrera...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        mFirestore.collection(Estado+"/"+idUser+"/Especializaciones").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(activity, "Eliminado", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }


    @NonNull
    @Override
    public AdapterEspecialUni.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_carreras,parent,false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Nombre;
        LinearLayoutCompat LinerCarrera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Nombre = itemView.findViewById(R.id.NombreCarreraItem);
            LinerCarrera = itemView.findViewById(R.id.LinerCarreras);
        }
    }
}
