package com.bekheddouma.ndif.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1.Companion.commune
import com.bekheddouma.ndif.Utils.Ext1.Companion.job
import com.bekheddouma.ndif.Utils.Ext1.Companion.sex
import com.bekheddouma.ndif.Utils.Ext1.Companion.wilaya
import com.bekheddouma.ndif.Utils.SharedPreferences
import kotlinx.android.synthetic.main.sign_up_layout.*
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce=false
    var d1=-1
    var d2=-1
    var d3=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_layout)
        verif()

        ATB0.setOnClickListener {
            ATB0.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            signup()
        }

        ATB1.setOnClickListener {
            var dt= DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                var calendar = Calendar.getInstance().apply { set(selectedYear, selectedMonth, selectedDay) }
                ATET1.setText(SimpleDateFormat("dd-MM-yyyy").format(calendar.time))
                ATTV2.visibility=View.VISIBLE
            }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
            dt.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dt.setButton(DatePickerDialog.BUTTON_POSITIVE, TEXT_PLUS(9), dt)
            dt.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", dt)
            dt.show()
        }

        ATB2.setOnClickListener { open(ATET2,sex[SharedPreferences(this).get_22_lang()],ATTV3) }

        ATB3.setOnClickListener { open(ATET3,job[SharedPreferences(this).get_22_lang()],ATTV4) }

        ATB4.setOnClickListener { open(ATET4,wilaya,ATTV5) }

        ATB5.setOnClickListener {

            if(d3!=-1) open(ATET5,commune[d3],ATTV6) else {
                var builder = AlertDialog.Builder(this)
                builder.setTitle( TEXT_PLUS(10) )
                builder.setMessage( TEXT_PLUS(11) )
                builder.setNegativeButton( TEXT_PLUS(9) ) { _, _ -> }
                builder.create().show()
            }
        }
    }

    fun verif() {
        ATTV2.visibility=View.GONE
        ATTV3.visibility=View.GONE
        ATTV4.visibility=View.GONE
        ATTV5.visibility=View.GONE
        ATTV6.visibility=View.GONE

        if(SharedPreferences(this).get_22_lang()!=0) {
            ATTV1.text=TEXT_PLUS(2)
            ATB0.text=TEXT_PLUS(3)
            ATTV2.text=TEXT_PLUS(4)
            ATTV3.text=TEXT_PLUS(5)
            ATTV4.text=TEXT_PLUS(6)
            ATTV5.text=TEXT_PLUS(7)
            ATTV6.text=TEXT_PLUS(8)
            ATET1.hint=TEXT_PLUS(4)
            ATET2.hint=TEXT_PLUS(5)
            ATET3.hint=TEXT_PLUS(6)
            ATET4.hint=TEXT_PLUS(7)
            ATET5.hint=TEXT_PLUS(8)
        }
    }

    fun signup() {

        if(ATET1.text.isEmpty()||ATET2.text.isEmpty()||ATET3.text.isEmpty()||ATET4.text.isEmpty()||ATET5.text.isEmpty()) {
            var builder = AlertDialog.Builder(this)
            builder.setTitle( TEXT_PLUS(10) )
            builder.setMessage( TEXT_PLUS(12) )
            builder.setNegativeButton( TEXT_PLUS(9) ) { _, _ -> }
            builder.create().show()
        } else {
            SharedPreferences(this).set_22_ddn(""+ATET1.text)
            SharedPreferences(this).set_22_genr( d1==0 )
            SharedPreferences(this).set_22_job(d2)
            SharedPreferences(this).set_22_wdr(d3)
            SharedPreferences(this).set_22_cdr(""+ATET5.text)
            startActivity(Intent(this, M1Activity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }

    fun open(et: EditText, il:List<String>,rv:TextView) {
        var List= ListView(this)
        List.adapter= ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, il)
        var builder = AlertDialog.Builder(this)
        builder.setTitle(rv.text)
        builder.setView(List)
        var dialog=builder.create()
        dialog.show()

        List.setOnItemClickListener { _, _, position, _ ->
            et.setText(il[position])

            when(et) {
                ATET2-> d1=position
                ATET3-> d2=position
                ATET4-> {

                    if(d3!=position) {
                        d3=position
                        ATTV6.visibility=View.GONE
                        ATET5.setText("")
                    }
                }
            }
            dialog.dismiss()
            rv.visibility=View.VISIBLE
        }
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0-> when (i) {
            1-> "Veuillez cliquer de nouveau sur Retour pour quitter"
            2-> "Inscription"
            3-> "Valider"
            4-> "Date de naissance"
            5-> "Genre"
            6-> "Profession"
            7-> "Wilaya de résidence"
            8-> "Commune de résidence"
            9-> "OK"
            10-> "Remarque"
            11-> "Choisissez d'abord la wilaya"
            else-> "Remplissez tous les champs svp"
        } 1 -> when (i) {
            1-> "الرجاء النقر فوق السابق مرة أخرى للخروج"
            2-> "التسجيل"
            3-> "التحقق"
            4-> "تاريخ الميلاد"
            5-> "جنس"
            6-> "مهنة"
            7-> "ولاية الإقامة"
            8-> "بلدية سكنية"
            9-> "حسنا"
            10-> "ملحوظة"
            11-> "اختر الولاية أولاً"
            else-> "ملء جميع الحقول من فضلك"
        }  else-> when (i) {
            1-> "Please click Back again to exit"
            2-> "Registration"
            3-> "Validate"
            4-> "Date of Birth"
            5-> "Gender"
            6-> "Profession"
            7-> "Wilaya of residence"
            8-> "Residential commune"
            9-> "OK"
            10-> "Note"
            11-> "First choose the wilaya"
            else-> "Please fill in all fields"
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
