/**
 *
 */
package com.mobatia.vkcsalesapp.activities

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.DealersListViewAdapter
import com.mobatia.vkcsalesapp.appdialogs.StateDistrictPlaceDialog
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveSelectedUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveSelectedUserName
import com.mobatia.vkcsalesapp.manager.DataBaseManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.DealersCityModel
import com.mobatia.vkcsalesapp.model.DealersDistrictModel
import com.mobatia.vkcsalesapp.model.DealersShopModel
import com.mobatia.vkcsalesapp.model.DealersStateModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * @author mobatia-user
 */
class RetailersListViewOnSearch : AppCompatActivity(), VKCUrlConstants,
    VKCDbConstants {
    private var databaseManager: DataBaseManager? = null
    var rlState: RelativeLayout? = null
    var rlDistrict: RelativeLayout? = null
    var rlPlace: RelativeLayout? = null
    var llTop: LinearLayout? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    var dealersListView: ListView? = null
    var mIsError = false
    var textViewState: TextView? = null
    var textViewDistrict: TextView? = null
    var textViewPlace: TextView? = null
    var imageViewSearch: ImageView? = null
    private var mActivity: Activity? = null
    private var mType: String? = null
    var dealersStateModels = ArrayList<DealersStateModel>()
    var districtModels = ArrayList<DealersDistrictModel>()
    var cityModels = ArrayList<DealersCityModel>()
    var dealersShopModels = ArrayList<DealersShopModel>()
    var key: String = ""
    var userType: String = ""
    private val tableCount = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_dealers_listview)
        databaseManager = DataBaseManager(this)
        mActivity = this
        AppController.isSelectedDealer = false
        val extras = intent.extras
        if (extras != null) {
            mType = extras.getString("mType")
            if (mType == "SalesHead") {
                key = extras.getString("key")!!
                userType = getCustomerCategory(this@RetailersListViewOnSearch)
            }
        }
        initialiseUI()
    }

    private fun initialiseUI() {
        val actionBar = supportActionBar
        actionBar!!.setSubtitle("")
        actionBar.setTitle("")
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
        imageViewSearch = findViewById<View>(R.id.imageViewSearch) as ImageView
        textViewState = findViewById<View>(R.id.textViewState) as TextView
        textViewDistrict = findViewById<View>(R.id.textViewDistrict) as TextView
        textViewPlace = findViewById<View>(R.id.textViewPlace) as TextView
        rlState = findViewById<View>(R.id.rlState) as RelativeLayout
        rlDistrict = findViewById<View>(R.id.rlDistrict) as RelativeLayout
        rlPlace = findViewById<View>(R.id.rlPlace) as RelativeLayout
        llTop = findViewById<View>(R.id.llTop) as LinearLayout
        dealersListView = findViewById<View>(R.id.dealersListView) as ListView
        rlState!!.setOnClickListener(ClickOnView())
        rlDistrict!!.setOnClickListener(ClickOnView())
        rlPlace!!.setOnClickListener(ClickOnView())
        imageViewSearch!!.setOnClickListener(ClickOnView())
        dealersListView!!.onItemClickListener = ClickOnItemView()
        if (VKCUtils.checkInternetConnection(this)) {
            if (getUserType(this) == "7") {
                llTop!!.visibility = View.GONE
                myDealersApi
            } else if (getUserType(this) == "4") {
                llTop!!.visibility = View.GONE
                // getMyDealersApi();
                myDealersSalesHeadApi
            } else {
                stateApi
            }
        } else {
            mIsError = true
            VKCUtils.showtoast(mActivity, 0)
        }
    }

    private fun parseJSON(successResponse: String) {
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

    // TODO Auto-generated method stub
    private val stateApi: Unit
        private get() {
            val manager = VKCInternetManager(
                VKCUrlConstants.DEALERS_GETSTATE
            )
            manager.getResponse(mActivity, object : ResponseListener {
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
        var manager: VKCInternetManager? = null
        if (mType == "Retailer") {
            manager = VKCInternetManager(VKCUrlConstants.GET_RETAILERS)
        } else if (mType == "Dealer") {
            manager = VKCInternetManager(VKCUrlConstants.GET_DEALERS)
        } else if (mType == "SalesHead") {
            manager = VKCInternetManager(VKCUrlConstants.LIST_MY_DEALERS_SALES_HEAD_URL)
        }
        val name = arrayOf("state")
        val value = arrayOf(stateCode)
        manager!!.getResponsePOST(mActivity, name, value, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseDistrictJSON(successResponse, stateCode)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                mIsError = true
            }
            /*override fun responseSuccess(successResponse: String) {
            }

            override fun responseFailure(failureResponse: String) {
                // TODO Auto-generated method stub
            }*/
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
        val dealersShopModels = ArrayList<DealersShopModel>()
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
        dealersShopModel?.address = jsonObject.optString("address")
        dealersShopModel?.city = jsonObject.optString("city")
        dealersShopModel?.contact_person = jsonObject
            .optString("contact_person")
        dealersShopModel?.dealerId = jsonObject.optString("dealerId")
        dealersShopModel?.country = jsonObject.optString("country")
        dealersShopModel?.customer_id = jsonObject.optString("customer_id")
        dealersShopModel?.id = jsonObject.optString("id")
        dealersShopModel?.name = jsonObject.optString("name")
        dealersShopModel?.phone = jsonObject.optString("phone")
        dealersShopModel?.pincode = jsonObject.optString("pincode")
        dealersShopModel?.state = jsonObject.optString("state")
        dealersShopModel?.state_name = jsonObject.optString("state_name")
        return dealersShopModel
    }

    private inner class ClickOnView : View.OnClickListener {
        override fun onClick(v: View) {
            // TODO Auto-generated method stub
            if (v === rlState) {
                dealersListView!!.visibility = View.INVISIBLE
                val states = arrayOfNulls<String>(dealersStateModels.size)
                for (i in dealersStateModels.indices) {
                    states[i] = dealersStateModels[i].stateName
                }
                val dialog = textViewState?.let {
                    StateDistrictPlaceDialog(
                        mActivity as FragmentActivity, states, it, "States",
                        object : StateDistrictPlaceDialog.OnDialogItemSelectListener {
                            override fun itemSelected(position: Int) {
                                // TODO Auto-generated method stub
                                getDistrictApi(
                                    dealersStateModels[position]
                                        .stateCode
                                )
                            }
                        })
                }
                dialog?.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                dialog?.setCancelable(true)
                dialog?.show()
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
                    val dialog = textViewDistrict?.let {
                        StateDistrictPlaceDialog(
                            this@RetailersListViewOnSearch, district, it, "District",
                            object : StateDistrictPlaceDialog.OnDialogItemSelectListener {
                                override fun itemSelected(position: Int) {
                                    // TODO Auto-generated method stub
                                    cityModels.clear()
                                    cityModels.addAll(
                                        districtModels[position].cityModels
                                    )
                                }
                            })
                    }
                    dialog?.window!!.setBackgroundDrawable(
                        ColorDrawable(Color.TRANSPARENT)
                    )
                    dialog?.setCancelable(true)
                    dialog?.show()
                    textViewPlace!!.text = ""
                } else {
                    VKCUtils.showtoast(mActivity, 20)
                }
            }
            if (v === rlPlace) {
                dealersListView!!.visibility = View.INVISIBLE
                if (textViewDistrict!!.text.length != 0) {
                    val city = arrayOfNulls<String?>(cityModels.size)
                    for (i in cityModels.indices) {
                        city[i] = cityModels[i].cityName
                    }
                    val dialog = textViewPlace?.let {
                        StateDistrictPlaceDialog(
                            this@RetailersListViewOnSearch, city, it, "Place",
                            object : StateDistrictPlaceDialog.OnDialogItemSelectListener {
                                override fun itemSelected(position: Int) {
                                    dealersShopModels.clear()
                                    dealersShopModels.addAll(
                                        cityModels[position].dealersShopModels
                                    )
                                }
                            })
                    }
                    dialog?.window!!.setBackgroundDrawable(
                        ColorDrawable(Color.TRANSPARENT)
                    )
                    dialog?.setCancelable(true)
                    dialog?.show()
                } else {
                    VKCUtils.showtoast(mActivity, 21)
                }
            }
            if (v === imageViewSearch) {
                if (textViewPlace!!.text.length != 0) {
                    setDealerShopList(dealersShopModels)
                } else {
                    VKCUtils.showtoast(mActivity, 22)
                }
            }
        }
    }

    private fun setDealerShopList(dealersShopModels: ArrayList<DealersShopModel>) {
        dealersListView!!.visibility = View.VISIBLE
        dealersListView!!.adapter = DealersListViewAdapter(
            dealersShopModels, mActivity!!
        )
    }

    private inner class ClickOnItemView : AdapterView.OnItemClickListener {
        override fun onItemClick(
            parent: AdapterView<*>?, view: View, position: Int,
            id: Long
        ) {
            // TODO Auto-generated method stub
            if (mType == "Retailer") {
                VKCObjectConstants.textRetailer!!.text = dealersShopModels[position].name
                VKCObjectConstants.selectedRetailerId = dealersShopModels[position].id
                finish()
            } else if (mType == "Dealer") {
                VKCObjectConstants.textDealer!!.text = dealersShopModels[position].name
                VKCObjectConstants.selectedDealerId = dealersShopModels[position].id
                finish()
            } else if (mType == "SubDealer") {
                VKCObjectConstants.textDealer!!.text = dealersShopModels[position].name
                val did = dealersShopModels[position].dealerId
                Log.i("dis", "" + did)
                VKCObjectConstants.selectedDealerId = dealersShopModels[position].dealerId
                saveSelectedUserId(
                    this@RetailersListViewOnSearch,
                    dealersShopModels[position].dealerId
                )
                AppController.isSelectedDealer = true
                finish()
            } else if (mType == "SalesHead") {
                VKCObjectConstants.textDealer!!.text = dealersShopModels[position].name
                saveSelectedUserName(
                    this@RetailersListViewOnSearch,
                    dealersShopModels[position].name
                )
                saveSelectedUserId(
                    this@RetailersListViewOnSearch,
                    dealersShopModels[position].dealerId
                )
                val did = dealersShopModels[position].dealerId
                Log.i("dis", "" + did)
                VKCObjectConstants.selectedDealerId = dealersShopModels[position].id
                AppController.isSelectedDealer = true
                clearDb()
                finish()
            }
        }
    }

    // TODO Auto-generated method stub
    private val myDealersApi: Unit
        private get() {
            var manager: VKCInternetManager? = null
            dealersShopModels.clear()
            val name = arrayOf("subdealer_id")
            val value = arrayOf(getUserId(this))
            manager = VKCInternetManager(VKCUrlConstants.LIST_MY_DEALERS_URL)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    if (successResponse != null) {
                        parseMyDealerJSON(successResponse)
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    mIsError = true
                }

            })
        }

    private fun parseMyDealerJSON(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            val respObj = JSONObject(successResponse)
            val response = respObj.getJSONObject("response")
            val status = response.getString("status")
            if (status == "Success") {
                val respArray = response.getJSONArray("dealers")
                for (i in 0 until respArray.length()) {
                    dealersShopModels
                        .add(parseShop(respArray.getJSONObject(i)))
                }
                dealersListView!!.visibility = View.VISIBLE
                dealersListView!!.adapter = DealersListViewAdapter(
                    dealersShopModels, mActivity!!
                )
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }// TODO Auto-generated method stub

    //	To Get The Subdealer or Dealers List
    private val myDealersSalesHeadApi: Unit
        private get() {
            var manager: VKCInternetManager? = null
            dealersShopModels.clear()
            val name = arrayOf("saleshead_id", "customer_type", "search_value")
            val value = arrayOf(getUserId(this), userType, key)
            manager = VKCInternetManager(VKCUrlConstants.LIST_MY_DEALERS_SALES_HEAD_URL)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    if (successResponse != null) {
                        parseMyDealerSalesHeadJSON(successResponse)
                    }
                    if (dealersShopModels.size == 0) {
                        val toast = CustomToast(mActivity!!)
                        toast.show(37)
                    }                }

                override fun responseFailure(failureResponse: String?) {
                    mIsError = true
                }
                /*override fun responseSuccess(successResponse: String) {
                    parseMyDealerSalesHeadJSON(successResponse)
                    if (dealersShopModels.size == 0) {
                        val toast = CustomToast(mActivity!!)
                        toast.show(37)
                    }
                }

                override fun responseFailure(failureResponse: String) {
                    // TODO Auto-generated method stub
                    mIsError = true
                }*/
            })
        }

    private fun parseMyDealerSalesHeadJSON(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            val respObj = JSONObject(successResponse)
            val response = respObj.getJSONObject("response")
            val status = response.getString("status")
            if (status == "Success") {
                val respArray = response.getJSONArray("dealers")
                for (i in 0 until respArray.length()) {
                    dealersShopModels
                        .add(parseShop(respArray.getJSONObject(i)))
                }
                dealersListView!!.visibility = View.VISIBLE
                dealersListView!!.adapter = DealersListViewAdapter(
                    dealersShopModels, mActivity!!
                )
                if (dealersShopModels.size == 0) {
                    val toast = CustomToast(mActivity!!)
                    toast.show(43)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    public override fun onResume() {
        super.onResume()
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun clearDb() {
        val databaseManager = DataBaseManager(
            this@RetailersListViewOnSearch
        )
        databaseManager.removeDb(VKCDbConstants.TABLE_SHOPPINGCART)
    }
}