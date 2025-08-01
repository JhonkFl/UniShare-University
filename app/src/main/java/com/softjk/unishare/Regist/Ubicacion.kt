package com.softjk.unishare.Regist

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.softjk.unishare.ImgUni
import com.softjk.unishare.Metodos.Permisos
import com.softjk.unishare.R

class Ubicacion : AppCompatActivity(), OnMapReadyCallback, OnMapClickListener {
    lateinit var Guardar: Button
    lateinit var mAuth: FirebaseAuth
    lateinit var CentrarUbicacion: ImageButton
    lateinit var Latitud: EditText
    lateinit var Longitud: EditText
    lateinit var mMap: GoogleMap
    lateinit var mfirestore: FirebaseFirestore
    lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_ubicacion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {
            v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Latitud = findViewById(R.id.txtLatitud)
        Longitud = findViewById(R.id.txtLongitud)
        Guardar = findViewById(R.id.btnUbicacion)
        CentrarUbicacion = findViewById(R.id.imgCentrarUbic)
        progressDialog = ProgressDialog(this)
        mfirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        val supportMapFragment =
            checkNotNull(supportFragmentManager.findFragmentById(R.id.LinerMapaSelec) as SupportMapFragment?)
        supportMapFragment.getMapAsync(this as OnMapReadyCallback)

        val preferences = this.getSharedPreferences("id", MODE_PRIVATE)
        val idEsta = preferences.getString("Estado", "")!!
        val idUni = mAuth.currentUser?.uid
        // String idUni = "IA9iP31DWGTNIHbWLMWlNbFQ3J03";
        val idActualizar = intent.getStringExtra("Actualizar")
        println(idEsta + idUni)
        if (idActualizar != null) {
            if (idUni != null) {
                ObtenerDatos(idEsta, idUni)
            }else{
                Toast.makeText(this, "Error al Obtener id", Toast.LENGTH_SHORT)
            }
        }

        Permisos.getLocalizacion(this@Ubicacion)


        Guardar.setOnClickListener(View.OnClickListener {
            val Lat = Latitud.text.toString().trim()
            val Lon = Longitud.text.toString().trim()
            if (Lat.isEmpty() && Lon.isEmpty()) {
                Toast.makeText(applicationContext, "Seleccione su Ubicación", Toast.LENGTH_SHORT).show()
            } else {
                if (idUni != null) {
                    GuardarDatos(Lat, Lon, idEsta, idUni)
                }else{
                    Toast.makeText(this,"Error al obtener el Id", Toast.LENGTH_SHORT)
                }
            }
        })

        CentrarUbicacion.setOnClickListener(View.OnClickListener { UbicacionPrecisa() })
    }

    private fun GuardarDatos(Lat: String, Lon: String, idEstado: String, idUni: String) {
        progressDialog.setMessage("Guardando Ubicación...")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val map: MutableMap<String, Any> = HashMap()
        map["Latitud"] = Lat
        map["Longitud"] = Lon
        mfirestore.collection(idEstado).document(idUni).update(map).addOnSuccessListener(
            OnSuccessListener<Void?> {
                progressDialog.dismiss()
                val msg = "Ubicación Guardada"
                toastCorrecto(msg)
                val intent = Intent(this@Ubicacion, ImgUni::class.java)
                intent.putExtra("Dato", "Uni")
                startActivity(intent)
                finish()
            }).addOnFailureListener {
            progressDialog.dismiss()
            val msg = "Error al Guardar los Datos"
            toastIncorrecto(msg)
        }
    }


    private fun ObtenerDatos(idEstado: String, idUni: String) {
        mfirestore.collection(idEstado).document(idUni).get().addOnSuccessListener(
            OnSuccessListener { documentSnapshot ->
                val Latit = documentSnapshot.getString("Latitud")
                val Longt = documentSnapshot.getString("Longitud")
                val ABc = documentSnapshot.getString("ABC")

                val Lat = Latit!!.toDouble()
                val Lon = Longt!!.toDouble()
                val supportMapFragment = checkNotNull(
                    supportFragmentManager.findFragmentById(R.id.LinerMapaSelec) as SupportMapFragment?)
                supportMapFragment.getMapAsync { googleMap ->
                    mMap = googleMap
                    val centroDelMapa = LatLng(Lat, Lon)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(centroDelMapa, 14f)  // posición y zoom definidos
                    mMap.addMarker(MarkerOptions().position(centroDelMapa).title(ABc))
                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(cameraUpdate));
                    mMap.moveCamera(cameraUpdate)
                    mMap.setOnMapClickListener { latLng ->
                        Latitud.setText("" + latLng.latitude)
                        Longitud.setText("" + latLng.longitude)
                        mMap.clear()
                        val mexico = LatLng(
                            latLng.latitude,
                            latLng.longitude
                        )
                        mMap.addMarker(MarkerOptions().position(mexico).title(""))
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico))
                    }
                }
            }).addOnFailureListener {
            Toast.makeText(this@Ubicacion, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val idActualizar = intent.getStringExtra("Actualizar")
        if (idActualizar == null) {
            mMap = googleMap
            mMap.setOnMapClickListener(this)
            val mexico = LatLng(22.25843382768379, -101.81628865562087)
            mMap.addMarker(MarkerOptions().position(mexico).title("Mi Universidad"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico))
        }
    }

    override fun onMapClick(latLng: LatLng) {
        Latitud.setText("" + latLng.latitude)
        Longitud.setText("" + latLng.longitude)
        mMap.clear()
        val mexico = LatLng(latLng.latitude, latLng.longitude)
        mMap.addMarker(MarkerOptions().position(mexico).title(""))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico))
    }


    fun UbicacionPrecisa() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mMap!!.isMyLocationEnabled = true
        mMap!!.uiSettings.isMyLocationButtonEnabled = false

        val locationManager =
            getSystemService(LOCATION_SERVICE) as LocationManager
        val locationListener = LocationListener { location ->
            val miUbicacion =
                LatLng(location.latitude, location.longitude)
            mMap!!.addMarker(MarkerOptions().position(miUbicacion).title("Mi Ubicación"))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion))
            val cameraPosition = CameraPosition.Builder()
                .target(miUbicacion)
                .zoom(14f)
                .bearing(90f)
                .tilt(45f)
                .build()
            mMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

            Latitud!!.setText("" + location.latitude)
            Longitud!!.setText("" + location.longitude)
        }
        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            0,
            0f,
            locationListener
        )
    }

    fun toastCorrecto(msg: String?) {
        val view = layoutInflater.inflate(R.layout.custom_toast_ok, null)
        val txtMensaje = view.findViewById<TextView>(R.id.txtMensajeToast1)
        txtMensaje.text = msg

        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 200)
        toast.duration = Toast.LENGTH_LONG
        toast.view = view
        toast.show()
    }

    fun toastIncorrecto(msg: String?) {
        val view = layoutInflater.inflate(R.layout.custom_toast_error, null)
        val txtMensaje = view.findViewById<TextView>(R.id.txtMensajeToast2)
        txtMensaje.text = msg

        val toast = Toast(applicationContext)
        toast.setGravity(Gravity.CENTER_VERTICAL or Gravity.BOTTOM, 0, 300)
        toast.duration = Toast.LENGTH_LONG
        toast.view = view
        toast.show()
    }
}