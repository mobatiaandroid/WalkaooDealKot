package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.PendingOrderAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.Pending_Order_Model
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SubDealerOrderDetails() : AppCompatActivity(), VKCUrlConstants, VKCDbConstants,
    View.OnClickListener {
    private var mLstView: ListView? = null
    private var mOrdrNumbr: TextView? = null
    private var mDate: TextView? = null
    private var mTotalQuantity: TextView? = null
    private var extras: Bundle? = null
    private var isError = false
    private var mView: View? = null
    var status = 1
    var orderNumber: String =""
    var flag: String? = null
    var listType: String? = null
    var mStatus: String? = null
    var parentOrderId: String? = null
    var btnApprove: Button? = null
    var btnReject: Button? = null
    var toast: CustomToast? = null
    var position = 0
    var mTotalQty = 0
    var listPendingOrder: ArrayList<Pending_Order_Model>? = null

    // LinearLayout llPending,llApproved;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.subdealer_order_details)
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
            orderNumber = extras!!.getString("ordr_no")!!
            parentOrderId = extras!!.getString("parent_order_id")
            flag = extras!!.getString("flag")
            mStatus = extras!!.getString("status")
            listType = extras!!.getString("listtype")
            if ((mStatus == "0")) {
                mOrdrNumbr!!.text = "Order number : $orderNumber"
            } else {
                mOrdrNumbr!!.text = "Order number : $parentOrderId"
            }
            if ((flag == "pending") || (flag == "0")) {
                btnApprove!!.visibility = View.VISIBLE
                btnReject!!.visibility = View.VISIBLE
                AppController.isEditable = true
            } else {
                btnApprove!!.visibility = View.GONE
                btnReject!!.visibility = View.GONE
                AppController.isEditable = false
            }
            position = extras!!.getInt("position")
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
        return (super.onOptionsItemSelected(item))
    }

    private fun initUi() {
        listPendingOrder = ArrayList()
        toast = CustomToast(this@SubDealerOrderDetails)
        mLstView = findViewById<View>(R.id.salesOrderList) as ListView
        mOrdrNumbr = findViewById<View>(R.id.txtViewOrder) as TextView
        mOrdrNumbr!!.visibility = View.VISIBLE
        mDate = findViewById<View>(R.id.txtViewDate) as TextView
        mTotalQuantity = findViewById<View>(R.id.totalQty) as TextView
        mView = findViewById(R.id.view)
        mView?.setVisibility(View.VISIBLE)
        btnApprove = findViewById<View>(R.id.btnApprove) as Button
        btnReject = findViewById<View>(R.id.btnReject) as Button
        btnApprove!!.setOnClickListener(this)
        btnReject!!.setOnClickListener(this)
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
        listPendingOrder!!.clear()
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
                showtoast(this@SubDealerOrderDetails, 17)
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
            if ((status == "Success")) {
                arrayOrders = response.optJSONArray("orderdetails")
                for (i in 0 until arrayOrders.length()) {
                    val obj = arrayOrders.getJSONObject(i)
                    val orderModel = Pending_Order_Model()
                    orderModel.caseId = obj.getString("case_id")
                    orderModel.colorId = obj.getString("color_id")
                    orderModel.color_name = obj.getString("color_name")
                    orderModel.orderDate = obj.getString("order_date")
                    orderModel.productId = obj.getString("product_id")
                    orderModel.quantity = obj.getString("quantity")
                    orderModel.ordered_quantity = obj
                        .getString("order_quantity")
                    orderModel.caseDetail = obj.getString("caseName")
                    orderModel.detailid = obj.getString("detailid")
                    orderModel.color_code = obj.getString("color_code")
                    orderModel.approved_qty = obj
                        .getString("approved_quantity")
                    listPendingOrder!!.add(orderModel)
                }
                val mSalesAdapter = PendingOrderAdapter(
                    this@SubDealerOrderDetails, (listPendingOrder)!!
                )
                mLstView!!.adapter = mSalesAdapter
                var mTotalQty = 0
                for (j in listPendingOrder!!.indices) {
                    mTotalQty = (mTotalQty
                            + listPendingOrder!![j]
                        .quantity.toInt())
                }
                mTotalQuantity!!.text = ("Total Qty : "
                        + mTotalQty.toString())
            }
        } catch (e: Exception) {
            isError = true
        }
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        when (v.id) {
            R.id.btnApprove -> {
                if (AppController.status != 2) // Status is used to set the status
                { // of order
                    AppController.status = 1
                }
                if (AppController.listErrors.size == 0) {
                    showConfirmDialog()
                } else {
                    val toast = CustomToast(this)
                    toast.show(30)
                }
            }
            R.id.btnReject -> {
                AppController.status = 3
                showConfirmDialog()
            }
            R.id.btnDispatch -> {
                AppController.status = 4
                showConfirmDialog()
            }
        }
    }

    private fun updateOrder() {
        val detail: String
        if (AppController.status == 2) {
            detail = createJson().toString()
        } else {
            detail = ""
        }
        val name = arrayOf("order_id", "status", "orderDetails")
        val values = arrayOf(
            orderNumber, AppController.status.toString(),
            detail
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.SET_ORDER_STATUS_API
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponseAfterUpdate(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                showtoast(this@SubDealerOrderDetails, 17)
                isError = true
            }
        })
    }

    private fun parseResponseAfterUpdate(result: String) {
        try {
            val arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val response = jsonObjectresponse.getJSONObject("response")
            val status = response.getString("status")
            if ((status == "Success")) {
                val toast = CustomToast(this)
                toast.show(28)
                AppController.subDealerModels.clear()
                AppController.TempSubDealerOrderDetailList.clear()
                AppController.subDealerOrderDetailList.clear()
                finish()
            } else {
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            isError = true
        }
    }

    private fun createJson(): JSONObject {
        println("18022015:Within createJson ")
        val jsonObject = JSONObject()
        try {
            jsonObject.putOpt("order_id", orderNumber)
            jsonObject.putOpt("status", 2)
        } catch (e1: JSONException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        val jsonArray = JSONArray()
        for (i in AppController.TempSubDealerOrderDetailList.indices) {
            val `object` = JSONObject()
            try {
                `object`.putOpt(
                    "order_detail_id",
                    AppController.TempSubDealerOrderDetailList[i]
                        .detailid
                )
                `object`.putOpt(
                    "product_id",
                    AppController.TempSubDealerOrderDetailList[i]
                        .productId
                )
                `object`.putOpt(
                    "new_quantity",
                    AppController.TempSubDealerOrderDetailList[i]
                        .quantity
                )

                // object.putOpt("grid_value",cartArrayList.get(i).getProdGridValue());
                jsonArray.put(i, `object`)
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
        }
        try {
            jsonObject.put("order_details", jsonArray)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return jsonObject
    }

    private fun showConfirmDialog() {
        val appExitDialog: Confirmation_Dialog = Confirmation_Dialog(this)
        appExitDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        appExitDialog.setCancelable(true)
        appExitDialog.show()
    }

    inner class Confirmation_Dialog(context: Context?) : Dialog(
        (context)!!
    ) {
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.confirm_dialog)
            init()
        }

        private fun init() {
            val buttonSet = findViewById<View>(R.id.buttonYes) as Button
            buttonSet.setOnClickListener(View.OnClickListener {
                dismiss()
                updateOrder()
            } // alrtDbldr.cancel();
            )
            val buttonCancel = findViewById<View>(R.id.buttonNo) as Button
            buttonCancel.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    dismiss()
                }
            })
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