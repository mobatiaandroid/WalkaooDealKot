package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import com.mobatia.vkcsalesapp.ui.activity.SalesHeadOrderDetailsActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class SalesHeadOrderList : Fragment(), VKCUrlConstants {
    var mActivity: Activity? = null
    private var mRootView: View? = null
    var orderList: ListView? = null
    var subDealerModels = ArrayList<SubDealerOrderListModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.sales_head_list_fragment,
            container, false
        )
        mActivity = activity
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "My Orders"
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setHasOptionsMenu(true)
        init(mRootView, savedInstanceState)
        getOrderList()
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        orderList = v!!.findViewById<View>(R.id.salesHeadOrderList) as ListView
        orderList!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(
                    activity,
                    SalesHeadOrderDetailsActivity::class.java
                )
                intent.putExtra(
                    "orderNumber", subDealerModels[position]
                        .orderid
                )
                /*intent.putExtra("dealerName", subDealerModels.get(position)
						.getName());
				intent.putExtra("dealerId", subDealerModels.get(position)
						.getDealerId());*/startActivity(intent)
            }
    }

    private fun getOrderList() {
        subDealerModels.clear()
        AppController.subDealerorderList.clear()
        AppController.subDealerModels.clear()
        val name = arrayOf("executive_id")
        val values = arrayOf(
            getUserId(
                mActivity!!
            )
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.SALES_HEAD_ORDERS_URL
        )
        for (i in name.indices) {
            Log.v("LOG", "12012015 name : " + name[i])
            Log.v("LOG", "12012015 values : " + values[i])
        }
        manager.getResponsePOST(mActivity, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    if (successResponse != null) {
                        parseResponse(successResponse)
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    showtoast(activity, 17)
                }
            })
    }

    /*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */
    private fun parseResponse(result: String) {
        try {
            var arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val response = jsonObjectresponse.getJSONObject("response")
            val status = response.getString("status")
            if (status == "Success") {
                if (response.has("orders")) {
                    arrayOrders = response.optJSONArray("orders")
                    // }
                    val len = arrayOrders.length()
                    for (i in 0 until arrayOrders.length()) {
                        val orderModel = SubDealerOrderListModel()
                        val obj = arrayOrders.getJSONObject(i)
                        orderModel.name = obj.getString("name")
                        // System.out.println("Name:"+orderModel.getName());
                        orderModel.orderid = obj.getString("orderid")
                        orderModel.address = obj.getString("city")
                        orderModel.phone = obj.getString("phone")
                        orderModel.totalqty = obj.getString("total_qty")
                        orderModel.status = obj.getString("status")
                        val JsonarrayOrders = obj
                            .optJSONArray("orderDetails")
                        /*for (int j = 0; j < JsonarrayOrders.length(); j++) {
							JSONObject objOrders = JsonarrayOrders
									.optJSONObject(j);
							SubDealerOrderDetailModel ordersModel = new SubDealerOrderDetailModel();
							ordersModel.setCaseId(objOrders
									.getString("case_id"));
							ordersModel.setColorId(objOrders
									.getString("color_id"));
							ordersModel.setCost(objOrders.getString("cost"));
							ordersModel.setDescription(objOrders
									.getString("description"));
							ordersModel.setName(objOrders.getString("name"));
							ordersModel.setOrderDate("order_date");
							ordersModel.setProductId("product_id");
							ordersModel.setQuantity("quantity");
							ordersModel.setDetailid("");
							ordersModel.setSapId("sap_id");
							AppController.subDealerorderList.add(ordersModel);
						}*/subDealerModels.add(orderModel)
                        AppController.subDealerModels.add(orderModel)
                        println(
                            "Order List"
                                    + AppController.subDealerorderList
                        )
                    }
                    if (subDealerModels.size > 0) {
                        val adapter = SubDealerOrderListAdapter(
                            activity!!, subDealerModels
                        )
                        orderList!!.adapter = adapter
                    } else {
                        val toast = CustomToast(activity!!)
                        toast.show(32)
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        val adapter = SubDealerOrderListAdapter(
            activity!!, AppController.subDealerModels
        )
        orderList!!.adapter = adapter
    }

    companion object {
        var mDealerName: String? = null
    }
}