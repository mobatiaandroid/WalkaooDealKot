package com.mobatia.vkcsalesapp.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.CustomerAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.CustomerModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MyCustomersFragment : Fragment(), VKCUrlConstants {
    private var mRootView: View? = null

    var submitLayout: RelativeLayout? = null
    var llTop: LinearLayout? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    var customersList: ListView? = null
    var listCustomers: ArrayList<CustomerModel>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "My Customers"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        mRootView = inflater.inflate(
            R.layout.fragment_my_customers, container,
            false
        )
        setDisplayParams()
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
        listCustomers = ArrayList()
        customersList = v!!.findViewById<View>(R.id.listMyCustomer) as ListView

        // Dealer List Click
        customersList!!.onItemClickListener =
            AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 ->
                // TODO Auto-generated method stub
            }
        customersApi
    }

    // TODO Auto-generated catch block
    private val customersApi: Unit
        private get() {
            listCustomers!!.clear()
            val name = arrayOf("cust_id")
            val values = arrayOf(
                getCustomerId(
                    activity!!
                )
            )
            val manager = VKCInternetManager(VKCUrlConstants.GET_CUSTOMERS)
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        try {
                            val obj = JSONObject(successResponse)
                            val objResponse = obj
                                .optJSONObject("response")
                            val status = objResponse.optString("status")
                            if (status == "Success") {
                                val arrayData = objResponse
                                    .optJSONArray("data")
                                if (arrayData.length() > 0) {
                                    for (i in 0 until arrayData.length()) {
                                        val objItem = arrayData
                                            .optJSONObject(i)
                                        val model = CustomerModel()
                                        model.name = objItem.optString("name")
                                        model.phone = objItem
                                            .optString("phone")
                                        model.role = objItem.optString("role")
                                        listCustomers!!.add(model)
                                    }
                                    val adapter = CustomerAdapter(
                                        activity!!, listCustomers!!
                                    )
                                    customersList!!.adapter = adapter
                                } else {
                                    showtoast(activity, 44)
                                }
                            } else {
                                showtoast(activity, 13)
                            }
                        } catch (e: JSONException) {
                            // TODO Auto-generated catch block
                            e.printStackTrace()
                        }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        showtoast(activity, 17)
                    }
                })
        }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}