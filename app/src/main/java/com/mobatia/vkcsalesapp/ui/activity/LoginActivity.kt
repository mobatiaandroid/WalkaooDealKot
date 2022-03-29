package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController.*

import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveDealerCount
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveIsCredit
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveIsGroupMember
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveLoginStatusFlag
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveStateCode
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveUserName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveUserType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setLoginCustomerName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setLoginPlace
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.LoginResponse
import com.mobatia.vkcsalesapp.model.LoginResponseModel
import com.vkc.loyaltyapp.api.RetrofitAPI
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var mUserName: EditText
    lateinit var mPassword: EditText
    lateinit var mBtnLogin: Button
    lateinit var mBtnSignUp: Button
    lateinit var mActivity: Activity
    private var dealerCount: Int? = 0
    private var roleID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mActivity = this
        initUI()
    }

    private fun initUI() {

        mUserName = findViewById<EditText>(R.id.etUserName)
        mPassword = findViewById<EditText>(R.id.etPassword)
        mBtnLogin = findViewById<Button>(R.id.btLogin)
        mBtnSignUp = findViewById<Button>(R.id.btLoginReq)
        mBtnLogin.setOnClickListener(this)
        mBtnSignUp.setOnClickListener(this)

        navMenuTitles = intent.extras!!.getStringArray(
            "MAINCATEGORYNAMELIST"
        )!!

        categoryIdList = intent.extras!!.getStringArray(
            "MAINCATEGORYIDLIST"
        )!!
    }

    override fun onClick(view: View?) {
        when (view?.id) {

            R.id.btLogin ->
                if (mUserName.text.toString().trim().equals("", ignoreCase = true)
                    || mPassword.text.toString().trim().equals("", ignoreCase = true)
                ) {

                    VKCUtils.showtoast(mActivity, 10)
                } else {
                    if (VKCUtils.checkInternetConnection(mActivity)) {
                        loginRequest()
                    } else {
                        VKCUtils.showtoast(mActivity, 2)
                    }
                    clearValues()
                }
            R.id.btLoginReq -> {
                var mIntent: Intent? = Intent(
                    this@LoginActivity,
                    SignupActivity::class.java
                )
                startActivity(mIntent)
            }


        }
    }

    private fun clearValues() {
        mUserName.setText("")
        mPassword.setText("")
    }

    private fun loginRequest() {


        // String url =
        // "http://www.androidhive.info/2014/05/android-working-with-volley-library-1/";
        /*val pDialog = CustomProgressBar(mActivity, R.drawable.loading)
        pDialog.show()*/
        RetrofitAPI.getClient().getLogin(mUserName.text.toString(), mPassword.text.toString(), "")
            .enqueue(
                object : Callback<LoginResponseModel> {
                    override fun onResponse(
                        call: Call<LoginResponseModel>,
                        response: Response<LoginResponseModel>
                    ) {

                     //   pDialog.dismiss()
                        var responseData: LoginResponse? = response.body()?.response?.get(0)
                        var loginResponse: String? = responseData?.login
                        if (loginResponse.equals("success")) {

                            VKCUtils.showtoast(mActivity, 1)
                            saveUserType(
                                mActivity,
                                responseData?.role_id
                            )
                            saveCustomerId(
                                mActivity,
                                responseData?.cust_id
                            )

                            if (responseData?.dist_name != null) {
                                setLoginPlace(
                                    mActivity,
                                    responseData?.dist_name
                                )
                            }

                            if (responseData?.customer_name
                                != null
                            ) {
                                setLoginCustomerName(
                                    mActivity,
                                    responseData?.customer_name
                                )
                            }
                            saveUserId(
                                mActivity,
                                responseData?.user_id
                            )
                            // Save Dealer Count
                            // Save Dealer Count
                            dealerCount = responseData?.dealer_count
                            saveDealerCount(mActivity, dealerCount.toString())
                            val isCredit: String? = responseData?.credit_view
                            saveIsCredit(mActivity, isCredit)
                            val mUserName: String? = responseData?.user_name
                            /*     if (getUserName(mActivity) != "") {
                                     if (getUserName(mActivity) != mUserName) {
                                         clearDb()
                                     } else {
                                     }
                                 }
     */
                            saveUserName(
                                mActivity,
                                mUserName
                            )
                            saveStateCode(
                                mActivity,
                                responseData?.state_code
                            )

                            userType = responseData?.role_id
                            roleID = userType!!.toInt()
                            saveLoginStatusFlag(mActivity, "true")
                            if (response.body()?.isgrpmember.equals("1")) {
                                saveIsGroupMember(mActivity, "1")
                                finish()

                                startActivity(
                                    Intent(
                                        this@LoginActivity,
                                        GroupMemberActivity::class.java
                                    )
                                )
                            } else {
                                saveIsGroupMember(mActivity, "0")
                                gotoDashBoard()
                            }


                        } else {
                            VKCUtils.showtoast(mActivity, 23)
                        }
                    }

                    override fun onFailure(call: Call<LoginResponseModel>, t: Throwable) {
                    }

                })
    }

    fun gotoDashBoard() {

        finish()

        val mIntent = Intent(
            this@LoginActivity,
            DashboardFActivity::class.java
        )
        mIntent.putExtra("MAINCATEGORYNAMELIST", navMenuTitles)
        mIntent.putExtra("MAINCATEGORYIDLIST", categoryIdList)
        mIntent.putExtra("USERTYPE", userType)
        mIntent.putExtra("DEALERCOUNT", dealerCount)
        mIntent.putExtra("ROLEID", roleID)
        startActivity(mIntent)
    }
}