/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_FEEDBACK_FAILED
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_FEEDBACK_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_FEEDBACK_SUCCESS
import com.mobatia.vkcsalesapp.manager.VKCUtils.setErrorForEditText
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.manager.VKCUtils.textWatcherForEditText
import com.mobatia.vkcsalesapp.ui.activity.ArticleListActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Bibin
 */
class FeedbackFragment : Fragment(), View.OnClickListener, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mTxtName: TextView? = null
    private var mEdtComplaint: EditText? = null
    private var mEdtName: EditText? = null
    private var mBtnSend: Button? = null
    private var mRadioGroup: RadioGroup? = null
    private var mRelFeedback: RelativeLayout? = null
    private var mRadioCustomer: RadioButton? = null
    private var mRadioProductLaunch: RadioButton? = null
    private var mFeedbackType: String = ""
    private var imageSearchCat: ImageView? = null
    var selectedId = 0
    var edtSearch: AutoCompleteTextView? = null
    var testSearch: String? = null
    private var mRadioButton: RadioButton? = null
    private var listArticleNumbers: ArrayList<String>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_feedback,
            container, false
        )
        setDisplayParams()
        init(mRootView, savedInstanceState)
        AppController.isCart = false
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
        textviewTitle.text = "Feedback"
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
        mTxtName = v!!.findViewById<View>(R.id.txtName) as TextView
        mEdtComplaint = v.findViewById<View>(R.id.etMessage) as EditText
        mBtnSend = v.findViewById<View>(R.id.imgSend) as Button
        mEdtName = v.findViewById<View>(R.id.etName) as EditText
        imageSearchCat = v.findViewById<View>(R.id.imageViewSearchCat) as ImageView
        mRelFeedback = v.findViewById<View>(R.id.relFeedback) as RelativeLayout
        mEdtName!!.setText(getUserName(activity!!))
        mRadioCustomer = v.findViewById<View>(R.id.radioCustomer) as RadioButton
        mRadioProductLaunch = v.findViewById<View>(R.id.radioLaunch) as RadioButton
        mRadioGroup = v.findViewById<View>(R.id.relativeType) as RadioGroup
        listArticleNumbers = ArrayList()
        articleNumbers
        edtSearch = v.findViewById<View>(R.id.autoSearch) as AutoCompleteTextView
        mFeedbackType = "1"
        if (getUserType(activity!!) != "4") {
            mRelFeedback!!.visibility = View.GONE
        }
        mRadioGroup!!.setOnCheckedChangeListener { group, checkedId -> // TODO Auto-generated method stub
            val selectedId = mRadioGroup!!.checkedRadioButtonId

            // find the radiobutton by returned id
            mRadioButton = v.findViewById<View>(selectedId) as RadioButton
            if (mRadioButton!!.text.toString() == "Product Launch") {
                mFeedbackType = "1"
            } else if (mRadioButton!!.text.toString() == "Customer") {
                mFeedbackType = "2"
            }
        }
        mBtnSend!!.setOnClickListener(this)
        imageSearchCat!!.setOnClickListener(this)
    }

    /**
     * Method FeedbackSubmitApi
     * Return Type:void
     * parameters:null
     * Date:Feb 18, 2015
     * Author:Archana.S
     *
     */
    fun FeedbackSubmitApi() {
        val name = arrayOf("feedbacktype", "user_id", "message", "article_no")
        val values = arrayOf(
            mFeedbackType, getUserId(
                activity!!
            ), mEdtComplaint!!.text.toString(), edtSearch!!.text.toString()
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.PRODUCT_FEEDBACK
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
                    Log.v("LOG", "18022015 Errror$failureResponse")
                }
            })
    }

    /**
     * Method Name:parseResponse
     * Return Type:void
     * parameters:response
     * Date:Feb 18, 2015
     * Author:Archana.S
     *
     */
    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val responseResult = jsonObject.getString(JSON_FEEDBACK_RESPONSE)
            if (responseResult == JSON_FEEDBACK_SUCCESS) {
                showtoast(activity, 14)
                clearEditText()
                edtSearch!!.setText("")
            } else if (responseResult == JSON_FEEDBACK_FAILED) {
                showtoast(activity, 13)
            }
        } catch (e: Exception) {
        }
    }

    /* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        if (v === mBtnSend) {
            if (mEdtComplaint!!.text.toString() == "") {
                textWatcherForEditText(mEdtComplaint!!, "Mandatory field")
                setErrorForEditText(mEdtComplaint!!, "Mandatory field")
            } else {
                FeedbackSubmitApi()
            }
        } else if (v === imageSearchCat) {
            testSearch = edtSearch!!.text.toString().trim { it <= ' ' }
            if (testSearch!!.length == 0) {
                val toast = CustomToast(activity!!)
                toast.show(38)
            } else if (testSearch!!.length > 0) {
                val intent = Intent(
                    activity,
                    ArticleListActivity::class.java
                )
                intent.putExtra("key", testSearch)
                startActivity(intent)
            } else {
                val toast = CustomToast(activity!!)
                toast.show(38)
            }
        }
    }

    private fun clearEditText() {
        mEdtComplaint!!.setText("")
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        edtSearch!!.setText(AppController.articleNumber)
        super.onResume()
    }// TODO

    // Auto-generated method stub
// TODO Auto-generated catch block
    // listArticle[i]=articleArray.getString(i);
    private val articleNumbers: Unit
        private get() {
            var manager: VKCInternetManager? = null
            listArticleNumbers!!.clear()
            val name = arrayOf("")
            val value = arrayOf("")
            manager = VKCInternetManager(VKCUrlConstants.GET_QUICK_ARTICLE_NO_URL)
            manager.getResponsePOST(activity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    try {
                        val responseObj = JSONObject(successResponse)
                        val response = responseObj.getJSONObject("response")
                        val status = response.getString("status")
                        if (status == "Success") {
                            val articleArray = response
                                .optJSONArray("articlenos")
                            if (articleArray.length() > 0) {
                                for (i in 0 until articleArray.length()) {
                                    // listArticle[i]=articleArray.getString(i);
                                    listArticleNumbers!!.add(
                                        articleArray
                                            .getString(i).toString()
                                    )
                                }
                                val adapter = ArrayAdapter(
                                    activity!!,
                                    android.R.layout.simple_list_item_1,
                                    listArticleNumbers!!
                                )
                                edtSearch!!.setAdapter(adapter)
                            }
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }

                override fun responseFailure(failureResponse: String?) { // TODO
                    // Auto-generated method stub
                }
            })
        }
}