package com.softjk.unishare

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.softjk.unishare.Adapter.AdapterCarreras
import com.softjk.unishare.Modelo.Carreras
import com.softjk.unishare.Regist.FragmentNewCarreras

class FragAddCarrerasUni : DialogFragment() {
    lateinit var Adapter: AdapterCarreras
    lateinit var TC: TextView
    lateinit var MasCarreras: ImageView
    lateinit var Buscar: SearchView

    lateinit var mfirestore: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog
    lateinit var ListaCarrUni: RecyclerView
    lateinit var ListaTodasCarre: RecyclerView
    lateinit var query: Query

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_add_carreras_uni, container, false)
        mfirestore = FirebaseFirestore.getInstance()
        MasCarreras = view.findViewById(R.id.MasCarr)
        Buscar = view.findViewById(R.id.BuscarCarr)
        TC = view.findViewById(R.id.lblTC)

        val args = arguments
        if (args != null) {
            val TCarreras = args.getString("TCarrera")
            println("Ver TCarrra $TCarreras")
            setUpRecyclerView(view, TCarreras!!)
            TC.setText("***** $TCarreras *****")


            /*  if (TCarreras.equals("Maestrias")){
                setUpRecyclerMaes(view);
            }else {
                setUpRecyclerView(view, TCarreras);
            } */
            MasCarreras.setOnClickListener(View.OnClickListener {
                val fragmentNewCarreras = FragmentNewCarreras()
                val args = Bundle()
                args.putString("TCarrera", TCarreras)
                fragmentNewCarreras.arguments = args
                fragmentNewCarreras.show(parentFragmentManager, "open fragment")
            })
        }
        search_view()

        return view
    }

    private fun setUpRecyclerView(view: View, TCarreras: String) {
        ListaTodasCarre = view.findViewById(R.id.ListaTodasCarreras)
        ListaTodasCarre.setLayoutManager(GridLayoutManager(requireContext(), 1))
        query = mfirestore.collection(TCarreras)
        val firestoreRecyclerOptions =
            FirestoreRecyclerOptions.Builder<Carreras>().setQuery(query, Carreras::class.java)
                .build()
        Adapter = AdapterCarreras(firestoreRecyclerOptions, requireActivity())
        //Adapter.notifyDataSetChanged()
        ListaTodasCarre.setAdapter(Adapter)
        Adapter.startListening()
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
    private fun search_view() {
        Buscar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                textSearch(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                textSearch(s)
                return false
            }
        })
    }

    fun textSearch(s: String) {
        val firestoreRecyclerOptions =
            FirestoreRecyclerOptions.Builder<Carreras>()
                .setQuery(
                    query.orderBy("Nombre")
                        .startAt(s).endAt("$s~"), Carreras::class.java
                ).build()
        Adapter = AdapterCarreras(firestoreRecyclerOptions, requireActivity())
        Adapter.startListening()
        ListaTodasCarre.adapter = Adapter
    }
}