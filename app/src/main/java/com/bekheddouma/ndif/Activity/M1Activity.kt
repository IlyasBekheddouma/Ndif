package com.bekheddouma.ndif.Activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.SharedPreferences
import kotlinx.android.synthetic.main.m1_layout.*

class M1Activity : AppCompatActivity() {
    var doubleBackToExitPressedOnce=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m1_layout)
        verif()

        AIIV1.setOnClickListener { startActivity( Intent( Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.bekheddouma.ndif"))) }

        AIB1.setOnClickListener {
            AIB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            startActivity(Intent(this, M2Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        AIB2.setOnClickListener {
            AIB2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            startActivity(Intent(this, M3Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        AIB3.setOnClickListener {
            AIB3.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            startActivity(Intent(this, M4Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }

        AIB4.setOnClickListener {
            AIB4.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            startActivity( Intent( Intent.ACTION_VIEW, Uri.parse("https://and.dz/presentation/contactez-nous/")))
        }

        AILL7.setOnClickListener { startActivity(Intent(this, M2Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AILL8.setOnClickListener { startActivity(Intent(this, M3Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AILL9.setOnClickListener { startActivity(Intent(this, M4Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AILL10.setOnClickListener { startActivity( Intent( Intent.ACTION_VIEW, Uri.parse( "https://and.dz/presentation/contactez-nous/"))) }
    }

    fun verif() {

        if(SharedPreferences(this).get_22_lang()!=0) {
            AIB1.text=TEXT_PLUS(2)
            AIB2.text=TEXT_PLUS(3).replace("\n"," ")
            AIB3.text=TEXT_PLUS(4).replace("\n"," ")
            AIB4.text=TEXT_PLUS(5)
            AITV2.text=TEXT_PLUS(2)
            AITV3.text=TEXT_PLUS(3)
            AITV4.text=TEXT_PLUS(4)
            AITV5.text=TEXT_PLUS(5)
        }
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0-> when (i) {
            1-> "Veuillez cliquer de nouveau sur Retour pour quitter"
            2-> "Profil"
            3-> "Déclarer\nun déchet"
            4-> "Déclarer\nune benne"
            else-> "Contact"
        } 1 -> when (i) {
            1-> "الرجاء النقر فوق السابق مرة أخرى للخروج"
            2-> "الملف الشخصي"
            3-> "قم بإعلان\nالنفايات"
            4-> "أعلن\nدلو"
            else-> "اتصل"
        } else-> when (i) {
            1-> "Please click Back again to exit"
            2-> "Profile"
            3-> "Declare\ntrash"
            4-> "Declare\nbucket"
            else-> "Contact"
        }
    }

    override fun onBackPressed() {

        if(doubleBackToExitPressedOnce) moveTaskToBack(true) else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this,TEXT_PLUS(1), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}
