package com.softjk.unishare.Adapter

import android.app.Activity
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.softjk.unishare.CarrerasUni
import com.softjk.unishare.Modelo.Carreras
import com.softjk.unishare.R

class AdapterMaestriaUni(options: FirestoreRecyclerOptions<Carreras?>, var activity: Activity) :
    FirestoreRecyclerAdapter<Carreras, AdapterMaestriaUni.ViewHolder>(options) {
    private val mAuth = FirebaseAuth.getInstance()
    private val mFirestore = FirebaseFirestore.getInstance()
    var progressDialog: ProgressDialog = ProgressDialog(activity)

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int, carreras: Carreras) {
        viewHolder.Nombre.text = carreras.Nombre

        viewHolder.LinerCarrera.setOnLongClickListener(OnLongClickListener {
            val documentSnapshot = snapshots.getSnapshot(viewHolder.adapterPosition)
            val id = documentSnapshot.id
            val Nomb = carreras.Nombre
            VentanaMsgDialog(Nomb, id)
            true
        })
    }

    private fun VentanaMsgDialog(Nombre: String, id: String) {
        SweetAlertDialog(activity, SweetAlertDialog.NORMAL_TYPE).setTitleText("Aviso")
            .setContentText("Eliminar Carrera: $Nombre")
            .setCancelText("No").setConfirmText("Si")
            .showCancelButton(true).setCancelClickListener { sDialog: SweetAlertDialog ->
                sDialog.dismissWithAnimation()
            }.setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                println("Ver id: $id")
                EliminarCarr(id)
            }.show()
    }

    private fun EliminarCarr(id: String) {
        val idUser = mAuth.currentUser!!.uid
        val Estado = CarrerasUni.estad

        progressDialog.setMessage("Eliminando Carrera...")
        progressDialog.show()
        progressDialog.setCancelable(false)

        println("ver Adapter daatos recibidos Ruta= $Estado/$idUser/Maestrias + Documento= $id")
        mFirestore.collection("$Estado/$idUser/Maestrias").document(id).delete()
            .addOnSuccessListener(
                OnSuccessListener {
                    Toast.makeText(activity, "Eliminado", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }).addOnFailureListener {
                Toast.makeText(activity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_carreras, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var Nombre: TextView = itemView.findViewById(R.id.NombreCarreraItem)
        var LinerCarrera: LinearLayoutCompat =
            itemView.findViewById(R.id.LinerCarreras)
    }
}
