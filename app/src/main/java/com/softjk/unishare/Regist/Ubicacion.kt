package com.softjk.unishare.Regist;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.ImgUni;
import com.softjk.unishare.MenuDrawer.MenuPrincipal;
import com.softjk.unishare.Metodos.Permisos;
import com.softjk.unishare.R;

import java.util.HashMap;
import java.util.Map;

public class Ubicacion extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    Button Guardar;
    FirebaseAuth mAuth;
    ImageButton CentrarUbicacion;
    EditText Latitud,Longitud;
    GoogleMap mMap;
    FirebaseFirestore mfirestore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ubicacion);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Latitud = findViewById(R.id.txtLatitud);
        Longitud = findViewById(R.id.txtLongitud);
        Guardar = findViewById(R.id.btnUbicacion);
        CentrarUbicacion = findViewById(R.id.imgCentrarUbic);
        progressDialog = new ProgressDialog(this);
        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.LinerMapaSelec);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync((OnMapReadyCallback) this);

        SharedPreferences preferences = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        String idEsta = preferences.getString("Estado","");
        String idUni = mAuth.getCurrentUser().getUid();
       // String idUni = "IA9iP31DWGTNIHbWLMWlNbFQ3J03";
        String idActualizar = getIntent().getStringExtra("Actualizar");
        System.out.println(idEsta+idUni);
        if (idActualizar != null){
            ObtenerDatos(idEsta,idUni);
        }

        Permisos.getLocalizacion(Ubicacion.this);


        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Lat = Latitud.getText().toString().trim();
                String Lon = Longitud.getText().toString().trim();
                if (Lat.isEmpty() && Lon.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Seleccione su Ubicación", Toast.LENGTH_SHORT).show();
                }else {
                    GuardarDatos( Lat, Lon,idEsta,idUni);
                }
            }
        });

        CentrarUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UbicacionPrecisa();
            }
        });

    }

    private void GuardarDatos(String Lat, String Lon,String idEstado, String idUni) {
        progressDialog.setMessage("Guardando Ubicación...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        Map<String, Object> map = new HashMap<>();
        map.put("Latitud", Lat);
        map.put("Longitud", Lon);
        mfirestore.collection(idEstado).document(idUni).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                String msg="Ubicación Guardada";
                toastCorrecto(msg);
                Intent intent = new Intent(Ubicacion.this, ImgUni.class);
                intent.putExtra("Dato","Uni");
                startActivity(intent);
                finish();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                String msg="Error al Guardar los Datos";
                toastIncorrecto(msg);
            }
        });
    }

    private void toastIncorrecto(String msg) {
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

    private void toastCorrecto(String msg) {
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

    private void ObtenerDatos(String idEstado, String idUni){
        mfirestore.collection(idEstado).document(idUni).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String Latit = documentSnapshot.getString("Latitud");
                String Longt = documentSnapshot.getString("Longitud");
                String ABc = documentSnapshot.getString("ABC");

                Double Lat = Double.valueOf(Latit);
                Double Lon = Double.valueOf(Longt);
                SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.LinerMapaSelec);
                assert supportMapFragment != null;
                supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        mMap = googleMap;
                        LatLng centroDelMapa = new LatLng(Lat, Lon);
                        // posición y zoom definidos
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(centroDelMapa, 14);
                        mMap.addMarker(new MarkerOptions().position(centroDelMapa).title(ABc));
                        //mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraUpdate));
                        mMap.moveCamera(cameraUpdate);

                        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                            @Override
                            public void onMapClick(@NonNull LatLng latLng) {
                                Latitud.setText(""+latLng.latitude);
                                Longitud.setText(""+latLng.longitude);
                                mMap.clear();
                                LatLng mexico = new LatLng(latLng.latitude, latLng.longitude);
                                mMap.addMarker(new MarkerOptions().position(mexico).title(""));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
                            }
                        });
                    }
                });
                }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Ubicacion.this, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        String idActualizar = getIntent().getStringExtra("Actualizar");
        if (idActualizar == null) {
            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            LatLng mexico = new LatLng(22.25843382768379, -101.81628865562087);
            mMap.addMarker(new MarkerOptions().position(mexico).title("Mi Universidad"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
        }
    }

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        Latitud.setText(""+latLng.latitude);
        Longitud.setText(""+latLng.longitude);
        mMap.clear();
        LatLng mexico = new LatLng(latLng.latitude, latLng.longitude);
        mMap.addMarker(new MarkerOptions().position(mexico).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
    }


    public void UbicacionPrecisa() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi Ubicación"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(miUbicacion)
                        .zoom(14)
                        .bearing(90)
                        .tilt(45)
                        .build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                Latitud.setText(""+location.getLatitude());
                Longitud.setText(""+location.getLongitude());
            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locationListener);
    }
}