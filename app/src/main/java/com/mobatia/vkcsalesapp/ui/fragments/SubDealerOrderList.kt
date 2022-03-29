package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.SubDealerOrderListAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import com.mobatia.vkcsalesapp.ui.activity.SubDealerOrderDetails
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SubDealerOrderList : Fragment(), VKCUrlConstants {
    var mActivity: Activity? = null
    var subDealerModels: ArrayList<SubDealerOrderListModel>? = null
    private var mRootView: View? = null
    var mStatusList: ListView? = null
    var adapter: SubDealerOrderListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_salesorderstatus,
            container, false
        )
        mActivity = activity
        val abar: ActionBar = (activity as AppCompatActivity?)?.getSupportActionBar()!!
        val viewActionBar: View = activity!!.layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle: TextView = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.setText("Subdealer Orders")
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        AppController.isDealerList = false
        /*
		 * abar.setDisplayShowTitleEnabled(false);
		 * //abar.setDisplayHomeAsUpEnabled(true);
		 * abar.setIcon(R.color.transparent); abar.setHomeButtonEnabled(true);
		 */setHasOptionsMenu(true)
        AppController.isCart = false
        if (savedInstanceState != null) {
            init(mRootView, savedInstanceState)
        }
        getSalesOrderStatus()
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle) {
        subDealerModels = ArrayList<SubDealerOrderListModel>()
        mStatusList = v!!.findViewById<View>(R.id.salesOrderList) as ListView
        mStatusList!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                val intent = Intent(
                    activity,
                    SubDealerOrderDetails::class.java
                )
                intent.putExtra(
                    "ordr_no", subDealerModels!![position]
                        .orderid
                )
                intent.putExtra("position", position)
                intent.putExtra(
                    "flag", subDealerModels!![position]
                        .status.toString()
                )
                intent.putExtra(
                    "status", subDealerModels!![position]
                        .status.toString()
                )
                startActivity(intent)
            }
        })
    }

    /*
						 * if (subDealerModels.size() > 0) { //setAdapter(); }
						 * else { VKCUtils.showtoast(getActivity(), 17); }
						 */


    /*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */

    private fun getSalesOrderStatus() {
        subDealerModels!!.clear()
        val name = arrayOf("dealer_id", "list_type")
        val values = arrayOf(
            getCustomerId(
                activity!!
            ),
            "all"
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBDEALER_ORDER_URL
        )
        manager.getResponsePOST(mActivity, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    parseResponse(successResponse!!)

                    /*
						 * if (subDealerModels.size() > 0) { //setAdapter(); }
						 * else { VKCUtils.showtoast(getActivity(), 17); }
						 */
                }

                override fun responseFailure(failureResponse: String?) {
                    VKCUtils.showtoast(activity, 17)
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

                /* if (response.has("orders")) { */
                arrayOrders = response.optJSONArray("orders")
                // }

                // int len = arrayOrders.length();
                for (i in 0 until arrayOrders.length()) {
                    val orderModel = SubDealerOrderListModel()
                    val obj: JSONObject = arrayOrders.optJSONObject(i)
                    orderModel.name = obj.getString("name")
                    // System.out.println("Name:"+orderModel.getName());
                    orderModel.orderid = obj.getString("orderid")
                    orderModel.address = obj.getString("city")
                    orderModel.phone = obj.getString("phone")
                    orderModel.totalqty = obj.getString("total_qty")
                    orderModel.orderDate = obj.getString("order_date")
                    orderModel.status = obj.getString("status")
                    subDealerModels!!.add(orderModel)
                }
                adapter = SubDealerOrderListAdapter(
                    activity!!,
                    subDealerModels!!
                )
                // adapter.notifyDataSetChanged();
                mStatusList!!.adapter = adapter
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
}