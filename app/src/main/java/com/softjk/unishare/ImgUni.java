package com.softjk.unishare;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codesgood.views.JustifiedTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softjk.unishare.MenuDrawer.MenuPrincipal;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImgUni extends AppCompatActivity {
    Button Siguiente,btnLogo,btnImg,btnRequis,btnINClass;
    ImageView Img;
    TextView Subtitulo;
    JustifiedTextView Informa;
    ImageCarousel carousel;
    private FirebaseFirestore mfirestore;
    private FirebaseAuth mAuth;
    StorageReference storageReference;
    public static String storage_path; //Crear carpeta para guardar los datosIMG
    private static final int COD_SEL_STOREGE = 200;
    private static final int COD_SEL_IMAGE = 300;
    private Uri image_url;
    ProgressDialog progressDialog;
    public static String TImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_img_uni);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        progressDialog = new ProgressDialog(this);
        Siguiente = findViewById(R.id.btnSiguiente);
        btnLogo = findViewById(R.id.btnSubirLogo);
        btnImg = findViewById(R.id.btnSubirImg);
        btnRequis = findViewById(R.id.btnSubirRequisit);
        btnINClass = findViewById(R.id.btnSubirInicioClass);
        Img = findViewById(R.id.ImgInstalacion);
        Subtitulo = findViewById(R.id.lblSubTituloIMG);
        Informa = findViewById(R.id.lblInforma);
        carousel = findViewById(R.id.carousel);

        mfirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        String Dato = getIntent().getStringExtra("Dato");
        if (Dato.equals("Uni")){
            btnLogo.setVisibility(View.VISIBLE);
            btnImg.setVisibility(View.VISIBLE);
            Subtitulo.setText("Suba nueva Imagen de la Institución");

            String Img1 = "Logo";
            String Img2 = "IMG";
            ObtenerImagen(Img1,Img2);

        } else if (Dato.equals("Promo")) {
            Siguiente.setVisibility(View.INVISIBLE);
            btnRequis.setVisibility(View.VISIBLE);
            btnINClass.setVisibility(View.VISIBLE);
            Informa.setVisibility(View.VISIBLE);

            String Im1 = "Requisitos";
            String Im2 = "FechaClass";
            ObtenerImagen(Im1,Im2);
        }

        btnLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TImg = "Logo";
                storage_path = "LogoUni/*";

                Img.setVisibility(View.VISIBLE);
                carousel.setVisibility(View.GONE);
              // if (ContextCompat.checkSelfPermission(ImgUni.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    SubirFoto();
                /* }else {
                    SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }*/

            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img.setVisibility(View.VISIBLE);
                carousel.setVisibility(View.GONE);
                TImg = "IMG";
                storage_path = "Universidades/*";
                SubirFoto();
            }
        });

        btnRequis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img.setVisibility(View.VISIBLE);
                carousel.setVisibility(View.GONE);
                TImg = "Requisitos";
                storage_path = "Requsitos/*";
                SubirFoto();
            }
        });

        btnINClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img.setVisibility(View.VISIBLE);
                carousel.setVisibility(View.GONE);
                TImg = "FechaClass";
                storage_path = "FechaClass/*";
                SubirFoto();
            }
        });


        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(ImgUni.this, MenuPrincipal.class);
                i.putExtra("Dato","OK");
                startActivity(i);
                finish();
            }
        });
    }

    private void ObtenerImagen(String I1, String I2) {
        SharedPreferences preferences = this.getSharedPreferences("id", Context.MODE_PRIVATE);
        String EstadoBD = preferences.getString("Estado","");
        String idUser = mAuth.getCurrentUser().getUid();
        System.out.println("Ver datos BD para Img: Estado = "+EstadoBD+" Id = "+idUser);

        mfirestore.collection(EstadoBD).document(idUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String Img1 = documentSnapshot.getString(I1);
                String Img2 = documentSnapshot.getString(I2);
               // String Img3 = documentSnapshot.getString("Nombre");
                System.out.println("Ver img "+Img1);

                try {

                    if (Img1==null || Img2 == null ){
                        Img.setVisibility(View.VISIBLE);
                        carousel.setVisibility(View.GONE);
                    }else {
                        carousel.registerLifecycle(getLifecycle());
                        List<CarouselItem> list = new ArrayList<>();

                        list.add(new CarouselItem(Img1));
                        list.add(new CarouselItem(Img2));
                        carousel.setData(list);
                    }

                }catch (Exception e){
                    Img.setVisibility(View.VISIBLE);
                    carousel.setVisibility(View.GONE);

                    Log.v("Error", "e: " + e);
                    System.out.println("Imagen NO Agregado... ");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ImgUni.this, "Error al Obtener las Imágenes", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SubirFoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("image_url", "requestCode - RESULT_OK: " + requestCode + " " + RESULT_OK);
        if (resultCode == RESULT_OK) {
            if (requestCode == COD_SEL_IMAGE) {
                image_url = data.getData();
                subirFoto(image_url);
                this.Img.setImageURI(image_url);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirFoto(Uri imageUrl) {
        progressDialog.setMessage("Subiendo");
        progressDialog.show();
        progressDialog.setCancelable(false);

        SharedPreferences preferences = getSharedPreferences("id", Context.MODE_PRIVATE);
        String idEstado = preferences.getString("Estado","");

        String rute_storage_photo = storage_path + "" + TImg + "" + mAuth.getUid();
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()) ;
                if (uriTask.isSuccessful()) {
                    uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            System.out.println("Ver Tipo de Img a Subir "+TImg);
                            String download_uri = uri.toString();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put(TImg, download_uri);
                            mfirestore.collection(idEstado).document(mAuth.getUid()).update(map);
                            Toast.makeText(ImgUni.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ImgUni.this, "Error al cargar la foto", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private ActivityResultLauncher<String> SolicitudPermisoGaleria = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        if (isGranted){
            SubirFoto();
        }else {
            Toast.makeText(this, "Permiso Denegado", Toast.LENGTH_SHORT).show();
        }
    });
}