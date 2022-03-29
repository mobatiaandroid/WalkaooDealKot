/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
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
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter
import com.mobatia.vkcsalesapp.activities.RetailersListViewOnSearch
import com.mobatia.vkcsalesapp.adapter.SalesOrderAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import com.mobatia.vkcsalesapp.customview.CustomTextView
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.*
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getLoginCustomerName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getLoginPlace
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSelectedUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSelectedUserName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getStateCode
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveCustomerCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setDate
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setFillTable
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setIsCallPendingAPI
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog
import com.mobatia.vkcsalesapp.model.CartModel
import com.mobatia.vkcsalesapp.model.DealersListModel
import com.mobatia.vkcsalesapp.model.DealersShopModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Bibin *
 */
class SalesOrderFragment() : Fragment(), VKCDbConstants, VKCUrlConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mRelDealer: RelativeLayout? = null
    private var mRelRetailer: RelativeLayout? = null
    private var mTextViewDealer: CustomTextView? = null
    private var mTextViewRetailer: CustomTextView? = null
    private var mDealersListView: ListView? = null
    private var mSalesAdapter: SalesOrderAdapter? = null
    private var databaseManager: DataBaseManager? = null
    private var cartModel: CartModel? = null
    private var lnrTableHeaders: LinearLayout? = null
    private var imageViewSubmit: ImageView? = null
    private var imageSearchCat: ImageView? = null
    private var imageViewClear: ImageView? = null
    private val salesOrderArray: String? = null
    private var dealersModel: ArrayList<DealersListModel>? = null
    private var lnrOne: LinearLayout? = null
    private var llCategory: LinearLayout? = null
    private var llSearch: LinearLayout? = null
    private var llTop: LinearLayout? = null
    private var txtRefr: TextView? = null
    private var txtDate: TextView? = null
    private var txtQty: TextView? = null
    private var txtValue: TextView? = null
    private var txtName: TextView? = null
    var labelText: TextView? = null
    private var txtPlace: TextView? = null
    private var textRetailer: TextView? = null
    var edtSearch: EditText? = null
    var testSearch: String? = null
    var categories: MutableList<String> = ArrayList()
    var spinner: Spinner? = null
    private var item: String? = null
    private var type: String? = null
    var dealersShopModels = ArrayList<DealersShopModel>()
    var pDialog: CustomProgressBar? = null
    var txtTotalItem: TextView? = null
    var txtTotalQty: TextView? = null
    var isClicked = false
    var savedInstanceState: Bundle? = null
    private var tableCount = 0
    var callPending = false
    var rlCategory: RelativeLayout? = null
    private var txtHint: TextView? = null
    private var txtCartValue: TextView? = null
    private var textCreditValue: TextView? = null
    var creditValue: String? = null
    var creditPrice: Long = 0
    var cartPrice: Long = 0
    private val FIVE_SECONDS = 10000
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_salesorder, container,
            false
        )
        /*
		 * getActivity().getSupportActionBar().setLogo(R.drawable.back);
		 * getActivity().getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		 * getActivity().getSupportActionBar().setHomeButtonEnabled(true);
		 */
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
        textviewTitle.text = "My Cart"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setDisplayParams()
        AppController.isCart = true
        AppController.isSelectedDealer = false
        init(mRootView, savedInstanceState)
        callPending = true
        val userType = getUserType((activity)!!)

        /*
		 * setCartQuantity(); updateCartValue(); getCreditValue();
		 */if (getUserType((activity)!!) != "4") {

            /*
			 * Date now = new Date(); Date alsoNow =
			 * Calendar.getInstance().getTime(); String nowAsString = new
			 * SimpleDateFormat("yyyy-MM-dd").format(now);
			 */
            /*
			 * for (int i = 0; i < AppController.cartArrayList.size(); i++) { if
			 * (AppController.cartArrayList.get(i).getStatus()
			 * .equals("pending")) { callPending = false; break; } else {
			 * callPending = true; } }
			 */

            // Don't Call Pending quantity if the cart contains pending orders
            // if (callPending) {

            // schedulePendingOrder();
            getCreditValue()
            pendingQuantity
            // } else if (AppController.isSelectedDealer)// In the case of
            // SubDealer check if he
            // selected dealer
            // {
            // getPendingQuantity();
            // }
            cartData
            /*
			 * } }
			 */
            // }
        } else {
            if (getSelectedUserId((activity)!!) != "") {
                mTextViewDealer!!.text = getSelectedUserName((activity)!!)
                getCreditValue()
                pendingQuantity
            } else {
                val toast = CustomToast((activity)!!)
                toast.show(56)
            }
            // getCartData();
        }
        return mRootView
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> {
            }
        }
        return (super.onOptionsItemSelected(item))
    }

    private fun setDisplayParams() {
        val displayManagerScale = DisplayManagerScale(
            activity!!
        )
        mDisplayHeight = displayManagerScale.deviceHeight
        mDisplayWidth = displayManagerScale.deviceWidth
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        databaseManager = DataBaseManager(activity!!)
        mRelDealer = v!!.findViewById<View>(R.id.rlDealer) as RelativeLayout
        mRelRetailer = v.findViewById<View>(R.id.rlRetailer) as RelativeLayout
        mTextViewDealer = v.findViewById<View>(R.id.textViewDealer) as CustomTextView
        mTextViewRetailer = v
            .findViewById<View>(R.id.textViewRetailer) as CustomTextView
        mDealersListView = v.findViewById<View>(R.id.dealersListView) as ListView
        lnrTableHeaders = v.findViewById<View>(R.id.ll2) as LinearLayout
        imageViewSubmit = v.findViewById<View>(R.id.imageViewSearch) as ImageView
        imageViewClear = v.findViewById<View>(R.id.imageViewClear) as ImageView
        imageSearchCat = v.findViewById<View>(R.id.imageViewSearchCat) as ImageView
        llTop = v.findViewById<View>(R.id.llTop) as LinearLayout
        lnrOne = v.findViewById<View>(R.id.lnrOne) as LinearLayout
        txtRefr = v.findViewById<View>(R.id.txtReferenceNumber) as TextView
        txtDate = v.findViewById<View>(R.id.txtDate) as TextView
        edtSearch = v.findViewById<View>(R.id.editSearch) as EditText
        textRetailer = v.findViewById<View>(R.id.textRetailer) as TextView
        rlCategory = v.findViewById<View>(R.id.rlCategory) as RelativeLayout
        llCategory = v.findViewById<View>(R.id.secCatLL) as LinearLayout
        llSearch = v.findViewById<View>(R.id.secSearchLL) as LinearLayout
        labelText = v.findViewById<View>(R.id.textView1) as TextView
        txtTotalItem = v.findViewById<View>(R.id.textTotalItem) as TextView
        txtTotalQty = v.findViewById<View>(R.id.textTotalQty) as TextView
        txtHint = v.findViewById<View>(R.id.hintText) as TextView
        edtSearch!!.setText("")
        txtCartValue = v.findViewById<View>(R.id.textCartValueCart) as TextView
        textCreditValue = v.findViewById<View>(R.id.textCreditValueCart) as TextView
        AppController.isClickedCartAdapter = false
        spinner = v.findViewById<View>(R.id.spinner) as Spinner
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MMM-yyyy")
        val formattedDate = df.format(c.time)
        txtDate!!.text = "Date :  $formattedDate"
        txtName = v.findViewById<View>(R.id.txtName) as TextView
        txtName!!.text = ("Name : "
                + getLoginCustomerName((activity)!!))
        txtPlace = v.findViewById<View>(R.id.txtPlace) as TextView
        txtPlace!!.text = ("Place : "
                + getLoginPlace((activity)!!))
        txtQty = v.findViewById<View>(R.id.txtTotalQty) as TextView
        txtValue = v.findViewById<View>(R.id.txtTotalValue) as TextView
        dealersModel = ArrayList()
        imageViewSubmit!!.isEnabled = true
        mDealersListView!!.visibility = View.VISIBLE
        categories.clear()
        categories.add("Please Select")
        categories.add("Dealer")
        categories.add("Sub Dealer")
        val dataAdapter = ArrayAdapter(
            (activity)!!, android.R.layout.simple_spinner_item, categories
        )

        // Drop down layout style - list view with radio button
        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner!!.adapter = dataAdapter
        imageViewClear!!.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
            val appDeleteDialog: DeleteAlert = DeleteAlert(
                activity!!
            )
            appDeleteDialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            appDeleteDialog.setCancelable(true)
            appDeleteDialog.show()
        })
        /*spinner.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				isClicked = true;
				return false;
			}
		});*/spinner!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>, arg1: View,
                position: Int, arg3: Long
            ) {
                // TODO Auto-generated method stub
                item = arg0.getItemAtPosition(position).toString()
                //	if (isClicked) {
                if ((item == "Dealer")) {
                    type = "1"
                    saveCustomerCategory(
                        (activity)!!,
                        "1"
                    )
                    labelText!!.text = "Dealer : "
                    if (getSelectedUserName((activity)!!)!!.length > 0) {
                        mTextViewDealer!!.text = getSelectedUserName((activity)!!)
                    } else {
                        mTextViewDealer!!.text = "Dealer Name"
                    }
                } else if ((item == "Sub Dealer")) {
                    type = "2"
                    labelText!!.text = "Sub Dealer : "
                    mTextViewDealer!!.text = "Sub Dealer Name"
                    saveCustomerCategory(
                        (activity)!!,
                        "2"
                    )
                }

                //}
                //	isClicked = false;
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        mTextViewDealer!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
                if (isClicked) {
                    //	clearDb();
                    // AppController.cartArrayList.clear();
                    //getPendingQuantity();
                    //getCartData();
                    mTextViewDealer!!.text = ""
                    txtQty!!.text = "Total quantity :  " + "" + 0
                } else if (AppController.isSelectedDealer) {
                    pendingQuantity
                    getCreditValue()
                    cartData
                }
                /*else if(AppPrefenceManager
						.getSelectedUserName(getActivity()).length()>0)
				{
					mTextViewDealer.setText(AppPrefenceManager
							.getSelectedUserName(getActivity()));
				}*/

                /*
				 * else {
				 * 
				 * clearDb(); // AppController.cartArrayList.clear();
				 * getPendingQuantity(); getCreditValue();
				 * 
				 * getCartData();
				 * 
				 * //txtQty.setText("Total quantity :  " + "" + 0);
				 * 
				 * }
				 */
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
                /*if (AppController.isSelectedDealer) {
					getPendingQuantity();
					getCreditValue();
					getCartData();
				}*/
            }
        })
        if ((getCustomerCategory((activity)!!) == "1")) {
            spinner!!.setSelection(1)
            labelText!!.text = "Dealer : "
            mTextViewDealer!!.text = getSelectedUserName((activity)!!)
        } else if ((getCustomerCategory((activity)!!)
                    == "2")
        ) {
            spinner!!.setSelection(2)
            labelText!!.text = "Sub Dealer : "
            mTextViewDealer!!.text = getSelectedUserName((activity)!!)
        } else {
            spinner!!.setSelection(0)
        }
        if ((getUserType((activity)!!) == "6")) { // UserType:Dealer
            mRelDealer!!.isClickable = false
            mRelRetailer!!.isClickable = false
            VKCObjectConstants.selectedDealerId = ""
            VKCObjectConstants.selectedSubDealerId = ""
            VKCObjectConstants.selectedRetailerId = ""
            mTextViewDealer!!.text = getUserName((activity)!!)
            llTop!!.visibility = View.GONE
            lnrOne!!.visibility = View.VISIBLE
            llCategory!!.visibility = View.GONE
        } else if ((getUserType((activity)!!) == "5")) { // UserType:Retailer
            llCategory!!.visibility = View.GONE
            llSearch!!.visibility = View.GONE
            mRelDealer!!.isClickable = true
            mRelRetailer!!.isClickable = false
            VKCObjectConstants.selectedSubDealerId = ""
            VKCObjectConstants.selectedRetailerId = ""
            mTextViewRetailer!!.text = getUserName((activity)!!)
            mRelDealer!!.setOnClickListener(OnClickView())
            llCategory!!.visibility = View.GONE
        } else if ((getUserType((activity)!!) == "7")) { // UserType:Sub
            // Dealer
            rlCategory!!.visibility = View.GONE
            llCategory!!.visibility = View.GONE //
            mRelRetailer!!.visibility = View.GONE
            llSearch!!.visibility = View.GONE
            textRetailer!!.visibility = View.GONE
            txtHint!!.visibility = View.GONE
            mTextViewDealer!!.text = "Select Dealer"
            VKCObjectConstants.selectedSubDealerId = ""
            VKCObjectConstants.selectedRetailerId = ""
            mRelDealer!!.isClickable = true
            mRelDealer!!.setOnClickListener(OnClickView())
        } else if ((getUserType((activity)!!) == "4")) {
            // Dealer
            //llSearch.setVisibility(View.VISIBLE);
            //llCategory.setVisibility(View.VISIBLE);
            mRelRetailer!!.visibility = View.GONE
            textRetailer!!.visibility = View.GONE
            txtHint!!.visibility = View.GONE
            // mTextViewDealer.setText("Dealer");
            VKCObjectConstants.selectedRetailerId = ""
            /*
			 * mRelDealer.setClickable(true); mRelDealer.setOnClickListener(new
			 * OnClickView());
			 */
        } else { // Usertype:SalesHead
            mRelDealer!!.isClickable = true
            llCategory!!.visibility = View.VISIBLE
            llSearch!!.visibility = View.VISIBLE
            VKCObjectConstants.selectedSubDealerId = ""
            // mRelRetailer.setClickable(true);
            // mRelDealer.setOnClickListener(new OnClickView());
            // mRelRetailer.setOnClickListener(new OnClickView());
            llCategory!!.setOnClickListener(OnClickView())
        }
        imageViewSubmit!!.setOnClickListener(OnClickView())
        imageSearchCat!!.setOnClickListener(OnClickView())
    }/*
			 * txtTotalItem.setText("Total Item : " +
			 * String.valueOf(cursor.getCount()));
			 */
    // txtQty.setText("Total quantity :  " + "" + 0);
    // setCartQuantity();
// txtQty.setText("Total quantity :  " + "" + 0);
    // setCartQuantity();
/*
				 * int totalQty = 0; for (int i = 0; i <
				 * AppController.cartArrayList.size(); i++) { totalQty =
				 * totalQty + Integer.parseInt(AppController.cartArrayList.get(
				 * i).getProdQuantity()); }
				 */
    // txtQty.setText("Total quantity :  " + "" + totalQty);
/*
			 * txtTotalItem.setText("Total Item : " +
			 * String.valueOf(cursor.getCount()));
			 *//*
		 * System.out.println("13022015:cursor.getCount()::" +
		 * cursor.getCount());
		 */
    /**
     * Method to retrieve items added to cart
     *
     */
    private val cartData: Unit
        private get() {
            AppController.cartArrayList.clear()
            val cols = arrayOf(
                VKCDbConstants.PRODUCT_ID,
                VKCDbConstants.PRODUCT_NAME,
                VKCDbConstants.PRODUCT_SIZEID,
                VKCDbConstants.PRODUCT_SIZE,
                VKCDbConstants.PRODUCT_COLORID,
                VKCDbConstants.PRODUCT_COLOR,
                VKCDbConstants.PRODUCT_QUANTITY,
                VKCDbConstants.PRODUCT_GRIDVALUE,
                "pid",
                "sapid",
                "catid",
                "status"
            )
            val cursor = databaseManager!!.fetchFromDB(
                cols, VKCDbConstants.TABLE_SHOPPINGCART,
                ""
            )
            /*
      * System.out.println("13022015:cursor.getCount()::" +
      * cursor.getCount());
      */if (cursor.count > 0) {
                /*
                  * txtTotalItem.setText("Total Item : " +
                  * String.valueOf(cursor.getCount()));
                  */
                while (!cursor.isAfterLast) {
                    AppController.cartArrayList.add(setCartModel(cursor))
                    cursor.moveToNext()
                }
                if (AppController.cartArrayList.size > 0) {
                    lnrTableHeaders!!.visibility = View.VISIBLE
                    mDealersListView!!.visibility = View.VISIBLE
                    /*
      * int totalQty = 0; for (int i = 0; i <
      * AppController.cartArrayList.size(); i++) { totalQty =
      * totalQty + Integer.parseInt(AppController.cartArrayList.get(
      * i).getProdQuantity()); }
      */
                    // txtQty.setText("Total quantity :  " + "" + totalQty);
                    mSalesAdapter = txtTotalQty?.let {
                        SalesOrderAdapter(
                            activity!!,
                            AppController.cartArrayList, lnrTableHeaders!!,
                            it!!, txtTotalItem, txtCartValue
                        )
                    }
                    mSalesAdapter!!.notifyDataSetChanged()
                    mDealersListView!!.adapter = mSalesAdapter
                    mDealersListView!!.setSelection(AppController.listScrollTo)
                } else {
                    // txtQty.setText("Total quantity :  " + "" + 0);
                    lnrTableHeaders!!.visibility = View.GONE
                    mDealersListView!!.visibility = View.GONE
                }
                isCart = true
                // setCartQuantity();
            } else {
                VKCUtils.showtoast(activity, 9)
                /*
      * txtTotalItem.setText("Total Item : " +
      * String.valueOf(cursor.getCount()));
      */isCart = false
                // txtQty.setText("Total quantity :  " + "" + 0);
                // setCartQuantity();
                lnrTableHeaders!!.visibility = View.GONE
                mDealersListView!!.visibility = View.GONE
                txtTotalQty!!.text = "Total Qty. :" + "0"
                txtCartValue!!.text = "Cart Value: ₹" + "0"
                txtTotalItem!!.text = "Total Item : " + "0"
            }
        }

    private fun createJson(): JSONObject {
        cartData
        println("18022015:Within createJson ")
        val jsonObject = JSONObject()
        try {
            /*
			 * jsonObject.putOpt("user_id",
			 * AppPrefenceManager.getUserId(getActivity())); if
			 * (AppPrefenceManager.getUserType(getActivity()).equals("")) {
			 * jsonObject.putOpt("state_code", ""); } else {
			 * jsonObject.putOpt("state_code",
			 * AppPrefenceManager.getStateCode(getActivity())); }
			 * 
			 * jsonObject.putOpt("dealer_id",
			 * VKCObjectConstants.selectedDealerId);
			 * jsonObject.putOpt("retailer_id",
			 * VKCObjectConstants.selectedRetailerId);
			 * jsonObject.putOpt("subdealer_id",
			 * VKCObjectConstants.selectedSubDealerId);
			 * jsonObject.putOpt("user_type",
			 * AppPrefenceManager.getUserType(getActivity()));
			 */
            jsonObject.putOpt(
                "user_id",
                getUserId((activity)!!)
            )
            if ((getUserType((activity)!!) == "")) {
                jsonObject.putOpt("state_code", "")
            } else {
                jsonObject.putOpt(
                    "state_code",
                    getStateCode((activity)!!)
                )
            }
            if (getUserType((activity)!!) != "4") {
                jsonObject.putOpt(
                    "dealer_id",
                    VKCObjectConstants.selectedDealerId
                )
                jsonObject.putOpt(
                    "retailer_id",
                    VKCObjectConstants.selectedRetailerId
                )
                jsonObject.putOpt(
                    "subdealer_id",
                    VKCObjectConstants.selectedSubDealerId
                )
            } else {
                if ((getCustomerCategory((activity)!!)
                            == "1")
                ) jsonObject
                    .putOpt("dealer_id", getSelectedUserId((activity)!!)) else jsonObject.putOpt(
                    "dealer_id",
                    VKCObjectConstants.selectedDealerId
                )
                jsonObject.putOpt(
                    "retailer_id",
                    VKCObjectConstants.selectedRetailerId
                )
                if ((getCustomerCategory((activity)!!)
                            == "2")
                ) jsonObject
                    .putOpt("subdealer_id", getSelectedUserId((activity)!!)) else jsonObject.putOpt(
                    "subdealer_id",
                    VKCObjectConstants.selectedSubDealerId
                )
            }
            jsonObject.putOpt(
                "user_type",
                getUserType((activity)!!)
            )
        } catch (e1: JSONException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        val jsonArray = JSONArray()
        for (i in AppController.cartArrayList.indices) {
            val jobject = JSONObject()
            try {

                /*
				 * object.putOpt("product_id",
				 * AppController.cartArrayList.get(i) .getProdId());
				 */
                jobject.putOpt(
                    "product_id", AppController.cartArrayList[i]
                        .prodName
                )
                jobject.putOpt(
                    "category_id", AppController.cartArrayList[i]
                        .catId
                )
                jobject.putOpt(
                    "case_id", AppController.cartArrayList[i]
                        .prodSizeId
                )
                jobject.putOpt(
                    "color_id", AppController.cartArrayList[i]
                        .prodColorId
                )
                jobject.putOpt(
                    "quantity", AppController.cartArrayList[i]
                        .prodQuantity
                )
                Log.e(
                    "Size Value", ("18022015 "
                            + AppController.cartArrayList[i].prodSizeId)
                )
                if ((AppController.cartArrayList[i].prodSizeId
                            == " ")
                ) {
                    jobject.put(
                        "size", AppController.cartArrayList[i]
                            .prodSize
                    )
                } else {
                    jobject.put("size", "")
                }
                // object.putOpt("grid_value",cartArrayList.get(i).getProdGridValue());
                jsonArray.put(i, jobject)
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                Log.e("18022015 Exception", e.toString())
            }

            // salesOrderArray=jsonArray.toString();
            Log.i("Cart Submit Length", " " + jsonArray.length())
            Log.e(" JSONArray length:", "18022015 $jsonArray")
        }
        try {
            jsonObject.put("order_details", jsonArray)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        Log.v("LOG", "20022015 $jsonObject")
        return jsonObject
    }

    /**
     * Method to set cart model
     *
     */
    private fun setCartModel(cursor: Cursor): CartModel {
        cartModel = CartModel()
        cartModel!!.prodId = cursor.getString(0)
        cartModel!!.prodName = cursor.getString(1)
        cartModel!!.prodSizeId = cursor.getString(2)
        cartModel!!.prodSize = cursor.getString(3)
        cartModel!!.prodColorId = cursor.getString(4)
        cartModel!!.prodColor = cursor.getString(5)
        cartModel!!.prodQuantity = cursor.getString(6)
        cartModel!!.prodGridValue = cursor.getString(7)
        cartModel!!.pid = cursor.getString(8)
        println("PID :" + cursor.getString(8))
        cartModel!!.sapId = cursor.getString(9)
        cartModel!!.catId = cursor.getString(10)
        cartModel!!.status = cursor.getString(11)
        return cartModel as CartModel
    }

    /**
     * Post Api to submit sales order
     *
     */
    fun submitSalesOrder() {
        imageViewSubmit!!.isEnabled = false
        val name = arrayOf("salesorder")
        val values = arrayOf(createJson().toString())
        pDialog = CustomProgressBar(activity, R.drawable.loading)
        pDialog!!.show()
        println("18022015:createJson:" + createJson())
        val manager = VKCInternetManager(
            VKCUrlConstants.PRODUCT_SALESORDER_SUBMISSION
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
                    setIsCallPendingAPI(
                        (activity)!!,
                        false
                    )
                }
            })
    }

    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val responseObj = jsonObject.getString("response")
            if ((responseObj == "1")) {
                pDialog!!.dismiss()
                VKCUtils.showtoast(activity, 15)
                clearDb()
                // setCartQuantity();
                setFillTable((activity)!!, "false")
                // clearOrderDb();
                AppController.cartArrayList.clear()
                AppController.cartArrayListSelected.clear()
                mSalesAdapter = SalesOrderAdapter(
                    activity!!,
                    AppController.cartArrayList, lnrTableHeaders!!, txtQty!!,
                    txtTotalQty, txtCartValue
                )
                setDate((activity)!!, "")
                // AppPrefenceManager.setIsCallPendingAPI(getActivity(), true);
                mSalesAdapter!!.notifyDataSetChanged()
                mDealersListView!!.adapter = mSalesAdapter
                DashboardFActivity.dashboardFActivity.displayView(-1)
                // updateCartValue();
                getCreditValue()
            } else {
                pDialog!!.dismiss()
                VKCUtils.showtoast(activity, 13)
            }
        } catch (e: Exception) {
        }
    }

    fun clearDb() {
        val databaseManager = DataBaseManager(activity!!)
        databaseManager.removeDb(VKCDbConstants.TABLE_SHOPPINGCART)
    }

    fun clearOrderDb() {
        val databaseManager = DataBaseManager(activity!!)
        databaseManager.removeDb(VKCDbConstants.TABLE_ORDERLIST)
    }

    // private void getDealerApi() {
    // final VKCInternetManager manager = new VKCInternetManager(
    // "http://dev.mobatia.com/vkc/api/getstate");
    // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
    //
    // manager.getResponse(getActivity(), new ResponseListener() {
    //
    // @Override
    // public void responseSuccess(String successResponse) {
    //
    // parseJSON(successResponse);
    //
    // }
    //
    // @Override
    // public void responseFailure(String failureResponse) {
    // // TODO Auto-generated method stub
    // Log.v("LOG", "04122014FAIL " + failureResponse);
    // // mIsError = true;
    //
    // }
    // });
    //
    // }
    //
    // private void parseJSON(String response)
    // {
    // try {
    // JSONObject respObj = new JSONObject(response);
    // JSONArray respArray = respObj.getJSONArray("states");
    // for (int i = 0; i < respArray.length(); i++) {
    // // JSONObject objState = respArray.getJSONObject(i);
    // dealersModel.add(parseDealerObject(respArray.getJSONObject(i)));
    // // dealersStateModels.add(parseSateObject(respArray
    // // .getJSONObject(i)));
    // }
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // }
    //
    // private DealersListModel parseDealerObject(JSONObject objDealer) {
    // DealersListModel dealerModel = new DealersListModel();
    // dealerModel.setId("1");
    // dealerModel.setDealerName("Dealer1");
    //
    // return dealerModel;
    // // TODO Auto-generated method stub
    //
    // }
    /*
	 * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
	 */
    fun doUserCheck(): Boolean? {
        if ((getUserType((activity)!!) == "4")) { // Saleshead
            return if (!((mTextViewDealer!!.text.toString() == "")) // &&
            // !(mTextViewRetailer.getText().toString().equals(""))
            ) {
                true
            } else {
                false
            }
        } else if ((getUserType((activity)!!) == "5")) { // Retailer
            return if (!((mTextViewDealer!!.text.toString() == ""))) {
                true
            } else {
                false
            }
        } else if ((getUserType((activity)!!) == "6")) { // Dealer
            return if (((mTextViewRetailer!!.text.toString() == ""))) {
                true
            } else {
                false
            }
        } else if ((getUserType((activity)!!) == "7")) { // Sub
            // Dealer
            return if (!((mTextViewDealer!!.text.toString() == ""))) {
                true
            } else {
                false
            }
        }
        // if(SalesOrderFragment.isCart==true){
        // return true;
        // }else{
        // return false;
        // }
        return null
    }

    inner class OnClickView() : View.OnClickListener {
        override fun onClick(v: View) {
            if (v === imageViewSubmit) {
                if (AppController.cartArrayList.size > 0) {
                    if ((doUserCheck())!!) {
                        if ((getUserType((activity)!!)
                                    == "4")
                        ) {
                            if ((item == "Please Select")) {
                                VKCUtils.showtoast(activity, 36)
                            } else {
                                submitSalesOrder()
                            }
                        } else {
                            /*
							 * if (cartPrice > creditPrice) {
							 * VKCUtils.showtoast(getActivity(), 46); } else {
							 */
                            submitSalesOrder()
                            // }
                        }
                    } else {
                        VKCUtils.showtoast(activity, 16)
                    }
                } else {
                    VKCUtils.showtoast(activity, 16)
                }
            }
            if (v === mRelDealer) {
                if ((getUserType((activity)!!) == "7")) {
                    val intent = Intent(
                        activity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "SubDealer")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                } else if ((getUserType((activity)!!)
                            == "4")
                ) {
                    val intent = Intent(
                        activity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "SalesHead")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                } else {
                    val intent = Intent(
                        activity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "Dealer")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                }
            }
            if (v === mRelRetailer) {
                val intent = Intent(
                    activity,
                    RetailersListViewOnSearch::class.java
                )
                intent.putExtra("mType", "Retailer")
                VKCObjectConstants.textRetailer = mTextViewRetailer
                startActivity(intent)
            }
            if (v === imageSearchCat) {
                AppController.isSelectedDealer = false
                testSearch = edtSearch!!.text.toString()
                if (testSearch!!.length > 0 && (item == "Please Select")) {
                    val toast = CustomToast((activity)!!)
                    toast.show(39)
                } else if ((testSearch!!.length > 0
                            && item != "Please Select")
                ) {
                    val intent = Intent(
                        activity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "SalesHead")
                    intent.putExtra("key", testSearch)
                    intent.putExtra("type", type)
                    edtSearch!!.setText("")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                } else {
                    val toast = CustomToast((activity)!!)
                    toast.show(38)
                }
            }
        }// TODO Auto-generated method stub

        // mIsError = true;
// parseJSON(successResponse);
        // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
        private val myDealersSalesHeadApi: Unit
            private get() {
                var manager: VKCInternetManager? = null
                dealersShopModels.clear()
                // Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
                val name = arrayOf("saleshead_id")
                val value = arrayOf(
                    getUserId(
                        (activity)!!
                    )
                )
                manager = VKCInternetManager(VKCUrlConstants.LIST_MY_DEALERS_SALES_HEAD_URL)
                manager.getResponsePOST(
                    activity, name, value,
                    object : ResponseListener {
                        override fun responseSuccess(successResponse: String?) {

                            // parseJSON(successResponse);
                            if (successResponse != null) {
                                parseMyDealerSalesHeadJSON(successResponse)
                            }
                        }

                        override fun responseFailure(failureResponse: String?) {
                            // TODO Auto-generated method stub
                            // mIsError = true;
                        }
                    })
            }

        private fun parseMyDealerSalesHeadJSON(successResponse: String) {
            // TODO Auto-generated method stub
            try {
                // ArrayList<DealersShopModel> dealersShopModels = new
                // ArrayList<DealersShopModel>();
                val respObj = JSONObject(successResponse)
                val response = respObj.getJSONObject("response")
                val status = response.getString("status")
                if ((status == "Success")) {
                    val respArray = response.getJSONArray("dealers")
                    for (i in 0 until respArray.length()) {
                        dealersShopModels.add(
                            parseShop(
                                respArray
                                    .getJSONObject(i)
                            )
                        )
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        private fun parseShop(jsonObject: JSONObject): DealersShopModel {
            val dealersShopModel = DealersShopModel()
            dealersShopModel.address = jsonObject.optString("address")
            dealersShopModel.city = jsonObject.optString("city")
            dealersShopModel.contact_person = jsonObject
                .optString("contact_person")
            dealersShopModel.dealerId = jsonObject.optString("dealerId")
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
    }

    fun setCartQuantity() {
        val cols = arrayOf(
            VKCDbConstants.PRODUCT_ID,
            VKCDbConstants.PRODUCT_NAME,
            VKCDbConstants.PRODUCT_SIZEID,
            VKCDbConstants.PRODUCT_SIZE,
            VKCDbConstants.PRODUCT_COLORID,
            VKCDbConstants.PRODUCT_COLOR,
            VKCDbConstants.PRODUCT_QUANTITY,
            VKCDbConstants.PRODUCT_GRIDVALUE
        )
        val cursor = databaseManager!!.fetchFromDB(
            cols, VKCDbConstants.TABLE_SHOPPINGCART,
            ""
        )
        var mCount = 0
        var cartount = 0
        if (cursor.moveToFirst()) {
            do {
                val count = cursor.getString(
                    cursor
                        .getColumnIndex("productqty")
                )
                cartount = count.toInt()
                mCount = mCount + cartount
                // do what ever you want here
            } while (cursor.moveToNext())
        }
        cursor.close()
        txtTotalQty!!.text = "Total Quantity : $mCount"
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        // getCartData();
        super.onResume()
        // init(mRootView, savedInstanceState);
        // getPendingQuantity();
        //getCartData();
        if ((getUserType((activity)!!) == "4") && AppController.isSelectedDealer) {
            //getPendingQuantity();
            getCreditValue()
            mTextViewDealer!!.text = getSelectedUserName((activity)!!)
            mDealersListView!!.setSelection(AppController.listScrollTo)
        } else if ((getUserType((activity)!!) == "4")) {
            mTextViewDealer!!.text = getSelectedUserName((activity)!!)
        } else {
            mDealersListView!!.setSelection(AppController.listScrollTo)
        }

        //
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        // getCartData();
        super.onPause()
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        // getPendingQuantity();
        super.onStop()
    }

    override fun onDestroyView() {
        // TODO Auto-generated method stub
        super.onDestroyView()

        // init(mRootView, savedInstanceState);
    }// TODO Auto-generated method stub// TODO Auto-generated method stub
    // System.out.println("Values" + values);

    // pDialog.setMessage("Loading...");
    // System.out.println("18022015:createJson:" + createJson());
    val pendingQuantity: Unit
        get() {
            AppController.cartArrayList.clear()
            var userType: String = ""
            var cust_id: String? = ""
            if ((getUserType((activity)!!) == "6")) {
                userType = "1"
                cust_id = getCustomerId((activity)!!)
            } else if ((getUserType((activity)!!) == "4")) {
                userType = "1"
                cust_id = getSelectedUserId((activity)!!)
            } else {
                userType = "2"
                cust_id = getSelectedUserId((activity)!!)
            }
            val name = arrayOf("customerId", "customerType")
            val values = arrayOf(cust_id, userType)
            // System.out.println("Values" + values);
            pDialog = CustomProgressBar(activity, R.drawable.loading)

            // pDialog.setMessage("Loading...");
            pDialog!!.show()
            // System.out.println("18022015:createJson:" + createJson());
            val manager = VKCInternetManager(
                VKCUrlConstants.URL_GET_PENDING_ORDER_CART
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        if (successResponse != null) {
                            parseResponseCart(successResponse)
                        }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "17022015 Errror$failureResponse")
                        setIsCallPendingAPI(
                            (activity)!!,
                            true
                        )
                    }
                })
        }

    private fun parseResponseCart(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            val objResponse = JSONObject(successResponse)
            val response = objResponse.getJSONObject("response")
            val status = response.optString("status")
            if ((status == "Success")) {
                pDialog!!.dismiss()
                AppController.isCalledApiOnce = true
                txtTotalQty!!.text = ("Total Qty. :"
                        + response.optString("tot_qty"))
                txtCartValue!!.text = ("Cart Value: ₹"
                        + response.optString("cart_value"))
                txtTotalItem!!.text = ("Total Item : "
                        + response.optString("tot_items"))

                // initialiseUI();
                setIsCallPendingAPI((activity)!!, false)
                val pendingArray = response.optJSONArray("pendingQty")
                if (pendingArray.length() > 0) {
                    // clearDb();
                    clearDb()
                    AppController.isSelectedDealer = false
                    // int cartCount = getCartCount();
                    // for(int i=0;i<cartCount;i++)
                    // {
                    /*
					 * if (cartCount > 0) {
					 * 
					 * databaseManager .removeFromDb(TABLE_SHOPPINGCART,
					 * "status", "pending");
					 * 
					 * SQLiteAdapter mAdapter = new SQLiteAdapter(
					 * getActivity(), DBNAME); mAdapter.deletePending(); }
					 */
                    // }
                    for (i in 0 until pendingArray.length()) {
                        val objPending = pendingArray.getJSONObject(i)
                        val product_id = objPending.optString("product_id")
                        val size_name = objPending.optString("size_name")
                        val size_id = objPending.optString("size_id")
                        val color_name = objPending.optString("color_name")
                        val color_id = objPending.optString("color_id")
                        val quantity = objPending.optString("quantity")
                        val sapId = objPending.optString("productSapId")
                        val catId = objPending.optString("categoryid")
                        // String price = objPending.optString("price");
                        val id = objPending.optString("id")

                        // getCartCount();
                        val values = arrayOf(
                            arrayOf(VKCDbConstants.PRODUCT_ID, product_id),
                            arrayOf(VKCDbConstants.PRODUCT_NAME, product_id),
                            arrayOf(VKCDbConstants.PRODUCT_SIZEID, size_id),
                            arrayOf(VKCDbConstants.PRODUCT_SIZE, size_name),
                            arrayOf(VKCDbConstants.PRODUCT_COLORID, color_id),
                            arrayOf(VKCDbConstants.PRODUCT_COLOR, color_name),
                            arrayOf(VKCDbConstants.PRODUCT_QUANTITY, quantity),
                            arrayOf(VKCDbConstants.PRODUCT_GRIDVALUE, ""),
                            arrayOf("pid", id),
                            arrayOf("sapid", sapId),
                            arrayOf("catid", catId)
                        )
                        databaseManager
                            ?.insertIntoDb(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    }
                    val now = Date()
                    val alsoNow = Calendar.getInstance().time
                    val nowAsString = SimpleDateFormat("yyyy-MM-dd")
                        .format(now)
                    setDate((activity)!!, nowAsString)
                }
                cartData
                // setCartQuantity();
                // updateCartValue();
                // updateCartValue();
                // finish();
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }// AppController.cartArrayList.add(setCartModel(cursor));

    // System.out.println("13022015:cursor.getCount()::" +
    // cursor.getCount());
    private val cartCount: Int
        private get() {
            AppController.cartArrayList.clear()
            val cols = arrayOf(
                VKCDbConstants.PRODUCT_ID,
                VKCDbConstants.PRODUCT_NAME,
                VKCDbConstants.PRODUCT_SIZEID,
                VKCDbConstants.PRODUCT_SIZE,
                VKCDbConstants.PRODUCT_COLORID,
                VKCDbConstants.PRODUCT_COLOR,
                VKCDbConstants.PRODUCT_QUANTITY,
                VKCDbConstants.PRODUCT_GRIDVALUE
            )
            val cursor = databaseManager!!.fetchFromDB(
                cols, VKCDbConstants.TABLE_SHOPPINGCART,
                ""
            )
            // System.out.println("13022015:cursor.getCount()::" +
            // cursor.getCount());
            if (cursor.count > 0) {
                while (!cursor.isAfterLast) {
                    // AppController.cartArrayList.add(setCartModel(cursor));
                    tableCount = tableCount + cursor.count
                    cursor.moveToNext()
                }
            }
            return cursor.count
        }

    fun updateCartValue() {
        val mAdapter = SQLiteAdapter(activity!!, VKCDbConstants.DBNAME)
        mAdapter.openToRead()
        val sumValue = mAdapter.cartSum
        if (sumValue == null) {
            txtCartValue!!.text = "Cart Value: ₹" + 0
        } else {
            cartPrice = sumValue.toInt().toLong()
            txtCartValue!!.text = "Cart Value: ₹$sumValue"
        }
    }

    private fun getCreditValue() {
        var manager: VKCInternetManagerWithoutDialog? = null
        var cust_id: String? = ""
        if ((getUserType((activity)!!) == "4")) {
            cust_id = getSelectedUserId((activity)!!)
        } else {
            cust_id = getCustomerId((activity)!!)
        }
        val name = arrayOf("dealerid")
        val value = arrayOf(cust_id)
        manager = VKCInternetManagerWithoutDialog(
            VKCUrlConstants.GET_QUICK_ORDER_CREDIT_URL
        )
        manager.getResponsePOST(
            activity, name, value,
            object : ResponseListenerWithoutDialog {
                override fun responseSuccess(successResponse: String) {
                    try {
                        val responseObj = JSONObject(
                            successResponse
                        )
                        val response = responseObj
                            .getJSONObject("response")
                        val status = response.getString("status")
                        if ((status == "Success")) {
                            creditValue = response.getString("creditvalue")
                            creditPrice = creditValue?.toInt()?.toLong()!!
                            textCreditValue!!.text = ("Order Limit: ₹"
                                    + creditValue)
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }

                override fun responseFailure(failureResponse: String) { // TODO
                    // Auto-generated method stub
                }
            })
    }

    inner class DeleteAlert     // TODO Auto-generated constructor stub
        (var mActivity: Activity) : Dialog(mActivity), View.OnClickListener, VKCDbConstants {
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_clear_cart)
            init()
        }

        private fun init() {
            val buttonSet = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    val deleteList = arrayOfNulls<String>(
                        AppController.cartArrayList
                            .size
                    )
                    val listDelete = ArrayList<String>()
                    for (i in AppController.cartArrayList.indices) {
                        listDelete.add(
                            AppController.cartArrayList[i]
                                .pid
                        )
                        /*
						 * deleteList[i]=AppController.cartArrayList.get(i)
						 * .getPid();
						 */
                    }
                    deleteApi(listDelete)
                    clearDb()
                    VKCUtils.showtoast(activity, 55)
                    // setCartQuantity();
                    cartData
                    setFillTable((activity)!!, "false")
                    // clearOrderDb();
                    // updateCartValue();
                    AppController.cartArrayList.clear()
                    AppController.cartArrayListSelected.clear()
                    mSalesAdapter = SalesOrderAdapter(
                        activity!!,
                        AppController.cartArrayList, lnrTableHeaders!!,
                        txtQty!!, txtTotalQty, txtCartValue
                    )
                    setDate((activity)!!, "")
                    // AppPrefenceManager.setIsCallPendingAPI(getActivity(),
                    // true);
                    mSalesAdapter!!.notifyDataSetChanged()
                    mDealersListView!!.adapter = mSalesAdapter
                    dismiss()
                }
            })
            val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    dismiss()
                }
            })
        }

        override fun onClick(v: View) {
            dismiss()
        }
    }

    fun deleteApi(listDelete: ArrayList<String>) {
        val name = arrayOf("ids")
        val values = arrayOf(listDelete.toString())
        val manager = VKCInternetManager(
            VKCUrlConstants.DELETE_CART_ITEM_API
        )
        manager.getResponsePOST(
            activity, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    try {
                        val obj = JSONObject(successResponse)
                        val status = obj.optString("status")
                        if ((status == "Success")) {
                            txtTotalQty!!.text = ("Total Qty. :"
                                    + obj.optString("tot_qty"))
                            txtCartValue!!.text = ("Cart Value: ₹"
                                    + obj.optString("cart_value"))
                            txtTotalItem!!.text = ("Total Item : "
                                    + obj.optString("tot_items"))
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    Log.v("LOG", "18022015 Errror$failureResponse")
                }
            })
    } /*
	 * public void schedulePendingOrder() { final Handler handler=new Handler();
	 * handler.postDelayed(new Runnable() { public void run() {
	 * getPendingQuantity(); handler.postDelayed(this, FIVE_SECONDS); } },
	 * FIVE_SECONDS); }
	 */

    companion object {
        var isCart = false
    }
}