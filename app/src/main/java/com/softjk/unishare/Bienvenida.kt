package com.softjk.unishare

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codesgood.views.JustifiedTextView
import com.google.firebase.auth.FirebaseAuth
import com.softjk.unishare.MenuDrawer.MenuPrincipal
import com.softjk.unishare.Metodos.Permisos
import com.softjk.unishare.Regist.RegistroUni
import com.softjk.unishare.Regist.Ubicacion

class Bienvenida : AppCompatActivity() {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val Si: Button = findViewById(R.id.btnSi)
        val No: Button = findViewById(R.id.btnNo)
        val Pregunta: TextView = findViewById(R.id.lblPregunta)
        val lblBienvenida: TextView = findViewById(R.id.Bienvenida)

        val DURACION1 = 3200
        val DURACION2 = 6200

        //Animacion
        val animation1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha)
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_izquierda)
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha)
        lblBienvenida.setAnimation(animation1)

        Handler().postDelayed({
            Pregunta.setVisibility(View.VISIBLE)
            Pregunta.setAnimation(animation1)
            Si.setVisibility(View.VISIBLE)
            Si.setAnimation(animation2)
            No.setVisibility(View.VISIBLE)
            No.setAnimation(animation3)
        }, DURACION1.toLong())

        //----------------------------------
        Handler().postDelayed({ Permisos.getNotificaciones(this@Bienvenida) }, DURACION2.toLong())

        //------------------------------------------
        No.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@Bienvenida,
                    Login::class.java
                )
            )
        })

        Si.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this@Bienvenida,
                    RegistroUni::class.java
                   // Ubicacion::class.java
                )
            )
        })
    }

    override fun onStart() {
        super.onStart()
        val user = mAuth.currentUser
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO) //No ModoOscuro por Defecto
        if (user != null) {
            val intent = Intent(this@Bienvenida, MenuPrincipal::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val REQUEST_NOTIFICATION_PERMISSION = 100
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes mostrar notificaciones
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show()
            } else {
                // Permiso denegado, manejar esta situación
                Toast.makeText(
                    this,
                    "Permiso Denegado: No Tendrá Información de los Cambios de la App",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}