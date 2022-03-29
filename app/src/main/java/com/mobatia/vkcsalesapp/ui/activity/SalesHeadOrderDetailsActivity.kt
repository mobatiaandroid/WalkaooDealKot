package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.SalesHeadOrderDetailAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel
import org.json.JSONArray
import org.json.JSONObject

class SalesHeadOrderDetailsActivity : AppCompatActivity(), VKCUrlConstants, VKCDbConstants {
    private var mLstView: ListView? = null
    private var mOrdrNumbr: TextView? = null
    private var mDate: TextView? = null
    private var mTotalQuantity: TextView? = null
    private var extras: Bundle? = null
    var llAppRej: LinearLayout? = null
    var llDispatch: LinearLayout? = null
    private var isError = false
    private var mView: View? = null
    var status = 1
    var orderNumber: String? = null
    var toast: CustomToast? = null
    var position = 0
    var mTotalQty = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sales_head_order_detail)
        AppController.listErrors.clear()
        initUi()
        val abar = supportActionBar
        val viewActionBar = layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Orders Details"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        extras = intent.extras
        if (extras != null) {
            orderNumber = extras!!.getString("orderNumber")
            position = extras!!.getInt("position")
            mOrdrNumbr!!.text = "Order number : $orderNumber"
        }
        mTotalQty = 0
        getSalesOrderDetails(orderNumber!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> {
                mTotalQty = 0
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi() {
        llAppRej = findViewById<View>(R.id.llAppOrRej) as LinearLayout
        llDispatch = findViewById<View>(R.id.llDispatch) as LinearLayout
        toast = CustomToast(this@SalesHeadOrderDetailsActivity)
        mLstView = findViewById<View>(R.id.salesOrderList) as ListView
        mOrdrNumbr = findViewById<View>(R.id.txtViewOrder) as TextView
        mOrdrNumbr!!.visibility = View.VISIBLE
        mDate = findViewById<View>(R.id.txtViewDate) as TextView
        mTotalQuantity = findViewById<View>(R.id.totalQty) as TextView
        // mDate.setVisibility(View.VISIBLE);
        mView = findViewById(R.id.view)
        mView?.setVisibility(View.VISIBLE)
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
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
        AppController.TempSubDealerOrderDetailList.clear()
        AppController.subDealerOrderDetailList.clear()
        val name = arrayOf("order_id")
        val values = arrayOf(ordrNo)
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBDEALER_ORDER_DETAILS
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                showtoast(this@SalesHeadOrderDetailsActivity, 17)
                isError = true
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
                    val orderModel = SubDealerOrderDetailModel()
                    orderModel.caseId = obj.getString("case_id")
                    orderModel.colorId = obj.getString("color_id")
                    orderModel.orderDate = obj.getString("order_date")
                    orderModel.productId = obj.getString("product_id")
                    orderModel.quantity = obj.getString("quantity")
                    orderModel.caseDetail = obj.getString("caseName")
                    orderModel.detailid = obj.getString("detailid")
                    orderModel.color_code = obj.getString("color_code")
                    orderModel.color_name = obj.getString("color_name")
                    AppController.subDealerOrderDetailList.add(orderModel)
                    AppController.TempSubDealerOrderDetailList.add(orderModel)
                }
                val mSalesAdapter = SalesHeadOrderDetailAdapter(
                    this@SalesHeadOrderDetailsActivity
                )
                mLstView!!.adapter = mSalesAdapter
                mTotalQty = 0
                for (j in AppController.TempSubDealerOrderDetailList
                    .indices) {
                    mTotalQty = (mTotalQty
                            + AppController.TempSubDealerOrderDetailList[j].quantity.toInt())
                }
                mTotalQuantity!!.text = mTotalQty.toString()
            }
        } catch (e: Exception) {
            isError = true
        }
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    override fun onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed()
        mTotalQty = 0
    }
}