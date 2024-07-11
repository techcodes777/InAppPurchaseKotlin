package com.eclatsol.inapppurchasekotlin

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.eclatsol.inapppurchasekotlin.adapter.SubscriptionAdapter
import com.eclatsol.inapppurchasekotlin.purchase.Preference
import com.eclatsol.inapppurchasekotlin.purchase.PurchaseClass
import com.eclatsol.inapppurchasekotlin.purchase.PurchaseClass.onPurchaseInitialization

class SubscriptionActivity : AppCompatActivity(), SubscriptionAdapter.PurChaseItemClick {
    private lateinit var recyclerViewPurchase: RecyclerView
    private lateinit var subscriptionAdapter: SubscriptionAdapter
    private lateinit var btnSubscription: Button

    var position = 0
    lateinit var tvNoDataFound: TextView

    lateinit var permission: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)

        Preference.getInstance()!!.init(this)
        onPurchaseInitialization(this)

        recyclerViewPurchase = findViewById(R.id.recyclerViewPurchase)
        btnSubscription = findViewById(R.id.btnSubscription)
        tvNoDataFound = findViewById(R.id.tvNoDataFound)
        subscriptionAdapter = SubscriptionAdapter(this, PurchaseClass.productDetailsList, this)
        recyclerViewPurchase.layoutManager = LinearLayoutManager(this)
        recyclerViewPurchase.adapter = subscriptionAdapter


        btnSubscription.setOnClickListener(View.OnClickListener {
            if (position > 0) {
                PurchaseClass.onClickRemoveAds(position)
            } else {
                Log.e("main", "onClick: position in empty")
                Toast.makeText(
                    this@SubscriptionActivity,
                    "Please select subscription",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if (PurchaseClass.productDetailsList.size > 0) {
            recyclerViewPurchase!!.visibility = View.VISIBLE
            tvNoDataFound!!.visibility = View.GONE
        } else {
            recyclerViewPurchase!!.visibility = View.INVISIBLE
            tvNoDataFound!!.visibility = View.VISIBLE
        }
    }

    override fun position(pos: Int) {
        position = pos
    }
}