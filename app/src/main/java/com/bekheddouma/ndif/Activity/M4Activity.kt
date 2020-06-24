package com.bekheddouma.ndif.Activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1
import com.bekheddouma.ndif.Utils.Ext1.Companion.commune
import com.bekheddouma.ndif.Utils.Ext1.Companion.db
import com.bekheddouma.ndif.Utils.Ext1.Companion.getImageUri
import com.bekheddouma.ndif.Utils.Ext1.Companion.isNetworkAvailable
import com.bekheddouma.ndif.Utils.Ext1.Companion.wilaya
import com.bekheddouma.ndif.Utils.SharedPreferences
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.bekheddouma.ndif.Data.dub
import kotlinx.android.synthetic.main.m4_layout.*
import java.text.SimpleDateFormat
import java.util.*

class M4Activity : AppCompatActivity(), OnMapReadyCallback {
    var wili=-1
    var geol1=""
    var geol2=""
    var chi=0
    var diburi:Uri?=null
    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m4_layout)
        verif()

        AQIV1.setOnClickListener { onBackPressed() }

        AQLL2.setOnClickListener { startActivity(Intent(this, M2Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AQLL3.setOnClickListener { startActivity(Intent(this, M3Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AQLL5.setOnClickListener { startActivity( Intent( Intent.ACTION_VIEW, Uri.parse( "https://and.dz/presentation/contactez-nous/"))) }

        AQB0.setOnClickListener {
            AQB0.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 128)
            } else startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply { type = "image/*" }, 1)
        }

        AQB1.setOnClickListener {
            AQB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 128)
            } else startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0)
        }

        AQB2.setOnClickListener { open(AQET1, wilaya,""+AQTV6.text ) }

        AQB3.setOnClickListener { open(AQET2, commune[wili],""+AQTV7.text ) }

        AQB4.setOnClickListener {
            AQB4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if((getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                try {

                    LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                        AQSV1.visibility=View.GONE
                        AQLL33.visibility=View.VISIBLE

                        if(it!=null) {
                            var markerOptions = MarkerOptions()
                            markerOptions.position(LatLng(it.latitude,it.longitude))
                            mMap.clear()
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.Builder().target(LatLng(it.latitude, it.longitude)).zoom(13F).build()))
                            mMap.addMarker(markerOptions)
                            geol2=""+it.latitude+","+it.longitude
                        }
                    }.addOnFailureListener { }
                } catch (ex:Exception) {
                    Toast.makeText(this, TEXT_PLUS(5), Toast.LENGTH_SHORT).show()
                    AQSV1.visibility=View.VISIBLE
                    AQLL33.visibility=View.GONE
                }
            } else {
                var builder = AlertDialog.Builder(this)
                builder.setTitle( TEXT_PLUS(11) )
                builder.setMessage( TEXT_PLUS(12) )
                builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                builder.create().show()
            }
        }

        AQB7.setOnClickListener {
            AQSV1.visibility=View.VISIBLE
            AQLL33.visibility=View.GONE
        }

        AQB8.setOnClickListener {
            Toast.makeText(this, TEXT_PLUS(29), Toast.LENGTH_SHORT).show()
            AQSV1.visibility=View.VISIBLE
            AQLL33.visibility=View.GONE

            geol1=if(geol2=="") ""+0.0+","+0.0 else geol2
        }

        AQLL15.setOnClickListener {
            chi=0
            AQLL19.setBackgroundResource(R.drawable.shape_let3)
            AQLL20.setBackgroundResource(R.drawable.shape_let4)
            AQLL21.setBackgroundResource(R.drawable.shape_let4)
            AQLL22.setBackgroundResource(R.drawable.shape_let4)
        }

        AQLL16.setOnClickListener {
            chi=1
            AQLL19.setBackgroundResource(R.drawable.shape_let4)
            AQLL20.setBackgroundResource(R.drawable.shape_let3)
            AQLL21.setBackgroundResource(R.drawable.shape_let4)
            AQLL22.setBackgroundResource(R.drawable.shape_let4)
        }

        AQLL17.setOnClickListener {
            chi=2
            AQLL19.setBackgroundResource(R.drawable.shape_let4)
            AQLL20.setBackgroundResource(R.drawable.shape_let4)
            AQLL21.setBackgroundResource(R.drawable.shape_let3)
            AQLL22.setBackgroundResource(R.drawable.shape_let4)
        }

        AQLL18.setOnClickListener {
            chi=3
            AQLL19.setBackgroundResource(R.drawable.shape_let4)
            AQLL20.setBackgroundResource(R.drawable.shape_let4)
            AQLL21.setBackgroundResource(R.drawable.shape_let4)
            AQLL22.setBackgroundResource(R.drawable.shape_let3)
        }

        AQB5.setOnClickListener { open(AQET3, listOf("1","2","3","4","5","6","7","8","9","10"),""+AQTV14.text ) }

        AQB6.setOnClickListener {
            AQB6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(diburi!=null) {

                if(geol1!="") {

                    if(isNetworkAvailable()) {

                        try {
                            uploide()
                        } catch (ex:Exception) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle( TEXT_PLUS(11) )
                            builder.setMessage( TEXT_PLUS(33) )
                            builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                            builder.create().show()
                        }
                    } else {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle( TEXT_PLUS(11) )
                        builder.setMessage( TEXT_PLUS(32) )
                        builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                        builder.create().show()
                    }
                } else Toast.makeText(this, TEXT_PLUS(31), Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, TEXT_PLUS(30), Toast.LENGTH_SHORT).show()
        }
    }

    fun uploide() {
        var rif= db.getReference("dub")
        var key=rif.push().key
        var mProgressDialog= ProgressDialog(this)
        mProgressDialog.setMessage( TEXT_PLUS(36) )
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()

        var rooftop=FirebaseStorage.getInstance().getReference("dub_image").child(""+key)
        rooftop.putFile(diburi!!).addOnSuccessListener {

            rooftop.downloadUrl.addOnSuccessListener {
                rif.child(key!!).setValue(
                    dub(""+SharedPreferences(applicationContext).get_22_idc(),
                        ""+SharedPreferences(applicationContext).get_22_ddn(),
                        SharedPreferences(applicationContext).get_22_genr(),
                        SharedPreferences(applicationContext).get_22_job(),
                        wilaya[SharedPreferences(applicationContext).get_22_wdr()],
                        ""+SharedPreferences(applicationContext).get_22_cdr(),
                        ""+AQET1.text,
                        ""+AQET2.text,
                        geol1,
                        SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date()),
                        (""+AQET3.text).toInt(),
                        chi,
                        (if(AQRB1.isChecked) 0 else if(AQRB2.isChecked) 1 else 2),
                        (if(AQRB4.isChecked) 0 else if(AQRB5.isChecked) 1 else 2),
                        ""+it)
                )
                mProgressDialog.dismiss()
                dilo(0)
            }.addOnFailureListener {
                mProgressDialog.dismiss()
                dilo(1)
            }
        }.addOnFailureListener {
            mProgressDialog.dismiss()
            dilo(1)
        }
    }

    fun dilo(i: Int) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle( TEXT_PLUS(11) )
        builder.setMessage( TEXT_PLUS( if(i==0) 34 else 35 ) )
        builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> if(i==0) startActivity(Intent(this, M1Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
        builder.setCancelable(false)
        builder.create().show()
    }

    fun verif() {
        (supportFragmentManager.findFragmentById(R.id.AQM1) as SupportMapFragment).getMapAsync(this)
        AQLL33.visibility=View.GONE
        AQIV6.visibility=View.GONE
        AQET1.setText(wilaya[SharedPreferences(this).get_22_wdr()])
        AQET2.setText(""+SharedPreferences(this).get_22_cdr())
        wili=SharedPreferences(this).get_22_wdr()

        if(SharedPreferences(this).get_22_lang()!=0) {
            AQTV2.text=TEXT_PLUS(2)
            AQTV3.text=TEXT_PLUS(3)
            AQTV4.text=TEXT_PLUS(4)
            AQTV5.text=TEXT_PLUS(1)
            AQB0.text=TEXT_PLUS(6)
            AQB1.text=TEXT_PLUS(7)
            AQTV6.text=TEXT_PLUS(8)
            AQTV7.text=TEXT_PLUS(9)
            AQB4.text=TEXT_PLUS(10)
            AQTV8.text=TEXT_PLUS(14)
            AQTV9.text=TEXT_PLUS(15)
            AQTV10.text=TEXT_PLUS(16)
            AQTV11.text=TEXT_PLUS(17)
            AQTV12.text=TEXT_PLUS(18)
            AQTV13.text=TEXT_PLUS(24)
            AQRB1.text=TEXT_PLUS(19)
            AQRB2.text=TEXT_PLUS(20)
            AQRB3.text=TEXT_PLUS(21)
            AQTV15.text=TEXT_PLUS(25)
            AQRB4.text=TEXT_PLUS(26)
            AQRB5.text=TEXT_PLUS(27)
            AQRB6.text=TEXT_PLUS(28)
            AQTV14.text=TEXT_PLUS(22)
            AQB6.text=TEXT_PLUS(23)
            AQB7.text=TEXT_PLUS(37)
            AQB8.text=TEXT_PLUS(38)
        }
    }

    fun open(et: EditText, il:List<String>, st:String) {
        var List= ListView(this)
        List.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, il)
        var builder = AlertDialog.Builder(this)
        builder.setTitle(st)
        builder.setView(List)
        var dialog=builder.create()
        dialog.show()

        List.setOnItemClickListener { _, _, position, _ ->
            et.setText(il[position])

            when(et) {
                AQET1-> {

                    if(wili!=position) {
                        wili=position
                        AQET2.setText(Ext1.commune[wili][0])
                    }
                }
            }
            dialog.dismiss()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        try {
            mMap=googleMap
            var sydney = LatLng(0.0, 0.0)
            mMap.addMarker(MarkerOptions().position(sydney))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

            mMap.setOnMapClickListener {

                if(it!=null) {
                    var markerOptions = MarkerOptions()
                    markerOptions.position(it)
                    mMap.clear()
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(it))
                    mMap.addMarker(markerOptions)
                    geol2=""+it.latitude+","+it.longitude
                }
            }
        } catch (ex:Exception) { }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK) {

            try {

                when(requestCode) {
                    0-> {
                        diburi=getImageUri(this, data!!.extras!!["data"] as Bitmap)
                        AQIV6.setImageURI(diburi!!)
                        AQIV6.visibility= View.VISIBLE
                    }
                    1-> {
                        diburi=getImageUri(this,MediaStore.Images.Media.getBitmap(contentResolver,data!!.data))
                        AQIV6.setImageURI(diburi!!)
                        AQIV6.visibility= View.VISIBLE
                    }
                }
            } catch (ex:Exception) { Toast.makeText(this, TEXT_PLUS(5), Toast.LENGTH_SHORT).show() }
        }
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0-> when (i) {
            1-> "Contact"
            2-> "Profil"
            3-> "Déclarer\nun déchet"
            4-> "Déclarer\nune benne"
            5-> "Désolé, quelque chose d'inattendu s'est mal passé"
            6-> "Galerie"
            7-> "Photo"
            8-> "Wilaya"
            9-> "Commune"
            10-> "Géolocalisation"
            11-> "Remarque"
            12-> "Veuillez activer votre localisation"
            13-> "OK"
            14-> "Quelle est la capacité idéale à votre avis ?"
            15-> "360 litres"
            16-> "1000 litres"
            17-> "10 metres cubes"
            18-> "Je ne sais pas"
            19-> "Une"
            20-> "Deux"
            21-> "Plus"
            22-> "Quelle est la fréquence de passage du camion d'ordures dans votre quartier, par semaine ?"
            23-> "Envoyer"
            24-> "Combien de bennes estimez vous nécessaire ?"
            25-> "Quel est l'état de la benne ?"
            26-> "Cassée"
            27-> "Inadaptée"
            28-> "Inexistante"
            29-> "Géolocalisation réussite"
            30-> "Veuillez sélectionner une photo"
            31-> "Veuillez sélectionner la géolocalisation"
            32-> "S'il vous plait, vérifiez votre connexion internet"
            33-> "Désolé, quelque chose d'inattendu s'est mal passé. Veuillez vous assurer que vous disposez d'une connexion réseau fiable et réessayer"
            34-> "Operation effectuée"
            35-> "Operation échouée"
            36-> "S'il vous plaît patienter ..."
            37-> "Annuler"
            else-> "Valider"
        } 1 -> when (i) {
            1-> "اتصل"
            2-> "الملف الشخصي"
            3-> "قم بإعلان\nالنفايات"
            4-> "أعلن\nدلو"
            5-> "عذرًا ، حدث خطأ غير متوقع"
            6-> "صالة عرض"
            7-> "صورة"
            8-> "ولاية"
            9-> "بلدية"
            10-> "تحديد الموقع"
            11-> "ملحوظة"
            12-> "يرجى تفعيل موقعك"
            13-> "حسنا"
            14-> "ما هي السعة المثالية في رأيك؟"
            15-> "360 لتر"
            16-> "1000 لتر"
            17-> "10 متر مكعب"
            18-> "لا أعرف"
            19-> "واحدة"
            20-> "أثنتان"
            21-> "أكثر"
            22-> "كم مرة تمر شاحنة القمامة في حيك في الأسبوع؟"
            23-> "لترسل"
            24-> "كم عدد التخطيطات التي تعتبرها ضرورية؟"
            25-> "ما هي حالة الدلو؟"
            26-> "مكسور"
            27-> "غير ملائم"
            28-> "غير موجود"
            29-> "نجاح تحديد الموقع الجغرافي"
            30-> "الرجاء تحديد صورة"
            31-> "يرجى تحديد الموقع الجغرافي"
            32-> "الرجاء التحقق من اتصال الانترنت الخاص بك"
            33-> "آسف ، حدث خطأ غير متوقع. يرجى التأكد من أن لديك اتصال شبكة موثوق وحاول مرة أخرى"
            34-> "تنفيذ العملية"
            35-> "فشلت العملية"
            36-> "ارجوك انتظر ..."
            37-> "لالغاء"
            else-> "التحقق"
        } else-> when (i) {
            1-> "Contact"
            2-> "Profile"
            3-> "Declare\ntrash"
            4-> "Declare\nbucket"
            5-> "Sorry, something unexpected went wrong"
            6-> "Gallery"
            7-> "Photo"
            8-> "Wilaya"
            9-> "Commune"
            10-> "Geolocation"
            11-> "Note"
            12-> "Please activate your location"
            13-> "OK"
            14-> "What is the ideal capacity in your opinion?"
            15-> "360 liters"
            16-> "1000 liters"
            17-> "10 cubic meters"
            18-> "I do not know"
            19-> "One"
            20-> "Two"
            21-> "Plus"
            22-> "How often does the garbage truck pass through your neighborhood, per week??"
            23-> "Send"
            24-> "How many skips do you consider necessary?"
            25-> "What is the condition of the bucket?"
            26-> "Broken"
            27-> "Unsuitable"
            28-> "Nonexistent"
            29-> "Geolocation success"
            30-> "Please select a photo"
            31-> "Please select geolocation"
            32-> "Please check your internet connection"
            33-> "Sorry, something unexpected went wrong. Please make sure you have a reliable network connection and try again"
            34-> "Operation performed"
            35-> "Operation failed"
            36-> "Please wait ..."
            37-> "Cancel"
            else-> "Validate"
        }
    }

    override fun onBackPressed() { startActivity(Intent(this, M1Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
}
