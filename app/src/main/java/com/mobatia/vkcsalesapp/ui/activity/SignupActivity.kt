package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_LOGINREQ_FAILED
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_LOGINREQ_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_LOGINREQ_SUCCESS
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.jSON_LOGINREQ_EMAILEXISTS
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.DealersCityModel
import com.mobatia.vkcsalesapp.model.DealersDistrictModel
import com.mobatia.vkcsalesapp.model.DealersStateModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception
import java.util.ArrayList

class SignupActivity : AppCompatActivity(), View.OnClickListener
    , VKCUrlConstants {
    private var mUserName: EditText? = null
    private var mEmail: EditText? = null
    private var mPhoneNumber: EditText? = null
    private var mPassword: EditText? = null
    private var mConfirmPassword: EditText? = null
    private var mShopName: EditText? = null
    private var mAddress: EditText? = null
    var mState: TextView? = null
    var mDistrict: TextView? = null
    private var btnRegister: Button? = null
    lateinit var mActivity: Activity
    var dealersStateModels = ArrayList<DealersStateModel>()
    var districtModels = ArrayList<DealersDistrictModel>()
    var cityModels = ArrayList<DealersCityModel>()
    var toast: CustomToast? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        toast = CustomToast(this@SignupActivity)
        mActivity = this
        val abar = supportActionBar
        val viewActionBar = layoutInflater.inflate(
            R.layout.actionbar_imageview, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        initialiseUI()
        setActionBar()
    }

    private fun initialiseUI() {
        mUserName = findViewById<View>(R.id.etUserName) as EditText
        mEmail = findViewById<View>(R.id.etEmail) as EditText
        mPhoneNumber = findViewById<View>(R.id.etPhone) as EditText
        mPassword = findViewById<View>(R.id.etPassword) as EditText
        mConfirmPassword = findViewById<View>(R.id.etConfirmPassword) as EditText
        mShopName = findViewById<View>(R.id.etShopName) as EditText
        mAddress = findViewById<View>(R.id.etAddress) as EditText
        mState = findViewById<View>(R.id.etState) as TextView
        mDistrict = findViewById<View>(R.id.etDistrict) as TextView
        btnRegister = findViewById<View>(R.id.btRegister) as Button
        btnRegister!!.setOnClickListener(this)
        if (VKCUtils.checkInternetConnection(this@SignupActivity)) {
            stateApi
        } else {
            VKCUtils.showtoast(this@SignupActivity, 0)
        }
        mState!!.setOnClickListener { // TODO Auto-generated method stub
            val states = arrayOfNulls<String>(dealersStateModels.size)
            for (i in dealersStateModels.indices) {
                states[i] = dealersStateModels[i].stateName
            }
            val dialog = StateDistrictPlaceDialog(
                this@SignupActivity, states, mState!!, "States",
                object :
                    StateDistrictPlaceDialog.OnDialogItemSelectListener {
                    override fun itemSelected(position: Int) {
                        // TODO Auto-generated method stub
                        getDistrictApi(
                            dealersStateModels[position]
                                .stateCode
                        )
                    }
                })
            dialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            dialog.setCancelable(true)
            dialog.show()
            mDistrict!!.text = ""
        }
        mDistrict!!.setOnClickListener { // TODO Auto-generated method stub
            if (mState!!.text.length != 0) {
                val district = arrayOfNulls<String>(districtModels.size)
                for (i in districtModels.indices) {
                    district[i] = districtModels[i].districtName
                }
                val dialog = StateDistrictPlaceDialog(
                    this@SignupActivity,
                    district,
                    mDistrict!!,
                    "District",
                    object :
                        StateDistrictPlaceDialog.OnDialogItemSelectListener {
                        override fun itemSelected(position: Int) {}
                    })
                dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                dialog.setCancelable(true)
                dialog.show()
                mDistrict!!.text = ""
            } else {
                VKCUtils.showtoast(this@SignupActivity, 20)
            }
        }
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        if (mPhoneNumber!!.text.toString() == "") {
            VKCUtils.textWatcherForEditText(mPhoneNumber!!, "Mandatory field")
            VKCUtils.setErrorForEditText(mPhoneNumber!!, "Mandatory field")
        } else if (mUserName!!.text.toString() == "") {
            VKCUtils.textWatcherForEditText(mUserName!!, "Mandatory field")
            VKCUtils.setErrorForEditText(mUserName!!, "Mandatory field")
        } else if (mShopName!!.text.toString() == "") {
            VKCUtils.textWatcherForEditText(mShopName!!, "Mandatory field")
            VKCUtils.setErrorForEditText(mShopName!!, "Mandatory field")
        } else if (mAddress!!.text.toString() == "") {
            VKCUtils.textWatcherForEditText(mAddress!!, "Mandatory field")
            VKCUtils.setErrorForEditText(mAddress!!, "Mandatory field")
        } else if (!VKCUtils.isValidEmail(mEmail!!.text.toString())) {
            VKCUtils.textWatcherForEditText(mEmail!!, "Enter valid Email ID")
            VKCUtils.setErrorForEditText(mEmail!!, "Enter valid Email ID")
        } else if (mPassword!!.text.toString() == "") {
            VKCUtils.textWatcherForEditText(mPassword!!, "Mandatory field")
            VKCUtils.setErrorForEditText(mPassword!!, "Mandatory field")
        } else if (mConfirmPassword!!.text.toString() == "") {
            VKCUtils.textWatcherForEditText(mConfirmPassword!!, "Mandatory field")
            VKCUtils.setErrorForEditText(mConfirmPassword!!, "Mandatory field")
        } else if (mPassword!!.text.toString() != mConfirmPassword!!.text.toString()) {
            VKCUtils.textWatcherForEditText(
                mConfirmPassword!!,
                "Passwords mismatch"
            )
            VKCUtils.setErrorForEditText(mConfirmPassword!!, "Passwords mismatch")
        } else if (mState!!.text.toString() == "") {
            toast!!.show(33)
        } else if (mDistrict!!.text.toString() == "") {
            toast!!.show(34)
        } else {
            loginRequestApi()
        }
    }

    fun loginRequestApi() {
        val name = arrayOf(
            "name", "email", "phone", "password", "imei_no",
            "shopName", "address", "state", "district"
        )
        var device_id = ""
        device_id = if (Build.VERSION.SDK_INT >= 27) {
            ""
        } else {
            VKCUtils.getDeviceID(mActivity!!)
        }
        val values = arrayOf(
            mUserName!!.text.toString(),
            mEmail!!.text.toString(), mPhoneNumber!!.text.toString(),
            mPassword!!.text.toString(),
            device_id,
            mShopName!!.text.toString(), mAddress!!.text.toString().trim { it <= ' ' },
            mState!!.text.toString(), mDistrict!!.text.toString().trim { it <= ' ' })
        val manager = VKCInternetManager(
            VKCUrlConstants.LOGIN_REQUEST_URL
        )
        manager.getResponsePOST(this@SignupActivity, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    parseResponse(successResponse)
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                }
            })
    }

    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val responseResult = jsonObject
                .getString(JSON_LOGINREQ_RESPONSE)
            if (responseResult == JSON_LOGINREQ_SUCCESS) {
                VKCUtils.showtoast(mActivity, 6)
                finish()
            } else if (responseResult == JSON_LOGINREQ_FAILED) {
                VKCUtils.showtoast(mActivity, 7)
            } else if (responseResult == jSON_LOGINREQ_EMAILEXISTS) {
                VKCUtils.showtoast(mActivity, 8)
            }
        } catch (e: Exception) {
        }
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        val actionBar = supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    // TODO Auto-generated method stub
    // mIsError = true;
    private val stateApi: Unit
        private get() {
            val manager = VKCInternetManager(
                VKCUrlConstants.DEALERS_GETSTATE
            )
            manager.getResponse(this, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    parseJSON(successResponse)
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    // mIsError = true;
                }
            })
        }

    private fun getDistrictApi(stateCode: String) {
        val manager = VKCInternetManager(VKCUrlConstants.DEALERS_GETDISTRICT)
        val name = arrayOf("state")
        val value = arrayOf(stateCode)
        println("State Code:" + value[0])
        manager.getResponsePOST(this, name, value, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                parseDistrictJSON(successResponse, stateCode)
            }

            override fun responseFailure(failureResponse: String?) {
                // TODO Auto-generated method stub
            }
        })
    }

    private fun parseDistrictJSON(successResponse: String?, stateCode: String) {
        // TODO Auto-generated method stub
        districtModels = ArrayList()
        try {
            val jsonObject = JSONObject(successResponse)
            val respArray = jsonObject.getJSONArray("response")
            for (i in 0 until respArray.length()) {
                districtModels.add(parseDistrict(respArray.getJSONObject(i)))
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    private fun parseDistrict(jsonObject: JSONObject): DealersDistrictModel {
        // TODO Auto-generated method stub
        val cityModels = ArrayList<DealersCityModel>()
        val dealersDistrictModel = DealersDistrictModel()
        dealersDistrictModel.districtName = jsonObject.optString("district")
        return dealersDistrictModel
    }

    private fun parseJSON(successResponse: String?) {
        // TODO Auto-generated method stub
        try {
            val respObj = JSONObject(successResponse)
            val respArray = respObj.getJSONArray("states")
            for (i in 0 until respArray.length()) {
                dealersStateModels.add(
                    parseSateObject(
                        respArray
                            .getJSONObject(i)
                    )
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun parseSateObject(objState: JSONObject): DealersStateModel {
        val stateModel = DealersStateModel()
        stateModel.stateCode = objState.optString("state")
        stateModel.stateName = objState.optString("state_name")
        return stateModel
        // TODO Auto-generated method stub
    }
}