package com.eclatsol.inapppurchasekotlin.purchase

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

object Preference {
    @Volatile
    private var mInstance: Preference? = null
    private var appSharedPrefs: SharedPreferences? = null
    private var prefsEditor: SharedPreferences.Editor? = null

    fun Pref(context: Context) {
        appSharedPrefs = context.getSharedPreferences("inapp_purchase", Activity.MODE_PRIVATE)
        prefsEditor = appSharedPrefs?.edit()
    }

    fun Pref() {}

    fun getInstance(): Preference? {
        if (null == mInstance) {
            synchronized(Preference::class.java) {
                if (null == mInstance) {
                    mInstance = Preference
                }
            }
        }
        return mInstance
    }

    fun init(context: Context?) {
        if (context == null) {
            appSharedPrefs =
                context?.getSharedPreferences("inapp_purchase", Activity.MODE_PRIVATE)
            prefsEditor = appSharedPrefs?.edit()
        }
        if (appSharedPrefs == null) {
            appSharedPrefs = context?.getSharedPreferences("inapp_purchase", Activity.MODE_PRIVATE)
            prefsEditor = appSharedPrefs?.edit()
        }
    }

    fun checkPreferenceSet(key_value: String?): Boolean {
        return appSharedPrefs!!.contains(key_value)
    }

    fun getBool(key_value: String?, default_value: Boolean): Boolean {
        return appSharedPrefs!!.getBoolean(key_value, default_value)
    }

    fun setBool(key_value: String?, default_value: Boolean) {
        prefsEditor!!.putBoolean(key_value, default_value).commit()
    }

    fun getInt(key_value: String?): Int {
        return appSharedPrefs!!.getInt(key_value, 0)
    }

    fun setInt(key_value: String?, default_value: Int) {
        prefsEditor!!.putInt(key_value, default_value).commit()
    }

    fun getString(key_value: String?, default_value: String?): String? {
        return appSharedPrefs!!.getString(key_value, default_value)
    }

    fun getString(key_value: String?): String? {
        return appSharedPrefs!!.getString(key_value, "")
    }

    fun setString(key_value: String?, default_value: String?) {
        prefsEditor!!.putString(key_value, default_value).commit()
    }

    fun getLong(key_value: String?): Long {
        return appSharedPrefs!!.getLong(key_value, -1)
    }

    fun setLong(key_value: String?, default_value: Long?) {
        prefsEditor!!.putLong(key_value, default_value!!).commit()
    }
}