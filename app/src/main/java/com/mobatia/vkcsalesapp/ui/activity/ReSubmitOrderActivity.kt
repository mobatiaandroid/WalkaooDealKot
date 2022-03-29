package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.OrderDetailAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.DealersShopModel
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ReSubmitOrderActivity : AppCompatActivity(), VKCUrlConstants, View.OnClickListener {
    private var extras: Bundle? = null
    var orderModel: SubDealerOrderDetailModel? = null
    var productId: TextView? = null
    var caseId: TextView? = null
    var mColour: TextView? = null
    var mDate: TextView? = null
    var mTotalQuantity: TextView? = null
    var mQuantity: EditText? = null
    var btnUpdate: Button? = null
    var status = 1
    var mTotalQty = 0
    var orderNumber: String = ""
    var dealerName: String? = null
    var dealerId: String = ""
    var listOrders: ListView? = null
    var spinnerDealer: Spinner? = null
    var dealersShopModels: ArrayList<DealersShopModel> = ArrayList<DealersShopModel>()
    var dealerNameList: MutableList<String> = ArrayList()

    // TODO Auto-generated method stub
    protected val layoutResourceId: Int
        protected get() =// TODO Auto-generated method stub
            R.layout.activity_subdealer_order_update

    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        extras = getIntent().getExtras()
        orderNumber = extras?.getString("orderNumber")!!
        dealerName = extras?.getString("dealerName")
        getSalesOrderDetails(orderNumber)
        initUi()
        val abar: ActionBar = getSupportActionBar()!!
        val viewActionBar: View = getLayoutInflater().inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle: TextView = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.setText("Reorder Product")
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        if (extras != null) {
            productId?.setText(" $orderNumber")
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initUi() {
        productId = findViewById<View>(R.id.textProductName) as TextView?
        btnUpdate = findViewById<View>(R.id.btnUpdate) as Button?
        spinnerDealer = findViewById<View>(R.id.spinnerDealer) as Spinner?
        listOrders = findViewById<View>(R.id.listOrders) as ListView?
        mTotalQuantity = findViewById<View>(R.id.totalQty) as TextView?
        myDealersApi
        spinnerDealer!!.prompt = dealerName
        spinnerDealer!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View, pos: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                dealerId = dealersShopModels[pos].dealerId
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        })
        btnUpdate!!.setOnClickListener(this)
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        val actionBar: ActionBar = getSupportActionBar()!!
        actionBar.setSubtitle("")
        actionBar.setTitle("")
        actionBar.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        actionBar.show()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back)
        getSupportActionBar()?.setHomeButtonEnabled(true)
    }

    private fun updateOrder() {
        val detail: String
        detail = createJson().toString()
        val name = arrayOf("salesorder")
        val values = arrayOf(
            detail, AppPrefenceManager.getUserId(this),
            dealerId, orderNumber
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBMIT_REORDER_URL
        )
        manager.getResponsePOST(this, name, values, object : VKCInternetManager.ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                VKCUtils.showtoast(this@ReSubmitOrderActivity, 17)
            }
        })
    }

    private fun parseResponse(resultValue: String) {
        try {
            val arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(resultValue)
            val result: String = jsonObjectresponse.getString("response")
            if (result == "1") {
                VKCUtils.showtoast(this@ReSubmitOrderActivity, 26)
                finish()
            }
        } catch (e: Exception) {
        }
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        when (v.id) {
            R.id.btnUpdate -> updateOrder()
        }
    }

    // TODO Auto-generated method stub
    private val myDealersApi: Unit
        private get() {
            var manager: VKCInternetManager? = null
            val name = arrayOf("subdealer_id")
            val value = arrayOf(getUserId(this))
            manager = VKCInternetManager(VKCUrlConstants.LIST_MY_DEALERS_URL)
            manager.getResponsePOST(this@ReSubmitOrderActivity, name, value,
                object : VKCInternetManager.ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        if (successResponse != null) {
                            parseMyDealerJSON(successResponse)
                        }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                    }
                })
        }

    private fun parseMyDealerJSON(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            // ArrayList<DealersShopModel> dealersShopModels = new
            // ArrayList<DealersShopModel>();
            val respObj = JSONObject(successResponse)
            val response: JSONObject = respObj.getJSONObject("response")
            val status: String = response.getString("status")
            if (status == "Success") {
                val respArray: JSONArray = response.getJSONArray("dealers")
                for (i in 0 until respArray.length()) {
                    dealersShopModels
                        .add(parseShop(respArray.getJSONObject(i)))
                }
                val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item,
                    dealerNameList
                )
                spinnerDealer!!.adapter = dataAdapter
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun parseShop(jsonObject: JSONObject): DealersShopModel {
        val dealersShopModel = DealersShopModel()
        dealersShopModel.address = jsonObject.optString("address")
        dealersShopModel.city = jsonObject.optString("city")
        dealersShopModel.contact_person = jsonObject
            .optString("contact_person")
        dealersShopModel.dealerId = jsonObject.optString("dealerId")
        dealersShopModel.country = jsonObject.optString("country")
        dealersShopModel.customer_id = jsonObject.optString("customer_id")
        dealersShopModel.id = jsonObject.optString("id")
        dealersShopModel.name = jsonObject.optString("name")
        dealersShopModel.phone = jsonObject.optString("phone")
        dealersShopModel.pincode = jsonObject.optString("pincode")
        dealersShopModel.state = jsonObject.optString("state")
        dealersShopModel.state_name = jsonObject.optString("state_name")
        dealerNameList.add(jsonObject.optString("name"))
        return dealersShopModel
    }

    private fun createJson(): JSONObject {
        val jsonObject = JSONObject()
        try {
            jsonObject.putOpt("user_id", AppPrefenceManager.getUserId(this))
            jsonObject.putOpt("dealer_id", dealerId)
            jsonObject.putOpt("order_id", orderNumber)
        } catch (e1: JSONException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        val jsonArray = JSONArray()
        for (i in AppController.subDealerOrderDetailList.indices) {
            val `object` = JSONObject()
            try {
                `object`.putOpt(
                    "product_id",
                    AppController.subDealerOrderDetailList.get(i)
                        .productId
                )
                `object`.putOpt(
                    "color_id",
                    AppController.subDealerOrderDetailList.get(i)
                        .colorId
                )
                `object`.putOpt(
                    "case_id", AppController.subDealerOrderDetailList
                        .get(i).caseId
                )
                `object`.putOpt(
                    "quantity",
                    AppController.subDealerOrderDetailList.get(i)
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

    private fun getSalesOrderDetails(ordrNo: String) {
        AppController.subDealerOrderDetailList.clear()
        val name = arrayOf("order_id")
        val values = arrayOf(ordrNo)
        val manager = VKCInternetManager(
            VKCUrlConstants.Companion.SUBDEALER_ORDER_DETAILS
        )
        manager.getResponsePOST(this, name, values, object : VKCInternetManager.ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponseOrder(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                VKCUtils.showtoast(this@ReSubmitOrderActivity, 17)
            }
        })
    }

    private fun parseResponseOrder(result: String) {
        try {
            var arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val response: JSONObject = jsonObjectresponse.getJSONObject("response")
            val status: String = response.getString("status")
            if (status == "Success") {
                arrayOrders = response.optJSONArray("orderdetails")
                for (i in 0 until arrayOrders.length()) {
                    val obj: JSONObject = arrayOrders.getJSONObject(i)
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
                    orderModel.detailid = obj.getString("detailid")
                    orderModel.color_code = obj.getString("color_code")
                    orderModel.caseDetail = obj.getString("caseName")
                    AppController.subDealerOrderDetailList.add(orderModel)
                    // AppController.TempSubDealerOrderDetailList.add(orderModel);
                }
                val mSalesAdapter = OrderDetailAdapter(
                    this@ReSubmitOrderActivity
                )
                listOrders!!.adapter = mSalesAdapter
                for (j in AppController.subDealerOrderDetailList
                    .indices) {
                    mTotalQty = (mTotalQty
                            + AppController.subDealerOrderDetailList
                        .get(j).quantity.toInt())
                }
                mTotalQuantity?.setText(mTotalQty.toString())
            }
        } catch (e: Exception) {
        }
    }

    companion object {
        var quantity = 0
    }
}