package com.softjk.unishare.MenuDrawer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.softjk.unishare.CarrerasUni;
import com.softjk.unishare.ImgUni;
import com.softjk.unishare.Login;
import com.softjk.unishare.R;
import com.softjk.unishare.Regist.RegistroUni;
import com.softjk.unishare.fragment_maps;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentInicio extends Fragment {

    FirebaseFirestore mFirestore;
    FirebaseAuth mAuth;

    RatingBar Califica;
    ImageView Mapas,IMG,DialogLogo,DialogMap,DialogInfo;
    ImageView Logo;
    TextView Abrev, Tipo, Estado, Ubicacion, Nombre, Telefono, Correo, Faceboock, Clave, Pagina;
    ImageView Carreras,Promocion,Ficha,Totu;

    ProgressDialog progressDialog;

    String idUser;
    String Estad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        getActivity().setTitle("Menú");

        Abrev = view.findViewById(R.id.AbcInfo);
        Tipo = view.findViewById(R.id.TipoInfo);
        Estado = view.findViewById(R.id.EstadoInfo);
        Ubicacion = view.findViewById(R.id.UbicacionInfo);
        Nombre = view.findViewById(R.id.NombreUniInfo);
        Telefono = view.findViewById(R.id.TelefonoInfo);
        Correo = view.findViewById(R.id.CorreoInfo);
        Faceboock = view.findViewById(R.id.FaceInfo);
        // clave = findViewById(R.id.ClaveInfo);
        Pagina = view.findViewById(R.id.PaginaInfo);
        Logo = view.findViewById(R.id.logoUniInfo);
        IMG = view.findViewById(R.id.ImgInfo);
        Promocion = view.findViewById(R.id.btnPromocion);
        Mapas = view.findViewById(R.id.btnMaps);
        Carreras = view.findViewById(R.id.btnCarrerasUni);
        Ficha = view.findViewById(R.id.btnFicha);
        Califica = view.findViewById(R.id.CalificaUni);
        Totu = view.findViewById(R.id.IMGTutorial);
        DialogInfo = view.findViewById(R.id.TTInfo);
        DialogLogo = view.findViewById(R.id.TTLogo);
        DialogMap = view.findViewById(R.id.TTMap);

        progressDialog = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        idUser = mAuth.getCurrentUser().getUid();

        SharedPreferences preferences = getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
        Estad = preferences.getString("Estado","");

        System.out.println("Ver estdo id "+ Estad + " Ver IdUser: "+idUser);
        ObtenerDatos(idUser,Estad);

        Nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RegistroUni.class);
                intent.putExtra("Actualizar","Si");
                startActivity(intent);
            }
        });

        Carreras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CarrerasUni.class));
            }
        });

        Logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ImgUni.class);
                intent.putExtra("Dato","Uni");
                startActivity(intent);
                //finish();
            }
        });

        Promocion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImgUni.class);
                intent.putExtra("Dato","Promo");
                startActivity(intent);
            }
        });

        Ficha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogTex(Estad,idUser);
            }
        });

        Totu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int DURACION1 = 1000;
                final int DURACION2 = 2600;
                final int DURACION3 = 4000;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogLogo.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DialogLogo.setVisibility(View.GONE);
                            }
                        },DURACION1);
                    }
                },DURACION1);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogInfo.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DialogInfo.setVisibility(View.GONE);
                            }
                        },DURACION1);
                    }
                },DURACION2);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        DialogMap.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                DialogMap.setVisibility(View.GONE);
                            }
                        },DURACION1);
                    }
                },DURACION3);

            }
        });
        return view;
    }

    private void AlertDialogTex(String Estd,String idUni) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LinearLayout liner = new LinearLayout(getActivity());
        TextView MS = new TextView(getActivity());
        EditText MSTX = new  EditText(getActivity());

        MS.setText("Ingrese el Link de su Ficha: ");
        MS.setTextSize(TypedValue.COMPLEX_UNIT_SP,16f);
        MSTX.setSingleLine();
        liner.setOrientation(LinearLayout.VERTICAL);
        liner.addView(MS);
        liner.addView(MSTX);
        liner.setPadding(50,40,50,10);

        builder.setView(liner);

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(), "Ficha no Agregado", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("Guardar Ficha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.setMessage("Agregando Ficha...");
                progressDialog.show();
                String Link = MSTX.getText().toString().trim();
                Map<String, Object> map = new HashMap<>();
                map.put("Ficha", Link);

                mFirestore.collection(Estd).document(idUni).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Ficha Agregado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "Error al Agregar la Ficha", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.create().show();
    }

    private void ObtenerDatos(String id,String idEstado) {
        mFirestore.collection(idEstado).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String NombreUni = documentSnapshot.getString("Nombre");
                String ABc = documentSnapshot.getString("ABC");
                String TipoUni = documentSnapshot.getString("Tipo");
                String EstadoUni = documentSnapshot.getString("Estado");
                String UbicacionUni = documentSnapshot.getString("Ubicacion");
                String TelefonoUni = documentSnapshot.getString("Telefono");
                String CorreoUni = documentSnapshot.getString("Correo");
                String FaceboockUni= documentSnapshot.getString("Facebook");
                String PaginaUni = documentSnapshot.getString("Pagina");
                String LogoUni = documentSnapshot.getString("Logo");
                String ImgUni = documentSnapshot.getString("IMG");
                String Latitud = documentSnapshot.getString("Latitud");
                String Longitud = documentSnapshot.getString("Longitud");
                String Calificacion = documentSnapshot.getString("Calificacion");

                Nombre.setText(NombreUni);
                Abrev.setText(ABc);
                Tipo.setText(TipoUni);
                Estado.setText(EstadoUni);
                Ubicacion.setText(UbicacionUni);
                Telefono.setText(TelefonoUni);
                Correo.setText(CorreoUni);
                Faceboock.setText(FaceboockUni);
                Pagina.setText(PaginaUni);

                if (getActivity() != null) {
                    SharedPreferences preferences = getActivity().getSharedPreferences("id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Nombre", NombreUni);
                    editor.putString("Logo", LogoUni);
                    editor.commit();
                }


                if (Calificacion != null){
                    Float Calif = Float.valueOf(Calificacion);
                    Califica.setRating(Calif);
                }
                System.out.println("Ver ABC "+ABc);
                if (ABc==null){
                    VentanaSesion(Estad);
                }

                if (LogoUni != null){
                    Toast toast = Toast.makeText(getActivity(), "Cargando foto", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 200);
                    toast.show();
                    Glide.with(getActivity())
                            .load(LogoUni)
                            .into(Logo);
                    System.out.println("Foto Agregado... "+LogoUni);
                }

                if (ImgUni != null){
                    Glide.with(getActivity())
                            .load(ImgUni)
                            .into(IMG);
                    System.out.println("Foto Agregado... "+LogoUni);
                }

                Mapas.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fragment_maps fragmentMaps = new fragment_maps();
                        Bundle args = new Bundle();
                        args.putString("Latitud", Latitud); // Aquí se asigna el dato al Bundle
                        args.putString("Longitud",Longitud);
                        args.putString("ABC",ABc);
                        fragmentMaps.setArguments(args);
                        fragmentMaps.show(getParentFragmentManager(),"open fragment");

                    }
                });

                Faceboock.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Titulo = "Abrir Faceboock";
                        String URL = Faceboock.getText().toString();
                        VentanaMsgDialog(Titulo,URL);
                    }
                });

                Pagina.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Titulo = "Abrir Página";
                        VentanaMsgDialog(Titulo,PaginaUni);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void VentanaMsgDialog(String Titulo,String URL) {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE).setTitleText("Aviso")
                .setContentText(Titulo)
                .setCancelText("No").setConfirmText("Si")
                .showCancelButton(true).setCancelClickListener(sDialog -> {
                    sDialog.dismissWithAnimation();

                }).setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    Uri Link = Uri.parse(URL);
                    Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                    startActivity(intent);
                }).show();
    }

    private void VentanaSesion(String Estado) {

        SweetAlertDialog dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        dialog.setCancelable(false); // Aquí estás estableciendo que no se pueda cancelar
        dialog.setTitleText("Datos No Encontrados en "+Estado);
        dialog.setContentText("Por Favor Cierre Sesión e Inicie Nuevamente, Seleccionando su ESTADO Correctamente");
        dialog.setConfirmText("Cerrar Sesión")
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.dismissWithAnimation();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(), Login.class));
                    mAuth.signOut();
                }).show(); // El diálogo no se puede cancelar

    }

}