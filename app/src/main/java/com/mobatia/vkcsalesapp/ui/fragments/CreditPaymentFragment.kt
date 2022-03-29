/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.CreditPaymentStatusAdapter
import com.mobatia.vkcsalesapp.adapter.DealersListViewAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getStateCode
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.CreditPaymentModel
import com.mobatia.vkcsalesapp.model.DealersCityModel
import com.mobatia.vkcsalesapp.model.DealersDistrictModel
import com.mobatia.vkcsalesapp.model.DealersShopModel
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * Bibin
 */
class CreditPaymentFragment : Fragment(), VKCDbConstants, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mRelDealer: RelativeLayout? = null
    private var mRelRetailer: RelativeLayout? = null

    //	private CustomTextView mTextViewDealer;
    //	private CustomTextView mTextViewRetailer;
    private var mDealersListView: ListView? = null

    //	private SalesOrderAdapter mSalesAdapter;
    //	private DataBaseManager databaseManager;
    //	private CartModel cartModel;
    //	private ArrayList<CartModel> cartArrayList;
    //	private LinearLayout lnrTableHeaders;
    //	private String salesOrderArray;
    //	private ArrayList<DealersListModel> dealersModel;
    var linearTop: LinearLayout? = null
    var rlSearchContainer: RelativeLayout? = null
    var creditModels: ArrayList<CreditPaymentModel>? = null
    var districtModels = ArrayList<DealersDistrictModel>()
    var dealersShopModels: ArrayList<DealersShopModel>? = null
    var dealersListViewAdapter: DealersListViewAdapter? = null
    private var isError = false
    private var imageViewSearch: ImageView? = null
    private var mType: String? = null
    private var mActivity: Activity? = null
    var mIsError = false
    private var etSearchText: EditText? = null
    private var dealersListViewAutoPopulate: ListView? = null

    //	private TextView mTxtRetailer, mTxtDealer;
    //	private ImageView clrRetail, clrDealer;
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_paymentstatus,
            container, false
        )
        setDisplayParams()
        init(mRootView, savedInstanceState)
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_imageview, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
        )
        AppController.isCart = false
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
        creditModels = ArrayList()
        //databaseManager = new DataBaseManager(getActivity());
        mRelDealer = v!!.findViewById<View>(R.id.rlDealer) as RelativeLayout
        mRelRetailer = v.findViewById<View>(R.id.rlRetailer) as RelativeLayout
        //mTextViewDealer = (CustomTextView) v.findViewById(R.id.textViewDealer);
        //mTextViewRetailer = (CustomTextView) v
        //.findViewById(R.id.textViewRetailer);
        mDealersListView = v.findViewById<View>(R.id.dealersListView) as ListView
        linearTop = v.findViewById<View>(R.id.llTop) as LinearLayout
        rlSearchContainer = v.findViewById<View>(R.id.rlSearchContainer) as RelativeLayout
        imageViewSearch = v.findViewById<View>(R.id.imageViewSearch) as ImageView
        etSearchText = v.findViewById<View>(R.id.etSearchText) as EditText
        dealersListViewAutoPopulate =
            v.findViewById<View>(R.id.dealersListViewAutoPopulate) as ListView
        mActivity = activity
        //		mTxtRetailer = (TextView) v.findViewById(R.id.txtRetailer);
//		mTxtDealer = (TextView) v.findViewById(R.id.txtDealer);
        imageViewSearch!!.setOnClickListener(OnClickView())
        //dealersModel = new ArrayList<DealersListModel>();
//		clrRetail = (ImageView) v.findViewById(R.id.imageViewCloseRet);
//		clrDealer = (ImageView) v.findViewById(R.id.imageViewCloseDea);
        if (getUserType(activity!!) == "6" || getUserType(
                activity!!
            ) == "7"
        ) { // UserType:Dealer | SubDealer
            linearTop!!.visibility = View.GONE
            rlSearchContainer!!.visibility = View.GONE
            paymentStatusApi
        } else if (getUserType(activity!!) == "5") { // UserType:Retailer
            linearTop!!.visibility = View.GONE
            rlSearchContainer!!.visibility = View.GONE
            paymentStatusApi
        } else { // Usertype:SalesHead
            mRelDealer!!.isClickable = true
            mRelRetailer!!.isClickable = true
            mRelDealer!!.setOnClickListener(OnClickView())
            mRelRetailer!!.setOnClickListener(OnClickView())
            imageViewSearch!!.setOnClickListener(OnClickView())
            dealersShopModels = ArrayList()
            dealersListViewAdapter = DealersListViewAdapter(
                dealersShopModels!!, mActivity!!
            )
            dealersListViewAutoPopulate!!.adapter = dealersListViewAdapter
            dealersListViewAutoPopulate!!.onItemClickListener = ClickOnItemView()
            mType = "Dealer"
            getDistrictApi(getStateCode(mActivity!!))
            etSearchText!!.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    // TODO Auto-generated method stub
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int, count: Int,
                    after: Int
                ) {
                    // TODO Auto-generated method stub
                }

                override fun afterTextChanged(s: Editable) {
                    // TODO Auto-generated method stub
                    if (0 != etSearchText!!.text.length) {
                        val shopName = etSearchText!!.text.toString()
                        setDealerShopList(shopName)
                    }
                }
            })
            //			mTxtRetailer.setOnClickListener(new OnClickView());
//			mTxtDealer.setOnClickListener(new OnClickView());
//			clrRetail.setOnClickListener(new OnClickView());
//			clrDealer.setOnClickListener(new OnClickView());
        }
    }// isError=true;

    /*******************************************************
     * Method name : getPaymentStatusApi Description : Api call Parameters : nil
     * Return type : void Date : Feb 26, 2015 Author : Archana S
     */
    private val paymentStatusApi: Unit
        private get() {
            creditModels!!.clear()
            var userId: String? = ""
            var type = ""
            var typeUserId: String? = ""
            println(
                "login user id "
                        + getUserType(activity!!)
            )
            userId = if (getUserType(activity!!) == "6") {
                getUserId(activity!!)
            } else if (getUserType(activity!!) == "5") {
                getUserId(activity!!)
            } else {
                getUserId(activity!!)
            }
            if (VKCObjectConstants.selectedDealerId == "") {
                typeUserId = VKCObjectConstants.selectedRetailerId
                type = "2"
            } else {
                typeUserId = VKCObjectConstants.selectedDealerId
                type = "1"
            }
            println("user id credit $userId")
            val name = arrayOf("user_id", "role_id", "customer_id", "type")
            val values = arrayOf(
                userId,
                getUserType(activity!!), typeUserId, type
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.GET_PAYMENT_STATUS
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        if (successResponse != null) {
                            parseResponse(successResponse)
                        }
                        if (creditModels!!.size > 0 && !isError) {
                            setAdapter()
                        } else {
                            showtoast(activity, 17)
                        }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        showtoast(activity, 17)
                        // isError=true;
                    }
                })
        }

    private fun setAdapter() {
        mDealersListView!!.adapter = CreditPaymentStatusAdapter(
            activity!!, creditModels!!
        )
    }

    private fun parseResponse(response: String) {
        try {
            val jsonObjectresponse = JSONObject(response)
            val jsonArrayresponse = jsonObjectresponse
                .getJSONArray(JSON_TAG_SETTINGS_RESPONSE)
            for (j in 0 until jsonArrayresponse.length()) {
                val creditPaymentModel = CreditPaymentModel()
                val jsonObjectZero = jsonArrayresponse.getJSONObject(j)
                creditPaymentModel.setmName(
                    jsonObjectZero
                        .optString("Customer_Name")
                )
                creditPaymentModel.setmId(
                    jsonObjectZero
                        .optString("Customer_ID")
                )
                creditPaymentModel.setmTotalBalance(
                    jsonObjectZero
                        .optString("Total_Balance")
                )
                creditPaymentModel.setmTotalDue(
                    jsonObjectZero
                        .optString("Total_Due")
                )
                creditPaymentModel.setmType1100(
                    jsonObjectZero
                        .optString("1100")
                )
                creditPaymentModel.setmType1200(
                    jsonObjectZero
                        .optString("1200")
                )
                creditPaymentModel.setmType1300(
                    jsonObjectZero
                        .optString("1300")
                )
                creditPaymentModel.setmType1400(
                    jsonObjectZero
                        .optString("1400")
                )
                creditPaymentModel.setmType1500(
                    jsonObjectZero
                        .optString("1500")
                )
                creditPaymentModel.setmType1600(
                    jsonObjectZero
                        .optString("1600")
                )
                creditPaymentModel.setmType2000(
                    jsonObjectZero
                        .optString("2000")
                )
                creditPaymentModel.city = jsonObjectZero.optString("city")
                creditPaymentModel.district = jsonObjectZero
                    .optString("district")
                creditPaymentModel.state_name = jsonObjectZero
                    .optString("state_name")
                creditModels!!.add(creditPaymentModel)
            }
        } catch (e: Exception) {
            isError = true
        }
    }

    inner class OnClickView : View.OnClickListener {
        override fun onClick(v: View) {
            if (v === imageViewSearch) {
                if (etSearchText!!.text.length > 0) {
                    setDealerShopList(etSearchText!!.text.toString())
                    //etSearchText.setText("");
                } else {
                    Toast.makeText(
                        mActivity,
                        "Please enter your search keyword",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                //getPaymentStatusApi();
            } else if (v === mRelDealer) {
                rlSearchContainer!!.visibility = View.VISIBLE
                etSearchText!!.setText("")
                mDealersListView!!.visibility = View.GONE
                mRelDealer!!.setBackgroundColor(mActivity!!.resources.getColor(R.color.dark_red))
                mRelRetailer!!.setBackgroundColor(mActivity!!.resources.getColor(R.color.light_red))
                mType = "Dealer"
                getDistrictApi(getStateCode(mActivity!!))
                dealersShopModels!!.clear()
                dealersListViewAdapter!!.notifyDataSetChanged()
                VKCObjectConstants.selectedRetailerId = ""
                //				ArrayList<DealersShopModel> dealersShopModels=new ArrayList<>();
//				mDealersListView.setAdapter(new DealersListViewAdapter(
//						dealersShopModels, mActivity));
            } else if (v === mRelRetailer) {
                rlSearchContainer!!.visibility = View.VISIBLE
                mDealersListView!!.visibility = View.GONE
                etSearchText!!.setText("")
                mRelDealer!!.setBackgroundColor(mActivity!!.resources.getColor(R.color.light_red))
                mRelRetailer!!.setBackgroundColor(mActivity!!.resources.getColor(R.color.dark_red))
                mType = "Retailer"
                getDistrictApi(getStateCode(mActivity!!))
                dealersShopModels!!.clear()
                dealersListViewAdapter!!.notifyDataSetChanged()
                VKCObjectConstants.selectedDealerId = ""

//				ArrayList<DealersShopModel> dealersShopModels=new ArrayList<>();
//				mDealersListView.setAdapter(new DealersListViewAdapter(
//						dealersShopModels, mActivity));
            }

//			if (v == mTxtDealer) {
//				mTxtRetailer.setText("Select Retailer");
//				Intent intent = new Intent(getActivity(),
//						RetailersListViewOnSearch.class);
//				if (!mTxtDealer.getText().toString()
//						.equalsIgnoreCase("Select Dealer")
//						&& !mTxtDealer.getText().toString()
//								.equalsIgnoreCase("")) {
//					System.out.println("dealer string1 "+mTxtDealer.getText().toString());
//					mTxtRetailer.setClickable(false);
//				} else {
//					System.out.println("dealer string2 "+mTxtDealer.getText().toString());
//					mTxtRetailer.setClickable(true);
//				}
//				intent.putExtra("mType", "Dealer");
//				VKCObjectConstants.selectedRetailerId = "";
//				VKCObjectConstants.textDealer = mTxtDealer;
//				startActivity(intent);
//			}
//			if (v == mTxtRetailer) {
//				mTxtDealer.setText("Select Dealer");
//				Intent intent = new Intent(getActivity(),
//						RetailersListViewOnSearch.class);
//				if (!mTxtRetailer.getText().toString()
//						.equalsIgnoreCase("Select Retailer")
//						&& !mTxtRetailer.getText().toString()
//								.equalsIgnoreCase("")) {
//					System.out.println("retailer string1 "+mTxtRetailer.getText().toString());
//					mTxtDealer.setClickable(false);
//				} else {
//					System.out.println("retailer string2 "+mTxtRetailer.getText().toString());
//					mTxtDealer.setClickable(true);
//				}
//				intent.putExtra("mType", "Retailer");
//				VKCObjectConstants.selectedDealerId = "";
//				VKCObjectConstants.textRetailer = mTxtRetailer;
//				startActivity(intent);
//			}
//			if (v == clrRetail) {
//				mTxtDealer.setClickable(true);
//				mTxtRetailer.setClickable(true);
//				mTxtRetailer.setText("Select Retailer");
//				mDealersListView.setAdapter(null);
//			}
//			if (v == clrDealer) {
//				mTxtDealer.setClickable(true);
//				mTxtRetailer.setClickable(true);
//				mTxtDealer.setText("Select Dealer");
//				mDealersListView.setAdapter(null);
//			}
        }
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        // getPaymentStatusApi();
    }

    private fun getDistrictApi(stateCode: String) {
        var manager: VKCInternetManager? = null
        if (mType == "Retailer") {
            manager = VKCInternetManager(VKCUrlConstants.GET_RETAILERS)
        } else if (mType == "Dealer") {
            manager = VKCInternetManager(VKCUrlConstants.GET_DEALERS)
        }
        val name = arrayOf("state")
        val value = arrayOf(stateCode)
        manager!!.getResponsePOST(mActivity, name, value, object : ResponseListener {
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

    private fun parseDistrictJSON(successResponse: String, stateCode: String?) {
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

    private fun setDealerShopList(searchText: String) {
        dealersShopModels!!.clear()
        for (districtindex in districtModels.indices) {
            val cityModels = districtModels[districtindex].cityModels
            for (cityIndex in cityModels.indices) {
                val shopModels = cityModels[cityIndex].dealersShopModels
                for (shopIndex in shopModels.indices) {
                    if (shopModels[shopIndex].name.toLowerCase()
                            .contains(searchText.toLowerCase())
                    ) {
                        dealersShopModels!!.add(shopModels[shopIndex])
                    }
                }
            }
        }
        if (dealersShopModels!!.size < 1) {
            Toast.makeText(mActivity, "No Item found for this search criteria", Toast.LENGTH_SHORT)
                .show()
        }
        dealersListViewAdapter!!.notifyDataSetChanged()
    }

    private inner class ClickOnItemView : AdapterView.OnItemClickListener {
        /*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.widget.AdapterView.OnItemClickListener#onItemClick(android
		 * .widget.AdapterView, android.view.View, int, long)
		 */
        override fun onItemClick(
            parent: AdapterView<*>?, view: View, position: Int,
            id: Long
        ) {
            // TODO Auto-generated method stub
            rlSearchContainer!!.visibility = View.GONE
            mDealersListView!!.visibility = View.VISIBLE
            if (mType == "Retailer") {
//				VKCObjectConstants.textRetailer.setText(dealersShopModels.get(
//						position).getName());
                VKCObjectConstants.selectedRetailerId = dealersShopModels!![position].id
            } else if (mType == "Dealer") {
//				VKCObjectConstants.textDealer.setText(dealersShopModels.get(
//						position).getName());
                VKCObjectConstants.selectedDealerId = dealersShopModels!![position].id
            }
            //finish();
            paymentStatusApi
        }
    }
}