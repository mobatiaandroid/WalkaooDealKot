package com.mobatia.vkcsalesapp.ui.activity

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
import com.mobatia.vkcsalesapp.adapter.DispatchedListAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SubDealerDispatchListActivity : AppCompatActivity(), VKCUrlConstants {
    var mActivity: Activity? = null
    var extras: Bundle? = null
    var subDealerModels: ArrayList<SubDealerOrderListModel>? = null
    var mStatusList: ListView? = null
    var adapter: DispatchedListAdapter? = null
    var listType: String? = null
    var status: String? = null
    var title: String? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatched_list)
        mStatusList = findViewById<View>(R.id.dispatchedList) as ListView?
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
            getSalesOrderStatus(listType)
        }
        mStatusList!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                val intent =
                    Intent(this@SubDealerDispatchListActivity, DispatchedListDetail::class.java)
                intent.putExtra(
                    "tranporter_name",
                    AppController.subDealerModels.get(position).getName()
                )
                intent.putExtra(
                    "transporter_contact",
                    AppController.subDealerModels.get(position).getPhone()
                )
                intent.putExtra(
                    "order_id",
                    AppController.subDealerModels.get(position).getOrderid()
                )
                intent.putExtra(
                    "order_date",
                    AppController.subDealerModels.get(position).getOrderDate()
                )
                startActivity(intent)
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

    private fun getSalesOrderStatus(listType: String?) {
        AppController.subDealerModels.clear()
        val name = arrayOf("subdealer_id")
        val values = arrayOf(getUserId(this))

        val manager = VKCInternetManager(
            VKCUrlConstants.GET_DISPATCH_ORDERS_URL
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                parseResponse(successResponse!!)
            }

            override fun responseFailure(failureResponse: String?) {
                VKCUtils.showtoast(this@SubDealerDispatchListActivity, 17)
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
                        orderModel.setName(obj.getString("name"))
                        orderModel.setOrderid(obj.getString("orderid"))
                        orderModel.setAddress(obj.getString("city"))
                        orderModel.setPhone(obj.getString("phone"))
                        orderModel.setTotalqty(obj.getString("total_qty"))
                        orderModel.setStatus(obj.getString("status"))
                        AppController.subDealerModels.add(orderModel)
                    }
                    adapter = DispatchedListAdapter(
                        this,
                        AppController.subDealerModels
                    )
                    // adapter.notifyDataSetChanged();
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
        getSalesOrderStatus(listType)
    }
}