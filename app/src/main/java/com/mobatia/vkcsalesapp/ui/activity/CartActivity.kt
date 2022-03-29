package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.activities.RetailersListViewOnSearch
import com.mobatia.vkcsalesapp.adapter.SalesOrderActivityAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCObjectConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import com.mobatia.vkcsalesapp.customview.CustomTextView
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.*
import com.mobatia.vkcsalesapp.model.CartModel
import com.mobatia.vkcsalesapp.model.DealersListModel
import com.mobatia.vkcsalesapp.model.DealersShopModel
import com.mobatia.vkcsalesapp.ui.fragments.SalesOrderFragment
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class CartActivity : AppCompatActivity(), VKCDbConstants, VKCUrlConstants {
    private val mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    private var mRelDealer: RelativeLayout? = null
    private var mRelRetailer: RelativeLayout? = null
    private var mTextViewDealer: CustomTextView? = null
    private var mTextViewRetailer: CustomTextView? = null
    private var mDealersListView: ListView? = null
    private var mSalesAdapter: SalesOrderActivityAdapter? = null
    private var databaseManager: DataBaseManager? = null
    private var cartModel: CartModel? = null
    var pDialog: CustomProgressBar? = null
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
    private var txtTotalItem: TextView? = null
    private var txtTotalQty: TextView? = null
    private var txtName: TextView? = null
    private var hintText: TextView? = null
    private var txtCartValue: TextView? = null
    private var textCreditValue: TextView? = null
    var creditValue: String? = null
    var labelText: TextView? = null
    private var txtPlace: TextView? = null
    private var textRetailer: TextView? = null
    var edtSearch: EditText? = null
    var isClicked = false
    var testSearch: String? = null
    var categories: MutableList<String> = ArrayList()
    var spinner: Spinner? = null
    private var item: String? = null
    private var type: String? = null
    var dealersShopModels: ArrayList<DealersShopModel> = ArrayList<DealersShopModel>()
    var mActivity: Activity? = null
    var isFirstTime = false
    private var tableCount = 0
    var callPending = false
    var creditPrice = 0
    var cartPrice = 0
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_salesorder)
        AppController.isCalledApiOnce = false
        AppController.isClickedCartItem = false
        mActivity = this
        isFirstTime = true
        callPending = true
        initialiseUI()
        if (AppPrefenceManager.getUserType(this) != "4") {
            getCreditValue()
            pendingQuantity
        } else {
            if (AppPrefenceManager.getSelectedUserId(this@CartActivity) != "") {
                getCreditValue()
                pendingQuantity
            } else {
                val toast = CustomToast(this@CartActivity)
                toast.show(56)
            }
        }
        // getCartData();
        // getPendingQuantity();
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            R.id.home -> {
                AppController.isClickedCartAdapter = true
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDisplayParams() {
        val displayManagerScale = DisplayManagerScale(
            this@CartActivity
        )
        mDisplayHeight = displayManagerScale.deviceHeight
        mDisplayWidth = displayManagerScale.deviceWidth
    }

    private fun initialiseUI() {
        val abar: ActionBar = getSupportActionBar()!!
        val viewActionBar: View = getLayoutInflater().inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle: TextView = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.setText("My Cart")
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        setDisplayParams()
        databaseManager = DataBaseManager(this)
        mRelDealer = findViewById<View>(R.id.rlDealer) as RelativeLayout?
        mRelRetailer = findViewById<View>(R.id.rlRetailer) as RelativeLayout?
        mTextViewDealer = findViewById<View>(R.id.textViewDealer) as CustomTextView?
        mTextViewRetailer = findViewById<View>(R.id.textViewRetailer) as CustomTextView?
        mDealersListView = findViewById<View>(R.id.dealersListView) as ListView?
        lnrTableHeaders = findViewById<View>(R.id.ll2) as LinearLayout?
        imageViewSubmit = findViewById<View>(R.id.imageViewSearch) as ImageView?
        imageViewClear = findViewById<View>(R.id.imageViewClear) as ImageView?
        imageSearchCat = findViewById<View>(R.id.imageViewSearchCat) as ImageView?
        llTop = findViewById<View>(R.id.llTop) as LinearLayout?
        lnrOne = findViewById<View>(R.id.lnrOne) as LinearLayout?
        txtRefr = findViewById<View>(R.id.txtReferenceNumber) as TextView?
        txtDate = findViewById<View>(R.id.txtDate) as TextView?
        txtTotalItem = findViewById<View>(R.id.textTotalItem) as TextView?
        txtTotalQty = findViewById<View>(R.id.textTotalQty) as TextView?
        txtCartValue = findViewById<View>(R.id.textCartValueCart) as TextView?
        hintText = findViewById<View>(R.id.hintText) as TextView?
        edtSearch = findViewById<View>(R.id.editSearch) as EditText?
        textRetailer = findViewById<View>(R.id.textRetailer) as TextView?
        llCategory = findViewById<View>(R.id.secCatLL) as LinearLayout?
        llSearch = findViewById<View>(R.id.secSearchLL) as LinearLayout?
        labelText = findViewById<View>(R.id.textView1) as TextView?
        edtSearch?.setText("")
        textCreditValue = findViewById<View>(R.id.textCreditValueCart) as TextView?
        spinner = findViewById<View>(R.id.spinner) as Spinner?
        imageViewSubmit!!.isEnabled = true
        mDealersListView!!.visibility = View.VISIBLE
        spinner!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                // TODO Auto-generated method stub
                isClicked = true
                return false
            }
        })
        imageViewClear!!.setOnClickListener { // TODO Auto-generated method stub
            val appDeleteDialog: DeleteAlert = DeleteAlert(this@CartActivity)
            appDeleteDialog.window!!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            appDeleteDialog.setCancelable(true)
            appDeleteDialog.show()
        }
        spinner!!.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>, arg1: View,
                position: Int, arg3: Long
            ) {
                // TODO Auto-generated method stub
                item = arg0.getItemAtPosition(position).toString()
                if (isClicked) {
                    if (item == "Dealer") {
                        type = "1"
                        AppPrefenceManager.saveCustomerCategory(this@CartActivity, "1")
                        labelText?.setText("Dealer : ")
                        mTextViewDealer?.setText("Dealer Name")
                    } else if (item == "Sub Dealer") {
                        type = "2"
                        AppPrefenceManager.saveCustomerCategory(this@CartActivity, "2")
                        labelText?.setText("Sub Dealer : ")
                        mTextViewDealer?.setText("Sub Dealer Name")
                    }
                    isFirstTime = false
                }
                isClicked = false
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        })
        mTextViewDealer?.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int,
                count: Int
            ) {
                // TODO Auto-generated method stub
                if (isClicked) {
                    clearDb()
                    cartData
                    txtQty?.setText("Total quantity :  " + "" + 0)
                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int,
                after: Int
            ) {
                // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) {
                // TODO Auto-generated method stub
            }
        })
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd-MMM-yyyy")
        val formattedDate = df.format(c.time)
        txtDate?.setText("Date :  $formattedDate")
        txtName = findViewById<View>(R.id.txtName) as TextView?
        txtName?.setText(
            "Name : "
                    + AppPrefenceManager.getLoginCustomerName(this)
        )
        txtPlace = findViewById<View>(R.id.txtPlace) as TextView?
        txtPlace?.setText("Place : " + AppPrefenceManager.getLoginPlace(this))
        txtQty = findViewById<View>(R.id.txtTotalQty) as TextView?
        txtValue = findViewById<View>(R.id.txtTotalValue) as TextView?
        dealersModel = ArrayList<DealersListModel>()
        cartData
        setCartQuantity()
        categories.clear()
        categories.add("Please Select")
        categories.add("Dealer")
        categories.add("Sub Dealer")
        val dataAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, categories
        )

        // Drop down layout style - list view with radio button
        dataAdapter
            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // attaching data adapter to spinner
        spinner!!.adapter = dataAdapter
        if (AppPrefenceManager.getCustomerCategory(this@CartActivity) == "1") {
            spinner!!.setSelection(1)
            labelText?.setText("Dealer : ")
            mTextViewDealer?.setText(
                AppPrefenceManager
                    .getSelectedUserName(this@CartActivity)
            )
        } else if (AppPrefenceManager.getCustomerCategory(this@CartActivity)
            == "2"
        ) {
            spinner!!.setSelection(2)
            labelText?.setText("Sub Dealer : ")
            mTextViewDealer?.setText(
                AppPrefenceManager
                    .getSelectedUserName(this@CartActivity)
            )
        } else {
            spinner!!.setSelection(0)
        }
        if (mActivity?.let { AppPrefenceManager.getUserType(it) } == "6") { // UserType:Dealer
            mRelDealer?.setClickable(false)
            mRelRetailer?.setClickable(false)
            VKCObjectConstants.selectedDealerId = ""
            VKCObjectConstants.selectedSubDealerId = ""
            VKCObjectConstants.selectedRetailerId = ""
            mTextViewDealer?.setText(AppPrefenceManager.getUserName(this@CartActivity))
            llTop?.setVisibility(View.GONE)
            lnrOne?.setVisibility(View.VISIBLE)
            llCategory?.setVisibility(View.GONE)
        } else if (AppPrefenceManager.getUserType(this@CartActivity) == "5") { // UserType:Retailer
            llCategory?.setVisibility(View.GONE)
            llSearch?.setVisibility(View.GONE)
            mRelDealer?.setClickable(true)
            mRelRetailer?.setClickable(false)
            VKCObjectConstants.selectedSubDealerId = ""
            VKCObjectConstants.selectedRetailerId = ""
            mTextViewRetailer
                ?.setText(AppPrefenceManager.getUserName(this@CartActivity))
            mRelDealer?.setOnClickListener(OnClickView())
            llCategory?.setVisibility(View.GONE)
        } else if (AppPrefenceManager.getUserType(this) == "7") { // UserType:Sub
            llCategory?.setVisibility(View.GONE) // Dealer
            mRelRetailer?.setVisibility(View.GONE)
            llSearch?.setVisibility(View.GONE)
            hintText?.setVisibility(View.GONE)
            textRetailer?.setVisibility(View.GONE)
            mTextViewDealer?.setText("Select Dealer")
            VKCObjectConstants.selectedSubDealerId = ""
            VKCObjectConstants.selectedRetailerId = ""
            mRelDealer?.setClickable(true)
            mRelDealer?.setOnClickListener(OnClickView())
        } else if (AppPrefenceManager.getUserType(this@CartActivity) == "4") {
            // Sales Head
            llSearch?.setVisibility(View.VISIBLE)
            llCategory?.setVisibility(View.VISIBLE)
            mRelRetailer?.setVisibility(View.GONE)
            textRetailer?.setVisibility(View.GONE)
            VKCObjectConstants.selectedRetailerId = ""
        } else {
            mRelDealer?.setClickable(true)
            llCategory?.setVisibility(View.VISIBLE)
            llSearch?.setVisibility(View.VISIBLE)
            VKCObjectConstants.selectedSubDealerId = ""
            llCategory?.setOnClickListener(OnClickView())
        }
        imageViewSubmit!!.setOnClickListener(OnClickView())
        imageSearchCat!!.setOnClickListener(OnClickView())
    }

    fun setActionBar() {
        val actionBar: ActionBar = getSupportActionBar()!!
        actionBar.setSubtitle("")
        actionBar.setTitle("")
        actionBar.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        actionBar.show()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back)
        getSupportActionBar()?.setHomeButtonEnabled(true)
    }

    // setCartQuantity();
    private val cartData: Unit
        private get() {
            AppController.cartArrayList.clear()
            val cols = arrayOf(
                VKCDbConstants.Companion.PRODUCT_ID,
                VKCDbConstants.Companion.PRODUCT_NAME,
                VKCDbConstants.Companion.PRODUCT_SIZEID,
                VKCDbConstants.Companion.PRODUCT_SIZE,
                VKCDbConstants.Companion.PRODUCT_COLORID,
                VKCDbConstants.Companion.PRODUCT_COLOR,
                VKCDbConstants.Companion.PRODUCT_QUANTITY,
                VKCDbConstants.Companion.PRODUCT_GRIDVALUE,
                "pid",
                "sapid",
                "catid"
            )
            val cursor: Cursor = databaseManager?.fetchFromDB(
                cols, VKCDbConstants.TABLE_SHOPPINGCART,
            )!!
            if (cursor.count > 0) {
                txtTotalItem?.setText(
                    "Total Items : "
                            + cursor.count.toString()
                )
                while (!cursor.isAfterLast) {
                    setCartModel(cursor)?.let { AppController.cartArrayList.add(it) }
                    cursor.moveToNext()
                }
                if (AppController.cartArrayList.size > 0) {
                    lnrTableHeaders?.setVisibility(View.VISIBLE)
                    mDealersListView!!.visibility = View.VISIBLE
                    var totalQty = 0
                    for (i in AppController.cartArrayList.indices) {
                        totalQty = (totalQty
                                + AppController.cartArrayList.get(
                            i
                        ).prodQuantity.toInt())
                    }
                    mSalesAdapter = SalesOrderActivityAdapter(
                        this@CartActivity, AppController.cartArrayList,
                        lnrTableHeaders!!, txtTotalQty!!, txtTotalItem,
                        txtCartValue
                    )
                    mSalesAdapter!!.notifyDataSetChanged()
                    mDealersListView!!.adapter = mSalesAdapter
                    mDealersListView!!.setSelection(AppController.listScrollTo)
                    txtQty?.setText("Total quantity :  $totalQty")
                    // setCartQuantity();
                } else {
                    txtQty?.setText("Total quantity :  " + "" + 0)
                    lnrTableHeaders?.setVisibility(View.GONE)
                    mDealersListView!!.visibility = View.GONE
                }
                SalesOrderFragment.isCart = true
            } else {
                txtTotalItem?.setText("Total Items : " + 0)
                VKCUtils.showtoast(this@CartActivity, 9)
                SalesOrderFragment.isCart = false
                lnrTableHeaders?.setVisibility(View.GONE)
                mDealersListView!!.visibility = View.GONE
            }
        }

    private fun createJson(): JSONObject {
        cartData
        println("18022015:Within createJson ")
        val jsonObject = JSONObject()
        try {
            jsonObject.putOpt(
                "user_id",
                AppPrefenceManager.getUserId(this@CartActivity)
            )
            if (AppPrefenceManager.getUserType(this@CartActivity) == "") {
                jsonObject.putOpt("state_code", "")
            } else {
                jsonObject.putOpt(
                    "state_code",
                    AppPrefenceManager.getStateCode(this@CartActivity)
                )
            }
            if (AppPrefenceManager.getUserType(this@CartActivity) != "4") {
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
                if (AppPrefenceManager.getCustomerCategory(this@CartActivity) ==
                    "1"
                ) jsonObject.putOpt(
                    "dealer_id",
                    AppPrefenceManager.getSelectedUserId(this@CartActivity)
                ) else jsonObject.putOpt(
                    "dealer_id",
                    VKCObjectConstants.selectedDealerId
                )
                jsonObject.putOpt(
                    "retailer_id",
                    VKCObjectConstants.selectedRetailerId
                )
                if (AppPrefenceManager.getCustomerCategory(this@CartActivity) ==
                    "2"
                ) jsonObject.putOpt(
                    "subdealer_id",
                    AppPrefenceManager.getSelectedUserId(this@CartActivity)
                ) else jsonObject.putOpt(
                    "subdealer_id",
                    VKCObjectConstants.selectedSubDealerId
                )
            }
            jsonObject
                .putOpt("user_type", AppPrefenceManager.getUserType(this))
        } catch (e1: JSONException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }
        val jsonArray = JSONArray()
        for (i in AppController.cartArrayList.indices) {
            val jobject = JSONObject()
            try {
                jobject.putOpt(
                    "product_id", AppController.cartArrayList.get(i)
                        .prodName
                )
                jobject.putOpt(
                    "category_id", AppController.cartArrayList.get(i)
                        .catId
                )
                jobject.putOpt(
                    "case_id", AppController.cartArrayList.get(i)
                        .prodSizeId
                )
                jobject.putOpt(
                    "color_id", AppController.cartArrayList.get(i)
                        .prodColorId
                )
                jobject.putOpt(
                    "quantity", AppController.cartArrayList.get(i)
                        .prodQuantity
                )
                jsonArray.put(i, jobject)
            } catch (e: JSONException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

            // salesOrderArray=jsonArray.toString();
        }
        try {
            jsonObject.put("order_details", jsonArray)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        return jsonObject
    }

    /**
     * Method to set cart model
     */
    private fun setCartModel(cursor: Cursor): CartModel? {
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
        cartModel!!.sapId = cursor.getString(9)
        cartModel!!.catId = cursor.getString(10)
        // cartModel.setStatus(cursor.getString(11));
        return cartModel
    }

    /**
     * Post Api to submit sales order
     */
    fun submitSalesOrder() {
        imageViewSubmit!!.isEnabled = false
        val name = arrayOf("salesorder")
        val values = arrayOf<String>(createJson().toString())
        pDialog = CustomProgressBar(this@CartActivity, R.drawable.loading)
        pDialog?.show()
        val manager = VKCInternetManager(
            VKCUrlConstants.PRODUCT_SALESORDER_SUBMISSION
        )
        manager.getResponsePOST(mActivity, name, values,
            object : VKCInternetManager.ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    parseResponse(successResponse)
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    AppPrefenceManager
                        .setIsCallPendingAPI(this@CartActivity, false)
                }
            })
    }

    fun parseResponse(response: String?) {
        try {
            val jsonObject = JSONObject(response)
            val responseObj: String = jsonObject.getString("response")
            if (responseObj == "1") {
                pDialog?.dismiss()
                VKCUtils.showtoast(this@CartActivity, 15)
                clearDb()
                setCartQuantity()
                AppPrefenceManager.setFillTable(this@CartActivity, "false")
                AppController.cartArrayList.clear()
                AppController.cartArrayListSelected.clear()
                AppPrefenceManager.setIsCallPendingAPI(this@CartActivity, true)
                AppPrefenceManager.setDate(this, "")
                getCreditValue()
                mActivity?.finish()
            } else {
                pDialog?.dismiss()
                VKCUtils.showtoast(this@CartActivity, 13)
            }
        } catch (e: Exception) {
        }
    }

    fun clearDb() {
        val databaseManager = DataBaseManager(mActivity!!)
        databaseManager.removeDb(VKCDbConstants.Companion.TABLE_SHOPPINGCART)
    }

    fun clearOrderDb() {
        val databaseManager = DataBaseManager(mActivity!!)
        databaseManager.removeDb(VKCDbConstants.Companion.TABLE_ORDERLIST)
    }

    /*
	 * Bibin Comment 4. Sales Head 5. Retailer 6. Dealer 7. Sub Dealer
	 */
    fun doUserCheck(): Boolean? {
        if (AppPrefenceManager.getUserType(this@CartActivity) == "4") { // Saleshead
            return if (mTextViewDealer?.getText().toString() != "" // &&
            // !(mTextViewRetailer.getText().toString().equals(""))
            ) {
                true
            } else {
                false
            }
        } else if (AppPrefenceManager.getUserType(this@CartActivity) == "5") { // Retailer
            return if (mTextViewDealer?.getText().toString() != "") {
                true
            } else {
                false
            }
        } else if (AppPrefenceManager.getUserType(this@CartActivity) == "6") { // Dealer
            return if (mTextViewRetailer?.getText().toString() == "") {
                true
            } else {
                false
            }
        } else if (AppPrefenceManager.getUserType(this@CartActivity) == "7") { // Sub
            // Dealer
            return if (mTextViewDealer?.getText().toString() != "") {
                true
            } else {
                false
            }
        }
        return null
    }

    inner class OnClickView : View.OnClickListener {
        override fun onClick(v: View) {
            if (v === imageViewSubmit) {
                if (AppController.cartArrayList.size > 0) {
                    if (doUserCheck()!!) {
                        if (AppPrefenceManager.getUserType(this@CartActivity) ==
                            "4"
                        ) {
                            if (item == "Please Select") {
                                VKCUtils.showtoast(this@CartActivity, 36)
                            } else {
                                submitSalesOrder()
                            }
                        } else {
                            submitSalesOrder()
                        }
                    } else {
                        VKCUtils.showtoast(this@CartActivity, 16)
                    }
                } else {
                    VKCUtils.showtoast(this@CartActivity, 16)
                }
            }
            if (v === mRelDealer) {
                if (mActivity?.let { AppPrefenceManager.getUserType(it) } == "7") {
                    val intent = Intent(
                        mActivity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "SubDealer")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                } else if (AppPrefenceManager.getUserType(this@CartActivity)
                    == "4"
                ) {
                    val intent = Intent(
                        mActivity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "SalesHead")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                } else {
                    val intent = Intent(
                        mActivity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "Dealer")
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                }
            }
            if (v === mRelRetailer) {
                val intent = Intent(
                    mActivity,
                    RetailersListViewOnSearch::class.java
                )
                intent.putExtra("mType", "Retailer")
                VKCObjectConstants.textRetailer = mTextViewRetailer
                startActivity(intent)
            }
            if (v === imageSearchCat) {
                testSearch = edtSearch?.getText().toString()
                if (testSearch!!.length > 0 && item == "Please Select") {
                    val toast = CustomToast(this@CartActivity)
                    toast.show(39)
                } else if (testSearch!!.length > 0
                    && item != "Please Select"
                ) {
                    val intent = Intent(
                        mActivity,
                        RetailersListViewOnSearch::class.java
                    )
                    intent.putExtra("mType", "SalesHead")
                    intent.putExtra("key", testSearch)
                    intent.putExtra("type", type)
                    VKCObjectConstants.textDealer = mTextViewDealer
                    startActivity(intent)
                } else {
                    val toast = CustomToast(this@CartActivity)
                    toast.show(38)
                }
            }
        }// TODO Auto-generated method stub


    }

    fun setCartQuantity() {
        val cols = arrayOf<String>(
            VKCDbConstants.Companion.PRODUCT_ID,
            VKCDbConstants.Companion.PRODUCT_NAME,
            VKCDbConstants.Companion.PRODUCT_SIZEID,
            VKCDbConstants.Companion.PRODUCT_SIZE,
            VKCDbConstants.Companion.PRODUCT_COLORID,
            VKCDbConstants.Companion.PRODUCT_COLOR,
            VKCDbConstants.Companion.PRODUCT_QUANTITY,
            VKCDbConstants.Companion.PRODUCT_GRIDVALUE
        )
        val cursor: Cursor? = databaseManager?.fetchFromDB(
            cols, VKCDbConstants.Companion.TABLE_SHOPPINGCART,
        )
        var mCount = 0
        var cartount = 0
        if (cursor?.moveToFirst()!!) {
            do {
                val count = cursor?.getString(
                    cursor
                        ?.getColumnIndex("productqty")
                )
                cartount = count?.toInt()!!
                mCount = mCount + cartount
                // do what ever you want here
            } while (cursor?.moveToNext()!!)
        }
        cursor?.close()
        txtTotalQty?.setText("Total Quantity : $mCount")
    }

    protected override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
        isFirstTime = false
        cartData
        mDealersListView!!.setSelection(AppController.listScrollTo)
    }

    protected override fun onResume() {
        // TODO Auto-generated method stub
        isFirstTime = false
        super.onResume()
        // getCartData();
        mDealersListView!!.setSelection(AppController.listScrollTo)
    }// TODO Auto-generated method stub

    // TODO Auto-generated method stub
    val pendingQuantity: Unit
        get() {
            AppController.cartArrayList.clear()
            var userType = ""
            var cust_id = ""
            if (AppPrefenceManager.getUserType(this@CartActivity) == "6") {
                userType = "1"
                cust_id = AppPrefenceManager.getCustomerId(this@CartActivity)!!
            } else if (AppPrefenceManager.getUserType(this@CartActivity) == "4") {
                userType = "1"
                cust_id = AppPrefenceManager.getSelectedUserId(this@CartActivity)!!
            } else {
                userType = "2"
                cust_id = AppPrefenceManager.getSelectedUserId(this@CartActivity)!!
            }
            val name = arrayOf("customerId", "customerType")
            val values = arrayOf(cust_id, userType)
            pDialog = CustomProgressBar(this, R.drawable.loading)
            pDialog?.show()
            val manager = VKCInternetManager(
                VKCUrlConstants.Companion.URL_GET_PENDING_ORDER_CART
            )
            manager.getResponsePOST(this, name, values, object :
                VKCInternetManager.ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    if (successResponse != null) {
                        parseResponseCart(successResponse)
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    AppPrefenceManager.setIsCallPendingAPI(this@CartActivity, true)
                }
            })
        }

    private fun parseResponseCart(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            val objResponse = JSONObject(successResponse)
            val response: JSONObject = objResponse.getJSONObject("response")
            val status: String = response.optString("status")
            if (status == "Success") {
                pDialog?.dismiss()
                AppController.isCalledApiOnce = true
                clearDb()
                AppPrefenceManager.setIsCallPendingAPI(this@CartActivity, false)
                val pendingArray: JSONArray = response.optJSONArray("pendingQty")
                if (pendingArray.length() > 0) {
                    clearDb()
                    txtTotalQty?.setText(
                        "Total Qty. :"
                                + response.optString("tot_qty")
                    )
                    txtCartValue?.setText(
                        "Cart Value: ₹"
                                + response.optString("cart_value")
                    )
                    txtTotalItem?.setText(
                        "Total Item : "
                                + response.optString("tot_items")
                    )
                    for (i in 0 until pendingArray.length()) {
                        val objPending: JSONObject = pendingArray.getJSONObject(i)
                        val product_id: String = objPending.optString("product_id")
                        val size_name: String = objPending.optString("size_name")
                        val size_id: String = objPending.optString("size_id")
                        val color_name: String = objPending.optString("color_name")
                        val color_id: String = objPending.optString("color_id")
                        val quantity: String = objPending.optString("quantity")
                        val sapId: String = objPending.optString("productSapId")
                        val catId: String = objPending.optString("categoryid")
                        val price: String = objPending.optString("price")
                        val id: String = objPending.optString("id")
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
                            ?.insertIntoDb(VKCDbConstants.Companion.TABLE_SHOPPINGCART, values)
                    }
                    val now = Date()
                    val alsoNow = Calendar.getInstance().time
                    val nowAsString = SimpleDateFormat("yyyy-MM-dd")
                        .format(now)
                    AppPrefenceManager.setDate(this, nowAsString)
                }
                cartData
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    private val cartCount: Int
        private get() {
            AppController.cartArrayList.clear()
            val cols = arrayOf<String>(
                VKCDbConstants.PRODUCT_ID,
                VKCDbConstants.PRODUCT_NAME,
                VKCDbConstants.PRODUCT_SIZEID,
                VKCDbConstants.PRODUCT_SIZE,
                VKCDbConstants.PRODUCT_COLORID,
                VKCDbConstants.PRODUCT_COLOR,
                VKCDbConstants.PRODUCT_QUANTITY,
                VKCDbConstants.PRODUCT_GRIDVALUE
            )
            val cursor: Cursor? = databaseManager?.fetchFromDB(
                cols, VKCDbConstants.TABLE_SHOPPINGCART,
            )
            if (cursor?.count!! > 0) {
                while (!cursor?.isAfterLast!!) {
                    tableCount = tableCount + cursor?.count
                    cursor?.moveToNext()
                }
            }
            return cursor?.count
        }

    override fun onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed()
        AppController.isClickedCartAdapter = true
        finish()
    }

    private fun getCreditValue() {
        var manager: VKCInternetManager? = null
        var cust_id = ""
        cust_id = if (AppPrefenceManager.getUserType(this@CartActivity) == "4") ({
            AppPrefenceManager.getSelectedUserId(this@CartActivity)
        })!!.toString() else ({
            AppPrefenceManager.getCustomerId(this@CartActivity)
        })!!.toString()
        val name = arrayOf("dealerid")
        val value = arrayOf(cust_id)
        manager = VKCInternetManager(VKCUrlConstants.GET_QUICK_ORDER_CREDIT_URL)
        manager.getResponsePOST(mActivity, name, value, object :
            VKCInternetManager.ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                try {
                    val responseObj = JSONObject(successResponse)
                    val response: JSONObject = responseObj.getJSONObject("response")
                    val status: String = response.getString("status")
                    if (status == "Success") {
                        creditValue = response.getString("creditvalue")
                        textCreditValue?.setText("Order Limit: ₹$creditValue")
                        creditPrice = creditValue!!.toInt()
                    }
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }

            override fun responseFailure(failureResponse: String?) { // TODO
            }
        })
    }

    inner class DeleteAlert(a: Activity) : Dialog(a), View.OnClickListener, VKCDbConstants {
        var mActivity: Activity
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_clear_cart)
            init()
        }

        private fun init() {
            val buttonSet = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener {
                val listDelete = ArrayList<String>()
                for (i in AppController.cartArrayList.indices) {
                    listDelete.add(
                        AppController.cartArrayList.get(i)
                            .pid
                    )
                }
                deleteApi(listDelete)
                clearDb()
                VKCUtils.showtoast(mActivity, 55)
                setCartQuantity()
                cartData
                AppPrefenceManager.setFillTable(mActivity, "false")
                AppController.cartArrayList.clear()
                AppController.cartArrayListSelected.clear()
                mSalesAdapter = SalesOrderActivityAdapter(
                    this@CartActivity, AppController.cartArrayList,
                    lnrTableHeaders!!, txtQty!!, txtTotalQty, txtCartValue
                )
                AppPrefenceManager.setDate(mActivity, "")
                mSalesAdapter!!.notifyDataSetChanged()
                mDealersListView!!.adapter = mSalesAdapter
                // updateCartValue();
                dismiss()
            }
            val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener { dismiss() }
        }

        override fun onClick(v: View) {
            dismiss()
        }

        init {
            // TODO Auto-generated constructor stub
            this.mActivity = a
        }
    }

    fun deleteApi(listDelete: ArrayList<String>) {
        val name = arrayOf("ids")
        val values = arrayOf(listDelete.toString())
        val manager = VKCInternetManager(
            VKCUrlConstants.Companion.DELETE_CART_ITEM_API
        )
        manager.getResponsePOST(mActivity, name, values,
            object : VKCInternetManager.ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    try {
                        val obj = JSONObject(successResponse)
                        val status: String = obj.optString("status")
                        if (status == "Success") {
                            txtTotalQty?.setText(
                                "Total Qty. :"
                                        + obj.optString("tot_qty")
                            )
                            txtCartValue?.setText(
                                "Cart Value: ₹"
                                        + obj.optString("cart_value")
                            )
                            txtTotalItem?.setText(
                                "Total Item : "
                                        + obj.optString("tot_items")
                            )
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

    companion object {
        var isCart = false
    }
}