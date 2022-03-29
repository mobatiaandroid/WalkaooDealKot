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
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import com.mobatia.vkcsalesapp.ui.activity.SubDealerDispatchListActivity
import com.mobatia.vkcsalesapp.ui.activity.SubDealerListByCategory
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * @author Archana.S
 */
class SubdealerOrderStatusFragment : Fragment(), VKCUrlConstants, View.OnClickListener {
    var mActivity: Activity? = null
    var isError = false
    private var mRootView: View? = null
    var mStatusList: ListView? = null
    var subDealerModels: ArrayList<SubDealerOrderListModel>? = null
    var buttonNew: Button? = null
    var buttonPending: Button? = null
    var buttonRejected: Button? = null
    var buttonDispatched: Button? = null
    var salesRepOrderModels = ArrayList<SalesRepOrderModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.subdealer_order_status_fragment,
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
        textviewTitle.text = "Order Status"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setHasOptionsMenu(true)
        AppController.isCart = false
        init(mRootView, savedInstanceState)
        //
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        buttonNew = v!!.findViewById<View>(R.id.btnPending) as Button
        buttonPending = v.findViewById<View>(R.id.btnApproved) as Button
        buttonRejected = v.findViewById<View>(R.id.btnRejected) as Button
        buttonDispatched = v.findViewById<View>(R.id.btnDispatch) as Button
        buttonNew!!.setOnClickListener(this)
        buttonPending!!.setOnClickListener(this)
        buttonRejected!!.setOnClickListener(this)
        buttonDispatched!!.setOnClickListener(this)
        subDealerModels = ArrayList()
        // getSalesOrderStatus();
        mStatusList = v.findViewById<View>(R.id.subdealerOrderList) as ListView
        mStatusList!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> /*
				 * Intent intent = new Intent(getActivity(),
				 * SubDealerListByCategory.class);
				 * 
				 * intent.putExtra("ordr_no", salesRepOrderModels.get(position)
				 * .getmOrderNo()); intent.putExtra("ordr_date",
				 * salesRepOrderModels.get(position) .getmOrderDate());
				 * 
				 * String status;
				 * 
				 * intent.putExtra("order_status", subDealerModels.get(position)
				 * .getStatus()); if
				 * (subDealerModels.get(position).getStatus().equals("0")) {
				 * intent.putExtra("title", "Pending Orders"); } else if
				 * (subDealerModels.get(position).getStatus().equals("1")) {
				 * intent.putExtra("title", "Approved Orders"); } else if
				 * (subDealerModels.get(position).getStatus().equals("3")) {
				 * intent.putExtra("title", "Rejected Orders"); } else if
				 * (subDealerModels.get(position).getStatus().equals("4")) {
				 * intent.putExtra("title", "Dispatched Orders"); }
				 * startActivity(intent);
				 */
                /*
                     * String listType = ""; if
                     * (subDealerModels.get(position).getStatus().equals("3")) {
                     * Intent intent2 = new Intent(getActivity(),
                     * ReSubmitOrderActivity.class); intent2.putExtra("orderNumber",
                     * subDealerModels.get(position) .getOrderid());
                     * intent2.putExtra("dealerName", subDealerModels
                     * .get(position).getName()); intent2.putExtra("dealerId",
                     * subDealerModels .get(position).getDealerId());
                     * 
                     * startActivity(intent2);
                     * 
                     * } else { Intent intent1 = new Intent(getActivity(),
                     * CategoryOrderListDetails.class); if
                     * (subDealerModels.get(position).getStatus().equals("0")) {
                     * intent1.putExtra("title", "Pending Orders");
                     * listType="pending"; } else if
                     * (subDealerModels.get(position).getStatus().equals("1")) {
                     * intent1.putExtra("title", "Approved Orders");
                     * listType="approved"; } else if
                     * (subDealerModels.get(position).getStatus().equals("3")) {
                     * intent1.putExtra("title", "Rejected Orders");
                     * listType="reject"; } else if
                     * (subDealerModels.get(position).getStatus().equals("4")) {
                     * intent1.putExtra("title", "Dispatched Orders");
                     * listType="dispatched"; } intent1.putExtra("ordr_no",
                     * subDealerModels .get(position).getOrderid());
                     * intent1.putExtra("listtype", listType); if
                     * (listType.equals("pending")) { intent1.putExtra("flag",
                     * "pending"); } else if (listType.equals("approved")) {
                     * intent1.putExtra("flag", "approved"); } else if
                     * (listType.equals("reject")) { intent1.putExtra("flag",
                     * "reject"); } else if (listType.equals("dispatched")) {
                     * intent1.putExtra("flag", "dispatched"); }
                     * startActivity(intent1); }
                     */
            }
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        when (v.id) {
            R.id.btnPending -> {
                val intent = Intent(
                    activity,
                    SubDealerListByCategory::class.java
                )
                intent.putExtra("order_status", "pending")
                intent.putExtra("title", "Pending Orders")
                startActivity(intent)
            }
            R.id.btnApproved -> {
                val intent1 = Intent(
                    activity,
                    SubDealerListByCategory::class.java
                )
                intent1.putExtra("order_status", "approved")
                intent1.putExtra("title", "Approved Orders")
                startActivity(intent1)
            }
            R.id.btnRejected -> {
                val intent2 = Intent(
                    activity,
                    SubDealerListByCategory::class.java
                )
                intent2.putExtra("order_status", "reject")
                intent2.putExtra("title", "Rejected Orders")
                startActivity(intent2)
            }
            R.id.btnDispatch -> {
                val intent3 = Intent(
                    activity,
                    SubDealerDispatchListActivity::class.java
                )
                intent3.putExtra("order_status", "dispatch")
                intent3.putExtra("title", "Dispatched Orders")
                startActivity(intent3)
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
}