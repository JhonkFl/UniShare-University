package com.softjk.unishare.MenuDrawer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.softjk.unishare.Bienvenida;
import com.softjk.unishare.Login;
import com.softjk.unishare.R;
import com.softjk.unishare.Regist.FragmentNewCarreras;
import com.softjk.unishare.Regist.RegistroUni;
import com.softjk.unishare.Turorial;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MenuPrincipal extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    int contador = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Toolbar toolbar = findViewById(R.id.TollbarAdmin);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.DrawerLayoutAdmin);
        NavigationView navigationView = findViewById(R.id.navigatioAdmin);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        if (intent.hasExtra("Msg")) {
            String mensaje = intent.getStringExtra("Msg");
           // Toast.makeText(this, "Final "+mensaje, Toast.LENGTH_SHORT).show();
            if (mensaje == null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentInicio()).commit();
                navigationView.setCheckedItem(R.id.Inicio);
            }else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentNovedades()).commit();
                navigationView.setCheckedItem(R.id.Novedades);
            }
        }else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentInicio()).commit();
                navigationView.setCheckedItem(R.id.Inicio);
        }

        String Dato = getIntent().getStringExtra("Dato");
        if (Dato != null){
            VentanaNoti();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int ItemId = item.getItemId();
        if (ItemId == R.id.Inicio ){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentInicio()).commit();
        } else if (ItemId == R.id.Novedades ) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentNovedades()).commit();
        } else if (ItemId == R.id.Politica) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentPolitica()).commit();
        } else if (ItemId == R.id.Acercade) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentAdmin, new FragmentAcercade()).commit();
        } else if (ItemId == R.id.Cerrar) {
            VentanaMsgDialog();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void VentanaMsgDialog() {
        new SweetAlertDialog(MenuPrincipal.this, SweetAlertDialog.NORMAL_TYPE).setTitleText("Cerrar Sesión")
                .setContentText("Quieres Cerrar Sesión?")
                .setCancelText("No").setConfirmText("Si")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();
                    System.out.println("sesion no cerrado");
                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    System.out.println("sesion cerrado");

                    finish();
                    startActivity(new Intent(MenuPrincipal.this, Login.class));
                    mAuth.signOut();
                }).show();
    }

    private void VentanaNoti() {
        new SweetAlertDialog(MenuPrincipal.this, SweetAlertDialog.NORMAL_TYPE).setTitleText("Aviso")
                .setContentText("La Ficha de Inscripción e Imágenes promocionales se Actualizan Cada vez que su institución abra convocatoria para las " +
                        "Inscripciones de nuevo ingreso. ¡Esa Información Estará disponible para los estudiantes de nivel Media Superior." +
                        " Para actualizar sus datos vea el Tutorial!")
                .setConfirmText("Tutorial")
                .setCancelText("OK")
                .showCancelButton(true).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                        startActivity(new Intent(MenuPrincipal.this, Turorial.class));
                }).show();
    }
    //pulsar 2 veces boton atras para salir de la aplicacion
    @Override
    public void onBackPressed() {

        if (contador == 0 ){
            Toast.makeText(this, "Presione 2 veces para Salir", Toast.LENGTH_SHORT).show();
            contador++;
        }else {
            Intent intent= new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            super.onBackPressed();
        }

        new CountDownTimer(300,1000){

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                contador = 0;
            }
        }.start();
    }

}