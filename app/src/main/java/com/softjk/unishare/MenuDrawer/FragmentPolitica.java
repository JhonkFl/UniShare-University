package com.softjk.unishare.MenuDrawer;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softjk.unishare.R;


public class FragmentPolitica extends Fragment {
    WebView myWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_politica, container, false);

        getActivity().setTitle("Politica de Privacidad");
        myWebView = view.findViewById(R.id.PoliticaWeb);
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("https://unisearchjk.blogspot.com/p/politica-de-privacidad-mi-universidad.html");

        return view;
    }
}