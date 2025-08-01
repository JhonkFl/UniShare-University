package com.softjk.unishare

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.softjk.unishare.MenuDrawer.MenuPrincipal

class Login : AppCompatActivity() {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val mfirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)) {
            v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val Iniciar: Button = findViewById(R.id.btnIniciar)
        val email: EditText = findViewById(R.id.txtCorreoLOg)
        val password: EditText = findViewById(R.id.txtPass)
        val Estado: Spinner = findViewById(R.id.EstadoLogin)
        val Recuperar: TextView = findViewById(R.id.lblRecuperarPass)

        val OpcionesEstado = arrayOf(
            "Seleccione Su Estado", "Aguascalientes", "Baja-California", "Baja-California-Sur", "Campeche",
            "CDMX", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Durango", "EstadoMx", "Guanajuato",
            "Guerrero", "Hidalgo", "Jalisco", "Michoacan", "Morelos", "Nayarit", "Nuevo-Leon",
            "Oaxaca", "Puebla", "Queretaro", "Quintana-Roo", "San-Luis-Potosi", "Sinaloa",
            "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatan", "Zacatecas"
        )
        val adapter2 = ArrayAdapter(this, R.layout.spinner_item_estilo, OpcionesEstado)
        Estado.setAdapter(adapter2)

        Iniciar.setOnClickListener(View.OnClickListener {
            val emailUser = email.text.toString().trim()
            val passUser = password.text.toString().trim()
            if (TextUtils.isEmpty(emailUser)) {
                email.setError("Ingrese su correo electronico")
                email.requestFocus()
            } else if (TextUtils.isEmpty(passUser)) {
                password.setError("Ingrese su contraseña")
                password.requestFocus()
            } else if (passUser.length < 6) {
                password.setError("Su contraseña es mayor de 5 digitos")
                password.requestFocus()
            } else if (Estado.getSelectedItem() == "Seleccione Su Estado") {
                val Msg = "Por favor seleccione su Estado"
                toastIncorrecto(Msg)
            } else {
                val Estd = Estado.getSelectedItem().toString().trim()
                val preferences = getSharedPreferences("id", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("Estado", Estd)
                editor.commit()
                loginUser(emailUser, passUser)
            }
        })

        Recuperar.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@Login,
                    RecuperarPass::class.java
                )
            )
        })
    }

    private fun loginUser(emailUser: String, passUser: String) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Iniciando Sesión")
        progressDialog.show()
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(this@Login, MenuPrincipal::class.java))
                finish()
            }
            progressDialog.dismiss()
        }.addOnFailureListener { e ->
            if (e.message == "The supplied auth credential is incorrect") {
                val msg = "Correo o Contraseña Incorrectos"
                toastIncorrecto(msg)
                progressDialog.dismiss()
            } else {
                val msg = "Error al iniciar sesión"
                toastIncorrecto(msg)
                progressDialog.dismiss()
                println(e)
            }
        }
    }

    fun toastIncorrecto(msg: String?) {
        val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(
            R.layout.custom_toast_error,
            findViewById<View>(R.id.ll_custom_toast_error) as ViewGroup
        )
        val txtMensaje = view.findViewById<TextView>(R.id.txtMensajeToast2)
        txtMensaje.text = msg ?: "Error desconocido"

        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 200)
        toast.duration = Toast.LENGTH_LONG
        toast.view = view
        toast.show()
    }
}