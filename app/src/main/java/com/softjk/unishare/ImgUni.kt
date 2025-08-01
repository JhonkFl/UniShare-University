package com.softjk.unishare

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.codesgood.views.JustifiedTextView
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.softjk.unishare.MenuDrawer.MenuPrincipal
import org.imaginativeworld.whynotimagecarousel.ImageCarousel
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class ImgUni : AppCompatActivity() {
    lateinit var Siguiente: Button
    lateinit var btnLogo: Button
    lateinit var btnImg: Button
    lateinit var btnRequis: Button
    lateinit var btnINClass: Button
    lateinit var Img: ImageView
    lateinit var Subtitulo: TextView
    lateinit var Informa: JustifiedTextView
    lateinit var carousel: ImageCarousel
    private lateinit var mfirestore: FirebaseFirestore
    private lateinit var mAuth: FirebaseAuth
    lateinit var storageReference: StorageReference
    private lateinit var image_url: Uri
    lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_img_uni)
        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.main)
        ) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progressDialog = ProgressDialog(this)
        Siguiente = findViewById(R.id.btnSiguiente)
        btnLogo = findViewById(R.id.btnSubirLogo)
        btnImg = findViewById(R.id.btnSubirImg)
        btnRequis = findViewById(R.id.btnSubirRequisit)
        btnINClass = findViewById(R.id.btnSubirInicioClass)
        Img = findViewById(R.id.ImgInstalacion)
        Subtitulo = findViewById(R.id.lblSubTituloIMG)
        Informa = findViewById(R.id.lblInforma)
        carousel = findViewById(R.id.carousel)

        mfirestore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        val Dato = intent.getStringExtra("Dato")
        if (Dato == "Uni") {
            btnLogo.setVisibility(View.VISIBLE)
            btnImg.setVisibility(View.VISIBLE)
            Subtitulo.setText("Suba nueva Imagen de la Institución")

            val Img1 = "Logo"
            val Img2 = "IMG"
            ObtenerImagen(Img1, Img2)
        } else if (Dato == "Promo") {
            Siguiente.setVisibility(View.INVISIBLE)
            btnRequis.setVisibility(View.VISIBLE)
            btnINClass.setVisibility(View.VISIBLE)
            Informa.setVisibility(View.VISIBLE)

            val Im1 = "Requisitos"
            val Im2 = "FechaClass"
            ObtenerImagen(Im1, Im2)
        }

        btnLogo.setOnClickListener(View.OnClickListener {
            TImg = "Logo"
            storage_path = "LogoUni/*"

            Img.setVisibility(View.VISIBLE)
            carousel.setVisibility(View.GONE)
            // if (ContextCompat.checkSelfPermission(ImgUni.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            SubirFoto()
            /* }else {
                                   SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                               }*/
        })

        btnImg.setOnClickListener(View.OnClickListener {
            Img.setVisibility(View.VISIBLE)
            carousel.setVisibility(View.GONE)
            TImg = "IMG"
            storage_path = "Universidades/*"
            SubirFoto()
        })

        btnRequis.setOnClickListener(View.OnClickListener {
            Img.setVisibility(View.VISIBLE)
            carousel.setVisibility(View.GONE)
            TImg = "Requisitos"
            storage_path = "Requsitos/*"
            SubirFoto()
        })

        btnINClass.setOnClickListener(View.OnClickListener {
            Img.setVisibility(View.VISIBLE)
            carousel.setVisibility(View.GONE)
            TImg = "FechaClass"
            storage_path = "FechaClass/*"
            SubirFoto()
        })


        Siguiente.setOnClickListener(View.OnClickListener {
            val i = Intent(this@ImgUni, MenuPrincipal::class.java)
            i.putExtra("Dato", "OK")
            startActivity(i)
            finish()
        })
    }

    private fun ObtenerImagen(I1: String, I2: String) {
        val preferences = this.getSharedPreferences("id", MODE_PRIVATE)
        val EstadoBD = preferences.getString("Estado", "")!!
        val idUser = mAuth.currentUser!!.uid
        println("Ver datos BD para Img: Estado = $EstadoBD Id = $idUser")

        mfirestore.collection(EstadoBD).document(idUser).get().addOnSuccessListener(
            OnSuccessListener<DocumentSnapshot> { documentSnapshot ->
                val Img1 = documentSnapshot.getString(I1)
                val Img2 = documentSnapshot.getString(I2)
                // String Img3 = documentSnapshot.getString("Nombre");
                println("Ver img $Img1")
                try {
                    if (Img1 == null || Img2 == null) {
                        Img.visibility = View.VISIBLE
                        carousel.visibility = View.GONE
                    } else {
                        carousel.registerLifecycle(lifecycle)
                        val list = mutableListOf<CarouselItem>()

                        list.add(CarouselItem(Img1))
                        list.add(CarouselItem(Img2))
                        carousel.setData(list)
                    }
                } catch (e: Exception) {
                    Img.visibility = View.VISIBLE
                    carousel.visibility = View.GONE

                    Log.v("Error", "e: $e")
                    println("Imagen NO Agregado... ")
                }
            }).addOnFailureListener {
            Toast.makeText(this@ImgUni, "Error al Obtener las Imágenes", Toast.LENGTH_SHORT).show()
        }
    }

    private fun SubirFoto() {
        val i = Intent(Intent.ACTION_PICK)
        i.setType("image/*")
        startActivityForResult(i, COD_SEL_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d("image_url", "requestCode - RESULT_OK: " + requestCode + " " + RESULT_OK)
        if (resultCode == RESULT_OK && requestCode == COD_SEL_IMAGE) {
            val imageUri = data?.data
            if (imageUri != null) {
                image_url = imageUri
                subirFoto(imageUri)
                Img.setImageURI(imageUri)
            } else {
                Toast.makeText(this, "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show()
            }
        }

        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun subirFoto(imageUrl: Uri?) {
        progressDialog.setMessage("Subiendo")
        progressDialog.show()
        progressDialog.setCancelable(false)

        val preferences = getSharedPreferences("id", MODE_PRIVATE)
        val idEstado = preferences.getString("Estado", "")

        val rute_storage_photo = storage_path + "" + TImg + "" + mAuth!!.uid
        val reference = storageReference.child(rute_storage_photo)
        reference.putFile(image_url!!)
            .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                while (!uriTask.isSuccessful);
                if (uriTask.isSuccessful) {
                    uriTask.addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                        println("Ver Tipo de Img a Subir " + TImg)
                        val download_uri = uri.toString()
                        val map = HashMap<String?, Any>()
                        map[TImg] = download_uri
                        if (idEstado != null) {
                            mfirestore.collection(idEstado).document(mAuth.uid!!).update(map)
                            Toast.makeText(this@ImgUni, "Foto actualizada", Toast.LENGTH_SHORT).show()
                        }else{
                            Toast.makeText(this@ImgUni, "Error Id User", Toast.LENGTH_SHORT).show()
                        }

                        progressDialog.dismiss()
                    })
                }
            }).addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this@ImgUni, "Error al cargar la foto", Toast.LENGTH_SHORT)
                    .show()
            }
    }


    private val SolicitudPermisoGaleria = registerForActivityResult(
        RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            SubirFoto()
        } else {
            Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        var storage_path: String? = null //Crear carpeta para guardar los datosIMG
        private const val COD_SEL_STOREGE = 200
        private const val COD_SEL_IMAGE = 300
        var TImg: String? = null
    }
}