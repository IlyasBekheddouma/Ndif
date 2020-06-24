package com.bekheddouma.ndif.Activity

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bekheddouma.ndif.R
import com.bekheddouma.ndif.Utils.Ext1.Companion.isNetworkAvailable
import com.bekheddouma.ndif.Utils.SharedPreferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.i_t_layout.*

class ITActivity : AppCompatActivity() {
    var doubleBackToExitPressedOnce=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.i_t_layout)
        verif()

        ARB1.setOnClickListener {
            ARB1.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))

            if(isNetworkAvailable()) {
                var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
                startActivityForResult(GoogleSignIn.getClient(this, gso).signInIntent, 0)
            } else dialog(5,6,7)
        }

        ARB2.setOnClickListener {
            ARB2.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_but))
            startActivity(Intent(this, VPNActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
        }
    }

    fun verif() {

        if(SharedPreferences(this).get_22_lang()!=0) {
            ARTV1.text=TEXT_PLUS(1)
            ARB1.text=TEXT_PLUS(2)
            ARB2.text=TEXT_PLUS(3)
        }
    }

    fun dialog(i:Int,j:Int,k:Int) {
        var builder = AlertDialog.Builder(this)
        builder.setTitle( TEXT_PLUS(i) )
        builder.setMessage( TEXT_PLUS(j) )
        builder.setNegativeButton( TEXT_PLUS(k) ) { _, _ -> }
        builder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                var account = task.getResult(ApiException::class.java)!!

                if(account!=null) {
                    var mAuth= FirebaseAuth.getInstance();

                    mAuth.signInWithCredential(GoogleAuthProvider.getCredential(account.idToken, null)).addOnCompleteListener(this, OnCompleteListener<AuthResult?> {

                        if(it.isSuccessful) {
                            SharedPreferences(this).set_22_idc(""+account.email)
                            startActivity(Intent(this, SignUpActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION))
                        } else dialog(8,9,7)
                    })
                } else dialog(8,9,7)
            } catch (e: ApiException) { dialog(8,9,7) }
        }
    }

    fun TEXT_PLUS(i:Int)=when(SharedPreferences(this).get_22_lang()) {
        0-> when (i) {
            1-> "Veuillez choisir une méthode d'inscription"
            2-> "Email"
            3-> "Numéro de téléphone"
            4-> "Veuillez cliquer de nouveau sur Retour pour quitter"
            5-> "ERREUR DE CONNEXION"
            6-> "S'il vous plait, vérifiez votre connexion internet"
            7-> "OK"
            8-> "ERREUR INCONNUE"
            else-> "Désolé, quelque chose d'inattendu s'est mal passé. Veuillez vous assurer que vous disposez d'une connexion réseau fiable et réessayer"
        } 1 -> when (i) {
            1-> "يرجى اختيار طريقة التسجيل"
            2-> "البريد الإلكتروني"
            3-> "رقم الهاتف"
            4-> "الرجاء النقر فوق السابق مرة أخرى للخروج"
            5-> "خطأ في الاتصال"
            6-> "الرجاء التحقق من اتصال الانترنت الخاص بك"
            7-> "حسنا"
            8-> "خطأ غير معروف"
            else-> "آسف ، حدث خطأ غير متوقع. يرجى التأكد من أن لديك اتصال شبكة موثوق وحاول مرة أخرى"
        } else-> when (i) {
            1-> "Please choose a registration method"
            2-> "E-mail"
            3-> "Phone number"
            4-> "Please click Back again to exit"
            5-> "CONNEXION ERROR"
            6-> "Please check your internet connection"
            7-> "OK"
            8-> "UNKNOWN ERROR"
            else-> "Sorry, something unexpected went wrong. Please make sure you have a reliable network connection and try again"
        }
    }

    override fun onBackPressed() {

        if(doubleBackToExitPressedOnce) moveTaskToBack(true) else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(this,TEXT_PLUS(4), Toast.LENGTH_SHORT).show()
            Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
        }
    }
}
