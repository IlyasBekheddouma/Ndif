package com.bekheddouma.ndif.Activity

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1.Companion.commune
import com.bekheddouma.ndif.Utils.Ext1.Companion.job
import com.bekheddouma.ndif.Utils.Ext1.Companion.sex
import com.bekheddouma.ndif.Utils.Ext1.Companion.wilaya
import com.bekheddouma.ndif.Utils.SharedPreferences
import kotlinx.android.synthetic.main.m2_layout.*
import java.text.SimpleDateFormat
import java.util.*

class M2Activity : AppCompatActivity() {
    var update=false
    var leng=-1
    var sexy=-1
    var joba=-1
    var wili=-1
    var comi=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.m2_layout)
        verif()

        AOIV1.setOnClickListener { onBackPressed() }

        AOLL3.setOnClickListener { startActivity(Intent(this, M3Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AOLL4.setOnClickListener { startActivity(Intent(this, M4Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }

        AOLL5.setOnClickListener { startActivity( Intent( Intent.ACTION_VIEW, Uri.parse( "https://and.dz/presentation/contactez-nous/"))) }

        AOB0.setOnClickListener { verif() }

        AOB1.setOnClickListener {

            if(SharedPreferences(this).get_22_lang()!=leng||SharedPreferences(this).get_22_ddn()!=""+AOET1.text||SharedPreferences(this).get_22_genr()!=( sexy==0 ) ||SharedPreferences(this).get_22_job()!=joba||SharedPreferences(this).get_22_wdr()!=wili||SharedPreferences(this).get_22_cdr()!=comi) {
                SharedPreferences(this).set_22_lang(leng)
                SharedPreferences(this).set_22_ddn(""+AOET1.text)
                SharedPreferences(this).set_22_genr( sexy==0 )
                SharedPreferences(this).set_22_job( joba )
                SharedPreferences(this).set_22_wdr( wili )
                SharedPreferences(this).set_22_cdr( comi )
                var builder = AlertDialog.Builder(this)
                builder.setTitle( TEXT_PLUS(14) )
                builder.setMessage( TEXT_PLUS(15) )
                builder.setNegativeButton( TEXT_PLUS(13) ) { _, _ -> }
                builder.create().show()
            }
            verif()
        }

        AOB2.setOnClickListener {
            AORG1.getChildAt(0).isEnabled = true
            AORG1.getChildAt(1).isEnabled = true
            AORG1.getChildAt(2).isEnabled = true
            update=true
            AOLL8.visibility=View.VISIBLE
            AOLL9.visibility=View.GONE
        }

        AORG1.setOnCheckedChangeListener { _, _ ->
            leng=(if(AORB2.isChecked) 0 else if(AORB3.isChecked) 1 else 2)
            AOTV2.text=TEXT_PLUS(2)
            AOTV3.text=TEXT_PLUS(3)
            AOTV4.text=TEXT_PLUS(4)
            AOTV5.text=TEXT_PLUS(1)
            AOB0.text=TEXT_PLUS(5)
            AOB1.text=TEXT_PLUS(6)
            AOB2.text=TEXT_PLUS(7)
            AOTV8.text=TEXT_PLUS(8)
            AOTV9.text=TEXT_PLUS(9)
            AOTV10.text=TEXT_PLUS(10)
            AOTV11.text=TEXT_PLUS(11)
            AOTV12.text=TEXT_PLUS(12)
            AOET2.setText(sex[leng][ sexy ])
            AOET3.setText(job[leng][ joba ])
            AOTV15.text=TEXT_PLUS(16)
        }

        AOB3.setOnClickListener {

            if(update) {
                var dt= DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    var calendar = Calendar.getInstance().apply { set(selectedYear, selectedMonth, selectedDay) }
                    AOET1.setText(SimpleDateFormat("dd-MM-yyyy").format(calendar.time))
                }, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                dt.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                dt.setButton(DatePickerDialog.BUTTON_POSITIVE, TEXT_PLUS(13), dt)
                dt.setButton(DatePickerDialog.BUTTON_NEGATIVE, "", dt)
                dt.show()
            }
        }

        AOB4.setOnClickListener { if(update) open(AOET2,sex[leng],""+AOTV9.text ) }

        AOB5.setOnClickListener { if(update) open(AOET3,job[leng],""+AOTV10.text ) }

        AOB6.setOnClickListener { if(update) open(AOET4, wilaya,""+AOTV11.text ) }

        AOB7.setOnClickListener { if(update) open(AOET5, commune[wili],""+AOTV12.text ) }
    }

    fun verif() {
        leng=SharedPreferences(this).get_22_lang()
        AORG1.getChildAt(0).isEnabled = false
        AORG1.getChildAt(1).isEnabled = false
        AORG1.getChildAt(2).isEnabled = false
        update=false
        AOLL8.visibility=View.GONE
        AOLL9.visibility=View.VISIBLE
        leng=SharedPreferences(this).get_22_lang()
        sexy=if(SharedPreferences(this).get_22_genr()) 0 else 1
        joba=SharedPreferences(this).get_22_job()
        wili=SharedPreferences(this).get_22_wdr()
        comi=""+SharedPreferences(this).get_22_cdr()
        AOTV2.text=TEXT_PLUS(2)
        AOTV3.text=TEXT_PLUS(3)
        AOTV4.text=TEXT_PLUS(4)
        AOTV5.text=TEXT_PLUS(1)
        AOB0.text=TEXT_PLUS(5)
        AOB1.text=TEXT_PLUS(6)
        AOB2.text=TEXT_PLUS(7)
        AOTV8.text=TEXT_PLUS(8)
        AOTV9.text=TEXT_PLUS(9)
        AOTV10.text=TEXT_PLUS(10)
        AOTV11.text=TEXT_PLUS(11)
        AOTV12.text=TEXT_PLUS(12)
        AOET1.setText(SharedPreferences(this).get_22_ddn())
        AOET2.setText(sex[leng][ sexy ])
        AOET3.setText(job[leng][ joba ])
        AOET4.setText(wilaya[ wili ])
        AOET5.setText(comi)
        AOTV15.text=TEXT_PLUS(16)

        when(leng) {
            0-> AORB2.isChecked=true
            1-> AORB3.isChecked=true
            else-> AORB1.isChecked=true
        }
    }

    fun open(et: EditText, il:List<String>,st:String) {
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
                AOET2-> sexy=position
                AOET3-> joba=position
                AOET4-> {

                    if(wili!=position) {
                        wili=position
                        comi=commune[wili][0]
                        AOET5.setText(comi)
                    }
                }
                AOET5-> comi=commune[wili][position]
            }
            dialog.dismiss()
        }
    }

    fun TEXT_PLUS(i:Int)=when(leng) {
        0-> when (i) {
            1-> "Contact"
            2-> "Profil"
            3-> "Déclarer\nun déchet"
            4-> "Déclarer\nune benne"
            5-> "Annuler"
            6-> "Valider"
            7-> "Modifier"
            8-> "Date de naissance"
            9-> "Genre"
            10-> "Profession"
            11-> "Wilaya de résidence"
            12-> "Commune de résidence"
            13-> "OK"
            14-> "Remarque"
            15-> "Modification effectuée"
            else-> "Langue"
        } 1 -> when (i) {
            1-> "اتصل"
            2-> "الملف الشخصي"
            3-> "قم بإعلان\nالنفايات"
            4-> "أعلن\nدلو"
            5-> "لالغاء"
            6-> "التحقق"
            7-> "تعديل"
            8-> "تاريخ الميلاد"
            9-> "جنس"
            10-> "مهنة"
            11-> "ولاية الإقامة"
            12-> "بلدية سكنية"
            13-> "حسنا"
            14-> "ملحوظة"
            15-> "التغيير تم إجراؤه"
            else-> "لغة"
        } else -> when (i) {
            1-> "Contact"
            2-> "Profile"
            3-> "Declare\ntrash"
            4-> "Declare\nbucket"
            5-> "Cancel"
            6-> "Validate"
            7-> "Edit"
            8-> "Date of Birth"
            9-> "Gender"
            10-> "Profession"
            11-> "Wilaya of residence"
            12-> "Residential commune"
            13-> "OK"
            14-> "Note"
            15-> "Modification made"
            else-> "Language"
        }
    }

    override fun onBackPressed() { startActivity(Intent(this, M1Activity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
}
