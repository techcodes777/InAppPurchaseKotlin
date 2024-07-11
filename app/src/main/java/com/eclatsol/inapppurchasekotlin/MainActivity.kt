package com.eclatsol.inapppurchasekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.eclatsol.inapppurchasekotlin.purchase.Preference

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        Preference.setString("data","Krishna")

        Log.e("sfdfsdfdf", "onCreate: ${Preference.getString("data")}")
    }
}