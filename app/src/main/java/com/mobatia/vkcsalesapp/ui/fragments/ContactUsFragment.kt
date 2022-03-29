package com.mobatia.vkcsalesapp.ui.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.DealersListViewAdapter
import com.mobatia.vkcsalesapp.adapter.SubDealersListViewAdapter
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getDealerCount
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils.checkInternetConnection
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.DealersCityModel
import com.mobatia.vkcsalesapp.model.DealersDistrictModel
import com.mobatia.vkcsalesapp.model.DealersShopModel
import com.mobatia.vkcsalesapp.model.DealersStateModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ContactUsFragment() : Fragment(), VKCUrlConstants {
    private var mRootView: View? = null
    var rlState: RelativeLayout? = null
    var rlDistrict: RelativeLayout? = null
    var rlPlace: RelativeLayout? = null
    var submitLayout: RelativeLayout? = null
    var llTop: LinearLayout? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    var dealersListView: ListView? = null
    var mFragmentTransaction: FragmentTransaction? = null
    var mIsError = false
    var textViewState: TextView? = null
    var textViewDistrict: TextView? = null
    var textViewPlace: TextView? = null
    var mFragmentManager: FragmentManager? = null
    var imageViewSearch: ImageView? = null
    var dealersStateModels = ArrayList<DealersStateModel>()
    var districtModels = ArrayList<DealersDistrictModel>()
    var cityModels = ArrayList<DealersCityModel>()
    var dealersShopModels = ArrayList<DealersShopModel>()
    var mContext: Context? = null
    var roleId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_dealers_listview,
            container, false
        )
        mContext = activity
        roleId = getUserType(mContext!!)
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        AppController.isCart = false
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_imageview, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
        )
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
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

    private fun setLayoutAlignment(v: View?) {
        val height = (mDisplayHeight * .35).toInt()
        llTop!!.layoutParams.height = height
        val secOneLL = v!!.findViewById<View>(R.id.secOneLL) as LinearLayout
        val secTwoLL = v.findViewById<View>(R.id.secTwoLL) as LinearLayout
        val secThreeLL = v
            .findViewById<View>(R.id.secThreeLL) as LinearLayout
        val secFourRL = v
            .findViewById<View>(R.id.secFourRL) as RelativeLayout
        secOneLL.layoutParams.height = height / 4
        secTwoLL.layoutParams.height = height / 4
        secThreeLL.layoutParams.height = height / 4
        secFourRL.layoutParams.height = height / 4
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        imageViewSearch = v!!.findViewById<View>(R.id.imageViewSearch) as ImageView
        submitLayout = v.findViewById<View>(R.id.submitMyDealer) as RelativeLayout
        textViewState = v.findViewById<View>(R.id.textViewState) as TextView
        textViewDistrict = v.findViewById<View>(R.id.textViewDistrict) as TextView
        textViewPlace = v.findViewById<View>(R.id.textViewPlace) as TextView
        rlState = v.findViewById<View>(R.id.rlState) as RelativeLayout
        rlDistrict = v.findViewById<View>(R.id.rlDistrict) as RelativeLayout
        rlPlace = v.findViewById<View>(R.id.rlPlace) as RelativeLayout
        llTop = v.findViewById<View>(R.id.llTop) as LinearLayout
        dealersListView = v.findViewById<View>(R.id.dealersListView) as ListView

        // Dealer List Click
        dealersListView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                arg0: AdapterView<*>?, arg1: View, arg2: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                val id: String = dealersShopModels.get(arg2).id
                if (AppController.listDealers?.size == 0) {
                    if (AppController.listDealers?.size!! < 7) AppController.listDealers!!.add(id) else Toast.makeText(
                        activity,
                        "Only six dealers allowed to add",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        setLayoutAlignment(v)
        rlState!!.setOnClickListener(ClickOnView())
        rlDistrict!!.setOnClickListener(ClickOnView())
        rlPlace!!.setOnClickListener(ClickOnView())
        imageViewSearch!!.setOnClickListener(ClickOnView())
        if (checkInternetConnection((activity)!!)) {
            stateApi
        } else {
            mIsError = true
            // CustomToast toast = new CustomToast(getActivity());
            // toast.show(0);
            showtoast(activity, 0)
        }
    }

    private fun parseJSON(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            val respObj = JSONObject(successResponse)
            val respArray = respObj.getJSONArray("states")
            for (i in 0 until respArray.length()) {
                // JSONObject objState = respArray.getJSONObject(i);
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

    // TODO Auto-generated method stub
    private val stateApi: Unit
        private get() {
            val manager = VKCInternetManager(
                VKCUrlConstants.DEALERS_GETSTATE
            )
            manager.getResponse(activity, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    if (successResponse != null) {
                        parseJSON(successResponse)
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    mIsError = true
                }
            })
        }

    private fun getDistrictApi(stateCode: String) {
        val manager = VKCInternetManager(VKCUrlConstants.GET_RETAILERS)
        val name = arrayOf("state")
        val value = arrayOf(stateCode)
        manager.getResponsePOST(
            activity, name, value,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {

                    // parseJSON(successResponse);
                    if (successResponse != null) {
                        parseDistrictJSON(successResponse, stateCode)
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    mIsError = true
                }
            })
    }

    private fun parseDistrictJSON(successResponse: String, stateCode: String) {
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
        val arrayShops: JSONArray
        try {
            arrayShops = jsonObject.getJSONArray("city")
            for (i in 0 until arrayShops.length()) {
                cityModels.add(parseCity(arrayShops.getJSONObject(i)))
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        dealersDistrictModel.cityModels = cityModels
        return dealersDistrictModel
    }

    private fun parseCity(jsonObject: JSONObject): DealersCityModel {
        dealersShopModels = ArrayList()
        val cityModel = DealersCityModel()
        cityModel.cityName = jsonObject.optString("city")
        val arrayShops: JSONArray
        try {
            arrayShops = jsonObject.getJSONArray("shop")
            for (i in 0 until arrayShops.length()) {
                dealersShopModels.add(parseShop(arrayShops.getJSONObject(i)))
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        cityModel.dealersShopModels = dealersShopModels
        return cityModel
    }

    private fun parseShop(jsonObject: JSONObject): DealersShopModel {
        val dealersShopModel = DealersShopModel()
        dealersShopModel.address = jsonObject.optString("address")
        dealersShopModel.city = jsonObject.optString("city")
        dealersShopModel.contact_person = jsonObject
            .optString("contact_person")
        dealersShopModel.country = jsonObject.optString("country")
        dealersShopModel.customer_id = jsonObject.optString("customer_id")
        dealersShopModel.id = jsonObject.optString("id")
        dealersShopModel.name = jsonObject.optString("name")
        dealersShopModel.phone = jsonObject.optString("phone")
        dealersShopModel.pincode = jsonObject.optString("pincode")
        dealersShopModel.state = jsonObject.optString("state")
        dealersShopModel.state_name = jsonObject.optString("state_name")
        return dealersShopModel
    }

    private inner class ClickOnView() : View.OnClickListener {
        override fun onClick(v: View) {
            // TODO Auto-generated method stub
            if (v === rlState) {
                dealersListView!!.visibility = View.INVISIBLE
                val states = arrayOfNulls<String>(dealersStateModels.size)
                for (i in dealersStateModels.indices) {
                    states[i] = dealersStateModels[i].stateName
                }
                val dialog = StateDistrictPlaceDialog(
                    activity!!, states, textViewState!!, "States",
                    object : StateDistrictPlaceDialog.OnDialogItemSelectListener {
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
                textViewPlace!!.text = ""
                textViewDistrict!!.text = ""
            }
            if (v === rlDistrict) {
                dealersListView!!.visibility = View.INVISIBLE
                if (textViewState!!.text.length != 0) {
                    val district = arrayOfNulls<String>(districtModels.size)
                    for (i in districtModels.indices) {
                        district[i] = districtModels[i].districtName
                    }
                    val dialog = StateDistrictPlaceDialog(
                        activity!!, district, textViewDistrict!!,
                        "District", object : StateDistrictPlaceDialog.OnDialogItemSelectListener {
                            override fun itemSelected(position: Int) {
                                // TODO Auto-generated method stub
                                cityModels.clear()
                                cityModels.addAll(districtModels[position].cityModels)
                            }
                        })
                    dialog.window!!.setBackgroundDrawable(
                        ColorDrawable(Color.TRANSPARENT)
                    )
                    dialog.setCancelable(true)
                    dialog.show()
                    textViewPlace!!.text = ""
                } else {
                    // CustomToast toast = new CustomToast(getActivity());
                    // toast.show(20);
                    showtoast(activity, 20)
                }
            }
            if (v === rlPlace) {
                dealersListView!!.visibility = View.INVISIBLE
                if (textViewDistrict!!.text.length != 0) {
                    val city = arrayOfNulls<String>(cityModels.size)
                    for (i in cityModels.indices) {
                        city[i] = cityModels[i].cityName
                    }
                    val dialog = StateDistrictPlaceDialog(
                        activity!!, city, textViewPlace!!, "Place",
                        object : StateDistrictPlaceDialog.OnDialogItemSelectListener {
                            override fun itemSelected(position: Int) {
                                dealersShopModels.clear()
                                dealersShopModels.addAll(cityModels[position].dealersShopModels)
                            }
                        })
                    dialog.window!!.setBackgroundDrawable(
                        ColorDrawable(Color.TRANSPARENT)
                    )
                    dialog.setCancelable(true)
                    dialog.show()
                } else {
                    showtoast(activity, 21)
                }
            }
            if (v === imageViewSearch) {
                if (textViewPlace!!.text.length != 0) {
                    setDealerShopList(dealersShopModels)
                } else {
                    showtoast(activity, 22)
                }
            }
        }
    }

    private fun setDealerShopList(dealersShopModels: ArrayList<DealersShopModel>) {
        dealersListView!!.visibility = View.VISIBLE
        val dealerCount = getDealerCount((activity)!!)
        val Count_Dealer = dealerCount!!.toInt()
        if ((roleId == "7") && Count_Dealer == 0) {
            if (dealersShopModels.size > 0) {
                dealersListView!!.adapter = SubDealersListViewAdapter(
                    dealersShopModels, activity!!
                )
                submitLayout!!.visibility = View.VISIBLE
            } else {
                submitLayout!!.visibility = View.GONE
            }
        } else {
            dealersListView!!.adapter = DealersListViewAdapter(
                dealersShopModels, activity!!
            )
        }
    }

    private fun submitMyDealersApi() {
        val manager = VKCInternetManager(
            VKCUrlConstants.SUBMIT_MY_DEALER_URL
        )
        // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
        val name = arrayOf("subdealer_id", "dealers_id")
        val value = arrayOf(
            getUserId(
                (activity)!!
            ),
            AppController.listDealers.toString()
        )
        manager.getResponsePOST(
            activity, name, value,
            object : ResponseListener {
                private val mHomeFragment: HomeFragment? = null
                override fun responseSuccess(successResponse: String?) {

                    // parseJSON(successResponse);
                    try {
                        val responseObj = JSONObject(
                            successResponse
                        )
                        val response = responseObj.getString("response")
                        val resultObj = JSONObject(response)
                        val status = resultObj.getString("status")
                        Toast.makeText(
                            activity, status,
                            Toast.LENGTH_SHORT
                        ).show()
                        if ((status == "Success")) {
                            AppController.listDealers?.clear()
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    mIsError = true
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