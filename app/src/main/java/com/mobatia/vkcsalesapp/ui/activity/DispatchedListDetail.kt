package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.DispatchedListDetailAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.model.DispatchedDetailModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class DispatchedListDetail : AppCompatActivity(), VKCUrlConstants {
    var mTextOrderNo: TextView? = null
    var mLstView: ListView? = null
    var mTranporterName: String? = null
    var mTransporterContact: String? = null
    var mOrderId: String? = null
    var mOrderDate: String? = null
    var dispatchList: ArrayList<DispatchedDetailModel>? = null
    var mActivity: Activity? = null
    private var parentOrderId: String? = null
    private var mTotalQuantity: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dealer_dispatch_detail)
        val abar = supportActionBar
        mActivity = this
        val viewActionBar = layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Order Details"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        val intent = intent
        mOrderId = intent.extras!!.getString("order_id")
        parentOrderId = intent.extras!!.getString("parent_order_id")
        initUI()
    }

    private fun initUI() {
        // TODO Auto-generated method stub
        dispatchList = ArrayList()
        mTotalQuantity = findViewById<View>(R.id.totalQty) as TextView
        mTextOrderNo = findViewById<View>(R.id.txtViewOrderDispatch) as TextView
        mTextOrderNo!!.text = "Order no. $parentOrderId"
        mLstView = findViewById<View>(R.id.dispatchOrderList) as ListView
        getSalesOrderDetails(mOrderId!!)
    }

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getSalesOrderDetails(ordrNo: String) {
        dispatchList!!.clear()
        val name = arrayOf("order_id")
        val values = arrayOf(ordrNo)
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBDEALER_NEW_ORDER_DETAILS
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                //VKCUtils?.showtoast(DispatchedListDetail.this, 17);
                // isError = true;
            }
        })
    }

    private fun parseResponse(result: String) {
        try {
            var arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val response = jsonObjectresponse.getJSONObject("response")
            val status = response.getString("status")
            if (status == "Success") {
                arrayOrders = response.optJSONArray("orderdetails")
                for (i in 0 until arrayOrders.length()) {
                    val obj = arrayOrders.getJSONObject(i)
                    val orderModel = DispatchedDetailModel()
                    orderModel.caseId = obj.getString("case_id")
                    orderModel.colorId = obj.getString("color_id")
                    orderModel.color_name = obj.getString("color_name")
                    // orderModel.setCost(obj.getString("cost"));
                    // orderModel.setDescription(obj.getString("description"));
                    // orderModel.setName(obj.getString("name"));
                    orderModel.orderDate = obj.getString("order_date")
                    orderModel.productId = obj.getString("product_id")
                    orderModel.quantity = obj.getString("quantity")
                    // orderModel.setSapId(obj.getString("sap_id"));
                    orderModel.caseDetail = obj.getString("caseName")
                    orderModel.detailid = obj.getString("detailid")
                    orderModel.color_code = obj.getString("color_code")
                    orderModel.approved_qty = obj
                        .getString("approved_quantity")
                    dispatchList!!.add(orderModel)
                    /*
					 * sap_id product_id
					 * 
					 * cost description
					 */
                }

                /*
				 * mLstView.setAdapter(new SubDealerOrderDetailsAdapter(
				 * SubDealerOrderDetails.this,
				 * AppController.subDealerOrderDetailList));
				 */
                // getOrderData();
                val mSalesAdapter = DispatchedListDetailAdapter(
                    mActivity!!, dispatchList!!
                )
                mLstView!!.adapter = mSalesAdapter
                var mTotalQty = 0
                for (j in dispatchList!!
                    .indices) {
                    mTotalQty = (mTotalQty
                            + dispatchList!!
                        .get(j).quantity.toInt())
                }
                mTotalQuantity!!.text = ("Total Qty : "
                        + mTotalQty.toString())
            }
        } catch (e: Exception) {
            // isError = true;
        }
    }
}