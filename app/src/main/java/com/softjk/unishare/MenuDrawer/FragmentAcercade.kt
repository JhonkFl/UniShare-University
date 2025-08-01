package com.softjk.unishare.MenuDrawer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.fragment.app.Fragment
import com.softjk.unishare.R

class FragmentAcercade : Fragment() {
    lateinit var Faceboock: ImageView
    lateinit var Instagram: ImageView
    lateinit var Youtube: ImageView
    lateinit var PlayStore: ImageView
    lateinit var Calificar: RatingBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_acercade, container, false)
        requireActivity().title = "Acerca De..."

        Faceboock = view.findViewById(R.id.Faceboock)
        Youtube = view.findViewById(R.id.Youtube)
        PlayStore = view.findViewById(R.id.PlayStore)
        Calificar = view.findViewById(R.id.ratingBar)

        Faceboock.setOnClickListener(View.OnClickListener {
            val Link = Uri.parse("https://www.facebook.com/profile.php?id=61568168055109")
            val intent = Intent(Intent.ACTION_VIEW, Link)
            startActivity(intent)
        })

        Youtube.setOnClickListener(View.OnClickListener {
            val Link = Uri.parse("https://www.youtube.com/@Soft-jkCompany")
            val intent = Intent(Intent.ACTION_VIEW, Link)
            startActivity(intent)
        })

        PlayStore.setOnClickListener(View.OnClickListener {
            val Link = Uri.parse("https://play.google.com/store/apps/developer?id=Soft-jk")
            val intent = Intent(Intent.ACTION_VIEW, Link)
            startActivity(intent)
        })

        Calificar.setOnRatingBarChangeListener(OnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val Link = Uri.parse( "https://play.google.com/store/apps/details?id=com.softjk.uni")
            val intent = Intent(Intent.ACTION_VIEW, Link)
            startActivity(intent)
        })

        return view
    }
}