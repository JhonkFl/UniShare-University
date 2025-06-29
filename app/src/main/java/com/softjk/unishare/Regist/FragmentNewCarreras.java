package com.softjk.unishare.Regist;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.softjk.unishare.Adapter.AdapterCarrerasUni;
import com.softjk.unishare.Modelo.Carreras;
import com.softjk.unishare.R;

import java.util.HashMap;
import java.util.Map;

public class FragmentNewCarreras extends DialogFragment {
    ImageView Logo;
    EditText Nombre,Info,ABC;
    Button Add;
    Spinner Categoria;
    FirebaseFirestore mfFirestore;
    AdapterCarrerasUni Adapter;
    ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_carreras, container, false);
        getActivity().setTitle("Carreras");
        progressDialog = new ProgressDialog(getActivity());
        Nombre = view.findViewById(R.id.txtCarrera);
        ABC = view.findViewById(R.id.txtABCAddCar);
        Info = view.findViewById(R.id.txtInfo);
        Logo = view.findViewById(R.id.ImgCarrera);
        Add = view.findViewById(R.id.BtnAddCarre);
        Categoria = view.findViewById(R.id.SpCategoria);
        mfFirestore = FirebaseFirestore.getInstance();

        String [] OpcionesCate = {"Seleccione una Categioria","Arte","Ciencias","Comunicación","Derecho","Educación","Finanzas",
                "Humanidades","Ingeniería","Medicina","Militar","Servicios","Tecnología","Otro"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getActivity(),R.layout.spinner_item_estilo,OpcionesCate);
        Categoria.setAdapter(adapter2);

        Bundle args = getArguments();
        if (args != null) {
            String TCarreras = args.getString("TCarrera");
            Add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Categ = Categoria.getSelectedItem().toString().trim();
                    if(Categ.equals("Seleccione una Categioria")){
                        Toast.makeText(getActivity(), "Por Favor Seleccione una Categoria de la Carrera", Toast.LENGTH_SHORT).show();
                    }else {
                        if (TCarreras.equals("Maestrias")) {
                            GuardarCarreras("Maestrias");
                        } else if (TCarreras.equals("Doctorado")) {
                            GuardarCarreras("Doctorado");
                        } else if (TCarreras.equals("Especializaciones")) {
                            GuardarCarreras("Especializaciones");
                        } else if (TCarreras.equals("Carreras")) {
                            GuardarCarreras("Carreras");
                        } else {
                            Toast.makeText(getActivity(), "Error! no se encontro el Tipo de Carrera", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
        return view;

    }

    private void GuardarCarreras(String TCarr) {
        progressDialog.setMessage("Guardando Datos...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        Map<String, Object> map = new HashMap<>();
        String Nom = Nombre.getText().toString().trim();
        String Abc = ABC.getText().toString().trim();
        String Categ = Categoria.getSelectedItem().toString().trim();

        map.put("Nombre", Nom);
        map.put("ABC","New_"+Abc);
        map.put("Categoria",Categ);

        mfFirestore.collection(TCarr).document("New_"+Abc).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                if (TCarr.equals("Doctorado")){
                    Toast.makeText(getActivity(), "Nuevo Doctorado Creado" , Toast.LENGTH_SHORT).show();
                } else if (TCarr.equals("Especializaciones")) {
                    Toast.makeText(getActivity(), "Nueva Especialidad Creada", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Nueva " + TCarr + " Creada", Toast.LENGTH_SHORT).show();
                }
                ABC.setText("");
                Nombre.setText("");
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al Crear La Carrera", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

}
