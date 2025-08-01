package com.softjk.unishare;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.softjk.unishare.Modelo.Carreras;
import com.softjk.unishare.Adapter.AdapterCarreras;
import com.softjk.unishare.Regist.FragmentNewCarreras;


public class FragAddCarrerasUni extends DialogFragment {
    AdapterCarreras Adapter;
    TextView TC;
    ImageView MasCarreras;
    SearchView Buscar;

    FirebaseFirestore mfirestore;
    ProgressDialog progressDialog;
    RecyclerView ListaCarrUni, ListaTodasCarre;
    Query query;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_add_carreras_uni, container, false);
        mfirestore = FirebaseFirestore.getInstance();
        MasCarreras = view.findViewById(R.id.MasCarr);
        Buscar = view.findViewById(R.id.BuscarCarr);
        TC = view.findViewById(R.id.lblTC);

        Bundle args = getArguments();
        if (args != null) {
            String TCarreras = args.getString("TCarrera");
            System.out.println("Ver TCarrra " + TCarreras);
            setUpRecyclerView(view, TCarreras);
            TC.setText("***** "+TCarreras+" *****");
          /*  if (TCarreras.equals("Maestrias")){
                setUpRecyclerMaes(view);
            }else {
                setUpRecyclerView(view, TCarreras);
            } */


            MasCarreras.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentNewCarreras fragmentNewCarreras = new FragmentNewCarreras();
                    Bundle args = new Bundle();
                    args.putString("TCarrera",TCarreras);
                    fragmentNewCarreras.setArguments(args);
                    fragmentNewCarreras.show(getParentFragmentManager(),"open fragment");
                }
            });
        }
        search_view();

        return view;
    }

    private void setUpRecyclerView(View view,String TCarreras) {

        ListaTodasCarre = view.findViewById(R.id.ListaTodasCarreras);
        ListaTodasCarre.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        query = mfirestore.collection(TCarreras);
        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras.class).build();
        Adapter = new AdapterCarreras(firestoreRecyclerOptions,getActivity());
        Adapter.notifyDataSetChanged();
        ListaTodasCarre.setAdapter(Adapter);
        Adapter.startListening();
    }

   /* private void setUpRecyclerMaes(View view) {

        ListaTodasCarre = view.findViewById(R.id.ListaTodasCarreras);
        ListaTodasCarre.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        query = mfirestore.collection("Maestrias");
        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras.class).build();
        Adapter = new AdapterCarreras(firestoreRecyclerOptions,getActivity());
        Adapter.notifyDataSetChanged();
        ListaTodasCarre.setAdapter(Adapter);
        Adapter.startListening();
    } */


    private void search_view() {
        Buscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });
    }

    public void textSearch(String s) {
        FirestoreRecyclerOptions<Carreras> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Carreras>()
                        .setQuery(query.orderBy("Nombre")
                                .startAt(s).endAt(s + "~"), Carreras.class).build();
        Adapter = new AdapterCarreras(firestoreRecyclerOptions, getActivity());
        Adapter.startListening();
        ListaTodasCarre.setAdapter(Adapter);
    }

}