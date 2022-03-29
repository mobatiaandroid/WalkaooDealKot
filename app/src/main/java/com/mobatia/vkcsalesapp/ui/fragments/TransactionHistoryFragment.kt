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
import com.mobatia.vkcsalesapp.adapter.TransactionHistoryAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.HistoryModel
import com.mobatia.vkcsalesapp.model.TransactionModel
import org.json.JSONObject
import java.util.*

class TransactionHistoryFragment : Fragment(), View.OnClickListener, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    var listViewHistory: ExpandableListView? = null

    // ArrayList<HistoryModel> ;
    var listHistory: ArrayList<TransactionModel>? = null
    private var lastExpandedPosition = -1
    var textEarned: TextView? = null
    var textTransferred: TextView? = null
    var textBalance: TextView? = null
    var textCredit: TextView? = null
    var textDebit: TextView? = null
    var historyType: String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_transaction_history,
            container, false
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
        textviewTitle.text = "Transaction History"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
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
        listHistory = ArrayList()
        listViewHistory = v
            ?.findViewById<View>(R.id.listViewHistory) as ExpandableListView
        textEarned = v!!.findViewById<View>(R.id.textEarned) as TextView
        textTransferred = v.findViewById<View>(R.id.textTransferred) as TextView
        textBalance = v.findViewById<View>(R.id.textBalance) as TextView
        listViewHistory!!
            .setOnGroupExpandListener(OnGroupExpandListener { groupPosition ->
                if (lastExpandedPosition != -1
                    && groupPosition != lastExpandedPosition
                ) {
                    listViewHistory!!.collapseGroup(lastExpandedPosition)
                }
                lastExpandedPosition = groupPosition
            })
        textCredit = v.findViewById<View>(R.id.textCredit) as TextView
        textDebit = v.findViewById<View>(R.id.textDebit) as TextView
        textDebit!!.setOnClickListener(this)
        textCredit!!.setOnClickListener(this)
        historyType = "CREDIT"
        myHistory
    }// TODO Auto-generated method stub
    // Log.v("LOG", "18022015 Errror" + failureResponse);
// TODO Auto-generated method stub
    // Log.v("LOG", "18022015 success" + successResponse);
    /**
     * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
     * 2015 Author:Archana.S
     *
     */
    val myHistory: Unit
        get() {
            listHistory!!.clear()
            val name = arrayOf("userid", "role", "type")
            val values = arrayOf(
                getUserId(
                    activity!!
                ),
                getUserType(activity!!), historyType
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.TRANSACTION_HISTORY
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "18022015 success" + successResponse);
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
            textEarned!!.text = objResponse.optString("total_credits")
            textTransferred!!.text = objResponse.optString("total_debits")
            textBalance!!.text = objResponse.optString("balance_point")
            if (status == "Success") {
                val arrayData = objResponse.optJSONArray("data")
                if (arrayData.length() > 0) {
                    for (i in 0 until arrayData.length()) {
                        val model = TransactionModel()
                        val obj = arrayData.optJSONObject(i)
                        val arrayDetail = obj.optJSONArray("details")
                        /*
						 * JSONArray arrayDetail = new JSONArray(
						 * obj.getString("details"));
						 */println("Detail Array $arrayDetail")
                        model.userName = obj.getString("to_name")
                        model.totalPoints = obj.getString("tot_points")
                        val listHist = ArrayList<HistoryModel>()
                        for (j in 0 until arrayDetail.length()) {
                            val obj1 = arrayDetail.optJSONObject(j)
                            val model1 = HistoryModel()
                            model1.points = obj1.getString("points")
                            model1.type = obj1.getString("type")
                            model1.to_name = obj1.getString("to_name")
                            model1.to_role = obj1.getString("to_role")
                            model1.dateValue = obj1.getString("date")
                            model1.invoiceNo = obj1.getString("invoice_no")
                            // System.out.println("Date Value "+model1.getDateValue());
                            listHist.add(model1)
                        }

                        // System.out.println("Parsed " +
                        // listHist.get(i).getDateValue());
                        model.listHistory = listHist
                        listHistory!!.add(model)
                        // System.out.println("List History "+listHistory.get(0).getListHistory().get(1).getDateValue());
                    }
                    /*
					 * HistoryAdapter adapter = new
					 * HistoryAdapter(getActivity(), listHistory);
					 * listViewHistory.setAdapter(adapter);
					 */
                    val adapter = TransactionHistoryAdapter(
                        activity!!, listHistory!!
                    )
                    adapter.notifyDataSetChanged()
                    listViewHistory!!.setAdapter(adapter)
                } else {
                    showtoast(activity, 44)
                    listHistory!!.clear()
                    val adapter = TransactionHistoryAdapter(
                        activity!!, listHistory!!
                    )
                    adapter.notifyDataSetChanged()
                    listViewHistory!!.setAdapter(adapter)
                }
            } else if (status == "scheme_error") {
                showtoast(activity, 57)
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
        if (v === textCredit) {
            historyType = "CREDIT"
            textCredit!!.setBackgroundResource(R.drawable.rounded_rect_green)
            textDebit!!.setBackgroundResource(R.drawable.rounded_rect_redline)
            myHistory
        } else if (v === textDebit) {
            historyType = "DEBIT"
            textCredit!!.setBackgroundResource(R.drawable.rounded_rect_redline)
            textDebit!!.setBackgroundResource(R.drawable.rounded_rect_green)
            myHistory
        }
    }

    override fun onResume() {
        super.onResume()
    }
}