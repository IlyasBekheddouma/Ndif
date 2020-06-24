package com.bekheddouma.ndif.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1.Companion.isNetworkAvailable
import com.bekheddouma.ndif.Utils.SharedPreferences
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.android.synthetic.main.v_c_layout.*
import java.util.concurrent.TimeUnit

class VCActivity : AppCompatActivity() {
    var verificationid=""
    var number=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.v_c_layout)
        verif()

        AUIV2.setOnClickListener{ onBackPressed() }

        AUB1.setOnClickListener {
            AUB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            verifyCode(""+AUET1.text)
        }
    }

    fun verif() {
        AULL3.visibility= View.GONE
        number=intent.getStringExtra("NUMBERALG")
        AUTV1.text=TEXT_PLUS(1)+"\n"+number

        if(SharedPreferences(this).get_22_lang()!=0) {
            AUB1.text=TEXT_PLUS(3)
            AUTV2.text=TEXT_PLUS(2)
            AUET1.hint=TEXT_PLUS(2)
        }

        AUET1.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) { if(!s!!.isEmpty()) AULL3.visibility=View.VISIBLE else AULL3.visibility=View.GONE }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) { }
        })

        if(isNetworkAvailable()) {

            try {

                FirebaseAuth.getInstance().setLanguageCode( when(SharedPreferences(this).get_22_lang()) {
                    0-> "fr"
                    else-> "ar"
                })

                PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onCodeSent(s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                        super.onCodeSent(s, forceResendingToken)
                        verificationid = s
                    }

                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        var code = phoneAuthCredential.smsCode

                        if(code != null) verifyCode(code)
                    }

                    override fun onVerificationFailed(e: FirebaseException) { }
                })
            } catch (ex:Exception) { }
        }
    }

    fun verifyCode(code: String) {

        try {

            FirebaseAuth.getInstance().signInWithCredential(PhoneAuthProvider.getCredential(verificationid, code)).addOnCompleteListener{ task ->

                if(task.isSuccessful) {
                    SharedPreferences(this).set_22_idc("0"+(number.substring(4)))
                    startActivity(Intent(this, SignUpActivity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                } else {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle( TEXT_PLUS(4))
                    builder.setMessage( TEXT_PLUS(5) )
                    builder.setNegativeButton( TEXT_PLUS(6) ) { _, _ -> }
                    builder.create().show()
                }
            }
        } catch (ex:Exception) { }
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0 -> when (i) {
            1-> "Entrez le code envoyé à"
            2-> "Code de vérification"
            3-> "Suivant"
            4-> "Remarque"
            5-> "Code de vérification erroné"
            else-> "OK"
        } 1 -> when (i) {
            1-> "أدخل الرمز المرسل إلى"
            2-> "رمز التحقق"
            3-> "التالى"
            4-> "ملحوظة"
            5-> "رمز التحقق غير صحيح"
            else-> "حسنا"
        } else -> when (i) {
            1-> "Enter the code sent to"
            2-> "Verification code"
            3-> "Next"
            4-> "Note"
            5-> "Wrong verification code"
            else-> "OK"
        }
    }

    override fun onBackPressed() { startActivity(Intent(this, VPNActivity::class.java ).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)) }
}
