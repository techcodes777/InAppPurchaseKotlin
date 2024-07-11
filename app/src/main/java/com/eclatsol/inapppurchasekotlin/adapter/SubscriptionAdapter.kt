package com.eclatsol.inapppurchasekotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.ProductDetails
import com.eclatsol.inapppurchasekotlin.R

class SubscriptionAdapter(val context: Context?,
                          private val productDetailsList: List<ProductDetails>?,
                          private val purChaseItemClick: PurChaseItemClick?) : RecyclerView.Adapter<SubscriptionAdapter.SubViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubViewHolder {
        return SubViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_subscription, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SubViewHolder, position: Int) {
        holder.tvTitle.text = productDetailsList!![position].title
        holder.tvTitle.text =
            productDetailsList!![position].subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice
        holder.itemView.setOnClickListener { purChaseItemClick!!.position(position) }
    }

    override fun getItemCount(): Int {
        return productDetailsList!!.size
    }

    class SubViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView
        var tvPrice: TextView

        init {
            tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
            tvPrice = itemView.findViewById<TextView>(R.id.tvPrice)
        }
    }

    interface PurChaseItemClick {
        fun position(pos: Int)
    }
}