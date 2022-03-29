package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.MemberAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.model.MembersModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class GroupMemberActivity : AppCompatActivity(), View.OnClickListener, VKCUrlConstants {
    var mContext: Activity? = null
    var listViewMembers: ListView? = null
    var textTotalCoupon: TextView? = null
    var textIssuedCoupon: TextView? = null
    var textOrderLimit: TextView? = null
    var textTotalBalance: TextView? = null
    var textTotalDue: TextView? = null
    var listMembers: ArrayList<MembersModel>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_member)
        mContext = this
        initialiseUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialiseUI() {
        listMembers = ArrayList()
        val abar = supportActionBar
        val viewActionBar = layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Group Member"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        supportActionBar!!.setLogo(R.drawable.back)
        textTotalCoupon = findViewById<View>(R.id.textTotalCoupon) as TextView
        textIssuedCoupon = findViewById<View>(R.id.textIssued) as TextView
        textOrderLimit = findViewById<View>(R.id.textTotalOrderLimit) as TextView
        textTotalBalance = findViewById<View>(R.id.textTotalBalance) as TextView
        textTotalDue = findViewById<View>(R.id.textTotalDue) as TextView
        listViewMembers = findViewById<View>(R.id.listViewMembers) as ListView
        listViewMembers!!.onItemClickListener =
            AdapterView.OnItemClickListener { arg0, arg1, position, arg3 ->
                // TODO Auto-generated method stub
            }
        members
    }

    fun setActionBar() {
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }// TODO Auto-generated method stub

    // TODO Auto-generated catch block
    private val members: Unit
        private get() {
            var manager: VKCInternetManager? = null
            val name = arrayOf("cust_id")
            val value = arrayOf(
                getCustomerId(
                    mContext!!
                )
            )
            manager = VKCInternetManager(VKCUrlConstants.GET_MEMBERS_LIST)
            manager.getResponsePOST(mContext, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    try {
                        val objResponse = JSONObject(successResponse)
                        val arrayMembers = objResponse
                            .getJSONArray("response")
                        textTotalCoupon!!.text = objResponse.getString("loyalty_points")
                        textIssuedCoupon!!.text = objResponse.getString("tot_issued_points")
                        textOrderLimit!!.text = objResponse.getString("order_limit")
                        textTotalBalance!!.text = objResponse.getString("total_balance")
                        textTotalDue!!.text = objResponse.getString("total_due")
                        listViewMembers = findViewById<View>(R.id.listViewMembers) as ListView
                        if (arrayMembers.length() > 0) {
                            for (i in 0 until arrayMembers.length()) {
                                val obj = arrayMembers.optJSONObject(i)
                                val model = MembersModel()
                                model.address = obj.optString("address")
                                model.city_name = obj.optString("city_name")
                                model.credit_view = obj.optString("credit_view")
                                model.cust_id = obj.optString("cust_id")
                                model.customer_name = obj
                                    .optString("customer_name")
                                model.dealer_count = obj.optString("dealer_count")
                                model.dist_name = obj.optString("dist_name")
                                model.login = obj.optString("login")
                                model.role_id = obj.optString("role_id")
                                model.role_name = obj.optString("role_name")
                                model.shopName = obj.optString("shopName")
                                model.state_code = obj.optString("state_code")
                                model.state_name = obj.optString("state_name")
                                model.user_email = obj.optString("user_email")
                                model.user_id = obj.optString("user_id")
                                model.user_name = obj.optString("user_name")
                                model.user_phone = obj.optString("user_phone")
                                listMembers!!.add(model)
                            }
                            val adapter = MemberAdapter(mContext!!, listMembers!!)
                            listViewMembers!!.setAdapter(adapter)!!
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                }
            })
        }

    fun parseJSON(response: String?) {}
    override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    override fun onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
    }
}