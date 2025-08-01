package com.softjk.unishare.Regist

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.softjk.unishare.MenuDrawer.MenuPrincipal
import com.softjk.unishare.Metodos.SPMunicipios
import com.softjk.unishare.R

class RegistroUni : AppCompatActivity() {
    val mfirestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var progressDialog: ProgressDialog
    lateinit var Titulo: TextView
    lateinit var ABC: EditText
    lateinit var Nombre: EditText
    lateinit var txtUbicacion: EditText
    lateinit var Localidad: EditText
    lateinit var Telefono: EditText
    lateinit var Correo: EditText
    lateinit var Facebook: EditText
    lateinit var Pagina: EditText
    lateinit var txtCodigo: EditText
    lateinit var Email: EditText
    lateinit var Pass: EditText
    lateinit var Tipo: Spinner
    lateinit var Estado: Spinner
    lateinit var Municipio: Spinner
    lateinit var lblMunicipio: TextInputLayout
    lateinit var lblABC: TextInputLayout
    lateinit var sesion: LinearLayoutCompat
    lateinit var Politica: LinearLayoutCompat
    lateinit var Acepto: RadioButton
    lateinit var IMGUbicac: ImageView
    lateinit var Guardar: Button
    lateinit var btnCodigo: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_registro_uni)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) {
            v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        progressDialog = ProgressDialog(this)
        ABC = findViewById(R.id.txtABC)
        Nombre = findViewById(R.id.txtNombre)
        Municipio = findViewById(R.id.SpMunicipio)
        txtUbicacion = findViewById(R.id.txtUbicacion)
        Localidad = findViewById(R.id.txtLocalidad)
        Telefono = findViewById(R.id.txtTelefono)
        Facebook = findViewById(R.id.txtFacebook)
        IMGUbicac = findViewById(R.id.EditarUbic)
        Pagina = findViewById(R.id.txtPagina)
        Guardar = findViewById(R.id.GuardarCarrSec)
        Tipo = findViewById(R.id.Tipo)
        Estado = findViewById(R.id.Estado)

        Correo = findViewById(R.id.txtCorreo)
        Email = findViewById(R.id.EmailUser)
        Pass = findViewById(R.id.Password)
        lblMunicipio = findViewById(R.id.lblLocalidad)
        lblABC = findViewById(R.id.lblABC)
        Acepto = findViewById(R.id.btnAcepto)
        sesion = findViewById(R.id.linerSesion2)

        btnCodigo = findViewById(R.id.btnCodigo)
        Politica = findViewById(R.id.linerPolitica)
        Titulo = findViewById(R.id.lblTituloRe)
        txtCodigo = findViewById(R.id.Codigo)

        val idActualizar = intent.getStringExtra("Actualizar")

        if (idActualizar != null) {
            IMGUbicac.setVisibility(View.VISIBLE)
            sesion.setVisibility(View.GONE)
            Politica.setVisibility(View.GONE)
            Guardar.setEnabled(true)
            val preferences = this.getSharedPreferences("id", MODE_PRIVATE)
            val idEsta = preferences.getString("Estado", "")!!
            val idUni = mAuth.currentUser?.uid

            if (idUni != null) {
                ObtenerDatos(idUni, idEsta)
            } else {
                Toast.makeText(applicationContext, "Error al Obtener lo Datos", Toast.LENGTH_SHORT).show()
            }

            Guardar.setText("Actualizar")
            Titulo.setText("Ok vamos Actualizar los Datos de su Institución")

            Guardar.setOnClickListener(View.OnClickListener {
                val Nom = Nombre.text.toString().trim()
                val Ubi = txtUbicacion.text.toString().trim()
                val Loca = Localidad.text.toString().trim()
                val Tel = Telefono.text.toString().trim()
                val Corr = Correo.text.toString().trim()
                val Face = Facebook.text.toString().trim()
                val Pag = Pagina.text.toString().trim()

                if (Nom.isEmpty() || Ubi.isEmpty() || Loca.isEmpty()) {
                    Toast.makeText(applicationContext, "Ingrese los Datos", Toast.LENGTH_SHORT).show()
                } else {
                    if (idUni != null) {
                        updateLocal(Nom, Ubi, Loca, Tel, Corr, Face, Pag, idEsta, idUni)
                    }else{
                        Toast.makeText(applicationContext, "Error al Obtener idUser", Toast.LENGTH_SHORT).show()
                    }
                }
            })

            IMGUbicac.setOnClickListener(View.OnClickListener {
                val intent = Intent(this@RegistroUni, Ubicacion::class.java)
                intent.putExtra("Actualizar", "Si")
                startActivity(intent)
                finish()
            })
        } else {
            val OpcionesTipo = arrayOf("Pública", "Privada")
            val adapter1 = ArrayAdapter(this, R.layout.spinner_item_estilo, OpcionesTipo)
            Tipo.setAdapter(adapter1)

            val OpcionesEstado = arrayOf("Seleccione su Estado", "Aguascalientes", "Baja-California", "Baja-California-Sur",
                "Campeche", "CDMX", "Chiapas", "Chihuahua", "Coahuila", "Colima", "Durango", "EstadoMx", "Guanajuato",
                "Guerrero", "Hidalgo", "Jalisco", "Michoacan", "Morelos", "Nayarit", "Nuevo-Leon", "Oaxaca",
                "Puebla", "Queretaro", "Quintana-Roo", "San-Luis-Potosi", "Sinaloa", "Sonora", "Tabasco",
                "Tamaulipas", "Tlaxcala", "Veracruz", "Yucatan", "Zacatecas"
            )
            val adapter2 = ArrayAdapter(this, R.layout.spinner_item_estilo, OpcionesEstado)
            Estado.setAdapter(adapter2)

            ObtenerMunicipio()
            ObtenrCodigo()

            Municipio.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    val Item = parent.selectedItem.toString()
                    if (Item == "Otro Municipio") {
                        lblMunicipio.setHint("Escriba su Municipio")
                    } else {
                        lblMunicipio.setHint("Localidad o Colonia")
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            })

            Guardar.setOnClickListener(View.OnClickListener {
                val emailUser = Email.text.toString().trim()
                val passUser = Pass.text.toString().trim()
                val Abc = ABC.text.toString().trim()
                val Nom = Nombre.text.toString().trim()
                val ub = txtUbicacion.text.toString().trim()
                val CodigUser = txtCodigo.text.toString().trim()

                if (Abc.isEmpty() || Nom.isEmpty() || ub.isEmpty()) {
                    Toast.makeText(this@RegistroUni, "Complete los datos", Toast.LENGTH_SHORT).show()

                } else if (passUser.length < 6) {
                    Pass.setError("Su contraseña debe tener minimo 6 digitos")
                    Pass.requestFocus()

                } else if (TextUtils.isEmpty(emailUser)) {
                    Email.setError("Ingrese su correo electronico")
                    Email.requestFocus()

                } else if (Estado.getSelectedItem() == "Seleccione su Estado") {
                    Toast.makeText(this@RegistroUni, "Seleccione su Estado!", Toast.LENGTH_SHORT).show()

                } else if (Municipio.getSelectedItem() == "Seleccione su Municipio") {
                    Toast.makeText(this@RegistroUni, "Seleccione su Municipio!", Toast.LENGTH_SHORT).show()
                } else {
                    if (CodigUser == Codigo) {
                        GuardarDatos()
                    } else {
                        toastIncorrecto("Código Incoreccto")
                    }
                }
            })

            Acepto.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                Guardar.setEnabled(
                    true
                )
            })

            btnCodigo.setOnClickListener(View.OnClickListener {
                val URL = "https://m.me/505661245972084"
                val Link = Uri.parse(URL)
                val intent = Intent(Intent.ACTION_VIEW, Link)
                startActivity(intent)
            })
        }
    }

    private fun ObtenerMunicipio() {
        //String Estd = Estado.getSelectedItem().toString().trim();
        SPMunicipios.SelecciMuni(Estado, Municipio, this@RegistroUni)
    }


    private fun GuardarDatos() {
        progressDialog.setMessage("Guardando Datos...")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val map: MutableMap<String, Any?> = HashMap()
        val Abre = ABC.text.toString().trim()
        val Nombr = Nombre.text.toString().trim()
        val Ubicaci = txtUbicacion.text.toString().trim()
        val Loca = Localidad.text.toString().trim()
        val Telefo = Telefono.text.toString().trim()
        val Coree = Correo.text.toString().trim()
        val Gmail = Email.text.toString().trim()
        val Face = Facebook.text.toString().trim()
        val Pag = Pagina.text.toString().trim()

        val Password = Pass.text.toString().trim()

        val TipoUni = Tipo.selectedItem.toString().trim()
        val EstadoRegion = Estado.selectedItem.toString().trim()
        val Asentamie = Municipio.selectedItem.toString().trim()
        println("ver estado $EstadoRegion")

        val preferences = getSharedPreferences("id", MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("Estado", EstadoRegion)
        editor.commit()

        mAuth.createUserWithEmailAndPassword(Gmail, Password).addOnSuccessListener(
            OnSuccessListener<AuthResult?> {
                val preferences = getSharedPreferences("Token", MODE_PRIVATE)
                val tok = preferences.getString("idToken", "")
                val id = mAuth.currentUser?.uid
                map["IdUser"] = id
                map["ABC"] = Abre
                map["Nombre"] = Nombr
                map["Tipo"] = TipoUni
                map["Ubicacion"] = Ubicaci
                map["Municipio"] = Asentamie
                map["Localidad"] = Loca
                map["Estado"] = EstadoRegion
                map["Telefono"] = Telefo
                map["Correo"] = Coree
                map["Facebook"] = Face
                map["Pagina"] = Pag
                map["Token"] = tok
                if (id != null) {
                    mfirestore.collection(EstadoRegion).document(id).set(map).addOnSuccessListener(
                        OnSuccessListener {
                            Toast.makeText(applicationContext, "Universidad Creado", Toast.LENGTH_SHORT).show()
                            ABC.setText("")
                            Nombre.setText("")
                            txtUbicacion.setText("")
                            Telefono.setText("")
                            Correo.setText("")
                            Facebook.setText("")
                            Localidad.setText("")
                            Pagina.setText("")
                            Pass.setText("")
                            progressDialog.dismiss()

                            val intent = Intent(this@RegistroUni, Ubicacion::class.java)
                            intent.putExtra("Dato", "Uni")
                            startActivity(intent)
                            finish()

                        }).addOnFailureListener {
                        Toast.makeText(applicationContext, "Error al Crear La Universidad", Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()
                    }
                }else{
                    Toast.makeText(applicationContext, "Error No se pudo Obtener el Id del User", Toast.LENGTH_SHORT).show()
                    progressDialog.dismiss()
                }
            }).addOnFailureListener {
            val msg = "Error al registrar"
            toastIncorrecto(msg)
            progressDialog.dismiss()
        }
    }


    private fun updateLocal(NomLocal: String, Localiza: String, Localidad: String, Tele: String, Corre: String,
        Face: String, Pag: String, idEstado: String, idUni: String) {

        val map: MutableMap<String, Any> = HashMap()
        map["Nombre"] = NomLocal
        map["Ubicacion"] = Localiza
        map["Localidad"] = Localidad
        map["Telefono"] = Tele
        map["Correo"] = Corre
        map["Facebook"] = Face
        map["Pagina"] = Pag

        mfirestore.collection(idEstado).document(idUni).update(map).addOnSuccessListener(
            OnSuccessListener {
                val msg = "Actualización Exitosa"
                toastCorrecto(msg)
                startActivity(Intent(this@RegistroUni, MenuPrincipal::class.java))
                finish()
            }).addOnFailureListener {
            val msg = "Error al Actualizar"
            toastIncorrecto(msg)
        }
    }


    private fun ObtenerDatos(idUni: String, idEstado: String) {
        mfirestore.collection(idEstado).document(idUni).get().addOnSuccessListener(
            OnSuccessListener { documentSnapshot ->
                val NombreUni = documentSnapshot.getString("Nombre")
                val UbicacionUni = documentSnapshot.getString("Ubicacion")
                val Localid = documentSnapshot.getString("Localidad")
                val TelefonoUni = documentSnapshot.getString("Telefono")
                val CorreoUni = documentSnapshot.getString("Correo")
                val FaceboockUni = documentSnapshot.getString("Facebook")
                val PaginaUni = documentSnapshot.getString("Pagina")

                Nombre.setText(NombreUni)
                lblABC.visibility = View.GONE
                Tipo.visibility = View.GONE
                Estado.visibility = View.GONE
                Municipio.visibility = View.GONE
                Localidad.setText(Localid)
                txtUbicacion.setText(UbicacionUni)
                Telefono.setText(TelefonoUni)
                Correo.setText(CorreoUni)
                Facebook.setText(FaceboockUni)
                Pagina.setText(PaginaUni)
            }).addOnFailureListener {
            Toast.makeText(this@RegistroUni, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show()
        }
    }

    fun ObtenrCodigo() {
        mfirestore.collection("Novedades").document("Universidades").get().addOnSuccessListener(
            OnSuccessListener { documentSnapshot ->
                Codigo = documentSnapshot.getString("Codigo")
            }).addOnFailureListener {
            Toast.makeText(this@RegistroUni, "Error al Obtener el Código", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
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

    companion object {
        var Codigo: String? = null
    }
}