package com.softjk.unishare.Adapter

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.softjk.unishare.CarrerasUni
import com.softjk.unishare.Modelo.Carreras
import com.softjk.unishare.R

class AdapterCarreras(options: FirestoreRecyclerOptions<Carreras?>,private var activity: Activity) :
    FirestoreRecyclerAdapter<Carreras, AdapterCarreras.ViewHolder>(options) {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val progressDialog: ProgressDialog = ProgressDialog(activity)
    private val mfirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    val preferences: SharedPreferences =
        activity.getSharedPreferences("id", Context.MODE_PRIVATE)

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int, carreras: Carreras) {
        viewHolder.Nombre.text = carreras.Nombre
        viewHolder.Nombre.visibility = View.VISIBLE

        val Categoria = carreras.Categoria
        val Carrera = carreras.Nombre
        val ABC = carreras.ABC

        val idUni = mAuth.currentUser?.uid
        val Estado = CarrerasUni.estad
        val TCarrera = CarrerasUni.tCarreras
        println("ver idUni: $idUni y estado: $Estado")

        viewHolder.BtnAdd.apply {
            visibility = View.VISIBLE
            setOnClickListener{
                progressDialog.apply {
                    setMessage("Guardando Datos ...")
                    setCancelable(false)
                    show()
                }
                if(Estado != null && idUni != null && ABC != null){
                    when(TCarrera){
                        "Carreras" -> AddTC(Estado, idUni, TCarrera, Carrera ,ABC, Categoria)
                        "Especializaciones", "Maestrias", "Doctorado" ->
                            ActualizarTC(Estado, idUni, TCarrera, Carrera, ABC, Categoria)
                    }
                }
            }
        }
    }

    private fun ActualizarTC(Estado: String, idUni: String, TCarrera: String, Nombre: String?, ABC: String, Categori: String) {
        progressDialog.setMessage("Preparando...")
        progressDialog.show()
        val map: MutableMap<String, Any> = HashMap()
        map[TCarrera] = "Si"

        mfirestore.collection(Estado).document(idUni).update(map).addOnSuccessListener(
            OnSuccessListener {
                Toast.makeText(activity.applicationContext, "Guardando", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
                AddTC(Estado, idUni, TCarrera, Nombre, ABC, Categori)
            }).addOnFailureListener {
            Toast.makeText(activity.applicationContext, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun AddTC(Estado: String, idUni: String, TCarrera: String, Nombre: String?, ABC: String, Cate: String) {
        val map: MutableMap<String, Any?> = HashMap()
        map["Nombre"] = Nombre
        map["Categoria"] = Cate

        mfirestore.collection("$Estado").document("$idUni").collection("$TCarrera").document(ABC).set(map)
            .addOnSuccessListener(
                OnSuccessListener {
                    Toast.makeText(activity.applicationContext, "$TCarrera Agregado", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                    actualizarMapCarr(ABC,Cate,Estado,idUni);
                    if (TCarrera == "Carreras") {
                        AgregarUniCollec(Estado, idUni, ABC)
                    }
                }).addOnFailureListener {
                Toast.makeText(activity.applicationContext, "Error al Agregar La Carrera", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(activity.applicationContext, "Error al Agregar La Carrera", Toast.LENGTH_SHORT).show()
            }
    }

    private fun AgregarUniCollec(estado: String, idUni: String, idCarrera: String) {
        val NombreUni = preferences.getString("Nombre", "")!!
        val LogoUni = preferences.getString("Logo", "")!!
        val map: MutableMap<String, Any> = HashMap()
        map["Nombre"] = NombreUni
        map["Logo"] = LogoUni
        map["Estado"] = estado

        mfirestore.collection("Carreras/$idCarrera/Uni").document(idUni).set(map)
            .addOnSuccessListener(
                OnSuccessListener {
                    Toast.makeText(activity, "Agregado...", Toast.LENGTH_SHORT).show()
                }).addOnFailureListener { }
    }

    private fun actualizarMapCarr(ABC: String, Categoria: String, Estado: String, idUni: String) {
        val updateData: MutableMap<String, Any> = HashMap()
        updateData[FieldPath.of("Carreras", ABC).toString()] =
            FieldValue.arrayUnion(Categoria)

        mfirestore.collection(Estado).document(idUni).update(updateData).addOnSuccessListener(
            OnSuccessListener {
                Toast.makeText(
                    activity.applicationContext, "$Categoria Agregado", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }).addOnFailureListener {
            Toast.makeText(activity.applicationContext, "Error al Agregar La Carrera", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carreras, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Nombre: TextView = itemView.findViewById(R.id.NombreCarreraItem)
        var Logo: ImageView? = null
        var Carrera: LinearLayoutCompat = itemView.findViewById(R.id.LinerCarreras)
        var BtnAdd: Button = itemView.findViewById(R.id.addCarrera)
    }
}
