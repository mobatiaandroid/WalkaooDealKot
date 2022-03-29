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
import com.mobatia.vkcsalesapp.adapter.NotificationListAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.NotificationListModel
import com.mobatia.vkcsalesapp.ui.activity.NotificationDetailActivity
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class NotificationListFragment : Fragment(), VKCUrlConstants {
    private var mActivity: Activity? = null
    private var rootView: View? = null
    var listNotification: ArrayList<NotificationListModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.activity_notification_list, null)
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
        textviewTitle.setText("Notifications")
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        initialiseUI()

        // System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));
        return view

        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private fun initialiseUI() {
        // TODO Auto-generated method stub
        AppController.mNotificationList = view
            ?.findViewById<View>(R.id.listNotification) as ListView
        listNotification = ArrayList<NotificationListModel>()
        notificationList
        AppController.mNotificationList!!
            .setOnItemClickListener(object : AdapterView.OnItemClickListener {
                override fun onItemClick(
                    parent: AdapterView<*>?, view: View,
                    position: Int, id: Long
                ) {
                    val intent = Intent(
                        activity,
                        NotificationDetailActivity::class.java
                    )
                    intent.putExtra(
                        "MESSAGE",
                        listNotification!![position].message
                    )
                    intent.putExtra(
                        "MESSAGE_DATE",
                        listNotification!![position].messageDate
                    )
                    /*
						 * intent.putExtra("orderNumber",
						 * AppController.subDealerModels.get(position)
						 * .getOrderid()); intent.putExtra("dealerName",
						 * AppController.subDealerModels
						 * .get(position).getName());
						 * intent.putExtra("dealerId",
						 * AppController.subDealerModels
						 * .get(position).getDealerId());
						 */startActivity(intent)
                }
            })
    }/*
						 * if (subDealerModels.size() > 0) { //setAdapter(); }
						 * else { VKCUtils.showtoast(getActivity(), 17); }
						 */

    // AppPrefenceManager.getUserType(this)
    private val notificationList: Unit
        private get() {
            listNotification!!.clear()
            val name = arrayOf("userid", "role")
            val values = activity?.let { AppPrefenceManager.getUserId(it) }?.let {
                activity?.let { AppPrefenceManager.getUserType(it) }?.let { it1 ->
                    arrayOf<String>(
                        it,
                        it1
                    )
                }
            }
            val manager = VKCInternetManager(
                VKCUrlConstants.NOTIFICATION_LIST_URL
            )
            if (values != null) {
                manager.getResponsePOST(
                    activity, name, values,
                    object : VKCInternetManager.ResponseListener {
                        override fun responseSuccess(successResponse: String?) {
                            // Log.v("LOG", "19022015 success$successResponse")
                            if (successResponse != null) {
                                parseResponse(successResponse)
                            }

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
        }

    /*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */
    private fun parseResponse(result: String) {
        try {
            val arrayOrders: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            // JSONObject response =
            // jsonObjectresponse.getJSONObject("response");
            val status: String = jsonObjectresponse.getString("status")
            if (status == "200") {
                val arrayNotification: JSONArray = jsonObjectresponse
                    .getJSONArray("data")
                if (arrayNotification.length() > 0) {
                    for (i in 0 until arrayNotification.length()) {
                        val jobject: JSONObject = arrayNotification.getJSONObject(i)
                        val model = NotificationListModel()
                        model.message = jobject.getString("message")
                        model.messageDate = jobject.getString("date")
                        model.messageId = jobject.getString("id")
                        listNotification!!.add(model)
                    }
                    val adapter = NotificationListAdapter(
                        listNotification!!, mActivity!!
                    )
                    adapter.notifyDataSetChanged()
                    AppController.mNotificationList?.setAdapter(adapter)
                } else {
                    VKCUtils.showtoast(mActivity, 44)
                }
            }
        } catch (e: Exception) {
        }
    }
}