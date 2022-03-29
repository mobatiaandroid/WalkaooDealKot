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
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import com.mobatia.vkcsalesapp.ui.activity.CategoryOrderListDetails
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class RecentOrdersFragment : Fragment(), VKCUrlConstants {
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
        textviewTitle.text = "Recent Orders"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)

        /*
		 * abar.setDisplayShowTitleEnabled(false);
		 * //abar.setDisplayHomeAsUpEnabled(true);
		 * abar.setIcon(R.color.transparent); abar.setHomeButtonEnabled(true);
		 */setHasOptionsMenu(true)
        init(mRootView, savedInstanceState)
        salesOrderStatus
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        subDealerModels = ArrayList()
        mStatusList = v!!.findViewById<View>(R.id.salesOrderList) as ListView
        mStatusList!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val intent = Intent(
                    activity,
                    CategoryOrderListDetails::class.java
                )
                intent.putExtra(
                    "ordr_no", subDealerModels!![position]
                        .orderid
                )
                intent.putExtra("flag", "")
                intent.putExtra("listtype", "")
                startActivity(intent)
            }
    }// Log.v("LOG", "19022015 success" + successResponse);

    /*
        * if (subDealerModels.size() > 0) { //setAdapter(); }
        * else { VKCUtils.showtoast(getActivity(), 17); }
        */
    /*
            * for (int i = 0; i < name.length; i++) { Log.v("LOG",
            * "12012015 name : " + name[i]); Log.v("LOG", "12012015 values : " +
            * values[i]);
            * 
            * }
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
            /*
        * for (int i = 0; i < name.length; i++) { Log.v("LOG",
        * "12012015 name : " + name[i]); Log.v("LOG", "12012015 values : " +
        * values[i]);
        * 
        * }
        */manager.getResponsePOST(mActivity, name, values,
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
                    // System.out.println("Name:"+orderModel.getName());
                    orderModel.orderid = obj.getString("orderid")
                    orderModel.address = obj.getString("city")
                    orderModel.phone = obj.getString("phone")
                    orderModel.totalqty = obj.getString("total_qty")
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