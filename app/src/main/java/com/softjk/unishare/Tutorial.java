package com.softjk.unishare;


import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Tutorial extends AppCompatActivity {
    WebView Tuturial;
    TextView EstadoV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tutorial);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Tuturial = findViewById(R.id.VideoTutorial);
       // EstadoV = findViewById(R.id.EstadoV);
       // EstadoV.setText("Descargando Tuturial");

        String URL= "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/4T1ruFdntY4?si=hSL4rpZHow532GdS&amp;start=1\" title=\"Tutorial 1\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";

        Tuturial.loadData(URL,"text/html","utf-8");
        Tuturial.getSettings().setJavaScriptEnabled(true);
        Tuturial.setWebChromeClient(new WebChromeClient());
    }


}