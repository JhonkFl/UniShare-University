package com.softjk.unishare.MenuDrawer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.codesgood.views.JustifiedTextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.softjk.unishare.R

class FragmentNovedades : Fragment() {
    lateinit var Titulo: TextView
    lateinit var Punto1: TextView
    lateinit var Punto2: TextView
    lateinit var Punto3: TextView
    lateinit var Punto4: TextView
    lateinit var Punto5: TextView
    lateinit var Info: JustifiedTextView
    lateinit var Imagen: ImageView
    lateinit var BtnNovedades: Button

    val mFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_novedades, container, false)
        requireActivity().title = "Novedades"

        Titulo = view.findViewById(R.id.TituloNovedades)
        Info = view.findViewById(R.id.InfoNovedades)
        Imagen = view.findViewById(R.id.ImagenNovedades)
        Punto1 = view.findViewById(R.id.InfoPunto1)
        Punto2 = view.findViewById(R.id.InfoPunto2)
        Punto3 = view.findViewById(R.id.InfoPunto3)
        Punto4 = view.findViewById(R.id.InfoPunto4)
        Punto5 = view.findViewById(R.id.InfoPunto5)
        BtnNovedades = view.findViewById(R.id.btnNovedades)

        ObtenerDatos()
        return view
    }

    private fun ObtenerDatos() {
        mFirestore.collection("Novedades").document("Universidades").get().addOnSuccessListener(
            OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                val TituloN = documentSnapshot.getString("Titulo")
                val InfoN = documentSnapshot.getString("Informacion")
                val Info1 = documentSnapshot.getString("Punto1")
                val Info2 = documentSnapshot.getString("Punto2")
                val Info3 = documentSnapshot.getString("Punto3")
                val Info4 = documentSnapshot.getString("Punto4")
                val Info5 = documentSnapshot.getString("Punto5")
                val ImgN = documentSnapshot.getString("IMG")
                val Actualizar = documentSnapshot.getString("Actualizar")

                Titulo.text = TituloN
                Info.text = InfoN

                // Agregar Datos con for a todos los que tienen datos y a los que no Ocultarlos
                val lblPunto = listOf(Punto1, Punto2, Punto3, Punto4, Punto5 )
                val Info = listOf(Info1,Info2,Info3,Info4,Info5)

                for (i in Info.indices){
                    val dato = Info[i]
                    if (dato.isNullOrBlank()){
                        lblPunto[i].visibility = View.GONE
                    }else{
                        lblPunto[i].visibility = View.VISIBLE
                        lblPunto[i].text = dato
                    }
                }   //   ******** fin -- una solocion en vez de usar if


                try { //Cargar Imagen
                    if (ImgN != "") {
                        Glide.with(this@FragmentNovedades)
                            .load(ImgN)
                            .into(Imagen)
                    }
                } catch (e: Exception) {
                    Log.v("Error", "e$e")
                }
                if (Actualizar == "Si") {  //Mostrar Boton y dar clic
                    BtnNovedades.visibility = View.VISIBLE

                    BtnNovedades.setOnClickListener(View.OnClickListener {
                        val URL = "https://play.google.com/store/apps/details?id=com.softjk.uni"
                        val Link = Uri.parse(URL)
                        val intent = Intent(Intent.ACTION_VIEW, Link)
                        startActivity(intent)
                    })
                }
            }).addOnFailureListener {
            Toast.makeText(activity, "Error al Cargar los Datos", Toast.LENGTH_SHORT).show()
        }
    }
}