package com.mobatia.vkcsalesapp.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapterimport.RedeemListAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.GiftListModel
import com.mobatia.vkcsalesapp.model.GiftUserModel
import org.json.JSONObject
import java.util.*

class RedeemedListFragment : Fragment(), View.OnClickListener, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    var listViewHistory: ExpandableListView? = null

    // ArrayList<HistoryModel> ;
    var listGift: ArrayList<GiftListModel>? = null
    private var lastExpandedPosition = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_redeem_list, container,
            false
        )
        setDisplayParams()
        init(mRootView, savedInstanceState)
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Redeemed Gift List"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        redeemList
        return mRootView
    }

    private fun setDisplayParams() {
        val displayManagerScale = DisplayManagerScale(
            activity!!
        )
        mDisplayHeight = displayManagerScale.deviceHeight
        mDisplayWidth = displayManagerScale.deviceWidth
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        listGift = ArrayList()
        listViewHistory = v
            ?.findViewById<View>(R.id.listViewRedeem) as ExpandableListView
        listViewHistory!!
            .setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
                if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition
                ) {
                    listViewHistory!!.collapseGroup(lastExpandedPosition)
                }
                lastExpandedPosition = groupPosition
            })
    }// TODO Auto-generated method stub
    // Log.v("LOG", "18022015 Errror" + failureResponse);
// TODO Auto-generated method stub
    /**
     * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
     * 2015 Author:Archana.S
     *
     */
    val redeemList: Unit
        get() {
            listGift!!.clear()
            val name = arrayOf("cust_id")
            val values = arrayOf(
                getCustomerId(
                    activity!!
                )
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.GET_REDEEM_LIST
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        parseResponse(successResponse)
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 Errror" + failureResponse);
                    }
                })
        }

    @SuppressLint("NewApi")
    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val objResponse = jsonObject.optJSONObject("response")
            val status = objResponse.optString("status")
            if (status == "Success") {
                val arrayData = objResponse.optJSONArray("data")
                if (arrayData.length() > 0) {
                    for (i in 0 until arrayData.length()) {
                        val model = GiftListModel()
                        val obj = arrayData.optJSONObject(i)
                        val arrayDetail = obj.optJSONArray("details")
                        /*
						 * JSONArray arrayDetail = new JSONArray(
						 * obj.getString("details"));
						 */println("Detail Array $arrayDetail")
                        model.name = obj.getString("name")
                        model.phone = obj.getString("phone")
                        val listHist = ArrayList<GiftUserModel>()
                        for (j in 0 until arrayDetail.length()) {
                            val obj1 = arrayDetail.optJSONObject(j)
                            val model1 = GiftUserModel()
                            model1.gift_title = obj1.getString("gift_title")
                            model1.gift_image = obj1.getString("gift_image")
                            model1.gift_type = obj1.getString("gift_type")
                            model1.quantity = obj1.getString("quantity")
                            // System.out.println("Date Value "+model1.getDateValue());
                            listHist.add(model1)
                        }

                        // System.out.println("Parsed " +
                        // listHist.get(i).getDateValue());
                        model.listGiftUser = listHist
                        listGift!!.add(model)
                        // System.out.println("List History "+listHistory.get(0).getListHistory().get(1).getDateValue());
                    }
                    /*
					 * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
                    val adapter = RedeemListAdapter(
                        activity!!, listGift!!
                    )
                    listViewHistory!!.setAdapter(adapter)
                } else {
                    showtoast(activity, 44)
                }
            } else {
                showtoast(activity, 13)
            }
        } catch (e: Exception) {
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        /*
		 * if (v == imageSubmit) { if
		 * (edtSearch.getText().toString().trim().equals("")) { CustomToast
		 * toast = new CustomToast(getActivity()); toast.show(49); } else if
		 * (userType.equals("")) { CustomToast toast = new
		 * CustomToast(getActivity()); toast.show(49); } else if
		 * (mEditPoint.getText().toString().equals("")) {
		 * VKCUtils.textWatcherForEditText(mEditPoint, "Mandatory field");
		 * 
		 * } else if (Integer.parseInt(mEditPoint.getText().toString().trim()) >
		 * myPoint) { // FeedbackSubmitApi(); CustomToast toast = new
		 * CustomToast(getActivity()); toast.show(48); } else {
		 * //submitPoints(); }
		 * 
		 * }
		 */
    }

    override fun onResume() {
        super.onResume()
    }
}