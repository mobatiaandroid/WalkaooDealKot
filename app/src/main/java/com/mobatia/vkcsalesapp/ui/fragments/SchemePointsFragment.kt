package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.UserModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SchemePointsFragment() : Fragment(), View.OnClickListener, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mTxtPoint: TextView? = null
    var categories: MutableList<String> = ArrayList()
    private val listArticleNumbers: ArrayList<String>? = null
    var spinnerUserType: Spinner? = null
    var btnSubmit: Button? = null
    var btnReset: Button? = null
    var mEditPoint: EditText? = null
    var myPoint = 0
    var userType: String=""
    var selectedId: String = ""
    private var edtSearch: AutoCompleteTextView? = null
    var listUsers: ArrayList<UserModel>? = null
    var textId: TextView? = null
    var textName: TextView? = null
    var textAddress: TextView? = null
    var textPhone: TextView? = null
    var textType: TextView? = null
    var textRetailer: TextView? = null
    var textSubdealer: TextView? = null
    var llData: LinearLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.scheme_poits_fragment, container,
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
        textviewTitle.text = "Scheme Points"
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
        listUsers = ArrayList()
        spinnerUserType = v!!.findViewById<View>(R.id.spinnerUserType) as Spinner
        mTxtPoint = v.findViewById<View>(R.id.textPoints) as TextView
        btnSubmit = v.findViewById<View>(R.id.btnSubmit) as Button
        btnReset = v.findViewById<View>(R.id.btnReset) as Button
        mEditPoint = v.findViewById<View>(R.id.editPoints) as EditText
        llData = v.findViewById<View>(R.id.llData) as LinearLayout
        llData!!.visibility = View.GONE
        textId = v.findViewById<View>(R.id.textViewId) as TextView
        textName = v.findViewById<View>(R.id.textViewName) as TextView
        textAddress = v.findViewById<View>(R.id.textViewAddress) as TextView
        textPhone = v.findViewById<View>(R.id.textViewPhone) as TextView
        textType = v.findViewById<View>(R.id.textViewType) as TextView
        edtSearch = v.findViewById<View>(R.id.autoSearch) as AutoCompleteTextView
        textRetailer = v.findViewById<View>(R.id.textRetailer) as TextView
        textSubdealer = v.findViewById<View>(R.id.textSubdealer) as TextView
        btnSubmit!!.setOnClickListener(this)
        btnReset!!.setOnClickListener(this)
        categories.clear()
        categories.add("Select User Type")
        categories.add("Retailer")
        categories.add("Sub Dealer")
        edtSearch!!.setOnClickListener { // TODO Auto-generated method stub
            edtSearch!!.showDropDown()
        }
        edtSearch!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                arg0: AdapterView<*>?, arg1: View, arg2: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                val selectedData: String = edtSearch!!.text.toString()
                for (i in listUsers!!.indices) {
                    if ((listUsers!!.get(i).userName == selectedData)) {
                        selectedId = listUsers!!.get(i).userId
                        println("Selected Id : $selectedId")
                        userData
                        break
                    } else {
                        selectedId = ""
                    }
                }
            }
        }
        edtSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length > 0) {
                } else {
                    selectedId = ""
                    llData!!.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })
        val dataAdapter = ArrayAdapter(
            (activity)!!, android.R.layout.simple_spinner_item, categories
        )

        // Drop down layout style - list view with radio button
        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinnerUserType!!.adapter = dataAdapter
        spinnerUserType!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View, pos: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                if (pos > 0) {
                    if (pos == 1) {
                        userType = "5"
                        selectedId = ""
                        edtSearch!!.setText("")
                        // mEditPoint.setText("");
                        getUsers(userType!!)
                    } else {
                        userType = "7"
                        selectedId = ""
                        edtSearch!!.setText("")
                        getUsers(userType!!)
                    }
                } else {
                    userType = ""
                }
                println("User Type : $userType")
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        myPoints
    }// TODO Auto-generated method stub// TODO Auto-generated method stub
    //   Log.v("LOG", "18022015 success" + successResponse);
    /**
     * Method FeedbackSubmitApi Return Type:void parameters:null Date:Feb 18,
     * 2015 Author:Archana.S
     */
    val myPoints: Unit
        get() {
            val name = arrayOf("cust_id", "role")
            val values = arrayOf(
                getCustomerId(
                    (activity)!!
                ),
                getUserType((activity)!!)
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.GET_DEALER_POINTS
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        //   Log.v("LOG", "18022015 success" + successResponse);
                        parseResponse(successResponse)
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "18022015 Errror$failureResponse")
                    }
                })
        }

    /**
     * Method Name:parseResponse Return Type:void parameters:response Date:Feb
     * 18, 2015 Author:Archana.S
     */
    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val objResponse = jsonObject.optJSONObject("response")
            val status = objResponse.optString("status")
            if ((status == "Success")) {

                // VKCUtils.showtoast(getActivity(), 14);

                /*
                 * clearEditText(); edtSearch.setText("");
                 */
                val points = objResponse.optString("loyality_point")
                myPoint = points.toInt()
                mTxtPoint!!.text = points
                textRetailer!!.text = "To Retailer: " + objResponse.optString("to_retailer")
                textSubdealer!!.text = "To Subdealer: " + objResponse.optString("to_subdealer")
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
        if (v === btnSubmit) {
            if ((userType == "")) {
                val toast = CustomToast((activity)!!)
                toast.show(49)
            } else if ((edtSearch!!.text.toString().trim { it <= ' ' } == "")) {
                val toast = CustomToast((activity)!!)
                toast.show(51)
            } else if ((mEditPoint!!.text.toString().trim { it <= ' ' } == "")) {
                // VKCUtils.textWatcherForEditText(mEditPoint,
                // "Mandatory field");
                val toast = CustomToast((activity)!!)
                toast.show(52)
            } else if (mEditPoint!!.text.toString().trim { it <= ' ' }.toInt() > myPoint) {
                // FeedbackSubmitApi();
                val toast = CustomToast((activity)!!)
                toast.show(48)
            } else {
                val dialog = ConfirmIssuePointDIalog(
                    activity, ""
                )
                dialog.show()
            }
        } else if (v === btnReset) {
            spinnerUserType!!.setSelection(0)
            userType = ""
            selectedId = ""
            edtSearch!!.setText("")
            mEditPoint!!.setText("")
            llData!!.visibility = View.GONE
        }
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        // edtSearch.setText(AppController.articleNumber);
        super.onResume()
    }

    private fun getUsers(type: String) {
        listUsers!!.clear()
        var manager: VKCInternetManager? = null
        val name = arrayOf("cust_id", "user_type")
        val value = arrayOf(
            getCustomerId(
                (activity)!!
            ),
            type
        )
        manager = VKCInternetManager(VKCUrlConstants.GET_USERS)
        manager.getResponsePOST(
            activity, name, value,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    try {
                        val responseObj = JSONObject(
                            successResponse
                        )
                        val response = responseObj
                            .getJSONObject("response")
                        val status = response.getString("status")
                        if ((status == "Success")) {
                            val dataArray = response
                                .optJSONArray("data")
                            if (dataArray.length() > 0) {
                                for (i in 0 until dataArray.length()) {
                                    // listArticle[i]=articleArray.getString(i);
                                    val obj = dataArray
                                        .getJSONObject(i)
                                    val model = UserModel()
                                    model.userId = obj.getString("id")
                                    model.userName = obj.getString("name")
                                    // model.setCity(obj.getString("city"));
                                    listUsers!!.add(model)
                                }
                                val listUser = ArrayList<String>()
                                for (i in listUsers!!.indices) {
                                    listUser.add(
                                        listUsers!![i]
                                            .userName
                                    )
                                }
                                val adapter = ArrayAdapter(
                                    (activity)!!,
                                    android.R.layout.simple_list_item_1,
                                    listUser
                                )
                                edtSearch!!.threshold = 1
                                edtSearch!!.setAdapter(adapter)
                            } else {
                                val toast = CustomToast(
                                    (activity)!!
                                )
                                toast.show(17)
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

    fun submitPoints() {
        val name = arrayOf("userid", "to_user_id", "to_role", "points", "role")
        val values = arrayOf(
            getUserId(
                (activity)!!
            ),
            selectedId, userType, mEditPoint!!.text.toString(),
            getUserType((activity)!!)
        )
        val manager = VKCInternetManager(VKCUrlConstants.ISSUE_POINTS)
        manager.getResponsePOST(
            activity, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    //Log.v("LOG", "18022015 success" + successResponse);
                    try {
                        val objResponse = JSONObject(
                            successResponse
                        )
                        val status = objResponse.optString("response")
                        if ((status == "1")) {
                            val toast = CustomToast(
                                (activity)!!
                            )
                            toast.show(50)
                            edtSearch!!.setText("")
                            mEditPoint!!.setText("")
                            val dataAdapter = ArrayAdapter(
                                (activity)!!,
                                android.R.layout.simple_spinner_item,
                                categories
                            )
                            dataAdapter
                                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerUserType!!.adapter = dataAdapter
                            myPoints
                        } else {
                            val toast = CustomToast(
                                (activity)!!
                            )
                            toast.show(13)
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }

                    // parseResponse(successResponse);
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    Log.v("LOG", "18022015 Errror$failureResponse")
                }
            })
    }

    inner class ConfirmIssuePointDIalog     // TODO Auto-generated constructor stub
        (var mActivity: Activity?, var TEXTTYPE: String) : Dialog(
        (mActivity)!!
    ), View.OnClickListener {
        var d: Dialog? = null
        var mCheckBoxDis: CheckBox? = null
        var mImageView: ImageView? = null

        // public Button yes, no;
        var bUploadImage: Button? = null
        var mProgressBar: ProgressBar? = null
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.confirm_point_issue)
            init()
        }

        private fun init() {
            val disp = DisplayManagerScale(mActivity!!)
            val displayH = disp.deviceHeight
            val displayW = disp.deviceWidth
            val relativeDate = findViewById<View>(R.id.datePickerBaseConfirm) as RelativeLayout

            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);
            val buttonSet = findViewById<View>(R.id.buttonYes) as Button
            buttonSet.setOnClickListener(View.OnClickListener {
                dismiss()
                submitPoints()
            } // alrtDbldr.cancel();
            )
            val buttonCancel = findViewById<View>(R.id.buttonNo) as Button
            buttonCancel.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    dismiss()
                }
            })
        }

        override fun onClick(v: View) {
            dismiss()
        }
    }// TODO

    // Auto-generated method stub
// TODO Auto-generated catch block
    //listUsers.clear();
    private val userData: Unit
        private get() {
            //listUsers.clear();
            var manager: VKCInternetManager? = null
            val name = arrayOf("cust_id", "role")
            val value = arrayOf(selectedId, userType)
            manager = VKCInternetManager(VKCUrlConstants.GET_DATA)
            manager.getResponsePOST(
                activity, name, value,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        try {
                            val responseObj = JSONObject(
                                successResponse
                            )
                            val response = responseObj
                                .getJSONObject("response")
                            val status = response.getString("status")
                            if ((status == "Success")) {
                                val objData = response
                                    .optJSONObject("data")
                                val cust_id = objData
                                    .optString("customer_id")
                                val address = objData.optString("address")
                                val name = objData.optString("name")
                                val phone = objData.optString("phone")
                                if ((userType == "5")) {
                                    textType!!.text = ": " + "Retailer"
                                } else if ((userType == "7")) {
                                    textType!!.text = ": " + "Sub Dealer"
                                }
                                textId!!.text = ": $cust_id"
                                textName!!.text = ": $name"
                                textAddress!!.text = ": $address"
                                textPhone!!.text = ": $phone"
                                llData!!.visibility = View.VISIBLE
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