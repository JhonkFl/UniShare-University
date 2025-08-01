package com.softjk.unishare;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.MenuDrawer.MenuPrincipal;
import com.softjk.unishare.Regist.RegistroUni;

public class Login extends AppCompatActivity {
    Button Iniciar;
    EditText email, password;
    TextView Recuperar;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore mfirestore;
    Spinner Estado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Iniciar = findViewById(R.id.btnIniciar);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        email = findViewById(R.id.txtCorreoLOg);
        password = findViewById(R.id.txtPass);
        Estado = findViewById(R.id.EstadoLogin);
        Recuperar = findViewById(R.id.lblRecuperarPass);

        String [] OpcionesEstado = {"Seleccione Su Estado","Aguascalientes","Baja-California","Baja-California-Sur","Campeche","CDMX","Chiapas","Chihuahua","Coahuila","Colima","Durango",
                "EstadoMx","Guanajuato","Guerrero","Hidalgo","Jalisco","Michoacan","Morelos","Nayarit","Nuevo-Leon","Oaxaca","Puebla","Queretaro","Quintana-Roo","San-Luis-Potosi","Sinaloa","Sonora",
                "Tabasco","Tamaulipas","Tlaxcala","Veracruz","Yucatan","Zacatecas"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,R.layout.spinner_item_estilo,OpcionesEstado);
        Estado.setAdapter(adapter2);

        Iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser = email.getText().toString().trim();
                String passUser = password.getText().toString().trim();
                if (TextUtils.isEmpty(emailUser)) {
                    email.setError("Ingrese su correo electronico");
                    email.requestFocus();
                } else if (TextUtils.isEmpty(passUser)) {
                    password.setError("Ingrese su contraseña");
                    password.requestFocus();
                }
                else if (passUser.length()<6) {
                    password.setError("Su contraseña es mayor de 5 digitos");
                    password.requestFocus();
                } else if (Estado.getSelectedItem().equals("Seleccione Su Estado")) {
                    String Msg = "Por favor seleccione su Estado";
                    toastIncorrecto(Msg);
                } else {
                    String Estd = Estado.getSelectedItem().toString().trim();
                    SharedPreferences preferences = getSharedPreferences("id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Estado", Estd);
                    editor.commit();
                    loginUser(emailUser, passUser);
                }
            }
        });

        Recuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, RecuperarPass.class));
            }
        });

    }

    private void loginUser(String emailUser, String passUser) {
        progressDialog.setMessage("Iniciando Sesión");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(Login.this, MenuPrincipal.class));
                    finish();
                    progressDialog.dismiss();
                }
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e.equals("The supplied auth credential is incorrect")){
                    String msg = "Correo o Contraseña Incorrectos";
                    toastIncorrecto(msg);
                    progressDialog.dismiss();
                }else {
                String msg = "Error al iniciar sesión";
                toastIncorrecto(msg);
                progressDialog.dismiss();
                System.out.println(e);}
            }
        });
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

}