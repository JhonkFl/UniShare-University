package com.softjk.unishare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codesgood.views.JustifiedTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softjk.unishare.MenuDrawer.MenuPrincipal;
import com.softjk.unishare.Metodos.Permisos;
import com.softjk.unishare.Regist.RegistroUni;
import com.softjk.unishare.Regist.Ubicacion;

public class Bienvenida extends AppCompatActivity {

    Button Si, No;
    TextView Pregunta;
    JustifiedTextView lblBienvenida;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // SplashScreen splashScreen = SplashScreen

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bienvenida);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Si = findViewById(R.id.btnSi);
        No = findViewById(R.id.btnNo);
        Pregunta = findViewById(R.id.lblPregunta);
        lblBienvenida = findViewById(R.id.Bienvenida);
        mAuth = FirebaseAuth.getInstance();

        final int DURACION1 = 3200;
        final int DURACION2 = 6200;

        //Animacio
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_izquierda);
        Animation animation3 = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_derecha);
        lblBienvenida.setAnimation(animation1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Pregunta.setVisibility(View.VISIBLE);
                Pregunta.setAnimation(animation1);
                Si.setVisibility(View.VISIBLE);
                Si.setAnimation(animation2);
                No.setVisibility(View.VISIBLE);
                No.setAnimation(animation3);
            }
        }, DURACION1);

        //----------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Permisos.getNotificaciones(Bienvenida.this);
            }
        }, DURACION2);

        //------------------------------------------

        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Bienvenida.this, Login.class));
            }
        });

        Si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 startActivity(new Intent(Bienvenida.this, RegistroUni.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); //No ModoOscuro por Defecto
        if (user != null) {
            Intent intent = new Intent(Bienvenida.this, MenuPrincipal.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
     final int REQUEST_NOTIFICATION_PERMISSION = 100;
        if (requestCode == REQUEST_NOTIFICATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes mostrar notificaciones
                Toast.makeText(this, "Permiso Concedido", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado, manejar esta situación
                Toast.makeText(this, "Permiso Denegado: No Tendrá Información de los Cambios de la App", Toast.LENGTH_SHORT).show();
            }
        }
    }


}