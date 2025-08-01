package com.softjk.unishare.MenuDrawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.softjk.unishare.R;
import com.softjk.unishare.Regist.RegistroUni;

public class FragmentAcercade extends Fragment {

ImageView Faceboock, Instagram, Youtube, PlayStore;
RatingBar Calificar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_acercade, container, false);
        getActivity().setTitle("Acerca De...");

        Faceboock = view.findViewById(R.id.Faceboock);
        Youtube = view.findViewById(R.id.Youtube);
        PlayStore = view.findViewById(R.id.PlayStore);
        Calificar = view.findViewById(R.id.ratingBar);

        Faceboock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri Link = Uri.parse("https://www.facebook.com/profile.php?id=61568168055109");
                Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                startActivity(intent);
            }
        });

        Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri Link = Uri.parse("https://www.youtube.com/@Soft-jkCompany");
                Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                startActivity(intent);
            }
        });

        PlayStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri Link = Uri.parse("https://play.google.com/store/apps/developer?id=Soft-jk");
                Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                startActivity(intent);
            }
        });

        Calificar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                String URL = "https://play.google.com/store/apps/details?id=com.softjk.uni";
                Uri Link = Uri.parse(URL);
                Intent intent = new Intent(Intent.ACTION_VIEW,Link);
                startActivity(intent);
            }
        });

        return view;
    }
}