/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.SubDealerOrderListAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import com.mobatia.vkcsalesapp.ui.activity.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * @author Archana.S
 */
class SalesOrderStatusListFragment : Fragment(), VKCUrlConstants,
    View.OnClickListener {
    var mActivity: Activity? = null
    var isError = false
    private var mRootView: View? = null
    var mStatusList: ListView? = null
    var subDealerModels: ArrayList<SubDealerOrderListModel>? = null
    var buttonNew: Button? = null
    var buttonPending: Button? = null
    var salesRepOrderModels = ArrayList<SalesRepOrderModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater
            .inflate(R.layout.fragment_status, container, false)
        mActivity = activity
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Order Status"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setHasOptionsMenu(true)
        AppController.isCart = false
        init(mRootView, savedInstanceState)
        // getSalesOrderStatus();
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        subDealerModels = ArrayList()
        buttonNew = v!!.findViewById<View>(R.id.btnNewOrder) as Button
        buttonPending = v.findViewById<View>(R.id.btnPendingDespatch) as Button
        buttonNew!!.setOnClickListener(this)
        buttonPending!!.setOnClickListener(this)
        mStatusList = v.findViewById<View>(R.id.dispatchedList) as ListView
        salesOrderStatus
        // mStatusList = (ListView) v.findViewById(R.id.salesOrderList);
        mStatusList!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                if (subDealerModels!![position].status
                    == "0"
                ) {
                    val intent = Intent(
                        activity,
                        SubDealerOrderDetails::class.java
                    )
                    if (getUserType(mActivity!!) == "5" || (getUserType(
                            mActivity!!
                        )
                                == "6") || (getUserType(mActivity!!)
                                == "7")
                    ) {
                        intent.putExtra("flag", "1")
                    } else {
                        intent.putExtra("flag", "0")
                    }
                    intent.putExtra(
                        "status", subDealerModels!![position]
                            .status
                    )
                    intent.putExtra(
                        "ordr_no", subDealerModels!![position]
                            .orderid
                    )
                    intent.putExtra(
                        "parent_order_id",
                        subDealerModels!![position].parent_order_id
                    )
                    startActivity(intent)
                } else if (subDealerModels!![position].status == "1") {
                    val intent = Intent(
                        activity,
                        Order_Detail_Approved::class.java
                    )
                    /*
					if (AppPrefenceManager.getUserType(mActivity).equals("5")
							|| AppPrefenceManager.getUserType(mActivity)
									.equals("6")
							|| AppPrefenceManager.getUserType(mActivity)
									.equals("7")) {
						intent.putExtra("flag", "1");
					} else {*/intent.putExtra("flag", "0")
                    //	}
                    intent.putExtra(
                        "status", subDealerModels!![position]
                            .status
                    )
                    intent.putExtra(
                        "ordr_no", subDealerModels!![position]
                            .orderid
                    )
                    intent.putExtra(
                        "parent_order_id",
                        subDealerModels!![position].parent_order_id
                    )
                    startActivity(intent)
                } else if (subDealerModels!![position].status
                    == "4"
                ) {
                    val intent1 = Intent(
                        activity,
                        DispatchedListDetail::class.java
                    )
                    intent1.putExtra(
                        "status", subDealerModels!![position]
                            .status
                    )
                    intent1.putExtra(
                        "order_id", subDealerModels!![position]
                            .orderid
                    )
                    intent1.putExtra(
                        "parent_order_id",
                        subDealerModels!![position].parent_order_id
                    )
                    /*
					 * intent1.putExtra("order_status","pending" );
					 * intent1.putExtra("title","Pending Dispatch" );
					 */startActivity(intent1)
                }
            }
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        when (v.id) {
            R.id.btnNewOrder -> {
                val intent = Intent(
                    activity,
                    SubDealerStatusListActivity::class.java
                )
                intent.putExtra("order_status", "new")
                intent.putExtra("title", "New Orders")
                startActivity(intent)
            }
            R.id.btnPendingDespatch -> {
                val intent1 = Intent(
                    activity,
                    Dealer_Dispatch_Activity::class.java
                )
                /*
			 * intent1.putExtra("order_status","pending" );
			 * intent1.putExtra("title","Pending Dispatch" );
			 */startActivity(intent1)
            }
        }
    }
    // Log.v("LOG", "19022015 success" + successResponse);

    /*
      * if (subDealerModels.size() > 0) { //setAdapter(); }
      * else { VKCUtils.showtoast(getActivity(), 17); }
      */
    private val salesOrderStatus: Unit
        private get() {
            subDealerModels!!.clear()
            val name = arrayOf("user_id")
            val values = arrayOf(
                getUserId(
                    activity!!
                )
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.GET_SUBDEALER_RECENT_ORDERS
            )
            manager.getResponsePOST(mActivity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // Log.v("LOG", "19022015 success" + successResponse);
                        if (successResponse != null) {
                            parseResponse(successResponse)
                        }

                        /*
                          * if (subDealerModels.size() > 0) { //setAdapter(); }
                          * else { VKCUtils.showtoast(getActivity(), 17); }
                          */
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

                /* if (response.has("orders")) { */
                arrayOrders = response.optJSONArray("orders")
                // }

                // int len = arrayOrders.length();
                for (i in 0 until arrayOrders.length()) {
                    val orderModel = SubDealerOrderListModel()
                    val obj = arrayOrders.optJSONObject(i)
                    orderModel.name = obj.getString("name")
                    orderModel.orderid = obj.getString("orderid")
                    orderModel.address = obj.getString("city")
                    orderModel.phone = obj.getString("phone")
                    orderModel.totalqty = obj.getString("total_qty")
                    orderModel.orderDate = obj.getString("order_date")
                    orderModel.parent_order_id = obj
                        .getString("parent_order_id")
                    orderModel.status = obj.getString("status")
                    subDealerModels!!.add(orderModel)
                }
                val adapter = SubDealerOrderListAdapter(
                    activity!!, subDealerModels!!
                )
                // adapter.notifyDataSetChanged();
                mStatusList!!.adapter = adapter
            }
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        // getSalesOrderStatus();
        super.onResume()
    }
}