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
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1.Companion.commune
import com.bekheddouma.ndif.Utils.Ext1.Companion.db
import com.bekheddouma.ndif.Utils.Ext1.Companion.getImageUri
import com.bekheddouma.ndif.Utils.Ext1.Companion.isNetworkAvailable
import com.bekheddouma.ndif.Utils.Ext1.Companion.wilaya
import com.bekheddouma.ndif.Utils.SharedPreferences
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.bekheddouma.ndif.Data.dud
import kotlinx.android.synthetic.main.m3_layout.*
import java.text.SimpleDateFormat
import java.util.*

class M3Activity : AppCompatActivity(), OnMapReadyCallback {
    var wili=-1
    var geol1=""
    var geol2=""
    var chi=0
    var diburi:Uri?=null
    lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m3_layout)
        verif()

        APIV1.setOnClickListener { onBackPressed() }

        APLL2.setOnClickListener { startActivity(Intent(this, M2Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        APLL4.setOnClickListener { startActivity(Intent(this, M4Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        APLL5.setOnClickListener { startActivity( Intent( Intent.ACTION_VIEW, Uri.parse( "https://and.dz/presentation/contactez-nous/"))) }

        APB0.setOnClickListener {
            APB0.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 128)
            } else startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply { type = "image/*" }, 1)
        }

        APB1.setOnClickListener {
            APB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 128)
            } else startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 0)
        }

        APB2.setOnClickListener { open(APET1, wilaya,""+APTV6.text ) }

        APB3.setOnClickListener { open(APET2, commune[wili],""+APTV7.text ) }

        APB4.setOnClickListener {
            APB4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if((getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                try {

                    getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                        APSV1.visibility=View.GONE
                        APLL28.visibility=View.VISIBLE

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
                    APSV1.visibility=View.VISIBLE
                    APLL28.visibility=View.GONE
                }
            } else {
                var builder = AlertDialog.Builder(this)
                builder.setTitle( TEXT_PLUS(11) )
                builder.setMessage( TEXT_PLUS(12) )
                builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                builder.create().show()
            }
        }

        APB7.setOnClickListener {
            APSV1.visibility=View.VISIBLE
            APLL28.visibility=View.GONE
        }

        APB8.setOnClickListener {
            Toast.makeText(this, TEXT_PLUS(24), Toast.LENGTH_SHORT).show()
            APSV1.visibility=View.VISIBLE
            APLL28.visibility=View.GONE

            geol1=if(geol2=="") ""+0.0+","+0.0 else geol2
        }

        APLL15.setOnClickListener {
            chi=0
            APLL19.setBackgroundResource(R.drawable.shape_let3)
            APLL20.setBackgroundResource(R.drawable.shape_let4)
            APLL21.setBackgroundResource(R.drawable.shape_let4)
            APLL22.setBackgroundResource(R.drawable.shape_let4)
        }

        APLL16.setOnClickListener {
            chi=1
            APLL19.setBackgroundResource(R.drawable.shape_let4)
            APLL20.setBackgroundResource(R.drawable.shape_let3)
            APLL21.setBackgroundResource(R.drawable.shape_let4)
            APLL22.setBackgroundResource(R.drawable.shape_let4)
        }

        APLL17.setOnClickListener {
            chi=2
            APLL19.setBackgroundResource(R.drawable.shape_let4)
            APLL20.setBackgroundResource(R.drawable.shape_let4)
            APLL21.setBackgroundResource(R.drawable.shape_let3)
            APLL22.setBackgroundResource(R.drawable.shape_let4)
        }

        APLL18.setOnClickListener {
            chi=3
            APLL19.setBackgroundResource(R.drawable.shape_let4)
            APLL20.setBackgroundResource(R.drawable.shape_let4)
            APLL21.setBackgroundResource(R.drawable.shape_let4)
            APLL22.setBackgroundResource(R.drawable.shape_let3)
        }

        APB5.setOnClickListener { open(APET3, listOf("1","2","3","4","5","6","7","8","9","10"),""+APTV14.text ) }

        APB6.setOnClickListener {
            APB6.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(diburi!=null) {

                if(geol1!="") {

                    if(isNetworkAvailable()) {

                        try {
                            uploide()
                        } catch (ex:Exception) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle( TEXT_PLUS(11) )
                            builder.setMessage( TEXT_PLUS(28) )
                            builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                            builder.create().show()
                        }
                    } else {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle( TEXT_PLUS(11) )
                        builder.setMessage( TEXT_PLUS(27) )
                        builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                        builder.create().show()
                    }
                } else Toast.makeText(this, TEXT_PLUS(26), Toast.LENGTH_SHORT).show()
            } else Toast.makeText(this, TEXT_PLUS(25), Toast.LENGTH_SHORT).show()
        }
    }

    fun uploide() {
        var rif=db.getReference("dud")
        var key=rif.push().key
        var mProgressDialog= ProgressDialog(this)
        mProgressDialog.setMessage( TEXT_PLUS(31) )
        mProgressDialog.setCancelable(false)
        mProgressDialog.show()

        var rooftop=FirebaseStorage.getInstance().getReference("dud_image").child(""+key)
        rooftop.putFile(diburi!!).addOnSuccessListener {

            rooftop.downloadUrl.addOnSuccessListener {
                rif.child(key!!).setValue(
                    dud(""+SharedPreferences(applicationContext).get_22_idc(),
                    ""+SharedPreferences(applicationContext).get_22_ddn(),
                    SharedPreferences(applicationContext).get_22_genr(),
                    SharedPreferences(applicationContext).get_22_job(),
                    wilaya[SharedPreferences(applicationContext).get_22_wdr()],
                    ""+SharedPreferences(applicationContext).get_22_cdr(),
                    ""+APET1.text,
                    ""+APET2.text,
                    geol1,
                    SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Date()),
                    (""+APET3.text).toInt(),
                    chi,
                    (if(APRB1.isChecked) 0 else 1),
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
        builder.setMessage( TEXT_PLUS( if(i==0) 29 else 30 ) )
        builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> if(i==0) startActivity(Intent(this, M1Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
        builder.setCancelable(false)
        builder.create().show()
    }

    fun verif() {
        (supportFragmentManager.findFragmentById(R.id.APM1) as SupportMapFragment).getMapAsync(this)
        APLL28.visibility=View.GONE
        APIV6.visibility=View.GONE
        APET1.setText(wilaya[SharedPreferences(this).get_22_wdr()])
        APET2.setText(""+SharedPreferences(this).get_22_cdr())
        wili=SharedPreferences(this).get_22_wdr()

        if(SharedPreferences(this).get_22_lang()!=0) {
            APTV2.text=TEXT_PLUS(2)
            APTV3.text=TEXT_PLUS(3)
            APTV4.text=TEXT_PLUS(4)
            APTV5.text=TEXT_PLUS(1)
            APB0.text=TEXT_PLUS(6)
            APB1.text=TEXT_PLUS(7)
            APTV6.text=TEXT_PLUS(8)
            APTV7.text=TEXT_PLUS(9)
            APB4.text=TEXT_PLUS(10)
            APTV8.text=TEXT_PLUS(14)
            APTV9.text=TEXT_PLUS(15)
            APTV10.text=TEXT_PLUS(16)
            APTV11.text=TEXT_PLUS(17)
            APTV12.text=TEXT_PLUS(18)
            APTV13.text=TEXT_PLUS(19)
            APRB1.text=TEXT_PLUS(20)
            APRB2.text=TEXT_PLUS(21)
            APTV14.text=TEXT_PLUS(22)
            APB6.text=TEXT_PLUS(23)
            APB7.text=TEXT_PLUS(32)
            APB8.text=TEXT_PLUS(33)
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
                APET1-> {

                    if(wili!=position) {
                        wili=position
                        APET2.setText(commune[wili][0])
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
                        APIV6.setImageURI(diburi!!)
                        APIV6.visibility= View.VISIBLE
                    }
                    1-> {
                        diburi=getImageUri(this,MediaStore.Images.Media.getBitmap(contentResolver,data!!.data))
                        APIV6.setImageURI(diburi!!)
                        APIV6.visibility= View.VISIBLE
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
            14-> "Quel est le type du déchet ?"
            15-> "Gravats"
            16-> "Végétal"
            17-> "Déchet Ménager"
            18-> "Autre"
            19-> "Quel est la nature du déchet ?"
            20-> "Décharge sauvage"
            21-> "Défaut de ramassage"
            22-> "Quelle est la fréquence de passage du camion d'ordures dans votre quartier, par semaine ?"
            23-> "Envoyer"
            24-> "Géolocalisation réussite"
            25-> "Veuillez sélectionner une photo"
            26-> "Veuillez sélectionner la géolocalisation"
            27-> "S'il vous plait, vérifiez votre connexion internet"
            28-> "Désolé, quelque chose d'inattendu s'est mal passé. Veuillez vous assurer que vous disposez d'une connexion réseau fiable et réessayer"
            29-> "Operation effectuée"
            30-> "Operation échouée"
            31-> "S'il vous plaît patienter ..."
            32-> "Annuler"
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
            14-> "ما هو نوع النفايات؟"
            15-> "الأنقاض"
            16-> "نباتي"
            17-> "النفايات المنزلية"
            18-> "آخر"
            19-> "ما هي طبيعة المخلفات؟"
            20-> "التفريغ البري"
            21-> "خطأ الالتقاط"
            22-> "كم مرة تمر شاحنة القمامة في حيك في الأسبوع؟"
            23-> "لترسل"
            24-> "نجاح تحديد الموقع الجغرافي"
            25-> "الرجاء تحديد صورة"
            26-> "يرجى تحديد الموقع الجغرافي"
            27-> "الرجاء التحقق من اتصال الانترنت الخاص بك"
            28-> "آسف ، حدث خطأ غير متوقع. يرجى التأكد من أن لديك اتصال شبكة موثوق وحاول مرة أخرى"
            29-> "تنفيذ العملية"
            30-> "فشلت العملية"
            31-> "ارجوك انتظر ..."
            32-> "لالغاء"
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
            14-> "What is the type of waste?"
            15-> "Rubble"
            16-> "Vegetal"
            17-> "Household Waste"
            18-> "Other"
            19-> "What is the nature of the waste?"
            20-> "Wild discharge"
            21-> "Pickup fault"
            22-> "How often does the garbage truck pass through your neighborhood, per week?"
            23-> "send"
            24-> "Geolocation success"
            25-> "Please select a photo"
            26-> "Please select geolocation"
            27-> "Please check your internet connection"
            28-> "Sorry, something unexpected went wrong. Please make sure you have a reliable network connection and try again"
            29-> "Operation performed"
            30-> "Operation failed"
            31-> "Please wait ..."
            32-> "Cancel"
            else-> "Validate"
        }
    }

    override fun onBackPressed() { startActivity(Intent(this, M1Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
}
