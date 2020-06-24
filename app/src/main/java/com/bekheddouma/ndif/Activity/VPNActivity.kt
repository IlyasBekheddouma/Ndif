package com.bekheddouma.ndif.Activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1
import com.bekheddouma.ndif.Utils.Ext1.Companion.chekShifre
import com.bekheddouma.ndif.Utils.Ext1.Companion.isNetworkAvailable
import com.bekheddouma.ndif.Utils.SharedPreferences
import kotlinx.android.synthetic.main.v_p_n_layout.*

class VPNActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v_p_n_layout)
        verif()

        AYIV2.setOnClickListener { onBackPressed() }

        AYB1.setOnClickListener {
            AYB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(isNetworkAvailable()) {

                if(chekShifre(AYET1)) startActivity( Intent(this, VCActivity::class.java).apply { putExtra("NUMBERALG", "+213"+(""+AYET1.text).substring(1)).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) } ) else {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle( TEXT_PLUS(8))
                    builder.setMessage( TEXT_PLUS(9) )
                    builder.setNegativeButton( TEXT_PLUS(7) ) { _,_ -> }
                    builder.create().show()
                }

            } else dialog(5,6,7)
        }
    }

    fun verif() {
        AYLL3.visibility=View.GONE
        AYLL4.visibility=View.GONE

        if(SharedPreferences(this).get_22_lang()!=0) {
            AYTV1.text=TEXT_PLUS(1)
            AYB1.text=TEXT_PLUS(2)
            AYTV3.text=TEXT_PLUS(4)
            AYTV2.text=TEXT_PLUS(3)
            AYET1.hint=TEXT_PLUS(3)
        }

        AYET1.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {

                if(!s!!.isEmpty()) {
                    AYLL3.visibility=View.VISIBLE
                    AYLL4.visibility=if(s[0]=='0') View.GONE else View.VISIBLE
                } else {
                    AYLL3.visibility=View.GONE
                    AYLL4.visibility=View.GONE
                }
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })
    }

    fun dialog(i:Int,j:Int,k:Int) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle( TEXT_PLUS(i) )
        builder.setMessage( TEXT_PLUS(j) )
        builder.setNegativeButton( TEXT_PLUS(k) ) { _, _ -> }
        builder.create().show()
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0-> when (i) {
            1-> "Vérification de votre numéro !"
            2-> "Suivant"
            3-> "Numéro de téléphone"
            4-> "Votre numéro doit commencer par 0"
            5-> "ERREUR DE CONNEXION"
            6-> "S'il vous plait, vérifiez votre connexion internet"
            7-> "OK"
            8-> "Remarque"
            else-> "Veuillez saisir un numéro de téléphone valide"
        } 1 -> when (i) {
            1-> "التحقق من رقمك !"
            2-> "التالى"
            3-> "رقم الهاتف"
            4-> "يرجى إدخال رقم هاتف صحيح"
            5-> "خطأ في الاتصال"
            6-> "الرجاء التحقق من اتصال الانترنت الخاص بك"
            7-> "حسنا"
            8-> "ملحوظة"
            else-> "يرجى إدخال رقم هاتف صحيح"
        } else -> when (i) {
            1-> "Checking your number!"
            2-> "Next"
            3-> "Phone number"
            4-> "Your number must start with 0"
            5-> "CONNEXION ERROR"
            6-> "Please check your internet connection"
            7-> "OK"
            8-> "Note"
            else-> "Please enter a valid phone number"
        }
    }

    override fun onBackPressed() { startActivity(Intent(this, ITActivity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
}
