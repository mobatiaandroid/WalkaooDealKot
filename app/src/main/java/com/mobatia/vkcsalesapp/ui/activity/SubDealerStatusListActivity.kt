package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.SubDealerOrderListAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SubDealerStatusListActivity : AppCompatActivity(), VKCUrlConstants {
    var mActivity: Activity? = null
    var extras: Bundle? = null
    var subDealerModels: ArrayList<SubDealerOrderListModel>? = null
    var mStatusList: ListView? = null
    var adapter: SubDealerOrderListAdapter? = null
    var listType: String? = null
    var status: String? = null
    var title: String? = null
    var flag: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_salesorderstatus)
        mStatusList = findViewById<View>(R.id.salesOrderList) as ListView
        subDealerModels = ArrayList()
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
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        extras = intent.extras
        if (extras != null) {
            status = extras!!.getString("order_status")
            title = extras!!.getString("title")
            flag = extras!!.getString("flag")
            if (status == "new") {
                listType = "all"
            } else if (status == "pending") {
                listType = "all"
            }
            textviewTitle.text = title.toString()
            getSalesOrderStatus(listType!!)
        }
        mStatusList!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(
                    this@SubDealerStatusListActivity,
                    SubDealerOrderDetails::class.java
                )
                intent.putExtra(
                    "ordr_no",
                    AppController.subDealerModels[position]
                        .orderid
                )
                intent.putExtra("listtype", listType)
                if (listType == "pending") {
                    intent.putExtra("flag", "pending")
                } else {
                    intent.putExtra("flag", "")
                }
                startActivity(intent)
            }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.title = ""
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    private fun getSalesOrderStatus(listType: String) {
        AppController.subDealerModels.clear()
        val name = arrayOf("dealer_id", "list_type")
        val values = arrayOf(getUserId(this), listType) // AppPrefenceManager.getCustomerId(this)
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBDEALER_ORDER_URL
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                showtoast(this@SubDealerStatusListActivity, 17)
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
                arrayOrders = response.optJSONArray("orders")
                for (i in 0 until arrayOrders.length()) {
                    val orderModel = SubDealerOrderListModel()
                    val obj = arrayOrders.optJSONObject(i)
                    orderModel.name = obj.getString("name")
                    orderModel.orderid = obj.getString("orderid")
                    orderModel.address = obj.getString("city")
                    orderModel.phone = obj.getString("phone")
                    orderModel.totalqty = obj.getString("total_qty")
                    orderModel.status = obj.getString("status")
                    orderModel.orderDate = obj.getString("order_date")
                    AppController.subDealerModels.add(orderModel)
                }
                adapter = SubDealerOrderListAdapter(
                    this,
                    AppController.subDealerModels
                )
                mStatusList!!.adapter = adapter
                if (AppController.subDealerModels.size > 0) {
                } else {
                    showtoast(mActivity, 44)
                }
            }
        } catch (e: Exception) {
        }
    }

    public override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    public override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
    }

    override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
        getSalesOrderStatus(listType!!)
    }
}