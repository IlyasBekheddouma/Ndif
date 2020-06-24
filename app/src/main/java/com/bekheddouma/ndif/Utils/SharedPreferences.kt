package com.bekheddouma.ndif.Utils

import android.content.Context

class SharedPreferences(context:Context) {
    /*
    Toast.makeText(applicationContext, "HHHH  HH:mm:ss  ", Toast.LENGTH_SHORT).show()
    splash_screen_layout AZ
    lengue_layout AE
    i_t_layout AR
    sign_up_layout AT
    v_p_n_layout AY
    v_c_layout AU
    m1_layout AI
    m2_layout AO
    m3_layout AP
    m4_layout AQ
    */
    var idc="idc"
    var lang="lang"
    var genr="genr"
    var job="job"
    var wdr="wdr"
    var cdr="cdr"
    var ddn="ddn"

    var pre1=context.getSharedPreferences(idc, Context.MODE_PRIVATE)
    var pre2=context.getSharedPreferences(lang,Context.MODE_PRIVATE)
    var pre3=context.getSharedPreferences(genr,Context.MODE_PRIVATE)
    var pre4=context.getSharedPreferences(job,Context.MODE_PRIVATE)
    var pre5=context.getSharedPreferences(wdr,Context.MODE_PRIVATE)
    var pre6=context.getSharedPreferences(cdr,Context.MODE_PRIVATE)
    var pre7=context.getSharedPreferences(ddn,Context.MODE_PRIVATE)

    fun get_22_idc() = pre1.getString(idc,"")

    fun set_22_idc(value:String) { pre1.edit().putString(idc,value).apply() }

    fun get_22_lang() = pre2.getInt(lang,-1)

    fun set_22_lang(value:Int) { pre2.edit().putInt(lang,value).apply() }

    fun get_22_genr() = pre3.getBoolean(genr,true)

    fun set_22_genr(value:Boolean) { pre3.edit().putBoolean(genr,value).apply() }

    fun get_22_job():Int = pre4.getInt(job,-1)

    fun set_22_job(value:Int) { pre4.edit().putInt(job,value).apply() }

    fun get_22_wdr() = pre5.getInt(wdr,-1)

    fun set_22_wdr(value:Int) { pre5.edit().putInt(wdr,value).apply() }

    fun get_22_cdr() = pre6.getString(cdr,"")

    fun set_22_cdr(value:String) { pre6.edit().putString(cdr,value).apply() }

    fun get_22_ddn() = pre7.getString(ddn,"")

    fun set_22_ddn(value:String) { pre7.edit().putString(ddn,value).apply() }
}