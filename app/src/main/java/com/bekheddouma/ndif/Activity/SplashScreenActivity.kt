package com.bekheddouma.ndif.Activity

import android.Manifest
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Process
import android.provider.MediaStore
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bekheddouma.ndif.Utils.SharedPreferences
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1.Companion.connectivityManager
import com.bekheddouma.ndif.Utils.Ext1.Companion.db
import com.bekheddouma.ndif.Utils.Ext1.Companion.isNetworkAvailable
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.splash_screen_layout.*
import org.json.JSONObject

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_layout)
        logo()

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),0) else {
            start()
            verif()
        }
    }

    fun logo() {
        AZIV1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_splash))

        ObjectAnimator.ofPropertyValuesHolder(AZIV1, PropertyValuesHolder.ofFloat("scaleX", 1.1f), PropertyValuesHolder.ofFloat("scaleY", 1.1f)).apply {
            duration=300
            repeatCount=2
            repeatMode= ObjectAnimator.REVERSE
        }.start()
    }

    fun verif() {
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE)
        db=FirebaseDatabase.getInstance()

        if(isNetworkAvailable()) {

            try {

                db.getReference("app").addListenerForSingleValueEvent(object : ValueEventListener {

                    override fun onDataChange(dt: DataSnapshot) {

                        if(""+dt.child("app1_v").value=="1.7") {

                            if(SharedPreferences(applicationContext).get_22_lang()==-1) startActivity(Intent(applicationContext, LengueActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) else {

                                if(FirebaseAuth.getInstance().currentUser==null) startActivity(Intent(applicationContext, ITActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) else {
                                    startActivity(Intent(applicationContext, if(SharedPreferences(applicationContext).get_22_ddn()=="") SignUpActivity::class.java else M1Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                                }
                            }
                        } else dialog(6,7,8)
                    }

                    override fun onCancelled(databaseError: DatabaseError) { dialog(4,5,3) }
                })
            } catch (ex:Exception) { dialog(4,5,3) }
        } else dialog(1,2,3)
    }

    fun start() {

        try {
            var mLocationRequest = LocationRequest()
            mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            mLocationRequest.interval = 10000
            mLocationRequest.fastestInterval = 2000
            var builder = LocationSettingsRequest.Builder()
            builder.addLocationRequest(mLocationRequest)
            var locationSettingsRequest = builder.build()
            var settingsClient = LocationServices.getSettingsClient(this)
            settingsClient.checkLocationSettings(locationSettingsRequest)

            LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(

                mLocationRequest, object : LocationCallback() {

                    override fun onLocationResult(locationResult: LocationResult) { }
                }, Looper.myLooper()
            )
        } catch (ex:Exception) { }
    }

    fun dialog(i:Int,j:Int,k:Int) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle( TEXT_PLUS(i) )
        builder.setMessage( TEXT_PLUS(j) )
        builder.setNegativeButton( TEXT_PLUS(k) ) { _, _ ->

            if(k==8) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bekheddouma.ndif")))
                android.os.Process.killProcess(android.os.Process.myPid());
            } else verif()
        }
        builder.setCancelable(false)
        builder.create().show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {

        if(requestCode==0) {

            if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                start()
                verif()
            } else {
                moveTaskToBack(true)
                Process.killProcess( Process.myPid() )
            }
            return
        }
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0,-1-> when (i) {
            1-> "ERREUR DE CONNEXION"
            2-> "S'il vous plait, vérifiez votre connexion internet"
            3-> "Réessayez"
            4-> "ERREUR INCONNUE"
            5-> "Désolé, quelque chose d'inattendu s'est mal passé. Veuillez vous assurer que vous disposez d'une connexion réseau fiable et réessayer"
            6-> "Télécharger gratuitement"
            7-> "Installer la nouvelle version pour continuer"
            else-> "OK"
        } 1 -> when (i) {
            1-> "خطأ في الاتصال"
            2-> "الرجاء التحقق من اتصال الانترنت الخاص بك"
            3-> "إعادة المحاولة"
            4-> "خطأ غير معروف"
            5-> "آسف ، حدث خطأ غير متوقع. يرجى التأكد من أن لديك اتصال شبكة موثوق وحاول مرة أخرى"
            6-> "تحميل مجاني"
            7-> "قم بتثبيت الإصدار الجديد للمتابعة"
            else-> "حسنا"
        } else -> when (i) {
            1-> "CONNEXION ERROR"
            2-> "Please check your internet connection"
            3-> "Try again"
            4-> "UNKNOWN ERROR"
            5-> "Sorry, something unexpected went wrong. Please make sure you have a reliable network connection and try again"
            6-> "Free download"
            7-> "Install the new version to continue"
            else-> "OK"
        }
    }

    override fun onBackPressed() { }
}
