/**
 *
 */
package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.SalesOrderDetailAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDERSTATUS_COMPANY
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDERSTATUS_DATE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDERSTATUS_NO
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDERSTATUS_PEN_QTY
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDERSTATUS_PRDCT_NAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDERSTATUS_QTY
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ORDER_PRDCT_DESC
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel
import org.json.JSONObject
import java.util.*

/**
 * @author Vandana Surendranath
 */
class SalesOrderDetailActivity : AppCompatActivity(), VKCUrlConstants {
    private var mLstView: ListView? = null
    private var mOrdrNumbr: TextView? = null
    private var mDate: TextView? = null
    private var extras: Bundle? = null
    private var salesRepOrderModels: ArrayList<SalesRepOrderModel>? = null
    private var isError = false
    private var mView: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_salesorderstatus)
        initUi()
        setActionBar()
        extras = intent.extras
        if (extras != null) {
            mOrdrNumbr!!.text = "Order number : " + extras!!.getString("ordr_no")
            mDate!!.text = "Order date : " + extras!!.getString("ordr_date")
        }
        getSalesOrderDetails(extras!!.getString("ordr_no")!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi() {
        mLstView = findViewById<View>(R.id.salesOrderList) as ListView
        mOrdrNumbr = findViewById<View>(R.id.txtViewOrder) as TextView
        mOrdrNumbr!!.visibility = View.VISIBLE
        mDate = findViewById<View>(R.id.txtViewDate) as TextView
        mDate!!.visibility = View.VISIBLE
        mView = findViewById(R.id.view)
        mView?.setVisibility(View.VISIBLE)
        salesRepOrderModels = ArrayList()
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun getSalesOrderDetails(ordrNo: String) {
        salesRepOrderModels!!.clear()
        val name = arrayOf("OrderNo")
        val values = arrayOf(ordrNo)
        val manager = VKCInternetManager(
            VKCUrlConstants.PRODUCT_SALESORDER_DETAILS
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
                if (salesRepOrderModels!!.size > 0 && !isError) {
                    setAdapter()
                } else {
                    showtoast(this@SalesOrderDetailActivity, 17)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                showtoast(this@SalesOrderDetailActivity, 17)
                isError = true
            }
        })
    }

    private fun setAdapter() {
        mLstView!!.adapter = SalesOrderDetailAdapter(
            salesRepOrderModels!!,
            this
        )
    }

    private fun parseResponse(response: String) {
        try {
            val jsonObjectresponse = JSONObject(response)
            val jsonArrayresponse = jsonObjectresponse
                .getJSONArray(JSON_TAG_SETTINGS_RESPONSE)
            for (j in 0 until jsonArrayresponse.length()) {
                val salesRepOrderModel = SalesRepOrderModel()
                val jsonObjectZero = jsonArrayresponse.getJSONObject(j)
                salesRepOrderModel.setmOrderNo(
                    jsonObjectZero
                        .optString(JSON_ORDERSTATUS_NO)
                )
                salesRepOrderModel.setmOrderQty(
                    jsonObjectZero
                        .optString(JSON_ORDERSTATUS_QTY)
                )
                salesRepOrderModel.setmPendingQty(
                    jsonObjectZero
                        .optString(JSON_ORDERSTATUS_PEN_QTY)
                )
                salesRepOrderModel.setmOrderDate(
                    jsonObjectZero
                        .optString(JSON_ORDERSTATUS_DATE)
                )
                salesRepOrderModel.setmCompany(
                    jsonObjectZero
                        .optString(JSON_ORDERSTATUS_COMPANY)
                )
                salesRepOrderModel.setmPrdctName(
                    jsonObjectZero
                        .optString(JSON_ORDERSTATUS_PRDCT_NAME)
                )
                salesRepOrderModel.setmPrdctDesc(
                    jsonObjectZero
                        .optString(JSON_ORDER_PRDCT_DESC)
                )
                salesRepOrderModels!!.add(salesRepOrderModel)
            }
        } catch (e: Exception) {
            isError = true
        }
    }
}