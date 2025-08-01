package com.softjk.unishare.MenuDrawer

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.softjk.unishare.Login
import com.softjk.unishare.R
import com.softjk.unishare.Tutorial

class MenuPrincipal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var drawerLayout: DrawerLayout
    var contador: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_principal)
        val toolbar = findViewById<Toolbar>(R.id.TollbarAdmin)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.DrawerLayoutAdmin)
        val navigationView = findViewById<NavigationView>(R.id.navigatioAdmin)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val intent = intent
        val mensaje = intent.getStringExtra("Msg")
        if (mensaje == null) {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentInicio()).commit()
            navigationView.setCheckedItem(R.id.Inicio)
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentNovedades()).commit()
            navigationView.setCheckedItem(R.id.Novedades)
        }

        val Dato = getIntent().getStringExtra("Dato")
        if (Dato != null) {
            VentanaNoti()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val ItemId = item.itemId
        when (item.itemId) {
            R.id.Inicio -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentInicio()).commit()
            R.id.Novedades -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentNovedades()).commit()
            R.id.Politica -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentPolitica()).commit()
            R.id.Acercade -> supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentAcercade()).commit()
            R.id.Cerrar -> VentanaMsgDialog()
        }

        /* if (ItemId == R.id.Inicio) {
             supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentInicio()).commit()
         } else if (ItemId == R.id.Novedades) {
             supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentNovedades()).commit()
         } else if (ItemId == R.id.Politica) {
             supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentPolitica()).commit()
         } else if (ItemId == R.id.Acercade) {
             supportFragmentManager.beginTransaction().replace(R.id.fragmentAdmin, FragmentAcercade()).commit()
         } else if (ItemId == R.id.Cerrar) {
             VentanaMsgDialog()
         } */

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun VentanaMsgDialog() {
        SweetAlertDialog(this@MenuPrincipal, SweetAlertDialog.NORMAL_TYPE)
            .setTitleText("Cerrar Sesión")
            .setContentText("Quieres Cerrar Sesión?")
            .setCancelText("No").setConfirmText("Si")
            .showCancelButton(true).setCancelClickListener { sDialog: SweetAlertDialog ->
                sDialog.dismissWithAnimation()
                println("sesion no cerrado")
            }.setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                println("sesion cerrado")

                finish()
                startActivity(Intent(this@MenuPrincipal, Login::class.java))
                mAuth.signOut()
            }.show()
    }

    private fun VentanaNoti() {
        SweetAlertDialog(this@MenuPrincipal, SweetAlertDialog.NORMAL_TYPE).setTitleText("Aviso")
            .setContentText(
                "La Ficha de Inscripción e Imágenes promocionales se Actualizan Cada vez que su institución abra convocatoria para las " +
                        "Inscripciones de nuevo ingreso. ¡Esa Información Estará disponible para los estudiantes de nivel Media Superior." +
                        " Para actualizar sus datos vea el Tutorial!"
            )
            .setConfirmText("Tutorial")
            .setCancelText("OK")
            .showCancelButton(true).setConfirmClickListener { sweetAlertDialog: SweetAlertDialog ->
                sweetAlertDialog.dismissWithAnimation()
                startActivity(Intent(this@MenuPrincipal, Tutorial::class.java))
            }.show()
    }

    //pulsar 2 veces boton atras para salir de la aplicacion
    override fun onBackPressed() {
        if (contador == 0) {
            Toast.makeText(this, "Presione 2 veces para Salir", Toast.LENGTH_SHORT).show()
            contador++
        } else {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            super.onBackPressed()
        }

        object : CountDownTimer(2000, 500) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                contador = 0
            }
        }.start()
    }
}