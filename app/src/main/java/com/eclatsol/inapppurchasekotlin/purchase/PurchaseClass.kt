package com.eclatsol.inapppurchasekotlin.purchase

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingFlowParams.ProductDetailsParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.eclatsol.inapppurchasekotlin.RestartAppActivity
import com.google.common.collect.ImmutableList

object PurchaseClass {

    var activityMain: Activity? = null
    var billingClient: BillingClient? = null
    var productDetailsList: MutableList<ProductDetails> = ArrayList()
    var purchasesUpdatedListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            // To be implemented in a later section.
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK
                && purchases != null
            ) {
                for (purchase in purchases) {
                    handlePurchase(purchase)
                }
            } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
                // Handle an error caused by a user cancelling the purchase flow.
            } else {
                // Handle any other error codes.
            }
        }

    fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder().setPurchaseToken(purchase.purchaseToken)
                        .build()
                billingClient!!.acknowledgePurchase(
                    acknowledgePurchaseParams
                ) { billingResult: BillingResult ->
                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
//                        Preferences.setPurchase(true);
                        Preference.getInstance()?.setString(Constant.PURCHASE_DONE, "done")
                        val intent = Intent(activityMain, RestartAppActivity::class.java)
                        activityMain!!.startActivity(intent)
                        activityMain!!.finish()
                    }
                }
            }
        }
    }

    fun onPurchaseInitialization(activity: Activity?) {
        activityMain = activity
        billingClient = BillingClient.newBuilder(activity!!)
            .setListener(purchasesUpdatedListener)
            .enablePendingPurchases()
            .build()
        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    showProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun showProducts() {
        val productList = ImmutableList.of( //Product 1
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("week")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("month")
                .setProductType(BillingClient.ProductType.SUBS)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("year")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        billingClient!!.queryProductDetailsAsync(
            params
        ) { billingResult, list ->
            if (list.size > 0) {
                productDetailsList.addAll(list)
                Preference.getInstance()!!.setString(Constant.TITLE, productDetailsList[0].title)
                Preference.getInstance()!!.setString(
                    Constant.PRICE,
                    productDetailsList[0].subscriptionOfferDetails!![0].pricingPhases.pricingPhaseList[0].formattedPrice
                )
                Log.e(
                    "title",
                    "onProductDetailsResponse: " + Preference.getInstance()!!.getString(Constant.TITLE)
                )
                Log.e(
                    "price",
                    "onProductDetailsResponse: " + Preference.getInstance()!!.getString(Constant.PRICE)
                )
            }
        }
    }

    fun onClickRemoveAds(position: Int) {
        val productDetails = productDetailsList[position]
        assert(productDetails.subscriptionOfferDetails != null)
        val productDetailsParamsList = ImmutableList.of(
            ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .setOfferToken(productDetails.subscriptionOfferDetails!![0].offerToken)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClient!!.launchBillingFlow(activityMain!!, billingFlowParams)
    }
}