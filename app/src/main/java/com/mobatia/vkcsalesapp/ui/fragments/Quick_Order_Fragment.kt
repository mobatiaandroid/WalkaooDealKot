package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter
import com.mobatia.vkcsalesapp.adapter.SizeAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.customview.HorizontalListView
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSelectedUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setIsCallPendingAPI
import com.mobatia.vkcsalesapp.manager.DataBaseManager
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_BRAND_IMAGENAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_COST
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_NAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_COLOR_ID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_COLOR_IMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_CASE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_COLOR
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_DESCRIPTION
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_ID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_IMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_NAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_OFFER
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_ORDER
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_QTY
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_SIZE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_TIMESTAMP
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_TYPE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_PRODUCT_VIEWS
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_BRANDID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_BRANDNAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_CASEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_CASENAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORCODE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class Quick_Order_Fragment() : Fragment(), View.OnClickListener, VKCUrlConstants, VKCDbConstants {
    private var mRootView: View? = null
    var mDisplayWidth = 0
    var mDisplayHeight = 0
    var listTemp: ArrayList<String>? = null
    var listTempCustom: ArrayList<String>? = null
    private var databaseManager: DataBaseManager? = null
    var mSpinnerDealer: Spinner? = null
    var mSpinnerSize: Spinner? = null
    var mSpinnerColor: Spinner? = null
    var mEditArticle: AutoCompleteTextView? = null
    var mImageSearch: ImageView? = null
    private val imageViewSubmit: ImageView? = null
    private var llDealer: LinearLayout? = null
    var mActivity: Activity? = null
    private var colorArrayList: ArrayList<ColorModel>? = null
    private var sizeArrayList: ArrayList<SizeModel>? = null
    var productModels: ArrayList<ProductModel>? = null
    private var caseArrayList: ArrayList<CaseModel>? = null
    private var newArrivalArrayList: ArrayList<ProductImages>? = null
    var listArticleNumbers: ArrayList<String>? = null
    var dealersShopModels: ArrayList<DealersShopModel>? = null
    var listDealer = ArrayList<String>()
    var sizeArray: ArrayList<SizeModel>? = null
    var productModel: ProductModel? = ProductModel()
    var tableLayout: TableLayout? = null
    var tableLayoutCustomSize: TableLayout? = null
    var editList: ArrayList<EditText>? = null
    var count = 0
    var editListCustom: ArrayList<EditText>? = null
    private var txtNameText: TextView? = null
    private var txtViewPrice: TextView? = null
    private var txtDescription: TextView? = null
    private var txtCatName: TextView? = null
    var tableCount = 0
    private var mImageAddToCart: ImageView? = null
    private var textCreditValue: TextView? = null
    var creditValue: String? = null
    var listviewSize: HorizontalListView? = null
    var caseSize = ""
    lateinit var listArticle: Array<String>
    var cartPrice = 0f
    var spinnerCategory: Spinner? = null
    var selectedCatId: String=""
    var arrayCategory: ArrayList<QuickOrderCategoryModel>? = null

    // String[] category;
    var listCategory: ArrayList<String>? = null
    private var txtCartValue: TextView? = null
    var priceTotal = 0f
    var isShowMessage = false
    private var cartValue = 0
    var isInserted = false
    private var pDialog: CustomProgressBar? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.fragment_quick_order, container,
            false
        )
        setHasOptionsMenu(true)
        AppController.isCart = false
        isShowMessage = false
        mActivity = activity
        setDisplayParams()
        tableCount = 0
        //getCartValue();
        //getPendingQuantity();
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
        textviewTitle.text = "Quick Order"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        init(mRootView)
        return mRootView
    }

    private fun setDisplayParams() {
        val displayManagerScale = DisplayManagerScale(
            activity!!
        )
        mDisplayHeight = displayManagerScale.deviceHeight
        mDisplayWidth = displayManagerScale.deviceWidth
    }

    private fun init(v: View?) {
        llDealer = v!!.findViewById<View>(R.id.llDealer) as LinearLayout
        mEditArticle = v
            .findViewById<View>(R.id.textArticleNo) as AutoCompleteTextView
        mEditArticle!!.setText("")
        // mImageSearch = (ImageView) v.findViewById(R.id.imageSearch);
        tableLayout = v.findViewById<View>(R.id.matrixLayout) as TableLayout
        tableLayoutCustomSize = v
            .findViewById<View>(R.id.matrixLayoutCustomSize) as TableLayout
        txtNameText = v.findViewById<View>(R.id.textModel) as TextView
        txtViewPrice = v.findViewById<View>(R.id.textPrice) as TextView
        txtDescription = v.findViewById<View>(R.id.textDescription) as TextView
        textCreditValue = v.findViewById<View>(R.id.textCreditValue) as TextView
        txtCartValue = v.findViewById<View>(R.id.textCartValue) as TextView
        txtCatName = v.findViewById<View>(R.id.textBrand) as TextView
        colorArrayList = ArrayList()
        sizeArrayList = ArrayList()
        caseArrayList = ArrayList()
        spinnerCategory = v.findViewById<View>(R.id.spinnerCategory) as Spinner
        newArrivalArrayList = ArrayList()
        productModels = ArrayList()
        dealersShopModels = ArrayList()
        listviewSize = v.findViewById<View>(R.id.listviewSize) as HorizontalListView
        sizeArray = ArrayList()
        listArticleNumbers = ArrayList()
        editListCustom = ArrayList()
        mImageAddToCart = v.findViewById<View>(R.id.imageAddtoCart) as ImageView
        arrayCategory = ArrayList()
        listCategory = ArrayList()
        // Set Cart Value
        //	updateCartValue();
        articleNumbers
        //AppController.arrayListSize.clear();
        if ((getUserType((activity)!!) == "7")) {
            myDealers
            llDealer!!.visibility = View.VISIBLE
        } else if ((getUserType((activity)!!) == "4")) {
            llDealer!!.visibility = View.GONE
            if (getSelectedUserId((activity)!!) != "") {
                getCreditValue(getCustomerId((activity)!!))
                pendingQuantity
            } else {
                val toast = CustomToast((activity)!!)
                toast.show(56)
            }
        } else {
            llDealer!!.visibility = View.GONE
            getCreditValue(getCustomerId((activity)!!))
            pendingQuantity
        }
        mImageAddToCart!!.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
            isInserted = false
            val addToCart: AddToCart = AddToCart()
            addToCart.execute()
        })
        mEditArticle!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View, pos: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                category
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        mEditArticle!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                arg0: AdapterView<*>?, arg1: View, arg2: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                category
            }
        }
        spinnerCategory!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View, pos: Int,
                arg3: Long
            ) {
                // TODO Auto-generated method stub
                if (pos > 0) {
                    selectedCatId = arrayCategory!!.get(pos - 1).category_id
                    getCartPrice()
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }
        editList = ArrayList()
        databaseManager = DataBaseManager(activity!!)
        mSpinnerDealer = v.findViewById<View>(R.id.spinnerDealer) as Spinner

        // Dealer List Api Call
        // getMyDealers();
        /*
		 * imageViewSubmit = (ImageView) v.findViewById(R.id.imageViewSubmit);
		 * 
		 * imageViewSubmit.setOnClickListener(this);
		 */mSpinnerDealer!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                arg0: AdapterView<*>?, arg1: View,
                position: Int, arg3: Long
            ) { // TODO Auto-generated method
                // stub
                if (position == 0) {
                } else {
                    getCreditValue(
                        dealersShopModels!!.get(position - 1)
                            .dealerId
                    )
                }
            }

            override fun onNothingSelected(arg0: AdapterView<*>?) { //
                // TODO Auto-generated method stub
            }
        }
    }

    fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater = activity!!.menuInflater
        inflater.inflate(R.menu.main, menu)
        val item = menu.findItem(R.id.action_settings)
        item.isVisible = false
        activity!!.invalidateOptionsMenu()
        return true
    }

    override fun onClick(v: View) {

        /*
		 * if (v == imageViewSubmit) {
		 * 
		 * } else if (v == mImageSearch) {
		 * 
		 * }
		 */
    }// TODO

    // Auto-generated method stub
    // parseJSON(successResponse);
    private val myDealers: Unit
        private get() {
            var manager: VKCInternetManager? = null
            dealersShopModels!!.clear()
            listDealer.clear()
            val name = arrayOf("subdealer_id")
            val value = arrayOf(
                getUserId(
                    (mActivity)!!
                )
            )
            manager = VKCInternetManager(VKCUrlConstants.LIST_MY_DEALERS_URL)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {

                    if (successResponse != null) {
                        parseMyDealerJSON(successResponse)
                    }
                }

                override fun responseFailure(failureResponse: String?) { // TODO
                    // Auto-generated method stub
                }
            })
        }

    private fun parseMyDealerJSON(successResponse: String) { // TODO
        // Auto-generated method stub
        try {
            val respObj = JSONObject(successResponse)
            val response = respObj.getJSONObject("response")
            val status = response.getString("status")
            listDealer.add("Select Dealer")
            if ((status == "Success")) {
                val respArray = response.getJSONArray("dealers")
                for (i in 0 until respArray.length()) {
                    dealersShopModels
                        ?.add(parseShop(respArray.getJSONObject(i)))
                }
                val dataAdapter = ArrayAdapter(
                    (mActivity)!!, android.R.layout.simple_spinner_item,
                    listDealer
                )

                // Drop down layout style - list view with radio button
                // dataAdapter
                dataAdapter
                    .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                // attaching data adapter to spinner
                mSpinnerDealer!!.adapter = dataAdapter
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
        listDealer.add(jsonObject.optString("name"))
        return dealersShopModel
    }//

    // TODO Auto-generated method stub
    private val products: Unit
        private get() {
            productModels!!.clear()
            colorArrayList!!.clear()
            caseArrayList!!.clear()
            sizeArrayList!!.clear()
            newArrivalArrayList!!.clear()
            AppController.arrayListSize.clear()
            val name = arrayOf("productId", "categoryId")
            val values = arrayOf(
                mEditArticle!!.text.toString().trim { it <= ' ' },
                selectedCatId
            )
            val manager = VKCInternetManager(
                VKCUrlConstants.GET_PRODUCT_DETAILS_URL
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) { //
                        // TODO Auto-generated method stub

                        if (successResponse != null) {
                            parseResponse(successResponse)
                        }
                        if (successResponse != "") {
                            txtNameText!!.text = (""
                                    + productModel!!.productType.get(0)
                                .name)
                            txtViewPrice!!.text = ("Rs."
                                    + productModel!!.getmProductPrize())
                            txtDescription!!.text = productModel
                                ?.productdescription
                            txtCatName!!.text = productModel!!.categoryName

                            // txtCatName.setText(productModel.);
                            colorArrayList = productModel!!.productColor
                            sizeArrayList = productModel!!.getmProductSize()
                            caseArrayList = productModel!!.getmProductCases()
                            for (i in sizeArrayList?.indices!!) {
                                val model = QuickSizeModel()
                                model.sizeName = sizeArrayList?.get(i)
                                    ?.name
                                model?.sizeId = sizeArrayList?.get(i)?.id
                                model?.isSelected = false
                                AppController.arrayListSize.add(model)


                            }
                            if (AppController.cartArrayListSelected.size > 0) {
                                createTableWithData()
                            } else {
                                createTable()
                            }
                            val adapter = SizeAdapter(
                                mActivity!!,
                                AppController.arrayListSize
                            )
                            listviewSize!!.adapter = adapter
                            AppController.product_id = ""
                        } else {
                            showtoast(mActivity, 42)
                        }
                    }

                    fun createTableWithData() {
                        // TODO Auto-generated method stub
                        tableLayout!!.removeAllViews()
                        if (AppController.cartArrayListSelected.size < caseArrayList
                                ?.size!!
                        ) {
                            val diff = (caseArrayList!!.size
                                    - AppController.cartArrayListSelected
                                .size)
                            for (i in 0..diff) {
                                val model = CartModel()
                                model.pid = ""
                                model.prodColor = ""
                                model.prodName = ""
                                model.prodQuantity = ""
                                model.prodSize = ""
                                AppController.cartArrayListSelected.add(model)
                            }
                        }
                        var caseName = ""
                        var colorName = ""
                        val width = activity!!.windowManager
                            .defaultDisplay.width
                        // int column_width = width / 6;
                        val column_width = 150
                        val tableLayoutParams = TableLayout.LayoutParams()
                        tableLayout!!.setBackgroundColor(
                            resources.getColor(
                                R.color.vkcred
                            )
                        )

                        // 2) create tableRow params
                        val tableRowParams = TableRow.LayoutParams()
                        tableRowParams.setMargins(1, 1, 1, 1)
                        tableRowParams.weight = 1f
                        editList!!.clear()
                        for (i in 0 until caseArrayList!!.size + 1) {
                            // 3) create tableRow
                            /*
                                * if(caseArrayList.size()+1<i) {
                                */
                            if (i == 0) {
                                caseName = ""
                            } else {
                                caseName = caseArrayList!![i - 1].name
                            }
                            println(
                                ("Case name " + i + ": "
                                        + caseName)
                            )
                            // }
                            val tableRow = TableRow(activity)
                            tableRow.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            tableRow.setBackgroundColor(
                                resources
                                    .getColor(R.color.vkcred)
                            )
                            tableRow.setPadding(0, 0, 0, 2)
                            for (j in 0 until colorArrayList!!.size + 1) {
                                if (j == 0) {
                                    colorName = ""
                                } else {
                                    colorName = colorArrayList!![j - 1]
                                        .colorName
                                }
                                println(
                                    ("Color name " + j + ": "
                                            + colorName)
                                )
                                // 4) create textView
                                val textView = TextView(activity)
                                // textView.setText(String.valueOf(j));
                                textView.setBackgroundColor(Color.WHITE)
                                textView.width = column_width
                                textView.gravity = (Gravity.CENTER
                                        or Gravity.CENTER_HORIZONTAL)
                                textView.layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                val s1 = Integer.toString(i)
                                val s2 = Integer.toString(j)
                                val s3 = s1 + s2
                                val id = s3.toInt()
                                Log.d("TAG", "-___>$id")
                                if (i == 0 && j == 0) {
                                    textView.text = "Test"
                                    textView.setBackgroundColor(Color.WHITE)
                                    textView.visibility = View.INVISIBLE
                                } else if (i == 0) {
                                    Log.d("TAAG", "set Column Headers")
                                    textView.text = colorArrayList!!.get(j - 1)
                                        .colorName
                                } else if (i == caseArrayList!!.size + 2) {
                                    Log.d("TAAG", "set Column Headers")
                                    textView.visibility = View.GONE
                                    val edit = EditText(activity)
                                    edit.inputType = InputType.TYPE_CLASS_NUMBER
                                    // edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
                                    edit.layoutParams = TableRow.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                    )
                                    edit.hint = "Case."
                                    edit.textSize = 15f
                                    edit.width = column_width
                                    edit.setBackgroundColor(Color.WHITE)
                                    edit.gravity = (Gravity.CENTER
                                            or Gravity.CENTER_HORIZONTAL)
                                    edit.tag = i * 10 + j
                                    edit.isSingleLine = true
                                    edit.id = i * 10 + j
                                    tableRow.addView(edit, tableRowParams)
                                    editList!!.add(edit)
                                } else if (j == 0) {
                                    Log.d("TAAG", "Set Row Headers")
                                    textView.text = caseArrayList!!.get(i - 1)
                                        .name
                                } else {
                                    textView.visibility = View.GONE
                                    val edit = EditText(activity)
                                    edit.inputType = InputType.TYPE_CLASS_NUMBER
                                    // edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
                                    edit.layoutParams = TableRow.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                    )
                                    edit.hint = "Qty."
                                    edit.textSize = 15f
                                    edit.width = column_width
                                    edit.setBackgroundColor(Color.WHITE)
                                    edit.gravity = (Gravity.CENTER
                                            or Gravity.CENTER_HORIZONTAL)
                                    edit.tag = i * 10 + j
                                    edit.isSingleLine = true
                                    edit.id = i * 10 + j
                                    tableRow.addView(edit, tableRowParams)
                                    editList!!.add(edit)
                                }
                                // 5) add textView to tableRow
                                tableRow.addView(textView, tableRowParams)
                            }

                            // 6) add tableRow to tableLayout
                            tableLayout!!.addView(tableRow, tableLayoutParams)
                        }
                        for (i in 0..1) {
                            // 3) create tableRow
                            val tableRow = TableRow(activity)
                            tableRow.layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            tableRow.setBackgroundColor(
                                resources
                                    .getColor(R.color.vkcred)
                            )
                            tableRow.setPadding(0, 0, 0, 2)
                            for (j in 0 until colorArrayList!!.size + 1) {
                                // 4) create textView
                                val textView = TextView(activity)
                                // textView.setText(String.valueOf(j));
                                textView.setBackgroundColor(Color.WHITE)
                                textView.width = column_width
                                textView.gravity = (Gravity.CENTER
                                        or Gravity.CENTER_HORIZONTAL)
                                textView.layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                val s1 = Integer.toString(i)
                                val s2 = Integer.toString(j)
                                val s3 = s1 + s2
                                val id = s3.toInt()
                                Log.d("TAG", "-___>$id")
                                if (i == 0 && j == 0) {
                                    textView.text = "Test"
                                    textView.setBackgroundColor(Color.WHITE)
                                    textView.visibility = View.GONE
                                } else if (i == 0) {
                                    textView.text = colorArrayList!!.get(j - 1)
                                        .colorName
                                } else if (i == 1 && j == 0) {
                                    textView.setBackgroundColor(
                                        resources
                                            .getColor(R.color.vkcred)
                                    )
                                    textView.visibility = View.GONE
                                } else {
                                    textView.visibility = View.GONE
                                    val edit = EditText(activity)
                                    edit.inputType = InputType.TYPE_CLASS_NUMBER
                                    // edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
                                    edit.layoutParams = TableRow.LayoutParams(
                                        TableRow.LayoutParams.MATCH_PARENT,
                                        TableRow.LayoutParams.WRAP_CONTENT
                                    )
                                    edit.hint = "Qty."
                                    edit.textSize = 15f
                                    edit.width = column_width
                                    edit.setBackgroundColor(Color.WHITE)
                                    edit.gravity = (Gravity.CENTER
                                            or Gravity.CENTER_HORIZONTAL)
                                    edit.tag = i * 10 + j
                                    edit.isSingleLine = true
                                    edit.id = i * 10 + j
                                    tableRow.addView(edit, tableRowParams)
                                    editListCustom!!.add(edit)
                                }
                                // 5) add textView to tableRow
                                tableRow.addView(textView, tableRowParams)
                            }

                            // 6) add tableRow to tableLayout
                            tableLayoutCustomSize!!.addView(
                                tableRow,
                                tableLayoutParams
                            )
                        }
                        setTableData()
                    }

                    fun setTableData() {
                        var caseName = ""
                        var listCaseName: String
                        var colorName = ""
                        var listColorName: String
                        var Quantity: String? = ""
                        var selectedRow = 0
                        var selectedColumn = 0
                        var setPosition: Int
                        for (i in AppController.cartArrayListSelected
                            .indices) {
                            colorName = AppController.cartArrayListSelected[i].prodColor
                            caseName = AppController.cartArrayListSelected[i].prodSize
                            Quantity = AppController.cartArrayListSelected[i].prodQuantity
                            AppController.size = caseName
                            AppController.color = colorName
                            for (j in caseArrayList!!.indices) {
                                listCaseName = caseArrayList!![j].name
                                if ((listCaseName == caseName)) {
                                    selectedRow = j
                                }
                            }
                            for (k in colorArrayList!!.indices) {
                                listColorName = colorArrayList!![k]
                                    .colorName
                                if ((listColorName == colorName)) {
                                    selectedColumn = k
                                }
                            }
                            if (((caseArrayList!![selectedRow].name
                                        == caseName) && (colorArrayList!![selectedColumn]
                                    .colorName == colorName))
                            ) {
                                setPosition = (selectedRow
                                        * colorArrayList!!.size
                                        + selectedColumn)
                                editList!![setPosition].setText(Quantity)
                            }
                        }
                    }

                    override fun responseFailure(failureResponse: String?) { //
                        // TODO Auto-generated method stub
                        // Log.d("LOG Product", "product response3");

                        // Log.v("LOG", "08012015 Errror" + failureResponse);
                    }
                })
        }

    private fun parseResponse(response: String) {
        if (response != "") {
            try {
                val jsonObjectresponse = JSONObject(response)
                val obj = jsonObjectresponse.optJSONObject("response")
                val status = obj.getString("status")
                val jsonArrayresponse = obj.getJSONArray("details")
                for (j in 0 until jsonArrayresponse.length()) {
                    val jsonObjectZero = jsonArrayresponse
                        .getJSONObject(j)
                    val productModel = ProductModel()
                    productModel.productdescription = jsonObjectZero
                        .optString("productdescription")
                    productModel.categoryName = jsonObjectZero
                        .optString(JSON_CATEGORY_NAME)
                    productModel.categoryId = jsonObjectZero
                        .optString("categoryid")
                    productModel.setmSapId(
                        jsonObjectZero
                            .optString("productSapId")
                    )
                    productModel.setmProductPrize(
                        jsonObjectZero
                            .optString(JSON_CATEGORY_COST)
                    )
                    productModel.id = jsonObjectZero
                        .optString(JSON_PRODUCT_ID)
                    productModel.setmProductName(
                        jsonObjectZero
                            .optString(JSON_PRODUCT_NAME)
                    )
                    productModel.productquantity = jsonObjectZero
                        .optString(JSON_PRODUCT_QTY)
                    productModel.productDescription = jsonObjectZero
                        .optString(JSON_PRODUCT_DESCRIPTION)
                    productModel.productViews = jsonObjectZero
                        .optString(JSON_PRODUCT_VIEWS)
                    productModel.timeStamp = jsonObjectZero
                        .optString(JSON_PRODUCT_TIMESTAMP)
                    productModel.setmProductOff(
                        jsonObjectZero
                            .optString(JSON_PRODUCT_OFFER)
                    )
                    var orderCount = 0
                    try {
                        orderCount = jsonObjectZero
                            .optString(JSON_PRODUCT_ORDER).toInt()
                    } catch (e: Exception) {
                        orderCount = 0
                    }
                    productModel.setmProductOrder(orderCount)
                    val productColorArray = jsonObjectZero
                        .getJSONArray(JSON_PRODUCT_COLOR)
                    val productImageArray = jsonObjectZero
                        .getJSONArray(JSON_PRODUCT_IMAGE)

                    val productSizeArray = jsonObjectZero
                        .getJSONArray(JSON_PRODUCT_SIZE)
                    val productTypeArray = jsonObjectZero
                        .getJSONArray(JSON_PRODUCT_TYPE)
                    val productCaseArray = jsonObjectZero
                        .getJSONArray(JSON_PRODUCT_CASE)
                    val productNewArrivalArray = jsonObjectZero
                        .getJSONArray("new_arrivals")
                    val colorModels = ArrayList<ColorModel>()
                    for (i in 0 until productColorArray.length()) {
                        val colorModel = ColorModel()
                        val jsonObject = productColorArray
                            .getJSONObject(i)
                        colorModel.id = jsonObject
                            .optString(JSON_SETTINGS_COLORID)
                        colorModel.colorcode = jsonObject
                            .optString(JSON_SETTINGS_COLORCODE)
                        colorModel.colorName = jsonObject
                            .optString("color_name")
                        colorModels.add(colorModel)
                    }
                    productModel.productColor = colorModels

                    // ////////////
                    val productImages = ArrayList<ProductImages>()
                    for (i in 0 until productImageArray.length()) {
                        val images = ProductImages()
                        val jsonObject = productImageArray
                            .getJSONObject(i)
                        images.id = jsonObject.optString("image_id")
                        images.imageName = (VKCUrlConstants.BASE_URL
                                + jsonObject.optString(JSON_COLOR_IMAGE))
                        images.productName = jsonObject
                            .optString("product_name")
                        val colorModel = ColorModel()
                        colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                        colorModel.colorcode = jsonObject
                            .optString(JSON_SETTINGS_COLORCODE)
                        images.colorModel = colorModel
                        productImages.add(images)
                    }
                    productModel.productImages = productImages
                    // ///
                    val newArrivals = ArrayList<ProductImages>()
                    for (i in 0 until productNewArrivalArray.length()) {
                        val images = ProductImages()
                        val jsonObject = productNewArrivalArray
                            .getJSONObject(i)
                        images.id = jsonObject
                            .optString(JSON_SETTINGS_COLORID)
                        images.imageName = (VKCUrlConstants.BASE_URL
                                + jsonObject.optString(JSON_COLOR_IMAGE))
                        images.productName = jsonObject
                            .optString("product_name")
                        val colorModel = ColorModel()
                        colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                        colorModel.colorcode = jsonObject
                            .optString(JSON_SETTINGS_COLORCODE)
                        images.colorModel = colorModel
                        images.catId = jsonObject.optString("categoryid")
                        newArrivals.add(images)
                    }
                    productModel.setmNewArrivals(newArrivals)
                    val sizeModels = ArrayList<SizeModel>()
                    for (i in 0 until productSizeArray.length()) {
                        val sizeModel = SizeModel()
                        val jsonObject = productSizeArray
                            .getJSONObject(i)
                        sizeModel.id = jsonObject
                            .optString(JSON_SETTINGS_SIZEID)
                        sizeModel.name = jsonObject
                            .optString(JSON_SETTINGS_SIZENAME)
                        sizeModels.add(sizeModel)
                    }
                    productModel.setmProductSize(sizeModels)
                    // /////
                    val brandTypeModels = ArrayList<BrandTypeModel>()
                    for (i in 0 until productTypeArray.length()) {
                        val typeModel = BrandTypeModel()
                        val jsonObject = productTypeArray
                            .getJSONObject(i)
                        typeModel.id = jsonObject
                            .optString(JSON_SETTINGS_BRANDID)
                        typeModel.name = jsonObject
                            .optString(JSON_SETTINGS_BRANDNAME)
                        typeModel.imgUrl = jsonObject
                            .optString(JSON_BRAND_IMAGENAME)
                        brandTypeModels.add(typeModel)
                    }
                    productModel.productType = brandTypeModels
                    val caseModels = ArrayList<CaseModel>()
                    for (i in 0 until productCaseArray.length()) {
                        val caseModel = CaseModel()
                        val jsonObject = productCaseArray
                            .getJSONObject(i)
                        caseModel.id = jsonObject
                            .optString(JSON_SETTINGS_CASEID)
                        caseModel.name = jsonObject
                            .optString(JSON_SETTINGS_CASENAME)
                        caseModels.add(caseModel)
                    }
                    productModel.setmProductCases(caseModels)
                    productModels!!.add(productModel)
                }
                productModel = productModels!![0]
                val imageListSize = productModel!!.productImages.size
            } catch (e: Exception) {
                e.printStackTrace()
            }
            getCreditValue(getCustomerId((activity)!!))
        } else {
            showtoast(mActivity, 42)
        }
    }

    fun createTable() {
        tableLayout!!.removeAllViews()
        tableLayoutCustomSize!!.removeAllViews()
        val width = activity!!.windowManager.defaultDisplay
            .width
        // int column_width = width / 7;
        val column_width = 150
        val tableLayoutParams = TableLayout.LayoutParams()
        tableLayout!!.setBackgroundColor(resources.getColor(R.color.vkcred))

        // 2) create tableRow params
        val tableRowParams = TableRow.LayoutParams()
        tableRowParams.setMargins(1, 1, 1, 1)
        tableRowParams.weight = 1f
        editList!!.clear()
        editListCustom!!.clear()
        if (caseArrayList!!.size > 0 && colorArrayList!!.size > 0) {
            for (i in 0 until caseArrayList!!.size + 1) {
                // 3) create tableRow
                val tableRow = TableRow(activity)
                tableRow.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                tableRow.setBackgroundColor(
                    resources.getColor(
                        R.color.vkcred
                    )
                )
                tableRow.setPadding(0, 0, 0, 2)
                for (j in 0 until colorArrayList!!.size + 1) {
                    // 4) create textView
                    val textView = TextView(activity)
                    // textView.setText(String.valueOf(j));
                    textView.setBackgroundColor(Color.WHITE)
                    textView.width = column_width
                    textView.gravity = (Gravity.CENTER
                            or Gravity.CENTER_HORIZONTAL)
                    textView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val s1 = Integer.toString(i)
                    val s2 = Integer.toString(j)
                    val s3 = s1 + s2
                    val id = s3.toInt()
                    Log.d("TAG", "-___>$id")
                    if (i == 0 && j == 0) {
                        textView.text = "Test"
                        textView.setBackgroundColor(Color.WHITE)
                        textView.visibility = View.INVISIBLE
                    } else if (i == 0) {
                        Log.d("TAAG", "set Column Headers")
                        textView.text = colorArrayList!!.get(j - 1)
                            .colorName
                    } else if (j == 0) {
                        Log.d("TAAG", "Set Row Headers")
                        textView.text = caseArrayList!!.get(i - 1).name
                    } else {
                        textView.visibility = View.GONE
                        val edit = EditText(activity)
                        edit.inputType = InputType.TYPE_CLASS_NUMBER
                        // edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
                        edit.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        edit.hint = "Qty."
                        edit.textSize = 15f
                        edit.width = column_width
                        edit.setBackgroundColor(Color.WHITE)
                        edit.gravity = (Gravity.CENTER
                                or Gravity.CENTER_HORIZONTAL)
                        edit.tag = i * 10 + j
                        edit.isSingleLine = true
                        edit.id = i * 10 + j
                        // edit.setBackgroundColor(Color.WHITE);
                        // edit.setText(Double.toString(matrix[i][j]));
                        tableRow.addView(edit, tableRowParams)
                        editList!!.add(edit)
                    }
                    // 5) add textView to tableRow
                    tableRow.addView(textView, tableRowParams)
                }

                // 6) add tableRow to tableLayout
                tableLayout!!.addView(tableRow, tableLayoutParams)
            }
            for (i in 0..1) {
                // 3) create tableRow
                val tableRow = TableRow(activity)
                tableRow.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                )
                tableRow.setBackgroundColor(
                    resources.getColor(
                        R.color.vkcred
                    )
                )
                tableRow.setPadding(0, 0, 0, 2)
                for (j in 0 until colorArrayList!!.size + 1) {
                    // 4) create textView
                    val textView = TextView(activity)
                    // textView.setText(String.valueOf(j));
                    textView.setBackgroundColor(Color.WHITE)
                    textView.width = column_width
                    textView.gravity = (Gravity.CENTER
                            or Gravity.CENTER_HORIZONTAL)
                    textView.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    val s1 = Integer.toString(i)
                    val s2 = Integer.toString(j)
                    val s3 = s1 + s2
                    val id = s3.toInt()
                    Log.d("TAG", "-___>$id")
                    if (i == 0 && j == 0) {
                        textView.text = "Test"
                        textView.setBackgroundColor(Color.WHITE)
                        textView.visibility = View.GONE
                    } else if (i == 0) {
                        // Log.d("TAAG", "set Column Headers");
                        textView.text = colorArrayList!!.get(j - 1)
                            .colorName
                    } else if (i == 1 && j == 0) {
                        textView.setBackgroundColor(
                            resources.getColor(
                                R.color.vkcred
                            )
                        )
                        textView.visibility = View.GONE
                    } else {
                        textView.visibility = View.GONE
                        val edit = EditText(activity)
                        edit.inputType = InputType.TYPE_CLASS_NUMBER
                        // edit.setBackgroundResource(R.drawable.rounded_rectangle_red);
                        edit.layoutParams = TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                        )
                        edit.hint = "Qty."
                        edit.textSize = 15f
                        edit.width = column_width
                        edit.setBackgroundColor(Color.WHITE)
                        edit.gravity = (Gravity.CENTER
                                or Gravity.CENTER_HORIZONTAL)
                        edit.tag = i * 10 + j
                        edit.isSingleLine = true
                        edit.id = i * 10 + j
                        // edit.setBackgroundColor(Color.WHITE);
                        // edit.setText(Double.toString(matrix[i][j]));
                        tableRow.addView(edit, tableRowParams)
                        editListCustom!!.add(edit)
                    }
                    // 5) add textView to tableRow
                    tableRow.addView(textView, tableRowParams)
                }

                // 6) add tableRow to tableLayout
                tableLayoutCustomSize!!.addView(tableRow, tableLayoutParams)
            }
        } else {
            // Toast.makeText(mActivity, "", duration)
        }
    }//getCartValue();// AppController.cartArrayList.add(setCartModel(cursor));

    //getCartValue();
    // AppController.cartArrayList.clear();
    private val cartDataForTable: Unit
        private get() {
            // AppController.cartArrayList.clear();
            AppController.cartArrayListSelected.clear()
            val cols = arrayOf(
                VKCDbConstants.PRODUCT_ID,
                VKCDbConstants.PRODUCT_NAME,
                VKCDbConstants.PRODUCT_SIZEID,
                VKCDbConstants.PRODUCT_SIZE,
                VKCDbConstants.PRODUCT_COLORID,
                VKCDbConstants.PRODUCT_COLOR,
                VKCDbConstants.PRODUCT_QUANTITY,
                VKCDbConstants.PRODUCT_GRIDVALUE,
                "pid"
            )
            val cursor = databaseManager!!.fetchFromDB(
                cols, VKCDbConstants.TABLE_SHOPPINGCART,
                ""
            )
            if (cursor.count > 0) {
                while (!cursor.isAfterLast) {
                    // AppController.cartArrayList.add(setCartModel(cursor));
                    val pid = cursor.getString(1)
                    val pname = productModel!!.getmProductName()
                    if ((pid == mEditArticle!!.text.toString().trim { it <= ' ' })) {
                        val model = CartModel()
                        model.pid = cursor.getString(8)
                        model.prodColor = cursor.getString(5)
                        model.prodName = cursor.getString(1)
                        model.prodQuantity = cursor.getString(6)
                        model.prodSize = cursor.getString(3)
                        AppController.cartArrayListSelected.add(model)
                    }
                    cursor.moveToNext()
                }
                //getCartValue();
                products
            } else {
                //getCartValue();
                products
            }
        }

    private inner class AddToCart() : AsyncTask<Void?, Void?, Void?>() {
        val pDialog = CustomProgressBar(
            mActivity,
            R.drawable.loading
        )

        override fun onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute()
            priceTotal = 0f
            pDialog.show()
        }



        override fun onPostExecute(result: Void?) {
            // TODO Auto-generated method stub
            super.onPostExecute(result)
            pDialog.dismiss()
            //	AppController.arrayListSize.clear();
            if (isShowMessage) {
                // VKCUtils.showtoast(mActivity, 46);
                showExceedDialog()
            } else {
                if (listTemp!!.size > 0) {
                    showtoast(mActivity, 5)
                    listTemp!!.clear()
                    listTempCustom!!.clear()
                    mEditArticle!!.setText("")
                    caseSize = ""
                    init(mRootView)
                    AppController.p_id = ""
                    for (i in AppController.arrayListSize.indices) {
                        AppController.arrayListSize.get(i).isSelected = false
                    }
                    AppController.arrayListSize.clear()
                    for (i in editList!!.indices) {
                        editList!![i].setText("")
                    }
                    for (i in editListCustom!!.indices) {
                        editListCustom!![i].setText("")
                    }
                    val adapter = SizeAdapter(
                        mActivity!!,
                        AppController.arrayListSize
                    )
                    listviewSize!!.adapter = adapter
                    //updateCartValue();
                    tableLayout!!.removeAllViews()
                    tableLayoutCustomSize!!.removeAllViews()
                    spinnerCategory!!.adapter = null
                    mSpinnerDealer!!.adapter = null
                    txtNameText!!.text = ""
                    txtViewPrice!!.text = ""
                    txtDescription!!.text = ""
                    txtCatName!!.text = ""
                    // listviewSize.setAdapter(null);
                    // AppController.arrayListSize.clear();
                } else if (listTempCustom!!.size > 0) {
                    if (caseSize.length > 0) {
                        showtoast(mActivity, 5)
                        listTemp!!.clear()
                        listTempCustom!!.clear()
                        mEditArticle!!.setText("")
                        caseSize = ""
                        init(mRootView)
                        AppController.p_id = ""
                        for (i in AppController.arrayListSize.indices) {
                            AppController.arrayListSize.get(i).isSelected = false
                        }
                        AppController.arrayListSize.clear()
                        for (i in editList!!.indices) {
                            editList!![i].setText("")
                        }
                        for (i in editListCustom!!.indices) {
                            editListCustom!![i].setText("")
                        }
                        val adapter = SizeAdapter(
                            mActivity!!,
                            AppController.arrayListSize
                        )
                        listviewSize!!.adapter = adapter
                        //updateCartValue();
                        tableLayout!!.removeAllViews()
                        tableLayoutCustomSize!!.removeAllViews()
                        spinnerCategory!!.adapter = null
                        mSpinnerDealer!!.adapter = null
                        txtNameText!!.text = ""
                        txtViewPrice!!.text = ""
                        txtDescription!!.text = ""
                        txtCatName!!.text = ""
                    } else {
                        showtoast(mActivity, 47)
                    }
                } else {
                    showtoast(mActivity, 40)
                }
            }
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            if (productModel != null) {
                // Used for checking whether filled any data in table
                listTemp = ArrayList()
                listTempCustom = ArrayList()
                if (editList!!.size > 0) {
                    for (j in editList!!.indices) {
                        val qty = editList!![j].text.toString()
                            .trim { it <= ' ' }
                        var itemCount: Long = 0
                        try {
                            // the String to int conversion happens here
                            itemCount = qty.toLong()

                            // print out the value after the conversion
                        } catch (nfe: NumberFormatException) {
                            println(
                                ("NumberFormatException: "
                                        + nfe.message)
                            )
                        }
                        priceTotal = priceTotal + (itemCount * cartPrice)
                    }
                    println("cartValue $cartValue")
                    println("priceTotal $priceTotal")
                    println("creditPrice " + creditPrice)
                    if ((cartValue + priceTotal) > creditPrice) {
                        isShowMessage = true
                    } else {
                        isShowMessage = false
                        if (editList!!.size > 0) {
                            insertToDb()
                        }
                    }
                } else {
                    if (AppController.cartArrayList.size > 0) {
                        var count = 0
                        for (i in AppController.cartArrayList.indices) {
                            val mAdapter = SQLiteAdapter(
                                mActivity!!, VKCDbConstants.DBNAME
                            )
                            mAdapter.openToRead()
                            count = mAdapter.getCountDuplicate(
                                AppController.cartArrayList[i]
                                    .prodName,
                                AppController.cartArrayList[i]
                                    .prodSize,
                                AppController.cartArrayList[i]
                                    .prodColor
                            )
                        }
                    }
                }
                // }AddT
                if (editListCustom!!.size > 0) {
                    for (j in editListCustom!!.indices) {
                        val qty = editListCustom!![j].text.toString()
                            .trim { it <= ' ' }
                        var itemCount: Long = 0
                        try {
                            // the String to int conversion happens here
                            itemCount = qty.toLong()

                            // print out the value after the conversion
                        } catch (nfe: NumberFormatException) {
                            println(
                                ("NumberFormatException: "
                                        + nfe.message)
                            )
                        }
                        priceTotal = priceTotal + (itemCount * cartPrice)
                    }
                    if ((cartPrice + priceTotal) > creditPrice) {
                        isShowMessage = true
                    } else {
                        isShowMessage = false
                    }
                    for (i in editListCustom!!.indices) {
                        val id = editListCustom!![i]
                            .id.toString()
                        val qty = editListCustom!![i].text
                            .toString().trim { it <= ' ' }
                        var numberofitems: Long? = null
                        if (qty.length > 0) {
                            try {
                                // the String to int conversion happens here
                                numberofitems = qty.toLong()

                                // print out the value after the conversion
                                println("int i = $i")
                            } catch (nfe: NumberFormatException) {
                                println(
                                    ("NumberFormatException: "
                                            + nfe.message)
                                )
                            }
                        }
                        var idForColor: Int
                        var idForSize: Int
                        if (id.length > 2) {
                            idForColor = id.substring(2).toInt() - 1
                            idForSize = id.substring(0, 2).toInt() - 1
                        } else {
                            idForColor = id.substring(1).toInt() - 1
                            // idForColor=colorId-1;
                            idForSize = id.substring(0, 1).toInt() - 1
                        }
                        if (qty.length > 0) {
                            cartCount
                            for (j in AppController.arrayListSize
                                .indices) {
                                if (AppController.arrayListSize[j]
                                        .isSelected
                                ) {
                                    caseSize = (caseSize
                                            + AppController.arrayListSize[j].sizeName
                                            + ",")
                                }
                            }
                            println("Size$caseSize")
                            if (caseSize.length > 0) {
                                val values = arrayOf(
                                    arrayOf(VKCDbConstants.PRODUCT_ID, productModel!!.id),
                                    arrayOf(
                                        VKCDbConstants.PRODUCT_NAME,
                                        productModel!!
                                            .getmProductName()
                                    ),
                                    arrayOf(VKCDbConstants.PRODUCT_SIZEID, " "),
                                    arrayOf(
                                        VKCDbConstants.PRODUCT_SIZE,
                                        caseSize.substring(
                                            0,
                                            caseSize.length - 1
                                        )
                                    ),
                                    arrayOf(
                                        VKCDbConstants.PRODUCT_COLORID,
                                        colorArrayList!![idForColor].id
                                    ),
                                    arrayOf(
                                        VKCDbConstants.PRODUCT_COLOR,
                                        colorArrayList!![idForColor]
                                            .colorName
                                    ),
                                    arrayOf(VKCDbConstants.PRODUCT_QUANTITY, qty),
                                    arrayOf(VKCDbConstants.PRODUCT_GRIDVALUE, ""),
                                    arrayOf(
                                        "pid", (tableCount + 1).toString()
                                    ),
                                    arrayOf("sapid", productModel!!.getmSapId()),
                                    arrayOf(
                                        "catid",
                                        productModel!!
                                            .categoryId
                                    ),
                                    arrayOf("status", "local")
                                ) //{
                                /*"price",
									String.valueOf(numberofitems
											* cartPrice) }*/

                                // ,{"price",String.valueOf(numberOfItems*cartPrice)}
                                println(
                                    colorArrayList!![idForColor].id
                                )
                                val mAdapter = SQLiteAdapter(
                                    mActivity!!, VKCDbConstants.DBNAME
                                )
                                mAdapter.openToRead()
                                val count = mAdapter.getCount(
                                    AppController.p_id, caseArrayList
                                        ?.get(idForSize)?.name!!,
                                    colorArrayList!![idForColor]
                                        .colorName, qty
                                )
                                // System.out.print("Count---g" + count);
                                if (count == 0) {
                                    mAdapter.deleteUser(
                                        AppController.p_id,
                                        caseArrayList!![idForSize]
                                            .name,
                                        colorArrayList!![idForColor]
                                            .colorName,
                                        productModel!!.categoryId
                                    )
                                    databaseManager!!.insertIntoDb(
                                        VKCDbConstants.TABLE_SHOPPINGCART, values
                                    )
                                   // Log.e("Values", "test $values")
                                }
                                mAdapter.close()
                            }
                            // System.out.println("" + values);
                            listTempCustom!!.add(qty)
                        }
                    }
                }
            }
            return null        }
    }

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
            if (cursor.count > 0) {
                while (!cursor.isAfterLast) {
                    tableCount = tableCount + cursor.count
                    cursor.moveToNext()
                }
            }
            return cursor.count
        }

    private fun getCreditValue(dealerId: String?) {
        var manager: VKCInternetManagerWithoutDialog? = null
        var cust_id: String? = ""
        if ((getUserType((mActivity)!!) == "4")) {
            cust_id = getSelectedUserId((mActivity)!!)
        } else {
            cust_id = getCustomerId((mActivity)!!)
        }
        val name = arrayOf("dealerid")
        val value = arrayOf(cust_id)
        manager = VKCInternetManagerWithoutDialog(VKCUrlConstants.GET_QUICK_ORDER_CREDIT_URL)
        manager.getResponsePOST(mActivity, name, value, object : ResponseListenerWithoutDialog {
            override fun responseSuccess(successResponse: String) {
                try {
                    val responseObj = JSONObject(successResponse)
                    val response = responseObj.getJSONObject("response")
                    Log.i("Credit Response", "" + successResponse)
                    val status = response.getString("status")
                    if ((status == "Success")) {
                        creditValue = response.getString("creditvalue")
                        if (creditValue != "null") {
                            creditPrice = response
                                .getString("creditvalue").toInt()
                            textCreditValue!!.text = ("Order Limit: ₹ "
                                    + creditValue)
                        } else {
                            creditPrice = 0
                            textCreditValue!!.text = "Order Value: ₹ " + "0"
                        }
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
    }// TODO

    // Auto-generated method stub
// TODO Auto-generated catch block
    // listArticle[i]=articleArray.getString(i);
    private val articleNumbers: Unit
        private get() {
            var manager: VKCInternetManager? = null
            listArticleNumbers!!.clear()
            val name = arrayOf("")
            val value = arrayOf("")
            manager = VKCInternetManager(VKCUrlConstants.GET_QUICK_ARTICLE_NO_URL)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    try {
                        val responseObj = JSONObject(successResponse)
                        val response = responseObj.getJSONObject("response")
                        val status = response.getString("status")
                        if ((status == "Success")) {
                            val articleArray = response
                                .optJSONArray("articlenos")
                            if (articleArray.length() > 0) {
                                for (i in 0 until articleArray.length()) {
                                    // listArticle[i]=articleArray.getString(i);
                                    listArticleNumbers!!.add(
                                        articleArray
                                            .getString(i).toString()
                                    )
                                }
                                val adapter = ArrayAdapter(
                                    (mActivity)!!,
                                    android.R.layout.simple_list_item_1,
                                    listArticleNumbers!!
                                )
                                mEditArticle!!.setAdapter(adapter)
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

    private fun getCartPrice() {
        var manager: VKCInternetManager? = null
        val name = arrayOf("productid", "userid", "categoryid")
        val value = arrayOf(
            mEditArticle!!.text.toString().trim { it <= ' ' },
            getUserId((mActivity)!!), selectedCatId
        )
        manager = VKCInternetManager(VKCUrlConstants.GET_CART_VALUE_URL)
        manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                try {
                    val responseObj = JSONObject(successResponse)
                    val response = responseObj.getJSONObject("response")
                    val status = response.getString("status")
                    if ((status == "Success")) {
                        try {
                            cartPrice = response
                                .getString("cartvalue").toFloat()
                        } catch (e: NumberFormatException) {
                            println("Exception $e")
                        }
                        println("Cart Value$cartPrice")
                        cartDataForTable
                        // getProducts();
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
    }// TODO

    // Auto-generated method stub
    // TODO Auto-generated catch block
    private val category: Unit
        private get() {
            var manager: VKCInternetManager? = null
            arrayCategory!!.clear()
            listCategory!!.clear()
            val name = arrayOf("articleno")
            val value = arrayOf(mEditArticle!!.text.toString().trim { it <= ' ' })
            manager = VKCInternetManager(VKCUrlConstants.GET_CATEGORY_URL)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    try {
                        val responseObj = JSONObject(successResponse)
                        val response = responseObj.getJSONObject("response")
                        val status = response.getString("status")
                        if ((status == "Success")) {
                            val arrayCat = response
                                .optJSONArray("categories")
                            if (arrayCat.length() > 0) {
                                for (i in 0 until arrayCat.length()) {
                                    val obj = arrayCat.getJSONObject(i)
                                    val model = QuickOrderCategoryModel()
                                    model.category_id = obj.getString("id")
                                    model.category_name = obj
                                        .getString("category_name")
                                    arrayCategory!!.add(model)
                                }
                                listCategory!!.add("Select Category")
                                for (j in arrayCategory!!.indices) {
                                    listCategory!!.add(
                                        arrayCategory!![j]
                                            .category_name
                                    )
                                }
                                val dataAdapter = ArrayAdapter(
                                    (mActivity)!!,
                                    android.R.layout.simple_spinner_item,
                                    listCategory!!
                                )
                                spinnerCategory!!.adapter = dataAdapter
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

    fun updateCartValue() {
        val mAdapter = SQLiteAdapter(mActivity!!, VKCDbConstants.DBNAME)
        mAdapter.openToRead()
        val sumValue = mAdapter.cartSum
        if (sumValue == null) {
            txtCartValue!!.text = "Cart Value: ₹" + 0
        } else {
            txtCartValue!!.text = "Cart Value: ₹$sumValue"
        }
    }

    fun getCartValue() {
        val mAdapter = SQLiteAdapter(mActivity!!, VKCDbConstants.DBNAME)
        mAdapter.openToRead()
        val sumValue = mAdapter.cartSum
        if (sumValue == null) {
            cartValue = 0
        } else {
            cartValue = sumValue.toInt()
        }
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
        mEditArticle!!.setText("")
    }

    inner class ExceedAlert     // TODO Auto-generated constructor stub
        (var mActivity: Activity) : Dialog(mActivity), View.OnClickListener, VKCDbConstants {
        var d: Dialog? = null
        var mCheckBoxDis: CheckBox? = null
        var mImageView: ImageView? = null

        // public Button yes, no;
        var bUploadImage: Button? = null
        var TEXTTYPE: String? = null
        var mProgressBar: ProgressBar? = null
        var databaseManager: DataBaseManager? = null
        var position = 0
        var cartList: ArrayList<CartModel>? = null
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_exceeds_limit)
            init()
        }

        private fun init() {
            val disp = DisplayManagerScale(mActivity)
            val displayH = disp.deviceHeight
            val displayW = disp.deviceWidth
            val relativeDate = findViewById<View>(R.id.datePickerBase) as RelativeLayout

            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);
            val buttonSet = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    insertToDb()
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

    private fun showExceedDialog() {
        val appDeleteDialog: ExceedAlert = mActivity?.let { ExceedAlert(it) }!!
        appDeleteDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        appDeleteDialog.setCancelable(true)
        appDeleteDialog.show()
    }

    fun insertToDb() {
        for (i in editList!!.indices) {
            val id = editList!![i].id.toString()
            // int colorId = editList.get(i).getId() % 2;
            val qty = editList!![i].text.toString().trim { it <= ' ' }
            var numberofitems: Long? = null
            if (qty.length > 0) {
                try {
                    // the String to int conversion happens here
                    numberofitems = qty.toLong()

                    // print out the value after the conversion
                    println("int i = $i")
                } catch (nfe: NumberFormatException) {
                    println(
                        ("NumberFormatException: "
                                + nfe.message)
                    )
                }
            }
            var idForColor: Int
            var idForSize: Int
            if (id.length > 2) {
                idForColor = id.substring(2).toInt() - 1
                idForSize = id.substring(0, 2).toInt() - 1
            } else {
                idForColor = id.substring(1).toInt() - 1
                // idForColor=colorId-1;
                idForSize = id.substring(0, 1).toInt() - 1
            }
            if (qty.length > 0) {
                cartCount
                val values = arrayOf(
                    arrayOf(VKCDbConstants.PRODUCT_ID, productModel!!.id),
                    arrayOf(VKCDbConstants.PRODUCT_NAME, productModel!!.getmProductName()),
                    arrayOf(VKCDbConstants.PRODUCT_SIZEID, caseArrayList!![idForSize].id),
                    arrayOf(VKCDbConstants.PRODUCT_SIZE, caseArrayList!![idForSize].name),
                    arrayOf(
                        VKCDbConstants.PRODUCT_COLORID,
                        colorArrayList!![idForColor].id
                    ),
                    arrayOf(
                        VKCDbConstants.PRODUCT_COLOR,
                        colorArrayList!![idForColor].colorName
                    ),
                    arrayOf(VKCDbConstants.PRODUCT_QUANTITY, qty),
                    arrayOf(VKCDbConstants.PRODUCT_GRIDVALUE, ""),
                    arrayOf("pid", (tableCount + 1).toString()),
                    arrayOf("sapid", productModel!!.getmSapId()),
                    arrayOf("catid", productModel!!.categoryId)
                )
                //System.out.println(colorArrayList.get(idForColor).getId());
                // Remove edited data and insert into DB
                /*
				 * SQLiteAdapter mAdapter = new SQLiteAdapter( mActivity,
				 * DBNAME); mAdapter.openToRead(); int count =
				 * mAdapter.getCount(AppController.p_id,
				 * caseArrayList.get(idForSize).getName(),
				 * colorArrayList.get(idForColor) .getColorName(), qty);
				 * System.out.print("Count---g" + count); if (count == 0) {
				 * 
				 * mAdapter.deleteUser(AppController.p_id,
				 * caseArrayList.get(idForSize).getName(),
				 * colorArrayList.get(idForColor) .getColorName(), productModel
				 * .getCategoryId()); databaseManager.insertIntoDb(
				 * TABLE_SHOPPINGCART, values); } mAdapter.close();
				 */

                // System.out.println("" + values);
                val mAdapter = SQLiteAdapter(mActivity!!, VKCDbConstants.DBNAME)
                mAdapter.openToRead()
                val count = mAdapter.getCount(
                    AppController.p_id, caseArrayList!!
                        .get(idForSize).name,
                    colorArrayList!![idForColor].colorName, qty
                )
                val countDuplicate = mAdapter.getCountDuplicateEntry(
                    productModel!!.getmProductName(),
                    caseArrayList!![idForSize].name, colorArrayList!!
                        .get(idForColor).colorName
                )
                print("Count---g$count")
                if (countDuplicate == 0) {
                    if (AppController.p_id.length > 0) {
                        /*mAdapter.deleteUser(AppController.p_id, caseArrayList
								.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).name,
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDb(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    } else {
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).name,
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        /*mAdapter.deleteUser(productModel.getmProductName(),
								caseArrayList.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/databaseManager
                            ?.insertIntoDb(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    }
                } else if (countDuplicate > 0) {
                    if (AppController.p_id.length > 0) {
                        /*mAdapter.deleteUser(AppController.p_id, caseArrayList
								.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).name,
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDb(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    } else {
                        /*	mAdapter.deleteUser(productModel.getmProductName(),
								caseArrayList.get(idForSize).getName(),
								colorArrayList.get(idForColor).getColorName(),
								productModel.getCategoryId());*/
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).name,
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDb(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    }
                    /*
					 * String oldqty = mAdapter.getQty(productModel
					 * .getmProductName(), colorArrayList.get(idForColor)
					 * .getColorName(), caseArrayList .get(idForSize).getName(),
					 * productModel.getCategoryId()); int total =
					 * Integer.parseInt(oldqty) + Integer.parseInt(qty);
					 */
                    // System.out.println("total" + total);
                    /*
					 * mAdapter.updateData(productModel .getmProductName(),
					 * colorArrayList.get(idForColor) .getColorName(),
					 * caseArrayList .get(idForSize).getName(),
					 * String.valueOf(Integer.parseInt(oldqty) +
					 * Integer.parseInt(qty)));
					 */
                }
                mAdapter.close()
                listTemp!!.add(qty)
            }
            /*		mActivity.runOnUiThread(new Runnable() {
				public void run() {
					// Here your code that runs on UI Threads

					try {
						if (listTemp.size() > 0) {
							VKCUtils.showtoast(mActivity, 5);
							
							
						} else {
							VKCUtils.showtoast(mActivity, 40);
						}
					} catch (Exception e) {
						System.out.println(e);
					}
					for (int i = 0; i < editList.size(); i++) {
						editList.get(i).setText("");
					}

					
				}
			});*/
        }

        /*
		 * if (listTemp.size() > 0) { VKCUtils.showtoast(mActivity, 5);
		 * mEditArticle.setText(""); } else { VKCUtils.showtoast(mActivity, 40);
		 * }
		 */

        /*
		 * for (int i = 0; i < editList.size(); i++) {
		 * editList.get(i).setText(""); } for (int i = 0; i <
		 * editListCustom.size(); i++) { editListCustom.get(i).setText(""); }
		 */isInserted = true
    }

    private fun addCartApi(
        product_id: String, qty: String, category_id: String,
        size: String, color: String, role: String, values: Array<Array<String>>
    ) {
        var manager: VKCInternetManager? = null
        var cust_id: String? = ""
        if ((getUserType((activity)!!) == "4")) {
            cust_id = getSelectedUserId((mActivity)!!)
        } else {
            cust_id = getCustomerId((mActivity)!!)
        }
        val name = arrayOf(
            "product_id", "quantity", "category_id", "size",
            "color", "role", "cust_id"
        )
        val value = arrayOf(
            product_id, qty, category_id, size, color, role,
            cust_id
        )
        manager = VKCInternetManager(VKCUrlConstants.ADD_TO_CART_API)
        manager.getResponsePOST(mActivity, name, value,
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
                            count++
                            txtCartValue!!.text = "Cart Value: " + response.optString("cart_value")
                            if (listTemp!!.size == count) {
                                if (listTemp!!.size > 0) {
                                    for (i in editList!!.indices) {
                                        editList!![i].setText("")
                                    }
                                    showtoast(mActivity, 5)
                                    for (i in editList!!.indices) {
                                        editList!![i].setText("")
                                    }
                                } else {
                                    showtoast(mActivity, 40)
                                }
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
    }// TODO Auto-generated method stub// TODO Auto-generated method stub
    //AppController.cartArrayList.clear();
    // System.out.println("Values" + values);

    // pDialog.setMessage("Loading...");
    // System.out.println("18022015:createJson:" + createJson());
    val pendingQuantity: Unit
        get() {
            //AppController.cartArrayList.clear();
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
                txtCartValue!!.text = ("Cart Value: ₹"
                        + response.optString("cart_value"))
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    companion object {
        var creditPrice = 0
    }
}