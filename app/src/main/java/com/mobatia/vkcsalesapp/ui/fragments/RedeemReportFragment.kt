package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.RedeemReportsAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.RedeemReportModel
import com.mobatia.vkcsalesapp.model.ReportDetailModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class RedeemReportFragment : Fragment(), VKCUrlConstants {
    var mActivity: Activity? = null
    var tempRedeemReportList: ArrayList<RedeemReportModel>? = null
    private var mRootView: View? = null
    var mStatusList: ListView? = null
    var adapter: RedeemReportsAdapter? = null
    var editSearch: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_redeem_report_list,
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
        textviewTitle.setText("Redeem Report")
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)

        /*
		 * abar.setDisplayShowTitleEnabled(false);
		 * //abar.setDisplayHomeAsUpEnabled(true);
		 * abar.setIcon(R.color.transparent); abar.setHomeButtonEnabled(true);
		 */setHasOptionsMenu(true)
        init(mRootView, savedInstanceState)
        report
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        tempRedeemReportList = ArrayList<RedeemReportModel>()
        mStatusList = v!!.findViewById<View>(R.id.listViewRedeemReportList) as ListView
        editSearch = v.findViewById<View>(R.id.editSearch) as EditText
        editSearch!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
                if (s.length > 0) {
                    tempRedeemReportList!!.clear()
                    for (i in AppController.listRedeemReport.indices) {
                        if (AppController.listRedeemReport.get(i).custId
                                .contains(s)
                            || AppController.listRedeemReport.get(i)
                                .custName.toLowerCase().contains(s)
                            || AppController.listRedeemReport.get(i)
                                .custMobile.contains(s)
                            || AppController.listRedeemReport.get(i)
                                .custPlace.toLowerCase().contains(s)
                        ) {
                            tempRedeemReportList
                                ?.add(AppController.listRedeemReport.get(i))
                        } else {
                            mStatusList!!.adapter = null
                        }
                    }
                    adapter = RedeemReportsAdapter(
                        activity!!,
                        tempRedeemReportList!!
                    )
                    adapter?.notifyDataSetChanged()
                    mStatusList!!.adapter = adapter
                } else {
                    adapter = RedeemReportsAdapter(
                        activity!!,
                        AppController.listRedeemReport
                    )
                    adapter?.notifyDataSetChanged()
                    mStatusList!!.adapter = adapter
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
        mStatusList!!.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {

                /*
				 * Intent intent = new Intent(getActivity(),
				 * SubDealerOrderDetails.class); intent.putExtra("ordr_no",
				 * subDealerModels.get(position) .getOrderid());
				 * intent.putExtra("position", position);
				 * intent.putExtra("flag", subDealerModels.get(position)
				 * .getStatus().toString()); intent.putExtra("status",
				 * subDealerModels.get(position) .getStatus().toString());
				 * startActivity(intent);
				 */
            }
        })
    }

    /*
						 * if (subDealerModels.size() > 0) { //setAdapter(); }
						 * else { VKCUtils.showtoast(getActivity(), 17); }
						 */
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
                VKCUrlConstants.GET_REDEEM_REPORT_APP
            )
            manager.getResponsePOST(mActivity, name, values,
                object : VKCInternetManager.ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
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

    /*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */
    private fun parseResponse(result: String) {
        try {
            var arrayData: JSONArray? = null
            val jsonObjectresponse = JSONObject(result)
            val objResponse: JSONObject = jsonObjectresponse
                .optJSONObject("response")
            val status: String = objResponse.getString("status")
            if (status == "Success") {

                /* if (response.has("orders")) { */
                arrayData = objResponse.optJSONArray("data")
                // }

                // int len = arrayOrders.length();
                if (arrayData.length() > 0) {
                    for (i in 0 until arrayData.length()) {
                        val redeemModel = RedeemReportModel()
                        val obj: JSONObject = arrayData.optJSONObject(i)
                        redeemModel.custId = obj.getString("cust_id")
                        redeemModel.custName = obj.getString("name")
                        redeemModel.custPlace = obj.getString("place")
                        redeemModel.custMobile = obj.getString("phone")
                        val objArray: JSONArray = obj.optJSONArray("details")
                        val listDetail: ArrayList<ReportDetailModel> =
                            ArrayList<ReportDetailModel>()
                        for (j in 0 until objArray.length()) {
                            val model = ReportDetailModel()
                            val objData: JSONObject = objArray.optJSONObject(j)
                            model.gift_name = objData.optString("gift_name")
                            model.gift_qty = objData.optString("gift_qty")
                            model.rwd_points = objData.optString("rwd_points")
                            model.tot_coupons = objData
                                .optString("tot_coupons")
                            listDetail.add(model)
                        }
                        redeemModel.listReportDetail = listDetail
                        AppController.listRedeemReport.add(redeemModel)
                    }
                    adapter = RedeemReportsAdapter(
                        activity!!,
                        AppController.listRedeemReport
                    )
                    // adapter.notifyDataSetChanged();
                    mStatusList!!.adapter = adapter
                } else {
                    VKCUtils.showtoast(activity, 43)
                }
            } else if (status == "scheme_error") {
                VKCUtils.showtoast(activity, 58)
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