/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserName
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_FEEDBACK_FAILED
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_FEEDBACK_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_FEEDBACK_SUCCESS
import com.mobatia.vkcsalesapp.manager.VKCUtils
import org.json.JSONObject

/**
 * Bibin
 */
class ComplaintFragment : Fragment(), View.OnClickListener, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mTxtName: TextView? = null
    private var mEdtComplaint: EditText? = null
    private var mEdtName: EditText? = null
    private var btnSend: Button? = null
    private var relFeedbackType: RelativeLayout? = null
    var searchLinear: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_feedback, container,
            false
        )
        setHasOptionsMenu(true)
        AppController.isCart = false
        setDisplayParams()
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
        textviewTitle.text = "Complaints"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        init(mRootView, savedInstanceState)
        return mRootView
    }

    private fun setDisplayParams() {
        val displayManagerScale = activity?.let {
            DisplayManagerScale(
                it
            )
        }
        mDisplayHeight = displayManagerScale?.deviceHeight!!
        mDisplayWidth = displayManagerScale?.deviceWidth!!
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        mTxtName = v!!.findViewById<View>(R.id.txtName) as TextView
        mEdtName = v.findViewById<View>(R.id.etName) as EditText
        searchLinear = v.findViewById<View>(R.id.secSearchLL) as LinearLayout
        searchLinear!!.visibility = View.GONE
        mEdtName!!.setText(getUserName(activity!!))
        mEdtComplaint = v.findViewById<View>(R.id.etMessage) as EditText
        btnSend = v.findViewById<View>(R.id.imgSend) as Button
        relFeedbackType = v.findViewById<View>(R.id.relFeedback) as RelativeLayout
        relFeedbackType!!.visibility = View.GONE
        btnSend!!.setOnClickListener(this)
    }

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = activity!!.menuInflater
        inflater.inflate(R.menu.main, menu)
        val item = menu.findItem(R.id.action_settings)
        item.isVisible = false
        activity!!.invalidateOptionsMenu()
        return true
    }

    /**
     * Method Name:ComplaintSubmitApi Return Type:void parameters:null Date:Feb
     * 18, 2015 Author:Archana.S
     *
     */
    fun ComplaintSubmitApi() {
        val name = arrayOf("user_id", "message")
        val values = arrayOf(
            getUserId(
                activity!!
            ),
            mEdtComplaint!!.text.toString()
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.PRODUCT_COMPLAINT
        )
        manager.getResponsePOST(
            activity, name, values,
            object : VKCInternetManager.ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    parseResponse(successResponse)
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                }
            })
    }

    /**
     * Method Name:parseResponse Return Type:void parameters:response Date:Feb
     * 18, 2015 Author:Archana.S
     *
     */
    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val responseResult = jsonObject
                .getString(JSON_FEEDBACK_RESPONSE)
            if (responseResult == JSON_FEEDBACK_SUCCESS) {
                VKCUtils.showtoast(activity, 12)
                clearEditText()
            } else if (responseResult == JSON_FEEDBACK_FAILED) {
                VKCUtils.showtoast(activity, 13)
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
        if (v === btnSend) {
            if (mEdtComplaint!!.text.toString() == "") {
                VKCUtils.textWatcherForEditText(
                    mEdtComplaint!!,
                    "Mandatory field"
                )
                VKCUtils.setErrorForEditText(mEdtComplaint!!, "Mandatory field")
            } else {
                ComplaintSubmitApi()
            }
        }
    }

    private fun clearEditText() {
        mEdtComplaint!!.setText("")
    }
}