package com.bekheddouma.ndif.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.SharedPreferences
import kotlinx.android.synthetic.main.lengue_layout.*

class LengueActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lengue_layout)

        AERG1.setOnCheckedChangeListener { _, _ ->
            AETV1.text=TEXT_PLUS(1)
            AEB1.text=TEXT_PLUS(2)
        }

        AEB1.setOnClickListener {
            AEB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            SharedPreferences(this).set_22_lang(if(AERB2.isChecked) 0 else if(AERB3.isChecked) 1 else 2)
            startActivity(Intent(this, ITActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }

    fun TEXT_PLUS(i:Int)=when( if(AERB2.isChecked) 0 else if(AERB3.isChecked) 1 else 2 ) {
        0-> when (i) {
            1-> "Veuillez sélectionner une langue"
            2-> "Suivant"
            else-> "Veuillez cliquer de nouveau sur Retour pour quitter"
        } 1 -> when (i) {
            1-> "يرجى تحديد لغة"
            2-> "التالى"
            else-> "الرجاء النقر فوق السابق مرة أخرى للخروج"
        } else-> when (i) {
            1-> "Please select a language"
            2-> "Next"
            else-> "Please click Back again to exit"
        }
    }

    override fun onBackPressed() {

        if(doubleBackToExitPressedOnce) moveTaskToBack(true) else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this,TEXT_PLUS(3), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}
