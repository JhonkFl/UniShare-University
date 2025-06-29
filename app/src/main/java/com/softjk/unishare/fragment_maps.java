package com.softjk.unishare;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class fragment_maps extends DialogFragment{
    GoogleMap mMap;
    Activity activity;
    String Latitud,Longitud,Abc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        activity = new Activity();

        Bundle args = getArguments();
        if (args != null) {
            // Obtén el dato utilizando la clave que usaste para enviarlo desde la actividad
            Latitud = args.getString("Latitud");
            Longitud = args.getString("Longitud");
            Abc = args.getString("ABC");
        }

        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.LinerMapa);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mMap = googleMap;
                double La = Double.parseDouble(Latitud);
                double Lo = Double.parseDouble(Longitud);

                LatLng mexico = new LatLng( La,Lo);
                mMap.addMarker(new MarkerOptions().position(mexico).title(Abc));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(mexico));
            }
        });
        return view;
    }

}