package com.softjk.unishare;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.softjk.unishare.MenuDrawer.MenuPrincipal;

public class RecuperarPass extends AppCompatActivity {

    EditText Correo;
    Button Recuperar;
    ProgressDialog progress;
    FirebaseAuth auth;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recupera_pass);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Correo = findViewById(R.id.txtCorreoRecupera);
        Recuperar = findViewById(R.id.btnRecuperar);
        auth = FirebaseAuth.getInstance();
        progress = new ProgressDialog(this);

        Recuperar();


    }

    private void Recuperar() {
        Recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = Correo.getText().toString().trim();

                if (!correo.isEmpty()){
                    progress.setMessage("Espere un momento..");
                    progress.setCanceledOnTouchOutside(false);
                    progress.show();

                    getEnviarCorreo();
                }else {
                    Toast.makeText(RecuperarPass.this, "El correo no se pudo enviar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getEnviarCorreo() {
        auth.setLanguageCode("es");
        auth.sendPasswordResetEmail(correo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RecuperarPass.this, "Porfavor Revise su correo para Restaurar la Contraseña", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RecuperarPass.this, Login.class);
                    startActivity(i);
                    finish();
                }else {
                    Toast.makeText(RecuperarPass.this, "El correo no se pudo enviar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}