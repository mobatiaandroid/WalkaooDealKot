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
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SubDealerListByCategory : AppCompatActivity(), VKCUrlConstants {
    var mActivity: Activity? = null
    var extras: Bundle? = null
    var subDealerModels: ArrayList<SubDealerOrderListModel>? = null
    var mStatusList: ListView? = null
    var adapter: SubDealerOrderListAdapter? = null
    var listType: String? = null
    var status: String? = null
    var title: String? = null
    var flag: String? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_salesorderstatus)
        mStatusList = findViewById<View>(R.id.salesOrderList) as ListView?
        subDealerModels = ArrayList<SubDealerOrderListModel>()
        val abar: ActionBar = getSupportActionBar()!!
        val viewActionBar: View = getLayoutInflater().inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle: TextView = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        extras = getIntent().getExtras()
        if (extras != null) {
            status = extras?.getString("order_status")
            title = extras?.getString("title")
            flag = extras?.getString("flag")
            if (status == "pending") {
                listType = "pending"
            } else if (status == "approved") {
                listType = "approved"
            } else if (status == "reject") {
                listType = "reject"
            } else if (status == "dispatch") {
                listType = "dispatch"
            }
            textviewTitle.setText(title.toString())
            getSalesOrderStatus(listType!!)
        }
        mStatusList!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                if (status == "reject") {
                    val intent = Intent(
                        this@SubDealerListByCategory,
                        ReSubmitOrderActivity::class.java
                    )
                    intent.putExtra(
                        "orderNumber",
                        AppController.subDealerModels.get(position)
                            .orderid
                    )
                    intent.putExtra(
                        "dealerName", AppController.subDealerModels
                            .get(position).name
                    )
                    intent.putExtra(
                        "dealerId", AppController.subDealerModels
                            .get(position).dealerId
                    )
                    startActivity(intent)
                } else {
                    val intent = Intent(
                        this@SubDealerListByCategory,
                        CategoryOrderListDetails::class.java
                    )
                    intent.putExtra(
                        "ordr_no", AppController.subDealerModels
                            .get(position).orderid
                    )
                    intent.putExtra("listtype", listType)
                    if (listType == "pending") {
                        intent.putExtra("flag", "pending")
                    } else if (listType == "approved") {
                        intent.putExtra("flag", "approved")
                    } else if (listType == "reject") {
                        intent.putExtra("flag", "reject")
                    }
                    startActivity(intent)
                }
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        val actionBar: ActionBar = getSupportActionBar()!!
        actionBar.setSubtitle("")
        actionBar.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        actionBar.setTitle("")
        actionBar.show()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(
            true
        )
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back)
        getSupportActionBar()?.setHomeButtonEnabled(true)
    }

    private fun getSalesOrderStatus(listType: String) {
        AppController.subDealerModels.clear()
        val name = arrayOf("subdealer_id", "list_type")
        val values = arrayOf(AppPrefenceManager.getUserId(this), listType)
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBDEALER_ORDER_URL_LIST
        )
        manager.getResponsePOST(this, name, values, object : VKCInternetManager.ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                VKCUtils.showtoast(this@SubDealerListByCategory, 17)
            }
        })
    }

    private fun parseResponse(result: String) {
        try {
            var arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val response: JSONObject = jsonObjectresponse.getJSONObject("response")
            val status: String = response.getString("status")
            if (status == "Success") {
                arrayOrders = response.optJSONArray("orders")
                if (arrayOrders.length() > 0) {
                    for (i in 0 until arrayOrders.length()) {
                        val orderModel = SubDealerOrderListModel()
                        val obj: JSONObject = arrayOrders.optJSONObject(i)
                        // JSONArray arrayDetail=obj.getJSONArray("orderDetails");
                        orderModel.name = obj.getString("name")
                        // System.out.println("Name:"+orderModel.getName());
                        orderModel.orderid = obj.getString("orderid")
                        orderModel.address = obj.getString("city")
                        orderModel.phone = obj.getString("phone")
                        orderModel.totalqty = obj.getString("total_qty")
                        orderModel.status = obj.getString("status")
                        AppController.subDealerModels.add(orderModel)
                    }
                    adapter = SubDealerOrderListAdapter(
                        this,
                        AppController.subDealerModels
                    )
                    mStatusList!!.adapter = adapter
                } else {
                    VKCUtils.showtoast(this, 44)
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
    }

    protected override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
        getSalesOrderStatus(listType!!)
    }
}