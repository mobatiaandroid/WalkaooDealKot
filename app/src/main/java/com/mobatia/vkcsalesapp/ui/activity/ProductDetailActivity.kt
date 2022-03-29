/**
 *
 */
package com.mobatia.vkcsalesapp.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter
import com.mobatia.vkcsalesapp.adapter.ColorGridAdapter
import com.mobatia.vkcsalesapp.adapter.ListImageAdapter
import com.mobatia.vkcsalesapp.adapter.SizeGridAdapter
import com.mobatia.vkcsalesapp.adapter.ViewpagerAdapter
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import com.mobatia.vkcsalesapp.customview.HorizontalListView
import com.mobatia.vkcsalesapp.manager.*
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSelectedUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
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
import com.mobatia.vkcsalesapp.model.*
import com.mobatia.vkcsalesapp.ui.activity.CartActivity
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ProductDetailActivity() : AppCompatActivity(), VKCUrlConstants, View.OnClickListener
    , VKCDbConstants, AdapterView.OnItemClickListener {
    private var mHorizontalListView: HorizontalListView? = null
    private var listViewColor: HorizontalListView? = null
    private var listViewSize: HorizontalListView? = null
    private var mListAdapter: ListImageAdapter? = null
    private var mViewPagerAdapter: ViewpagerAdapter? = null
    private var colorGridAdapter: ColorGridAdapter? = null
    private var sizeGridAdapter: SizeGridAdapter? = null
    private var mRelImage: RelativeLayout? = null
    private var mRelativText: RelativeLayout? = null
    var relativSecondSec: RelativeLayout? = null
    private var mRelBottom: LinearLayout? = null
    private var mFirstView: LinearLayout? = null
    private var mImgArrowRight: ImageView? = null
    private var mImgArrowLeft: ImageView? = null
    private var mActivity: Activity? = null
    var width = 0
    var creditPrice = 0
    var height = 0
    var x1 = 0f
    var x2 = 0f
    var y1 = 0f
    var y2 = 0f
    var isClickedAnimation = false
    var txtlikeCount: TextView? = null
    var txtNameText: TextView? = null
    var txtViewPrice: TextView? = null
    var txtCartCount: TextView? = null
    var txtDescription: TextView? = null
    var txtCatName: TextView? = null
    var relShare: RelativeLayout? = null
    var relCart: RelativeLayout? = null
    var isClicked = false
    var likeCount = 0
    private var mImagePager: ViewPager? = null
    var productModels: ArrayList<ProductModel>? = null
    var priceTotal = 0f
    var displayManagerScale: DisplayManagerScale? = null
    var productModel: ProductModel = ProductModel()
    var buttonLike: Button? = null
    private var databaseManager: DataBaseManager? = null
    private var edtQuantity: EditText? = null
    lateinit var imageUrls: ArrayList<ProductImages>
    lateinit var colorArrayList: ArrayList<ColorModel>
    lateinit var sizeArrayList: ArrayList<SizeModel>
    lateinit var caseArrayList: ArrayList<CaseModel>
    lateinit var newArrivalArrayList: ArrayList<ProductImages>
    lateinit var mPendingList: ArrayList<PendingQuantityModel>
    private var edtPendQuantity: TextView? = null
    var tableLayout: TableLayout? = null
    var editList: ArrayList<EditText>? = null
    var listTemp: ArrayList<String>? = null
    var tableCount = 0
    var tempSize = 0
    private var cartPrice = 0f
    var cartValue = 0
    var isShowMessage = false
    var isInserted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail_new)
        val abar = supportActionBar
        editList = ArrayList()
        tableCount = 0
        val viewActionBar = layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Product Details"
        abar!!.setCustomView(viewActionBar, params)
        AppController.p_id = ""
        abar.setDisplayShowCustomEnabled(true)
        isClickedAnimation = false
        isShowMessage = false
        mActivity = this
        val productid = AppController.product_id
        // System.out.println("Pid :" + AppController.p_id);
        AppController.isClickedCartAdapter = false
        val database = DatabaseHelper(this@ProductDetailActivity, "VKC")
        database.openDataBase()
        if (productid == "") {
            productModel = (intent.extras
                ?.getSerializable("MODEL") as ProductModel?)!!
            initialiseUI()
            AppController.product_id = productModel!!.getmProductName()
            AppController.category_id = productModel!!.categoryId
            cartDataForTable
            createTable()
            LikeCountApi()
            getProducts(AppController.product_id)
        } else {
            initialiseUI()
            cartDataForTable
            LikeCountApi()
            getProducts(productid)
        }
    }

    private fun productDetailStatus() {
        val manager = VKCInternetManager(
            VKCUrlConstants.GET_SALES_ORDER_STATUS
        )
        val name = arrayOf("user_id", "product_id")
        val value = arrayOf(
            getUserId(
                mActivity!!
            ),
            productModel!!.id
        )
        manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                try {
                    val jsonObj = JSONObject(successResponse)
                    val jsonArray = jsonObj
                        .getJSONArray("productSalesOrderStatus")
                    for (i in 0 until jsonArray.length()) {
                        val jObj = jsonArray.getJSONObject(i)
                        println("pending12 $jObj")
                        jObj.opt("id")
                        jObj.opt("CusId")
                        jObj.opt("productSapId")
                        jObj.opt("CusName")
                        jObj.opt("MaterialNo")
                        jObj.opt("OrderDate")
                        jObj.opt("OrderQty")
                        jObj.opt("PendingQty")
                        edtPendQuantity!!.text = jObj.opt("PendingQty")
                            .toString()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun responseFailure(failureResponse: String?) {
                // TODO Auto-generated method stub
            }
        })
    }

    private fun increasePopularCount() {
        val name = arrayOf("product_id")
        val values = arrayOf(productModel!!.id)
        val manager = VKCInternetManager(
            VKCUrlConstants.POPULARITY_COUNT_URL
        )
        manager.getResponsePOST(this@ProductDetailActivity, name, values,
            object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    productDetailStatus()
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                }
            })
    }

    /*
     * Method Name:setLayoutDimension() Parameter:nill Description:Set layout
     * according to device height and width
     */
    private fun setLayoutDimension() {
        displayManagerScale = DisplayManagerScale(mActivity!!)
        width = displayManagerScale!!.deviceWidth
        height = displayManagerScale!!.deviceHeight
        mRelImage!!.layoutParams.height = (height * 0.40).toInt()
        relativSecondSec!!.layoutParams.height = (height * .40).toInt()
        mRelativText!!.layoutParams.height = (height * .12).toInt()
        mRelBottom!!.layoutParams.height = (height * .08).toInt()
    }

    /*
     * Method Name:initialiseUI() Parameter:nill Description:Initialise UI
     * elements
     */
    private fun initialiseUI() {
        databaseManager = DataBaseManager(mActivity!!)
        relativSecondSec = findViewById<View>(R.id.relativSecondSec) as RelativeLayout
        mHorizontalListView = findViewById<View>(R.id.listView) as HorizontalListView
        listViewColor = findViewById<View>(R.id.listViewColor) as HorizontalListView
        listViewSize = findViewById<View>(R.id.listViewSize) as HorizontalListView
        txtlikeCount = findViewById<View>(R.id.txtLikeCount) as TextView
        tableLayout = findViewById<View>(R.id.matrixLayout) as TableLayout
        mRelImage = findViewById<View>(R.id.relImage) as RelativeLayout
        mImgArrowRight = findViewById<View>(R.id.imgArrowRight) as ImageView
        mImgArrowLeft = findViewById<View>(R.id.imgArrowLeft) as ImageView
        edtQuantity = findViewById<View>(R.id.edtViewQty) as EditText
        edtPendQuantity = findViewById<View>(R.id.edtViewQtyOneData) as TextView
        buttonLike = findViewById<View>(R.id.btnLike) as Button
        mRelativText = findViewById<View>(R.id.relativText) as RelativeLayout
        mRelBottom = findViewById<View>(R.id.relBottomLayout) as LinearLayout
        mImagePager = findViewById<View>(R.id.imagePager) as ViewPager
        txtNameText = findViewById<View>(R.id.txtNameText) as TextView
        txtViewPrice = findViewById<View>(R.id.txtViewPrice) as TextView
        txtDescription = findViewById<View>(R.id.txtCatText) as TextView
        txtCatName = findViewById<View>(R.id.txtCatName) as TextView

        // txtNameText = (TextView) findViewById(R.id.txtNameText);
        mFirstView = findViewById<View>(R.id.firstView) as LinearLayout
        // txtNameText.setText("VKC " /* + productModel.getId() */+ " "
        // + productModel.getmProductName());
        productModels = ArrayList()
        relShare = findViewById<View>(R.id.relShare) as RelativeLayout
        relCart = findViewById<View>(R.id.relCart) as RelativeLayout
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        txtCartCount = findViewById<View>(R.id.txtCartSize) as TextView
        setListeners()
        setCartQuantity()
        setLayoutDimension()
        setActionBar()
        colorArrayList = ArrayList()
        sizeArrayList = ArrayList()
        caseArrayList = ArrayList()
        newArrivalArrayList = ArrayList()
        val relativeParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        relativeParams.addRule(RelativeLayout.ABOVE, R.id.relBottomLayout)
        buttonLike!!.setOnClickListener { v ->
            // TODO Auto-generated method stub
            if (v.id == R.id.btnLike) {
                isClicked = !isClicked // toggle the boolean flag
                v.setBackgroundResource(if (isClicked) R.drawable.likepress else R.drawable.like)
                LikeApi(v)
            }
        }
        mRelativText!!.setOnTouchListener { v, touchevent -> // TODO Auto-generated method stub
            when (touchevent.action) {
                MotionEvent.ACTION_DOWN -> {
                    x1 = touchevent.x
                    y1 = touchevent.y
                }
                MotionEvent.ACTION_UP -> {
                    x2 = touchevent.x
                    y2 = touchevent.y

                    // if left to right sweep event on screen
                    if (x1 < x2) {
                        // Toast.makeText(this, "Left to Right Swap Performed",
                        // Toast.LENGTH_LONG).show();
                    }

                    // if right to left sweep event on screen
                    if (x1 > x2) {
                        // Toast.makeText(this, "Right to Left Swap Performed",
                        // Toast.LENGTH_LONG).show();
                    }

                    // if UP to Down sweep event on screen
                    if (y1 < y2) {
                        val slide_down = AnimationUtils
                            .loadAnimation(
                                applicationContext,
                                R.anim.slide_down
                            )
                        mFirstView!!.startAnimation(slide_down)
                        relativSecondSec!!.startAnimation(slide_down)
                        slide_down
                            .setAnimationListener(object : Animation.AnimationListener {
                                override fun onAnimationStart(
                                    animation: Animation
                                ) {
                                    // TODO Auto-generated method stub
                                }

                                override fun onAnimationRepeat(
                                    animation: Animation
                                ) {
                                    // TODO Auto-generated method stub
                                }

                                override fun onAnimationEnd(
                                    animation: Animation
                                ) {
                                    // TODO Auto-generated method stub
                                    isClickedAnimation = false
                                    mFirstView!!.visibility = View.VISIBLE
                                    relativSecondSec!!
                                        .setVisibility(View.VISIBLE)
                                }
                            })

                        // Toast.makeText(this, "UP to Down Swap Performed",
                        // Toast.LENGTH_LONG).show();
                    }

                    // if Down to UP sweep event on screen
                    if (y1 > y2) {
                        // Toast.makeText(this, "Down to UP Swap Performed",
                        // Toast.LENGTH_LONG).show();
                        val slide_up = AnimationUtils
                            .loadAnimation(
                                applicationContext,
                                R.anim.slide_up
                            )
                        /*
                                  * if(!isClickedAnimation) {
                                  */mFirstView!!.startAnimation(slide_up)
                        relativSecondSec!!.startAnimation(slide_up)
                        // }
                        mFirstView!!.visibility = View.GONE
                        relativSecondSec!!.visibility = View.GONE
                        // isClickedAnimation=true;
                        slide_up.setAnimationListener(object : Animation.AnimationListener {
                            override fun onAnimationStart(animation: Animation) {
                                // TODO Auto-generated method stub
                            }

                            override fun onAnimationRepeat(animation: Animation) {
                                // TODO Auto-generated method stub
                            }

                            override fun onAnimationEnd(animation: Animation) {
                                // TODO Auto-generated method stub
                            }
                        })
                    }
                }
            }
            false
        }
    }

    private fun setListeners() {
        relShare!!.setOnClickListener(this)
        relCart!!.setOnClickListener(this)
        mImgArrowRight!!.setOnClickListener(this)
        mImgArrowLeft!!.setOnClickListener(this)
        listViewSize!!.onItemClickListener = this
        txtCartCount!!.setOnClickListener(this)
        mRelativText!!.setOnClickListener(this)
    }

    private fun setHorizontalListAction(listView: HorizontalListView?) {
        listView!!.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                // mImagePager.setCurrentItem(position);
                AppController.product_id = newArrivalArrayList!!.get(position)
                    ?.getProductName()
                AppController.category_id = newArrivalArrayList!!.get(position)
                    .getCatId()
                println(
                    ("Selected Id :"
                            + newArrivalArrayList!!.get(position).getProductName())
                )
                println(
                    ("Selected Id Cat :"
                            + newArrivalArrayList!!.get(position).getCatId())
                )
                cartDataForTable
                getProducts(AppController.product_id)
            }
        }
    }

    private fun setColorGridClickListener(listView: HorizontalListView?) {
        listView!!.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View,
                position: Int, id: Long
            ) {
                // TODO Auto-generated method stub
                var imageUrls: ArrayList<ProductImages> = ArrayList()
                val imageUrlsTemp: ArrayList<ProductImages> = ArrayList()
                var colorArrayList: ArrayList<ColorModel> = ArrayList()
                imageUrls = productModel!!.productImages
                colorArrayList = productModel!!.productColor
                for (i in imageUrls.indices) {
                    if ((imageUrls
                            .get(i)
                            .getColorModel()
                            .getColorcode()
                                == colorArrayList.get(position).colorcode)
                    ) {
                        imageUrlsTemp.add(imageUrls.get(i))
                    }
                }
                mListAdapter = ListImageAdapter((mActivity)!!, imageUrlsTemp)
                mHorizontalListView!!.adapter = mListAdapter
                mViewPagerAdapter = ViewpagerAdapter(
                    (mActivity)!!,
                    imageUrlsTemp
                )
                mImagePager!!.adapter = mViewPagerAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // TODO Auto-generated method stub
            }
        }

        // listViewColor.set
        listViewColor!!.setSelection(0)
    }

    private fun shareIntent(link: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(
            Intent.createChooser(
                emailIntent, applicationContext
                    .resources.getString(R.string.app_name)
            )
        )
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        val actionBar = supportActionBar
        actionBar!!.setSubtitle("")
        actionBar.setTitle("")
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        if (v === relShare) {
            shareIntent("http://vkcgroup.com/")
        } else if (v === relCart) {
            isInserted = false
            if (getUserType(mActivity!!) == "4") {
                if ((getCustomerCategory(mActivity!!)
                            == "") && (getSelectedUserId(mActivity!!)
                            == "")
                ) {
                    VKCUtils.showtoast(mActivity, 41)
                } else {
                    val addToCart = AddToCart()
                    addToCart.execute()
                }
            } else {
                println("Product id---" + AppController.p_id)
                val addToCart = AddToCart()
                addToCart.execute()
            }
        } else if (v === mImgArrowRight) {
            mImagePager!!.currentItem = mImagePager!!.currentItem + 1
        } else if (v === mImgArrowLeft) {
            mImagePager!!.currentItem = mImagePager!!.currentItem - 1
        } else if (v === txtCartCount) {
            startActivity(
                Intent(
                    this@ProductDetailActivity,
                    CartActivity::class.java
                )
            )
            finish()
        } else if (v === mRelativText) {
        }
    }

    private inner class AddToCart() :
        AsyncTask<Void?, Void?, Void?>() {
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

        override fun doInBackground(vararg p0: Void?): Void? {
            // TODO Auto-generated method stub
            cartData
            getCartValue()
            if (productModel != null) {
                listTemp = ArrayList()
                if (AppController.p_id != "") {
                }
                if (editList!!.size > 0) {
                    // Sum of price value
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
                                "NumberFormatException: "
                                        + nfe.message
                            )
                        }
                        priceTotal = priceTotal + (itemCount * cartPrice)
                    }
                    if ((cartValue + priceTotal) > creditPrice) {
                        isShowMessage = true
                    } else {
                        isShowMessage = false
                        insertToDb()
                    }
                } else {
                    if (AppController.cartArrayList.size > 0) {
                        var count = 0
                        for (i in AppController.cartArrayList.indices) {
                            val mAdapter = SQLiteAdapter(
                                (mActivity)!!, VKCDbConstants.DBNAME
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
                        println(
                            ("Duplicate Count"
                                    + count.toString())
                        )
                    }
                }
            }
            return null
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        override fun onPostExecute(result: Void?) {
            // TODO Auto-generated method stub
            super.onPostExecute(result)
            pDialog.dismiss()
            if (isShowMessage) {
                // VKCUtils.showtoast(mActivity, 46);
                showExceedDialog()
            }
            setCartQuantity()
            AppController.p_id = ""
        }
    }

    private fun LikeApi(v: View) {
        val manager = VKCInternetManager(
            VKCUrlConstants.LIKE_PRODUCT_URL
        )
        val name = arrayOf("product_id", "user_id")
        val value = arrayOf(
            productModel!!.id,
            getUserId(this)
        )
        manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                try {
                    val jobj = JSONObject(successResponse)
                    val response = jobj.optJSONObject("response")
                    val status = response.optString("status")
                    if ((status == "Success")) {
                        v.setBackgroundResource(R.drawable.likepress)
                        likeCount = likeCount + 1
                        txtlikeCount!!.text = likeCount.toString()
                    } else {
                        // v.setBackgroundResource(R.drawable.like);
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

    private fun LikeCountApi() {
        val manager = VKCInternetManager(
            VKCUrlConstants.LIKE_COUNT_URL
        )
        val name = arrayOf("product_id", " user_id")
        val value = arrayOf(
            productModel!!.id,
            getUserId(this)
        )
        manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                try {
                    val jobj = JSONObject(successResponse)
                    val response = jobj.optJSONObject("response")
                    val status = response.optString("status")
                    val likCount = Integer.valueOf(response.optString("count"))
                    val isLiked = response.optString("isliked")
                    if ((status == "Success")) {
                        likeCount = likCount
                        txtlikeCount!!.text = likeCount.toString()
                        if ((isLiked == "1")) {
                            buttonLike
                                ?.setBackgroundResource(R.drawable.likepress)
                        } else {
                            buttonLike!!.setBackgroundResource(R.drawable.like)
                        }
                        increasePopularCount()
                    } else {
                    }
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }

            override fun responseFailure(failureResponse: String?) {
                // TODO Auto-generated method stub
                // mIsError = true;
            }
        })
    }

    override fun onItemClick(
        parent: AdapterView<*>?, view: View, position: Int,
        id: Long
    ) {
        // TODO Auto-generated method stub
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
            VKCDbConstants.PRODUCT_GRIDVALUE,
            "pid"
        )
        val cursor = databaseManager!!.fetchFromDB(
            cols, VKCDbConstants.TABLE_SHOPPINGCART
        )

        // List<CartModel> cartList = new ArrayList<>();
        //cartList = RealmController.with(ProductDetailActivity.this).getBooks();
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
        /*if (cartList.size() > 0) {
            for (int i = 0; i < cartList.size(); i++) {
                String count = cartList.get(i).getProdQuantity();
                cartount = Integer.parseInt(count);
                mCount = mCount + cartount;
            }
        }*/

        //System.out.println("CartList Size :" + cartList.size());
        txtCartCount!!.text = mCount.toString()
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()

        // setCartQuantity();
    }

    // AppController.cartArrayList.add(setCartModel(cursor));
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
                cols, VKCDbConstants.TABLE_SHOPPINGCART

            )
            if (cursor.count > 0) {
                while (!cursor.isAfterLast) {
                    // AppController.cartArrayList.add(setCartModel(cursor));
                    tableCount = tableCount + cursor.count
                    cursor.moveToNext()
                }
            }
            return cursor.count
        }

    private fun setCartModel(cursor: Cursor): CartModel {
        val cartModel = CartModel()
        cartModel.prodId = cursor.getString(0)
        cartModel.prodName = cursor.getString(1)
        cartModel.prodSizeId = cursor.getString(2)
        cartModel.prodSize = cursor.getString(3)
        cartModel.prodColorId = cursor.getString(4)
        cartModel.prodColor = cursor.getString(5)
        cartModel.prodQuantity = cursor.getString(6)
        cartModel.prodGridValue = cursor.getString(7)
        return cartModel
    }

    private fun parseResponse(response: String?) {
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
                    val CatId = jsonObjectZero.optString("categoryid")
                    productModel.setmSapId(
                        jsonObjectZero
                            .optString("productSapId")
                    )
                    val SapId = jsonObjectZero.optString("productSapId")
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
                    println(
                        ("size product image "
                                + productImageArray.length())
                    )
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
                    val productImages = ArrayList<ProductImages>()
                    for (i in 0 until productImageArray.length()) {
                        val images = ProductImages()
                        val jsonObject = productImageArray
                            .getJSONObject(i)
                        images.id = jsonObject.optString("image_id")
                        images.setImageName(
                            (VKCUrlConstants.BASE_URL
                                    + jsonObject.optString(JSON_COLOR_IMAGE))
                        )
                        images.setProductName(
                            jsonObject
                                .optString("product_name")
                        )
                        val colorModel = ColorModel()
                        colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                        colorModel.colorcode = jsonObject
                            .optString(JSON_SETTINGS_COLORCODE)
                        images.setColorModel(colorModel)
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
                        images.setImageName(
                            (VKCUrlConstants.BASE_URL
                                    + jsonObject.optString(JSON_COLOR_IMAGE))
                        )
                        images.setProductName(
                            jsonObject
                                .optString("product_name")
                        )
                        val colorModel = ColorModel()
                        colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                        colorModel.colorcode = jsonObject
                            .optString(JSON_SETTINGS_COLORCODE)
                        images.setColorModel(colorModel)
                        images.setCatId(jsonObject.optString("categoryid"))
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
                        sizeModel.setName(
                            jsonObject
                                .optString(JSON_SETTINGS_SIZENAME)
                        )
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
                        typeModel.setName(
                            jsonObject
                                .optString(JSON_SETTINGS_BRANDNAME)
                        )
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
                        caseModel.setName(
                            jsonObject
                                .optString(JSON_SETTINGS_CASENAME)
                        )
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
        } else {
            VKCUtils.showtoast(mActivity, 42)
        }
    }

    private fun getProducts(product_id: String) {
        productModels!!.clear()
        colorArrayList!!.clear()
        caseArrayList!!.clear()
        newArrivalArrayList!!.clear()
        val name = arrayOf("productId", "categoryId")
        val values = arrayOf(product_id, AppController.category_id)
        val manager = VKCInternetManager(
            VKCUrlConstants.URL_GET_PRODUCT_DETAIL
        )
        manager.getResponsePOST(this, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) { //
                // TODO Auto-generated method stub
                parseResponse(successResponse)
                if (successResponse != "") {
                    txtNameText!!.text = (""
                            + productModel!!.productType.get(0).getName())
                    txtViewPrice!!.text = ("Rs."
                            + productModel!!.getmProductPrize())
                    txtDescription!!.text = productModel!!.productdescription
                    txtCatName!!.text = productModel!!.categoryName
                    imageUrls = productModel!!.productImages
                    colorArrayList = productModel!!.productColor
                    sizeArrayList = productModel!!.getmProductSize()
                    caseArrayList = productModel!!.getmProductCases()
                    newArrivalArrayList = productModel!!.getmNewArrivals()
                    mImagePager = findViewById<View>(R.id.imagePager) as ViewPager
                    mViewPagerAdapter = ViewpagerAdapter(
                        (mActivity)!!,
                        imageUrls!!
                    )
                    mImagePager!!.adapter = mViewPagerAdapter
                    mListAdapter = ListImageAdapter(
                        (mActivity)!!,
                        newArrivalArrayList!!
                    )
                    mHorizontalListView!!.adapter = mListAdapter
                    colorGridAdapter = ColorGridAdapter(
                        (mActivity)!!,
                        colorArrayList, 1
                    )
                    sizeGridAdapter = SizeGridAdapter(
                        (mActivity)!!,
                        caseArrayList
                    )
                    listViewColor!!.adapter = colorGridAdapter
                    listViewSize!!.adapter = sizeGridAdapter
                    setColorGridClickListener(listViewColor)
                    setHorizontalListAction(mHorizontalListView)
                    getCartPrice()
                    if (AppController.cartArrayListSelected.size > 0) {
                        createTableWithData()
                    } else {
                        createTable()
                    }
                    AppController.product_id = ""
                } else {
                    VKCUtils.showtoast(mActivity, 42)
                }
            }

            fun createTableWithData() {
                // TODO Auto-generated method stub
                tableLayout!!.removeAllViews()
                if (AppController.cartArrayListSelected.size < caseArrayList!!
                        .size
                ) {
                    val diff = (caseArrayList!!.size
                            - AppController.cartArrayListSelected.size)
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
                tempSize = AppController.cartArrayListSelected.size
                var caseName: String? = ""
                var colorName: String? = ""
                val column_width = 140
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
                    if (i == 0) {
                        caseName = ""
                    } else {
                        caseName = caseArrayList!![i - 1].getName()
                    }
                    val tableRow = TableRow(this@ProductDetailActivity)
                    tableRow.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    tableRow.setBackgroundColor(
                        resources.getColor(
                            R.color.vkcred
                        )
                    )
                    tableRow.setPadding(0, 0, 0, 2)
                    for (j in 0 until colorArrayList!!.size + 1) {
                        if (j == 0) {
                            colorName = ""
                        } else {
                            colorName = colorArrayList!![j - 1]
                                .colorName
                        }
                        val textView = TextView(
                            this@ProductDetailActivity
                        )
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
                        if (i == 0 && j == 0) {
                            textView.text = "Test"
                            textView.setBackgroundColor(Color.WHITE)
                            textView.visibility = View.INVISIBLE
                        } else if (i == 0) {
                            textView.text = colorArrayList!!.get(j - 1)
                                .colorName
                        } else if (j == 0) {
                            textView.text = caseArrayList!!.get(i - 1).getName()
                        } else {
                            textView.visibility = View.GONE
                            val edit = EditText(
                                this@ProductDetailActivity
                            )
                            edit.inputType = InputType.TYPE_CLASS_NUMBER
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
                for (i in AppController.cartArrayListSelected.indices) {
                    colorName = AppController.cartArrayListSelected[i]
                        .prodColor
                    caseName = AppController.cartArrayListSelected[i]
                        .prodSize
                    Quantity = AppController.cartArrayListSelected[i]
                        .prodQuantity
                    AppController.size = caseName
                    AppController.color = colorName
                    for (j in caseArrayList!!.indices) {
                        listCaseName = caseArrayList!![j].getName()
                        if ((listCaseName == caseName)) {
                            selectedRow = j
                        }
                    }
                    for (k in colorArrayList!!.indices) {
                        listColorName = colorArrayList!![k].colorName
                        if ((listColorName == colorName)) {
                            selectedColumn = k
                        }
                    }
                    if (((caseArrayList!![selectedRow].getName()
                                == caseName) && (colorArrayList!![selectedColumn]
                            .colorName == colorName))
                    ) {
                        setPosition = (selectedRow * colorArrayList!!.size
                                + selectedColumn)
                        editList!![setPosition].setText(Quantity)
                    }
                }
            }

            override fun responseFailure(failureResponse: String?) { //
                // TODO Auto-generated method stub
            }
        })
    }

    fun createTable() {
        tableLayout!!.removeAllViews()
        val column_width = 140
        val tableLayoutParams = TableLayout.LayoutParams()
        tableLayout!!.setBackgroundColor(resources.getColor(R.color.vkcred))

        // 2) create tableRow params
        val tableRowParams = TableRow.LayoutParams()
        tableRowParams.setMargins(1, 1, 1, 1)
        tableRowParams.weight = 1f
        editList!!.clear()
        if (caseArrayList!!.size > 0 && colorArrayList!!.size > 0) {
            for (i in 0 until caseArrayList!!.size + 1) {
                // 3) create tableRow
                val tableRow = TableRow(this)
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
                    val textView = TextView(this)
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
                    if (i == 0 && j == 0) {
                        textView.text = "Test"
                        textView.setBackgroundColor(Color.WHITE)
                        textView.visibility = View.INVISIBLE
                    } else if (i == 0) {
                        textView.text = colorArrayList!!.get(j - 1)
                            .colorName
                    } else if (j == 0) {
                        textView.text = caseArrayList!!.get(i - 1).getName()
                    } else {
                        textView.visibility = View.GONE
                        val edit = EditText(this@ProductDetailActivity)
                        edit.inputType = InputType.TYPE_CLASS_NUMBER
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
        } else {
            // Toast.makeText(mActivity, "", duration)
        }
        if (mPendingList != null) {
            if (mPendingList.size > 0) {
                setTableDataFromApi()
            }
        }
    }

    override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
        // setCartQuantity();
    }

    fun setTableDataFromApi() {
        var caseName = ""
        var listCaseName: String
        var colorName = ""
        var listColorName: String
        var Quantity: String? = ""
        var selectedRow = 0
        var selectedColumn = 0
        var setPosition: Int
        for (i in mPendingList!!.indices) {
            colorName = mPendingList[i].color
            caseName = mPendingList[i].size
            Quantity = mPendingList[i].pendingQty
            for (j in caseArrayList!!.indices) {
                listCaseName = caseArrayList!![j].getName()
                if ((listCaseName == caseName)) {
                    selectedRow = j
                }
            }
            for (k in colorArrayList!!.indices) {
                listColorName = colorArrayList!![k].colorName
                if ((listColorName == colorName)) {
                    selectedColumn = k
                }
            }
            if (((caseArrayList!![selectedRow].getName() == caseName) && (colorArrayList!![selectedColumn].colorName
                        == colorName))
            ) {
                setPosition = (selectedRow * colorArrayList!!.size
                        + selectedColumn)
                editList!![setPosition].setText(Quantity)
            }
        }
    }

    private val cartDataForTable: Unit
        private get() {
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
                cols, VKCDbConstants.TABLE_SHOPPINGCART
            )
            if (cursor.count > 0) {
                while (!cursor.isAfterLast) {
                    val pid = cursor.getString(1)
                    if ((pid == AppController.product_id)) {
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
            }
        }
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
                cols, VKCDbConstants.TABLE_SHOPPINGCART
            )
            if (cursor.count > 0) {
                while (!cursor.isAfterLast) {
                    AppController.cartArrayList.add(setCartModel(cursor))
                    cursor.moveToNext()
                }
            } else {
            }
        }

    private fun getCartPrice() {
        var manager: VKCInternetManager? = null
        val name = arrayOf("productid", "userid", "categoryid")
        val value = arrayOf(
            AppController.product_id,
            getUserId((mActivity)!!),
            AppController.category_id
        ) // AppPrefenceManager.getUserId(mActivity)
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
                        }
                        creditValue
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

    fun getCartValue() {
        val mAdapter = SQLiteAdapter((mActivity)!!, VKCDbConstants.DBNAME)
        mAdapter.openToRead()
        val sumValue = mAdapter.cartSum
        if (sumValue == null) {
            cartValue = 0
        } else {
            cartValue = sumValue.toInt()
        }
    }// TODO

    // Auto-generated method stub
    // TODO Auto-generated catch block
    private val creditValue: Unit
        private get() {
            var manager: VKCInternetManager? = null
            var cust_id: String? = ""
            if ((getUserType((mActivity)!!) == "4")) {
                cust_id = getSelectedUserId((mActivity)!!)
            } else {
                cust_id = getCustomerId((mActivity)!!)
            }
            val name = arrayOf("dealerid")
            val value = arrayOf(cust_id)
            manager = VKCInternetManager(VKCUrlConstants.GET_QUICK_ORDER_CREDIT_URL)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    try {
                        val responseObj = JSONObject(successResponse)
                        val response = responseObj.getJSONObject("response")
                        val status = response.getString("status")
                        if ((status == "Success")) {
                            creditPrice = response
                                .getString("creditvalue").toInt()
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

    fun insertToDb() {
        for (i in editList!!.indices) {
            val id = editList!![i].id.toString()
            val qty = editList!![i].text.toString().trim { it <= ' ' }
            println("Edit Qty-$qty")
            println("id$id")
            var numberofitems: Long? = null
            if (qty.length > 0) {
                try {
                    // the String to int conversion happens here
                    numberofitems = qty.toLong()

                    // print out the value after the conversion
                } catch (nfe: NumberFormatException) {
                }
            }
            var idForColor: Int
            var idForSize: Int
            if (id.length > 2) {
                idForColor = id.substring(2).toInt() - 1
                idForSize = id.substring(0, 2).toInt() - 1
            } else {
                idForColor = id.substring(1).toInt() - 1
                idForSize = id.substring(0, 1).toInt() - 1
            }
            if (qty.length > 0) {
                cartCount
                val values = arrayOf(
                    arrayOf(VKCDbConstants.PRODUCT_ID, productModel!!.id),
                    arrayOf(
                        VKCDbConstants.PRODUCT_NAME, productModel!!.getmProductName()
                    ),
                    arrayOf(
                        VKCDbConstants.PRODUCT_SIZEID, caseArrayList!![idForSize].id
                    ),
                    arrayOf(
                        VKCDbConstants.PRODUCT_SIZE, caseArrayList!![idForSize].getName()
                    ),
                    arrayOf(
                        VKCDbConstants.PRODUCT_COLORID,
                        colorArrayList!![idForColor].id
                    ),
                    arrayOf(
                        VKCDbConstants.PRODUCT_COLOR,
                        colorArrayList!![idForColor].colorName
                    ),
                    arrayOf<String?>(VKCDbConstants.PRODUCT_QUANTITY, qty),
                    arrayOf<String?>(
                        VKCDbConstants.PRODUCT_GRIDVALUE, ""
                    ),
                    arrayOf("sapid", productModel!!.getmSapId()),
                    arrayOf("catid", productModel!!.categoryId)
                )
                val mAdapter = SQLiteAdapter((mActivity)!!, VKCDbConstants.DBNAME)
                mAdapter.openToRead()
                val count = mAdapter.getCount(
                    AppController.p_id, caseArrayList!!
                        .get(idForSize).getName(),
                    colorArrayList!![idForColor].colorName, qty
                )
                val countDuplicate = mAdapter.getCountDuplicateEntry(
                    productModel!!.getmProductName(),
                    caseArrayList!![idForSize].getName(), colorArrayList!!
                        .get(idForColor).colorName
                )
                if (countDuplicate == 0) {
                    if (AppController.p_id.length > 0) {
                        mAdapter.deleteUser(
                            AppController.p_id, caseArrayList!!
                                .get(idForSize).getName(),
                            colorArrayList!![idForColor].colorName,
                            productModel!!.categoryId
                        )
                        mAdapter.close()
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).getName(),
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDbNew(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    } else {
                        mAdapter.deleteUser(
                            productModel!!.getmProductName(),
                            caseArrayList!![idForSize].getName(),
                            colorArrayList!![idForColor].colorName,
                            productModel!!.categoryId
                        )
                        mAdapter.close()
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).getName(),
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDbNew(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    }
                } else if (countDuplicate > 0) {
                    if (AppController.p_id.length > 0) {
                        mAdapter.deleteUser(
                            AppController.p_id, caseArrayList!!
                                .get(idForSize).getName(),
                            colorArrayList!![idForColor].colorName,
                            productModel!!.categoryId
                        )
                        mAdapter.close()
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).getName(),
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDbNew(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    } else {
                        mAdapter.deleteUser(
                            productModel!!.getmProductName(),
                            caseArrayList!![idForSize].getName(),
                            colorArrayList!![idForColor].colorName,
                            productModel!!.categoryId
                        )
                        mAdapter.close()
                        addCartApi(
                            productModel!!.getmProductName(), qty,
                            productModel!!.categoryId, caseArrayList!!
                                .get(idForSize).getName(),
                            colorArrayList!![idForColor].colorName,
                            "6", values
                        )
                        databaseManager
                            ?.insertIntoDbNew(VKCDbConstants.TABLE_SHOPPINGCART, values)
                    }
                }
                listTemp!!.add(qty)
            }
        }
        mActivity!!.runOnUiThread {
            // Here your code that runs on UI Threads
            try {
                if (listTemp!!.size > 0) {
                    VKCUtils.showtoast(mActivity, 5)
                } else {
                    VKCUtils.showtoast(mActivity, 40)
                }
            } catch (e: Exception) {
                println(e)
            }
            for (i in editList!!.indices) {
                editList!![i].setText("")
            }

            //setCartQuantity();
        }
        AppController.p_id = ""
        isInserted = true
    }

    inner class ExceedAlert     // TODO Auto-generated constructor stub
        (var mActivity: Activity?) : Dialog((mActivity)!!), View.OnClickListener,
        VKCDbConstants {
        var d: Dialog? = null
        var position = 0
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_exceeds_limit)
            init()
        }

        private fun init() {
            val buttonSet = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener {
                insertToDb()
                dismiss()
            }
            val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener { dismiss() }
        }

        override fun onClick(v: View) {
            dismiss()
        }
    }

    private fun showExceedDialog() {
        val appDeleteDialog = ExceedAlert(mActivity)
        appDeleteDialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        appDeleteDialog.setCancelable(true)
        appDeleteDialog.show()
    }

    private fun addCartApi(
        product_id: String, qty: String, category_id: String,
        size: String, color: String, role: String, values: Array<Array<String?>>
    ) {
        var cust_id: String? = ""
        var manager: VKCInternetManagerWithoutDialog? = null
        val name = arrayOf(
            "product_id", "quantity", "category_id", "size",
            "color", "role", "cust_id"
        )
        if ((getUserType(this) == "4")) {
            cust_id = getSelectedUserId((mActivity)!!)
        } else {
            cust_id = getCustomerId((mActivity)!!)
        }
        val value = arrayOf(
            product_id, qty, category_id, size, color, role,
            cust_id
        )
        manager = VKCInternetManagerWithoutDialog(VKCUrlConstants.ADD_TO_CART_API)
        manager.getResponsePOST(mActivity, name, value,
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

    fun clearDb() {
        val databaseManager = DataBaseManager((mActivity)!!)
        databaseManager.removeDb(VKCDbConstants.TABLE_SHOPPINGCART)
    }

    companion object {
        var selectedFromSizeList: String? = null
        var selectedFromColorList: String? = null
        var selectedIDFromSizeList: String? = null
        var selectedIDFromColorList: String? = null
    }
}