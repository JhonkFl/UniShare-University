package com.softjk.unishare.Metodos

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object Permisos {
    fun getLocalizacion(context: Activity) {
        val permiso =
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (permiso == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context, Manifest.permission.ACCESS_FINE_LOCATION)
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    context, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }
    }

    //--------------------------------------------------------------------------------------------------
    fun getNotificaciones(activity: Activity) {
        val REQUEST_NOTIFICATION_PERMISSION = 100

        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION_PERMISSION
            )
        } else {
            // Si ya tiene el permiso, mostrar la notificación
        }
    }


    //--------------------------------------------------------------------------------------------------
    fun getAlamcenimiento(activity: Activity) {
        val LOCATION_REQUEST_CODE = 23

        if (ContextCompat.checkSelfPermission(
                activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), LOCATION_REQUEST_CODE
            )
        }
    }
}
