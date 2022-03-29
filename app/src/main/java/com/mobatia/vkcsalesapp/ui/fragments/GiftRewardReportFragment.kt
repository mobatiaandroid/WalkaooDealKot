package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ReportDetailAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.ReportDetailModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class GiftRewardReportFragment : Fragment(), VKCUrlConstants {
    var mActivity: Activity? = null
    var listGiftReward: ArrayList<ReportDetailModel>? = null
    private var mRootView: View? = null
    var mListViewGift: ListView? = null
    var adapter: ReportDetailAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_gift_reward_report,
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
        textviewTitle.text = "Gift & Reward Report"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setHasOptionsMenu(true)
        init(mRootView, savedInstanceState)
        report
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        listGiftReward = ArrayList()
        mListViewGift = v!!.findViewById<View>(R.id.listViewGiftReward) as ListView
    }

    private val report: Unit
        private get() {
            AppController.listRedeemReport.clear()
            val name = arrayOf("dealerId")
            val values = arrayOf(
                getCustomerId(
                    activity!!
                )
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.GET_GIFT_REWARD_REPORT_APP
            )
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

    private fun parseResponse(result: String) {
        try {
            var arrayData: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val objResponse = jsonObjectresponse
                .optJSONObject("response")
            val status = objResponse.getString("status")
            if (status == "Success") {

                /* if (response.has("orders")) { */
                arrayData = objResponse.optJSONArray("data")
                // }

                // int len = arrayOrders.length();
                if (arrayData.length() > 0) {
                    for (i in 0 until arrayData.length()) {
                        val model = ReportDetailModel()
                        val objData = arrayData.optJSONObject(i)
                        model.gift_name = objData.optString("gift_name")
                        model.gift_qty = objData.optString("gift_qty")
                        model.rwd_points = objData.optString("rwd_points")
                        model.tot_coupons = objData.optString("tot_coupons")
                        listGiftReward!!.add(model)
                    }
                }
                adapter = ReportDetailAdapter(activity!!, listGiftReward!!)
                // adapter.notifyDataSetChanged();
                mListViewGift!!.adapter = adapter
            } else if (status == "scheme_error") {
                showtoast(activity, 58)
            } else {
                showtoast(activity, 43)
            }
        } catch (e: Exception) {
            println("Error$e")
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