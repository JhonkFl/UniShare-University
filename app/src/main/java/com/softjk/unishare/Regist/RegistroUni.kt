package com.softjk.unishare.Regist;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.softjk.unishare.ImgUni;
import com.softjk.unishare.Login;
import com.softjk.unishare.MenuDrawer.MenuPrincipal;
import com.softjk.unishare.Metodos.SPMunicipios;
import com.softjk.unishare.R;
import com.softjk.unishare.fragment_maps;

import java.util.HashMap;
import java.util.Map;

public class RegistroUni extends AppCompatActivity {
    FirebaseFirestore mfirestore;
    ProgressDialog progressDialog;
    TextView Titulo;
    EditText ABC,Nombre, txtUbicacion,Localidad, Telefono, Correo, Facebook,Pagina,txtCodigo,Email,Pass;
    Spinner Tipo, Estado,Municipio;
    TextInputLayout lblMunicipio,lblABC;
    LinearLayoutCompat sesion,Politica,LinCodigo;
    RadioButton Acepto;

    ImageView IMGUbicac;
    Button Guardar, btnCodigo;
    FirebaseAuth mAuth;
    RecyclerView ListaCarr;
    Query query;
    static String Codigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_uni);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);
        ABC = findViewById(R.id.txtABC);
        Nombre = findViewById(R.id.txtNombre);
        Municipio = findViewById(R.id.SpMunicipio);
        txtUbicacion = findViewById(R.id.txtUbicacion);
        Localidad = findViewById(R.id.txtLocalidad);
        Telefono = findViewById(R.id.txtTelefono);
        Facebook = findViewById(R.id.txtFacebook);
        IMGUbicac = findViewById(R.id.EditarUbic);
        Pagina = findViewById(R.id.txtPagina);
        Guardar = findViewById(R.id.GuardarCarrSec);
        Tipo = findViewById(R.id.Tipo);
        Estado = findViewById(R.id.Estado);
        mfirestore = FirebaseFirestore.getInstance();

        Correo = findViewById(R.id.txtCorreo);
        Email = findViewById(R.id.EmailUser);
        Pass = findViewById(R.id.Password);
        lblMunicipio = findViewById(R.id.lblLocalidad);
        lblABC = findViewById(R.id.lblABC);
        Acepto = findViewById(R.id.btnAcepto);
        sesion = findViewById(R.id.linerSesion2);
       // lblPass = findViewById(R.id.lblPass);
        btnCodigo = findViewById(R.id.btnCodigo);
        Politica = findViewById(R.id.linerPolitica);
        Titulo = findViewById(R.id.lblTituloRe);
        txtCodigo = findViewById(R.id.Codigo);
        mAuth = FirebaseAuth.getInstance();
        String idActualizar = getIntent().getStringExtra("Actualizar");

        if (idActualizar != null){
            IMGUbicac.setVisibility(View.VISIBLE);
            sesion.setVisibility(View.GONE);
            Politica.setVisibility(View.GONE);
            Guardar.setEnabled(true);
            SharedPreferences preferences = this.getSharedPreferences("id", Context.MODE_PRIVATE);
            String idEsta = preferences.getString("Estado","");
            String idUni = mAuth.getCurrentUser().getUid();
            ObtenerDatos(idUni,idEsta);
            Guardar.setText("Actualizar");
            Titulo.setText("Ok vamos Actualizar los Datos de su Institución");

            Guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Nom = Nombre.getText().toString().trim();
                    String Ubi = txtUbicacion.getText().toString().trim();
                    String Loca = Localidad.getText().toString().trim();
                    String Tel = Telefono.getText().toString().trim();
                    String Corr = Correo.getText().toString();
                    String Face = Facebook.getText().toString();
                    String Pag = Pagina.getText().toString();

                    if (Nom.isEmpty() && Ubi.isEmpty() && Loca.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Ingrese los Datos", Toast.LENGTH_SHORT).show();
                    } else {
                        updateLocal(Nom, Ubi, Loca, Tel, Corr, Face, Pag, idEsta, idUni);
                    }
                }
            });

            IMGUbicac.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RegistroUni.this, Ubicacion.class);
                    intent.putExtra("Actualizar","Si");
                    startActivity(intent);
                    finish();
                }
            });
        }else {

            String[] OpcionesTipo = {"Pública", "Privada"};
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, R.layout.spinner_item_estilo, OpcionesTipo);
            Tipo.setAdapter(adapter1);

            String[] OpcionesEstado = {"Seleccione su Estado", "Aguascalientes", "Baja-California", "Baja-California-Sur", "Campeche", "CDMX", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Durango",
                    "EstadoMx", "Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Michoacan", "Morelos", "Nayarit", "Nuevo-Leon", "Oaxaca", "Puebla", "Queretaro", "Quintana-Roo", "San-Luis-Potosi", "Sinaloa", "Sonora",
                    "Tabasco", "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatan", "Zacatecas"};
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, R.layout.spinner_item_estilo, OpcionesEstado);
            Estado.setAdapter(adapter2);

            ObtenerMunicipio();
            ObtenrCodigo();

            Municipio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String Item = parent.getSelectedItem().toString();
                    if (Item.equals("Otro Municipio")) {
                        lblMunicipio.setHint("Escriba su Municipio");
                    } else {
                        lblMunicipio.setHint("Localidad o Colonia");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Guardar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String emailUser = Email.getText().toString().trim();
                    String passUser = Pass.getText().toString().trim();
                    String Abc = ABC.getText().toString().trim();
                    String Nom = Nombre.getText().toString().trim();
                    String ub = txtUbicacion.getText().toString().trim();
                    String CodigUser= txtCodigo.getText().toString().trim();

                    if (Abc.isEmpty() || Nom.isEmpty() || ub.isEmpty() ) {
                        Toast.makeText(RegistroUni.this, "Complete los datos", Toast.LENGTH_SHORT).show();
                    } else if (passUser.length() < 6) {
                        Pass.setError("Su contraseña debe tener minimo 6 digitos");
                        Pass.requestFocus();
                    } else if (TextUtils.isEmpty(emailUser)) {
                        Email.setError("Ingrese su correo electronico");
                        Email.requestFocus();
                    } else if (Estado.getSelectedItem().equals("Seleccione su Estado")) {
                        Toast.makeText(RegistroUni.this, "Seleccione su Estado!", Toast.LENGTH_SHORT).show();
                    } else if (Municipio.getSelectedItem().equals("Seleccione su Municipio")) {
                        Toast.makeText(RegistroUni.this, "Seleccione su Municipio!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (CodigUser.equals(Codigo)) {
                            GuardarDatos();
                        }else {
                            toastIncorrecto("Código Incoreccto");
                        }
                    }

                }
            });

            Acepto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Guardar.setEnabled(true);
                }
            });

            btnCodigo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String URL = "https://m.me/505661245972084";
                    Uri Link = Uri.parse(URL);
                    Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                    startActivity(intent);
                }
            });

        }
    }

    private void ObtenerMunicipio() {
        //String Estd = Estado.getSelectedItem().toString().trim();
        SPMunicipios.SelecciMuni(Estado,Municipio,RegistroUni.this);
    }


    private void GuardarDatos() {

        progressDialog.setMessage("Guardando Datos...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        Map<String, Object> map = new HashMap<>();
        String Abre = ABC.getText().toString().trim();
        String Nombr = Nombre.getText().toString().trim();
        String Ubicaci = txtUbicacion.getText().toString().trim();
        String Loca = Localidad.getText().toString().trim();
        String Telefo = Telefono.getText().toString().trim();
        String Coree = Correo.getText().toString().trim();
        String Gmail = Email.getText().toString().trim();
        String Face = Facebook.getText().toString().trim();
        String Pag = Pagina.getText().toString().trim();

        String Password = Pass.getText().toString().trim();

        String TipoUni = Tipo.getSelectedItem().toString().trim();
        String EstadoRegion = Estado.getSelectedItem().toString().trim();
        String Asentamie = Municipio.getSelectedItem().toString().trim();
        System.out.println("ver estado " + EstadoRegion);

        SharedPreferences preferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Estado", EstadoRegion);
        editor.commit();

        mAuth.createUserWithEmailAndPassword(Gmail, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                SharedPreferences preferences = getSharedPreferences("Token", Context.MODE_PRIVATE);
                String tok = preferences.getString("idToken","");
                String id = mAuth.getCurrentUser().getUid();
                map.put("IdUser",id);
                map.put("ABC", Abre);
                map.put("Nombre", Nombr);
                map.put("Tipo", TipoUni);
                map.put("Ubicacion", Ubicaci);
                map.put("Municipio", Asentamie);
                map.put("Localidad", Loca);
                map.put("Estado", EstadoRegion);
                map.put("Telefono", Telefo);
                map.put("Correo", Coree);
                map.put("Facebook", Face);
                map.put("Pagina", Pag);
                map.put("Token",tok);

                mfirestore.collection(EstadoRegion).document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Universidad Creado", Toast.LENGTH_SHORT).show();
                        ABC.setText("");
                        Nombre.setText("");
                        txtUbicacion.setText("");
                        Telefono.setText("");
                        Correo.setText("");
                        Facebook.setText("");
                        Localidad.setText("");
                        Pagina.setText("");
                        Pass.setText("");

                        Intent intent = new Intent(RegistroUni.this, Ubicacion.class);
                        intent.putExtra("Dato","Uni");
                        startActivity(intent);
                        finish();

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error al Crear La Universidad", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg="Error al registrar";
                toastIncorrecto(msg);
                progressDialog.dismiss();

            }
        });

    }


    private void updateLocal(String NomLocal, String Localiza, String Localidad, String Tele, String Corre, String Face,String Pag, String idEstado ,String idUni) {
        Map<String, Object> map = new HashMap<>();
        map.put("Nombre", NomLocal);
        map.put("Ubicacion", Localiza);
        map.put("Localidad", Localidad);
        map.put("Telefono", Tele);
        map.put("Correo", Corre);
        map.put("Facebook", Face);
        map.put("Pagina", Pag);

        mfirestore.collection(idEstado).document(idUni).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String msg="Actualización Exitosa";
                toastCorrecto(msg);
                startActivity(new Intent(RegistroUni.this, MenuPrincipal.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String msg="Error al Actualizar";
                toastIncorrecto(msg);
            }
        });
    }


    private void ObtenerDatos(String idUni,String idEstado) {
        mfirestore.collection(idEstado).document(idUni).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String NombreUni = documentSnapshot.getString("Nombre");
                String UbicacionUni = documentSnapshot.getString("Ubicacion");
                String Localid = documentSnapshot.getString("Localidad");
                String TelefonoUni = documentSnapshot.getString("Telefono");
                String CorreoUni = documentSnapshot.getString("Correo");
                String FaceboockUni= documentSnapshot.getString("Facebook");
                String PaginaUni = documentSnapshot.getString("Pagina");
                String Latit = documentSnapshot.getString("Latitud");
                String Longt = documentSnapshot.getString("Longitud");
                Double Lat = Double.valueOf(Latit);
                Double Lon = Double.valueOf(Longt);

                Nombre.setText(NombreUni);
               // ABC.setText(ABc);
                lblABC.setVisibility(View.GONE);
                Tipo.setVisibility(View.GONE);
                Estado.setVisibility(View.GONE);
                Municipio.setVisibility(View.GONE);
                Localidad.setText(Localid);
                txtUbicacion.setText(UbicacionUni);
                Telefono.setText(TelefonoUni);
                Correo.setText(CorreoUni);
                Facebook.setText(FaceboockUni);
                Pagina.setText(PaginaUni);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroUni.this, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ObtenrCodigo() {
        mfirestore.collection("Novedades").document("Universidades").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Codigo = documentSnapshot.getString("Codigo");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegistroUni.this, "Error al Obtener el Código", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void toastCorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_ok, (ViewGroup) findViewById(R.id.ll_custom_toast_ok));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast1);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 200);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    public void toastIncorrecto(String msg) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_toast_error, (ViewGroup) findViewById(R.id.ll_custom_toast_error));
        TextView txtMensaje = view.findViewById(R.id.txtMensajeToast2);
        txtMensaje.setText(msg);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 300);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }
}