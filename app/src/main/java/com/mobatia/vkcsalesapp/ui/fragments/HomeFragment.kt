package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.net.wifi.WifiConfiguration.AuthAlgorithm.strings
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper
import com.mobatia.vkcsalesapp.adapter.*
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import com.mobatia.vkcsalesapp.manager.*
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getBrandBannerResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFCMID
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getJsonOfferResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getNewArrivalBannerResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getPopularProductSliderResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFCMID
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveListingOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveOfferIDs
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setFillTable
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ARRIVAL_BANNER
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ARRIVAL_BANNERID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ARRIVAL_BANNER_IMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_ARRIVAL_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_BRAND_BANNERIMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_BRAND_ID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_BRAND_IMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_BRAND_IMAGENAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_BRAND_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_COST
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_ID
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
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_OFFER
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_OFFERID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_OFFERIMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME
import com.mobatia.vkcsalesapp.manager.VKCUtils.getDeviceID
import com.mobatia.vkcsalesapp.model.*
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class HomeFragment : Fragment(), VKCUrlConstants {
    // this Fragment will be called from MainActivity
    private var mRootView: View? = null
    private var mRelBanner: RelativeLayout? = null
    private var mRelArrivalBanner: RelativeLayout? = null
    private var mRelMiddleBanner: RelativeLayout? = null
    private var mRelFooter: RelativeLayout? = null
    private var mBottomBar: RelativeLayout? = null
    private var mLnrFooter: LinearLayout? = null
    private var LinearDisable: LinearLayout? = null
    private var mDisplayManager: DisplayManagerScale? = null
    var height = 0
    var width = 0
    private var productModels: ArrayList<ProductModel>? = null
    private var imgSearch: ImageView? = null
    private var edtSearch: EditText? = null
    private val homeImageBannerModels: ArrayList<HomeImageBannerModel>? = null
    var myPager: ViewPager? = null
    var offerPager: ViewPager? = null
    var brandBannerOne: ViewPager? = null
    var bannerShow = true
    var sliderBannerOfferCount = 0
    var sliderBannermyPager = 0
    var sliderBannerbrandBannerOne = 0
    lateinit var mContext: Context
    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        bannerShow = true
        startBannerShow()
    }

    override fun onPause() {
        // TODO Auto-generated method stub
        super.onPause()
        // Log.v("LOG", "13012015 onPause");
        bannerShow = false
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
    }

    override fun onStart() {
        // TODO Auto-generated method stub
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle = arguments
        saveOfferIDs(activity!!, "")
        mRootView = inflater.inflate(
            R.layout.home_activity_fragment,
            container, false
        )
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        AppController.isCart = false
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_imageview, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !H
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
        )
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowHomeEnabled(true)
        abar.setDisplayShowCustomEnabled(true)
        mContext = activity as AppCompatActivity
        abar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        setFillTable(activity!!, "false")
        initialiseUI()
        currentVersion = version
        val database = DatabaseHelper(activity!!, "VKC")
        try {
            database.createDataBase()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        GetAppVersion().execute(VKCUrlConstants.BASE_URL + VKCUrlConstants.URL_GET_APP_VERSION)
        setImageBanner(mRootView)
        LinearDisable!!.setOnClickListener { // TODO Auto-generated method stub
            updateApp(activity)
        }
        return mRootView
    }

    // TODO Auto-generated catch block
    private val version: String
        private get() {
            var packageinfo: PackageInfo? = null
            try {
                packageinfo = activity!!.packageManager.getPackageInfo(
                    activity!!.packageName, 0
                )
            } catch (e: PackageManager.NameNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return packageinfo!!.versionName.toString()
        }

    private fun setImageBanner(v: View?) {
        val homeImageBannerModels = ArrayList<HomeImageBannerModel>()
        val NewArrival = getNewArrivalBannerResponse(activity!!)
        productModels = ArrayList()
        try {
            val jsonArrayresponse = JSONArray(NewArrival)
            for (j in 0 until jsonArrayresponse.length()) {
                println("16122014 setImageBanner IN loop$j")
                val bannerModel = HomeImageBannerModel()
                val productJSONObject = jsonArrayresponse.getJSONObject(
                    j
                ).getJSONObject(JSON_ARRIVAL_RESPONSE)
                val productModel = parseProductModelTemp(productJSONObject)
                productModels!!.add(productModel)
                val jsonArrayZero = jsonArrayresponse.getJSONObject(j)
                val bannerArray = jsonArrayZero
                    .getJSONObject(JSON_ARRIVAL_BANNER)
                bannerModel.id = bannerArray.getString(JSON_ARRIVAL_BANNERID)
                bannerModel.bannerUrl = (VKCUrlConstants.BASE_URL
                        + bannerArray.getString(JSON_ARRIVAL_BANNER_IMAGE))
                homeImageBannerModels.add(bannerModel)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val adapter = HomeImageBannerAdapterFirst(
            activity!!, homeImageBannerModels, productModels, 0
        )
        sliderBannermyPager = homeImageBannerModels.size
        myPager = v!!.findViewById<View>(R.id.reviewpager) as ViewPager
        myPager!!.visibility = View.VISIBLE
        myPager!!.adapter = adapter
        myPager!!.currentItem = 0
        setBannerPosition(v)
        setOfferBanners(v)
        setThreeImageBanner(v)
        setBrandBannersOneAndTwo(v)
    }

    private fun setOfferBanner(v: View) {
        val homeImageBannerModels = ArrayList<HomeImageBannerModel>()
        val hreeImaegResp = getNewArrivalBannerResponse(activity!!)
        val bannerModel: HomeImageBannerModel? = null
        try {
            val jsonArray = JSONArray(hreeImaegResp)
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    fun getBandBannerObjectValues(jobject: JSONObject): BrandBannerModel {
        val brandBannerModel = BrandBannerModel()
        val typeModel = getBrandTypeModel(
            jobject
                .getJSONObject(JSON_BRAND_RESPONSE)
        )
        val jsonObjectBrandImage = jobject
            .getJSONObject(JSON_BRAND_IMAGE)
        brandBannerModel.id = jsonObjectBrandImage.getString(JSON_BRAND_ID)
        brandBannerModel.brandBannerOne = (VKCUrlConstants.BASE_URL
                + jsonObjectBrandImage.getString(JSON_BRAND_BANNERIMAGE))
        brandBannerModel.brandBannerTwo = (VKCUrlConstants.BASE_URL
                + jsonObjectBrandImage.getString(JSON_BRAND_IMAGENAME))
        brandBannerModel.typeModel = typeModel
        return brandBannerModel
    }

    @Throws(JSONException::class)
    fun getOffersObjectValues(jobject: JSONObject): OfferModel {
        val offerModel = OfferModel()
        offerModel.id = jobject.getString(JSON_SETTINGS_OFFERID)
        offerModel.name = jobject.getString(JSON_SETTINGS_OFFER)
        offerModel.offerBanner = (VKCUrlConstants.BASE_URL
                + jobject.getString(JSON_SETTINGS_OFFERIMAGE))
        return offerModel
    }

    private fun setOfferBanners(v: View?) {
        val offerModels = ArrayList<OfferModel>()
        try {
            val offerObjArray = JSONArray(
                getJsonOfferResponse(activity!!)
            )
            for (i in 0 until offerObjArray.length()) {
                val responseObj = offerObjArray.getJSONObject(i)
                offerModels.add(getOffersObjectValues(responseObj))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        offerPager = v!!.findViewById<View>(R.id.reviewpagerOffer) as ViewPager
        offerPager!!.visibility = View.VISIBLE
        val adapter = HomeOfferBannerAdapter(
            activity!!, offerModels /* ,displayView */
        )
        sliderBannerOfferCount = offerModels.size
        offerPager!!.adapter = adapter
        offerPager!!.currentItem = 0
    }

    private fun setBrandBannersOneAndTwo(v: View?) {
        val brandBannerModels = ArrayList<BrandBannerModel>()
        try {
            val bannerObjArray = JSONArray(
                getBrandBannerResponse(activity!!)
            )
            for (i in 0 until bannerObjArray.length()) {
                val responseObj = bannerObjArray.getJSONObject(i)
                brandBannerModels.add(getBandBannerObjectValues(responseObj))
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        brandBannerOne = v!!.findViewById<View>(R.id.pagerBrandBannerOne) as ViewPager
        brandBannerOne!!.visibility = View.VISIBLE
        val bannerOneAdapter = HomeBrandBannerOneAdapter(
            activity!!, brandBannerModels /* ,displayView */
        )
        sliderBannerbrandBannerOne = brandBannerModels.size
        brandBannerOne!!.adapter = bannerOneAdapter
        brandBannerOne!!.currentItem = 0
        val brandBannerTwo = v
            .findViewById<View>(R.id.pagerBrandBannerTwo) as ViewPager
        brandBannerTwo.visibility = View.VISIBLE
        val bannerTwoAdapter = HomeBrandBannerTwoAdapter(
            activity!!, brandBannerModels /* ,displayView */, 1
        )
        brandBannerTwo.adapter = bannerTwoAdapter
    }

    private fun parseProductModelTemp(jsonObjectZero: JSONObject): ProductModel {
        val productModel = ProductModel()
        try {
            productModel.productdescription = jsonObjectZero
                .optString("productdescription")
            productModel.categoryName = jsonObjectZero
                .optString(JSON_CATEGORY_NAME)
            productModel.categoryId = jsonObjectZero.optString("categoryid")
            productModel.setmSapId(jsonObjectZero.optString("productSapId"))
            productModel.categoryId = jsonObjectZero
                .optString(JSON_CATEGORY_ID)
            productModel.setmProductPrize(
                jsonObjectZero
                    .optString(JSON_CATEGORY_COST)
            )
            productModel.id = jsonObjectZero.optString(JSON_PRODUCT_ID)
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
                .optString(JSON_PRODUCT_OFFER)
            productModel.setmProductOff(
                jsonObjectZero
                    .optString(JSON_PRODUCT_TIMESTAMP)
            )
            val productColorArray = jsonObjectZero
                .getJSONArray(JSON_PRODUCT_COLOR)
            val productImageArray = jsonObjectZero
                .getJSONArray(JSON_PRODUCT_IMAGE)
            val productSizeArray = jsonObjectZero
                .getJSONArray(JSON_PRODUCT_SIZE)
            val productNewArrivalArray = jsonObjectZero
                .getJSONArray("new_arrivals")
            val productTypeArray = jsonObjectZero
                .getJSONArray(JSON_PRODUCT_TYPE)
            val productCaseArray = jsonObjectZero
                .getJSONArray(JSON_PRODUCT_CASE)
            val colorModels = ArrayList<ColorModel>()
            for (i in 0 until productColorArray.length()) {
                val colorModel = ColorModel()
                val jsonObject = productColorArray.getJSONObject(i)
                colorModel.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                colorModel.colorcode = jsonObject
                    .optString(JSON_SETTINGS_COLORCODE)
                colorModel.colorName = jsonObject.optString("color_name")
                colorModels.add(colorModel)
            }
            productModel.productColor = colorModels
            val newArrList = ArrayList<ProductImages>()
            for (i in 0 until productNewArrivalArray.length()) {
                val images = ProductImages()
                val jsonObject = productNewArrivalArray.getJSONObject(i)
                images.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                images.imageName = (VKCUrlConstants.BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE))
                images.productName = jsonObject.optString("product_name")
                val colorModel = ColorModel()
                colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                colorModel.colorcode = jsonObject
                    .optString(JSON_SETTINGS_COLORCODE)
                images.colorModel = colorModel
                images.catId = jsonObject.optString("categoryid")
                newArrList.add(images)
            }
            productModel.setmNewArrivals(newArrList)
            // ////////////
            val productImages = ArrayList<ProductImages>()
            for (i in 0 until productImageArray.length()) {
                val images = ProductImages()
                val jsonObject = productImageArray.getJSONObject(i)
                images.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                images.imageName = (VKCUrlConstants.BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE))
                val colorModel = ColorModel()
                colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                colorModel.colorcode = jsonObject
                    .optString(JSON_SETTINGS_COLORCODE)
                images.colorModel = colorModel
                productImages.add(images)
            }
            productModel.productImages = productImages
            // ///
            val sizeModels = ArrayList<SizeModel>()
            for (i in 0 until productSizeArray.length()) {
                val sizeModel = SizeModel()
                val jsonObject = productSizeArray.getJSONObject(i)
                sizeModel.id = jsonObject.optString(JSON_SETTINGS_SIZEID)
                sizeModel.name = jsonObject.optString(JSON_SETTINGS_SIZENAME)
                sizeModels.add(sizeModel)
            }
            productModel.setmProductSize(sizeModels)
            // /////
            val brandTypeModels = ArrayList<BrandTypeModel>()
            for (i in 0 until productTypeArray.length()) {
                brandTypeModels.add(
                    getBrandTypeModel(
                        productTypeArray
                            .getJSONObject(i)
                    )
                )
            }
            productModel.productType = brandTypeModels
            val caseModels = ArrayList<CaseModel>()
            for (i in 0 until productCaseArray.length()) {
                val caseModel = CaseModel()
                val jsonObject = productCaseArray.getJSONObject(i)
                caseModel.id = jsonObject.optString(JSON_SETTINGS_CASEID)
                caseModel.name = jsonObject.optString(JSON_SETTINGS_CASENAME)
                caseModels.add(caseModel)
            }
            productModel.setmProductCases(caseModels)
        } catch (e: Exception) {
        }
        return productModel
    }

    private fun getBrandTypeModel(jsonObject: JSONObject): BrandTypeModel {
        val typeModel = BrandTypeModel()
        typeModel.id = jsonObject.optString(JSON_SETTINGS_BRANDID)
        typeModel.name = jsonObject.optString(JSON_SETTINGS_BRANDNAME)
        return typeModel
    }

    private fun parseProductModel(jsonObjectZero: JSONObject): ProductModel {
        val productModel = ProductModel()
        try {
            productModel.productdescription = jsonObjectZero
                .optString("productdescription")
            productModel.categoryName = jsonObjectZero
                .optString(JSON_CATEGORY_NAME)
            productModel.categoryId = jsonObjectZero.optString("categoryid")
            productModel.setmSapId(jsonObjectZero.optString("productSapId"))
            productModel.categoryId = jsonObjectZero
                .optString(JSON_CATEGORY_ID)
            productModel.categoryId = jsonObjectZero
                .optString(JSON_CATEGORY_ID)
            productModel.categoryName = jsonObjectZero
                .optString(JSON_CATEGORY_NAME)
            productModel.setmProductPrize(
                jsonObjectZero
                    .optString(JSON_CATEGORY_COST)
            )
            productModel.id = jsonObjectZero.optString(JSON_PRODUCT_ID)
            productModel.setmSapId(jsonObjectZero.optString("productSapId"))
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
                .optString(JSON_PRODUCT_OFFER)
            productModel.setmProductOff(
                jsonObjectZero
                    .optString(JSON_PRODUCT_TIMESTAMP)
            )
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
                val jsonObject = productColorArray.getJSONObject(i)
                colorModel.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                colorModel.colorcode = jsonObject
                    .optString(JSON_SETTINGS_COLORCODE)
                colorModel.colorName = jsonObject.optString("color_name")
                colorModels.add(colorModel)
            }
            productModel.productColor = colorModels
            val newArrList = ArrayList<ProductImages>()
            for (i in 0 until productNewArrivalArray.length()) {
                val images = ProductImages()
                val jsonObject = productNewArrivalArray.getJSONObject(i)
                images.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                images.imageName = (VKCUrlConstants.BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE))
                images.productName = jsonObject.optString("product_name")
                val colorModel = ColorModel()
                colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                colorModel.colorcode = jsonObject
                    .optString(JSON_SETTINGS_COLORCODE)
                images.colorModel = colorModel
                images.catId = jsonObject.optString("categoryid")
                newArrList.add(images)
            }
            productModel.setmNewArrivals(newArrList)
            // ////////////
            val productImages = ArrayList<ProductImages>()
            for (i in 0 until productImageArray.length()) {
                val images = ProductImages()
                val jsonObject = productImageArray.getJSONObject(i)
                images.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                images.imageName = (VKCUrlConstants.BASE_URL
                        + jsonObject.optString(JSON_COLOR_IMAGE))
                val colorModel = ColorModel()
                colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                colorModel.colorcode = jsonObject
                    .optString(JSON_SETTINGS_COLORCODE)
                images.colorModel = colorModel
                productImages.add(images)
            }
            productModel.productImages = productImages
            // ///
            val sizeModels = ArrayList<SizeModel>()
            for (i in 0 until productSizeArray.length()) {
                val sizeModel = SizeModel()
                val jsonObject = productSizeArray.getJSONObject(i)
                sizeModel.id = jsonObject.optString(JSON_SETTINGS_SIZEID)
                sizeModel.name = jsonObject.optString(JSON_SETTINGS_SIZENAME)
                sizeModels.add(sizeModel)
            }
            productModel.setmProductSize(sizeModels)
            // /////
            val brandTypeModels = ArrayList<BrandTypeModel>()
            for (i in 0 until productTypeArray.length()) {
                val typeModel = BrandTypeModel()
                val jsonObject = productTypeArray.getJSONObject(i)
                typeModel.id = jsonObject.optString(JSON_SETTINGS_BRANDID)
                typeModel
                    .name = jsonObject.optString(JSON_SETTINGS_BRANDNAME)
                brandTypeModels.add(typeModel)
            }
            productModel.productType = brandTypeModels
            val caseModels = ArrayList<CaseModel>()
            for (i in 0 until productCaseArray.length()) {
                val caseModel = CaseModel()
                val jsonObject = productCaseArray.getJSONObject(i)
                caseModel.id = jsonObject.optString(JSON_SETTINGS_CASEID)
                caseModel.name = jsonObject.optString(JSON_SETTINGS_CASENAME)
                caseModels.add(caseModel)
            }
            productModel.setmProductCases(caseModels)
        } catch (e: Exception) {
        }
        return productModel
    }

    private fun setThreeImageBanner(v: View?) {
        val homeImageBannerModels = ArrayList<HomeImageBannerModel>()
        val hreeImaegResp = getPopularProductSliderResponse(activity!!)
        productModels = ArrayList()
        try {
            val jsonArrayresponse = JSONArray(
                getPopularProductSliderResponse(activity!!)
            )
            for (j in 0 until jsonArrayresponse.length()) {
                val bannerModel = HomeImageBannerModel()
                val productModel = parseProductModel(
                    jsonArrayresponse
                        .getJSONObject(j)
                )
                productModels!!.add(productModel)
                bannerModel.id = bannerModel.id
                bannerModel.bannerUrl = productModel.productImages[0]
                    .imageName
                bannerModel.slideId = productModel.productImages[0]
                    .id
                homeImageBannerModels.add(bannerModel)
            }
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        // Last Three image Banner
        val adapter = HomeImageBannerAdapter(
            activity!!, homeImageBannerModels, productModels, 1
        )
        val myPager = v
            ?.findViewById<View>(R.id.reviewpagerThreeImage) as ViewPager
        myPager.visibility = View.VISIBLE
        myPager.adapter = adapter
        myPager.currentItem = 0
    }

    private fun startBannerShow() {
        object : AsyncTask<Void?, Int, Void?>() {
            var count = 0
            protected override fun doInBackground(vararg p0: Void?): Void? {
                // TODO Auto-generated method stub
                while (bannerShow) {
                    try {
                        Thread.sleep(1000)
                        count++
                    } catch (e: InterruptedException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    publishProgress(count)
                }
                return null
            }

            override fun onProgressUpdate(values: Array<Int>) {
                if (values[0] % 3 == 0) {
                    if (sliderBannermyPager - 1 == myPager!!.currentItem) {
                        myPager!!.currentItem = 0
                    } else {
                        myPager!!.currentItem = myPager!!.currentItem + 1
                    }
                } else if (values[0] % 3 == 1) {
                    if (sliderBannerbrandBannerOne - 1 == brandBannerOne
                            ?.getCurrentItem()
                    ) {
                        brandBannerOne!!.currentItem = 0
                    } else {
                        brandBannerOne!!.currentItem = brandBannerOne!!
                            .getCurrentItem() + 1
                    }
                } else if (values[0] % 3 == 2) {
                    if (sliderBannerOfferCount - 1 == offerPager
                            ?.getCurrentItem()
                    ) {
                        offerPager!!.currentItem = 0
                    } else {
                        offerPager
                            ?.setCurrentItem(offerPager!!.currentItem + 1)
                    }
                }
            }
        }.execute()
    }

    private fun setBannerPosition(v: View?) {
        val relArrivalBannerNext: RelativeLayout
        val relArrivalBannerPrevious: RelativeLayout
        relArrivalBannerNext = v
            ?.findViewById<View>(R.id.relArrivalBannerNext) as RelativeLayout
        relArrivalBannerPrevious = v
            ?.findViewById<View>(R.id.relArrivalBannerPrevious) as RelativeLayout
        relArrivalBannerNext.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            myPager!!.currentItem = myPager!!.currentItem - 1
            false
        }
        relArrivalBannerPrevious.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            myPager!!.currentItem = myPager!!.currentItem + 1
            false
        }
    }

    fun getSupportActionBarHeight(activity: Activity?): Int {
        val styledAttributes = activity!!.theme
            .obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val mActionBarSize = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
        return mActionBarSize
    }

    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = resources.getIdentifier(
                "status_bar_height",
                "dimen", "android"
            )
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    private fun initialiseUI() {
        val fragmentHomeBase = mRootView
            ?.findViewById<View>(R.id.fragmentHomeBase) as RelativeLayout
        val relBrandBannerOne = mRootView
            ?.findViewById<View>(R.id.relBrandBannerOne) as RelativeLayout
        val relBrandBannerTwo = mRootView
            ?.findViewById<View>(R.id.relBrandBannerTwo) as RelativeLayout
        val relSearchHeader = mRootView
            ?.findViewById<View>(R.id.relSearchHeader) as RelativeLayout
        mRelBanner = mRootView!!.findViewById<View>(R.id.relBanner) as RelativeLayout
        mRelArrivalBanner = mRootView
            ?.findViewById<View>(R.id.relArrivalBanner) as RelativeLayout
        mRelMiddleBanner = mRootView
            ?.findViewById<View>(R.id.relMiddleBanner) as RelativeLayout
        mRelFooter = mRootView!!.findViewById<View>(R.id.relFooter) as RelativeLayout
        mBottomBar = mRootView!!.findViewById<View>(R.id.bottomBar) as RelativeLayout
        mLnrFooter = mRootView!!.findViewById<View>(R.id.lnrFooter) as LinearLayout
        LinearDisable = mRootView!!.findViewById<View>(R.id.llDisable) as LinearLayout
        mDisplayManager = DisplayManagerScale(activity!!)
        height = mDisplayManager!!.deviceHeight
        width = mDisplayManager!!.deviceWidth
        height = (height
                - (statusBarHeight + getSupportActionBarHeight(activity)))
        val bannerHeight = (height * 0.45).toInt()
        val arrBannerHeight = (height * 0.07).toInt()
        val relBrandBannerTwoHeight = width / 3
        val footerBannerHeight =  /* (int) (height * 0.245) */width / 3
        val bootomBarHeight = (height * 0.04).toInt()
        val middleBannerHeight = ( /* (height * 0.245) */height
                - (bannerHeight + arrBannerHeight + bootomBarHeight + footerBannerHeight))
        val relBrandBannerOneHeight = (height
                - (bannerHeight + arrBannerHeight + relBrandBannerTwoHeight))
        relBrandBannerOne.layoutParams.height = relBrandBannerOneHeight // 1
        relBrandBannerTwo.layoutParams.height = relBrandBannerTwoHeight // 1
        mRelBanner!!.layoutParams.height = bannerHeight // 1
        mRelMiddleBanner!!.layoutParams.height = relBrandBannerOneHeight // 3
        mRelFooter!!.layoutParams.height = footerBannerHeight // 4
        mBottomBar!!.layoutParams.height = bootomBarHeight // 5
        val manager = activity?.let { SearchHeaderManager(it) }
        manager?.getSearchHeader(relSearchHeader)
        manager?.searchAction(mContext, object : SearchHeaderManager.SearchActionInterface {
            override fun searchOnTextChange(key: String?) {
                // TODO Auto-generated method stub
                if (edtSearch!!.text.toString() != "") {
                    DashboardFActivity.dashboardFActivity
                        .goToSearchWithKey(edtSearch!!.text.toString())
                    saveListingOption(activity!!, "3")
                }
                edtSearch!!.setText("")
            }
        }, edtSearch!!.text.toString())
        mRelArrivalBanner!!.setOnClickListener {
            // TODO Auto-generated method stub
        }
        mRelMiddleBanner!!.setOnClickListener {
            // TODO Auto-generated method stub
        }
    }

    private inner class GetAppVersion : AsyncTask<String?, Void?, String?>() {
        val pDialog = CustomProgressBar(
            activity,
            R.drawable.loading
        )
        var NewVersion: String? = null
        override fun onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute()
            pDialog.show()
        }


        override fun onPostExecute(temp: String?) {
            pDialog.dismiss()
            currentVersion = version
            if (currentVersion!!.length > 3) {
                currentVersion = currentVersion!!.substring(0, 3)
            }
            if (currentVersion == NewVersion) {
                LinearDisable!!.visibility = View.GONE
                deviceRegister()
            } else {
                LinearDisable!!.visibility = View.VISIBLE
                updateApp(activity)
            }
        }

        override fun doInBackground(vararg p0: String?): String? {

            val weather = "UNDEFINED"
            val result: JSONObject
            try {
                val url = URL(strings[0])
                val urlConnection = url
                    .openConnection() as HttpURLConnection
                val stream: InputStream = BufferedInputStream(
                    urlConnection.inputStream
                )
                val bufferedReader = BufferedReader(
                    InputStreamReader(stream)
                )
                val builder = StringBuilder()
                var inputString: String?
                while (bufferedReader.readLine().also { inputString = it } != null) {
                    builder.append(inputString)
                }
                result = JSONObject(builder.toString())
                val response = result.getJSONObject("response")
                val status = response.getString("status")
                if (status == "Success") {
                    NewVersion = response.getString("appversion")
                }
                urlConnection.disconnect()
            } catch (e: IOException) {
                e.printStackTrace()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            return NewVersion
        }
    }

    private fun deviceRegister() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    // Log.w(TAG, "getInstanceId failed", task.getException());
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result!!.token
                saveFCMID(activity!!, token)
                // Log and toast
                /*  String msg = getString(R.string.msg_token_fmt, token);
                            Log.d(TAG, msg);
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();*/
            })
        var device_id = ""
        device_id = if (Build.VERSION.SDK_INT >= 27) {
            ""
        } else {
            getDeviceID(activity!!)
        }
        val name = arrayOf("imei_no", "gcm_id")
        val values = arrayOf(
            device_id, getFCMID(
                activity!!
            )
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.GCM_INITIALISATION
        )
        manager.getResponsePOST(activity, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                // TODO Auto-generated method stub
                //  Log.v("LOG", "20022015 successappinit" + successResponse);
                //  parseResponse(successResponse);
            }

            override fun responseFailure(failureResponse: String?) {
                // TODO Auto-generated method stub
                Log.v("LOG", "20022015 Errror$failureResponse")
            }
        })
    }

    companion object {
        var currentVersion: String? = null
        var newVersion: String? = null
        fun updateApp(act: Activity?) {
            val appPackageName = act!!.packageName
            val builder = AlertDialog.Builder(act)
            builder.setTitle("Update Available !") // act.getString(R.string.dialog_title_update_app)
                .setMessage("Please Update the app to continue") //
                // .setNegativeButton(act.getString(R.string.dialog_default_cancel),
                // null)
                .setPositiveButton("OK") { dialogInterface, i ->
                    try {
                        act.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "market://details?id="
                                            + appPackageName
                                )
                            )
                        )
                    } catch (anfe: ActivityNotFoundException) {
                        act.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                    "https://play.google.com/store/apps/details?id="
                                            + appPackageName
                                )
                            )
                        )
                    }
                }
            val dialog = builder.create()
            dialog.setCanceledOnTouchOutside(false)
            dialog.show()
        }
    }
}