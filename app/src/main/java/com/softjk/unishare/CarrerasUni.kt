package com.softjk.unishare

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.firebase.ui.firestore.SnapshotParser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.softjk.unishare.Modelo.Carreras
import com.google.firebase.auth.FirebaseAuth
import com.softjk.unishare.Adapter.AdapterCarrerasUni
import com.softjk.unishare.Adapter.AdapterDoctUni
import com.softjk.unishare.Adapter.AdapterEspecialUni
import com.softjk.unishare.Adapter.AdapterMaestriaUni


class CarrerasUni : AppCompatActivity() {
    lateinit var ListaCarreras: RecyclerView
    lateinit var ListaPosgrados: RecyclerView
    lateinit var ListaMaestria: RecyclerView
    lateinit var ListaDoctorado: RecyclerView
    lateinit var Carreras: Button
    lateinit var Especialidad: Button
    lateinit var Maestria: Button
    lateinit var Docturado: Button

    lateinit var Adapter: AdapterCarrerasUni
    lateinit var AdapterPos: AdapterEspecialUni
    lateinit var AdapterMaes: AdapterMaestriaUni
    lateinit var AdapterDoc: AdapterDoctUni
    lateinit var Totu: ImageView
    lateinit var DialogCar: ImageView
    lateinit var mAuth: FirebaseAuth
    val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_carreras)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {
            v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mAuth = FirebaseAuth.getInstance()
        val idUser = mAuth.currentUser!!.uid

        Carreras = findViewById(R.id.AddCarr)
        Especialidad = findViewById(R.id.AddEsp)
        Maestria = findViewById(R.id.AddMaes)
        Docturado = findViewById(R.id.AddDoc)
        Totu = findViewById(R.id.IMGTutorial2)
        DialogCar = findViewById(R.id.IMGEliminarCar)

        val preferences = getSharedPreferences("id", MODE_PRIVATE)
        estad = preferences.getString("Estado", "")

        setUpRecyclerView(idUser, estad)
        RecyclerPos(idUser, estad)
        RecyclerMaes(idUser, estad)
        RecyclerDoc(idUser, estad)

        Carreras.setOnClickListener(View.OnClickListener {
            tCarreras = "Carreras"
            val fragmentCarreras = FragAddCarrerasUni()
            val args = Bundle()
            args.putString("TCarrera", "Carreras")
            fragmentCarreras.arguments = args
            fragmentCarreras.show(supportFragmentManager, "open fragment")
        })

        Especialidad.setOnClickListener(View.OnClickListener {
            tCarreras = "Especializaciones"
            val fragmentCarreras = FragAddCarrerasUni()
            val args = Bundle()
            args.putString("TCarrera", "Especializaciones")
            fragmentCarreras.arguments = args
            fragmentCarreras.show(supportFragmentManager, "open fragment")
        })

        Maestria.setOnClickListener(View.OnClickListener {
            tCarreras = "Maestrias"
            val fragmentCarreras = FragAddCarrerasUni()
            val args = Bundle()
            args.putString("TCarrera", "Maestrias")
            fragmentCarreras.arguments = args
            fragmentCarreras.show(supportFragmentManager, "open fragment")
        })

        Docturado.setOnClickListener(View.OnClickListener {
            tCarreras = "Doctorado"
            val fragmentCarreras = FragAddCarrerasUni()
            val args = Bundle()
            args.putString("TCarrera", "Doctorado")
            fragmentCarreras.arguments = args
            fragmentCarreras.show(supportFragmentManager, "open fragment")
        })

        Totu.setOnClickListener(View.OnClickListener {
            val DURACION1 = 1000
            Handler().postDelayed({
                DialogCar.setVisibility(View.VISIBLE)
                Handler().postDelayed(
                    { DialogCar.setVisibility(View.GONE) },
                    DURACION1.toLong()
                )
            }, DURACION1.toLong())
        })
    }

    private fun setUpRecyclerView(idUni: String, Estados: String?) {
        ListaCarreras = findViewById(R.id.ListaCarreras)
        ListaCarreras.setLayoutManager(GridLayoutManager(this, 1))
        val query: Query = FirebaseFirestore.getInstance()
            .collection("$Estados/$idUni/Carreras")

        val options = FirestoreRecyclerOptions.Builder<Carreras>()
            .setQuery(query, Carreras().javaClass)
            .build()

        Adapter = AdapterCarrerasUni(options, this)
        Adapter.notifyDataSetChanged()
        if (ListaCarreras.isClickable()) {
            ItemCarrer = "Carreras"
        }
        ListaCarreras.setAdapter(Adapter)
    }

    private fun RecyclerPos(idUni: String, Estados: String?) {
        ListaPosgrados = findViewById(R.id.ListaEspecial)
        ListaPosgrados.setLayoutManager(GridLayoutManager(this, 1))
        val query: Query = FirebaseFirestore.getInstance()
            .collection("$Estados/$idUni/Especializaciones")

        val Options = FirestoreRecyclerOptions.Builder<Carreras>()
                .setQuery(query, Carreras().javaClass)
                .build()

        AdapterPos = AdapterEspecialUni(Options, this)
        AdapterPos.notifyDataSetChanged()
        ListaPosgrados.setAdapter(AdapterPos)
    }

    private fun RecyclerMaes(idUni: String, Estados: String?) {
        ListaMaestria = findViewById(R.id.ListaMaestria)
        ListaMaestria.setLayoutManager(GridLayoutManager(this, 1))
        val query: Query = mFirestore.collection("$Estados/$idUni/Maestrias")

        val options = FirestoreRecyclerOptions.Builder<Carreras>()
            .setQuery(query, Carreras().javaClass)
            .build()

        AdapterMaes = AdapterMaestriaUni(options, this)
        ListaMaestria.setAdapter(AdapterMaes)
    }

    private fun RecyclerDoc(idUni: String, Estados: String?) {
        ListaDoctorado = findViewById(R.id.ListaDoctorado)
        ListaDoctorado.setLayoutManager(GridLayoutManager(this, 1))
        val query: Query = mFirestore.collection("$Estados/$idUni/Doctorado")

        val firestoreRecyclerOptions = FirestoreRecyclerOptions.Builder<Carreras>()
            .setQuery(query, Carreras().javaClass)
            .build()

        AdapterDoc = AdapterDoctUni(firestoreRecyclerOptions, this)
        ListaDoctorado.setAdapter(AdapterDoc)
    }

    override fun onStart() {
        super.onStart()
        Adapter.startListening()
        AdapterPos.startListening()
        AdapterMaes.startListening()
        AdapterDoc.startListening()
    }

    companion object {
        var tCarreras: String? = null
        var estad: String? = null
        var ItemCarrer: String? = null
    }
}