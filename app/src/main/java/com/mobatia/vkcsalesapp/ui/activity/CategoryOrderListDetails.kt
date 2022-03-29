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
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper
import com.mobatia.vkcsalesapp.adapter.SubDealerOrderAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.DataBaseManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class CategoryOrderListDetails() : AppCompatActivity(), VKCUrlConstants, VKCDbConstants,
    View.OnClickListener {
    private var mLstView: ListView? = null
    private var mOrdrNumbr: TextView? = null
    private var mDate: TextView? = null
    private var extras: Bundle? = null
    var dataBase: DatabaseHelper? = null
    var llAppRej: LinearLayout? = null
    var llDispatch: LinearLayout? = null
    private var isError = false
    private var mView: View? = null
    var status = 1
    var orderNumber: String = ""
    var flag: String? = null
    var listType: String? = null
    var databaseManager: DataBaseManager? = null
    var btnApprove: Button? = null
    var btnReject: Button? = null
    var btnDispatch: Button? = null
    var toast: CustomToast? = null
    var position = 0
    private var mTotalQuantity: TextView? = null
    var mTotalQty = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_detail_layout)
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
            flag = extras!!.getString("flag")
            listType = extras!!.getString("listtype")
            if ((flag == "pending")) {
                llAppRej!!.visibility = View.GONE
                llDispatch!!.visibility = View.GONE
                AppController.isEditable = false
            } else if ((flag == "approved")) {
                llAppRej!!.visibility = View.GONE
                llDispatch!!.visibility = View.GONE
                AppController.isEditable = false
            } else if ((flag == "dispatched")) {
                llAppRej!!.visibility = View.GONE
                llDispatch!!.visibility = View.GONE
                AppController.isEditable = false
            }
            position = extras!!.getInt("position")
            mOrdrNumbr!!.text = "Order number : $orderNumber"
        }
        orderNumber?.let { getSalesOrderDetails(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return (super.onOptionsItemSelected(item))
    }

    private fun initUi() {
        llAppRej = findViewById<View>(R.id.llAppOrRej) as LinearLayout
        llDispatch = findViewById<View>(R.id.llDispatch) as LinearLayout
        toast = CustomToast(this@CategoryOrderListDetails)
        mLstView = findViewById<View>(R.id.salesOrderList) as ListView
        mOrdrNumbr = findViewById<View>(R.id.txtViewOrder) as TextView
        mOrdrNumbr!!.visibility = View.VISIBLE
        mDate = findViewById<View>(R.id.txtViewDate) as TextView
        mTotalQuantity = findViewById<View>(R.id.totalQty) as TextView
        mView = findViewById(R.id.view)
        mView?.setVisibility(View.VISIBLE)
        btnApprove = findViewById<View>(R.id.btnApprove) as Button
        btnReject = findViewById<View>(R.id.btnReject) as Button
        btnDispatch = findViewById<View>(R.id.btnDispatch) as Button
        btnApprove!!.setOnClickListener(this)
        btnReject!!.setOnClickListener(this)
        btnDispatch!!.setOnClickListener(this)
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
                showtoast(this@CategoryOrderListDetails, 17)
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
                    val orderModel = SubDealerOrderDetailModel()
                    orderModel.caseId = obj.getString("case_id")
                    orderModel.colorId = obj.getString("color_id")
                    orderModel.cost = obj.getString("cost")
                    orderModel.description = obj.getString("description")
                    orderModel.name = obj.getString("name")
                    orderModel.orderDate = obj.getString("order_date")
                    orderModel.productId = obj.getString("product_id")
                    orderModel.quantity = obj.getString("quantity")
                    orderModel.sapId = obj.getString("sap_id")
                    orderModel.caseDetail = obj.getString("caseName")
                    orderModel.detailid = obj.getString("detailid")
                    orderModel.color_code = obj.getString("color_code")
                    AppController.subDealerOrderDetailList.add(orderModel)
                    AppController.TempSubDealerOrderDetailList.add(orderModel)
                }
                val mSalesAdapter = SubDealerOrderAdapter(
                    this@CategoryOrderListDetails,
                    AppController.subDealerOrderDetailList
                )
                mLstView!!.adapter = mSalesAdapter
                for (j in AppController.subDealerOrderDetailList
                    .indices) {
                    mTotalQty = (mTotalQty
                            + AppController.subDealerOrderDetailList[j].quantity.toInt())
                }
                mTotalQuantity!!.text = mTotalQty.toString()
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
                showtoast(this@CategoryOrderListDetails, 17)
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
}