/**
 *
 */
package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.*
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getJsonColorResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getJsonOfferResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getJsonPriceResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getJsonSizeResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getJsonTypeResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getMainCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataBrand
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataColor
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataPrice
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataSize
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataSubCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveListingOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setCatId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setParentCatId
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_BRANDID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_BRANDNAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_CATEGORYID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_CATEGORYNAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORCODE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_OFFER
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_OFFERID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_OFFERIMAGE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRICEFROM
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRICEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRICETO
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRODUCTID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class FilterActivity : AppCompatActivity() {
    //BaseActivity
    private var mListFilter: ListView? = null
    private var mListFilterContent: ListView? = null
    private val mTextString = arrayOf(
        "Category", "SubCategory", "Size",
        "Brand", "Price", "Color", "Offers"
    )
    private var mActivity: Activity? = null
    private var tempArrayListCategory: ArrayList<CategoryModel>? = null
    private var tempArrayListMainCategory: ArrayList<CategoryModel>? = null
    private var tempArrayListSize: ArrayList<SizeModel>? = null
    private var tempArrayListBrand: ArrayList<BrandTypeModel>? = null
    private var tempArrayListPrice: ArrayList<PriceModel>? = null
    private var tempArrayListColor: ArrayList<ColorModel>? = null
    private var relApply: RelativeLayout? = null
    private var relClear: RelativeLayout? = null
    var typeArrayList: ArrayList<BrandTypeModel>? = null
    var sizeArrayList: ArrayList<SizeModel>? = null
    var colorArrayList: ArrayList<ColorModel>? = null
    var priceArrayList: ArrayList<PriceModel>? = null
    var categoryArrayList: ArrayList<CategoryModel>? = null
    var offerModels: ArrayList<OfferModel>? = null
    var tempofferModels: ArrayList<OfferModel>? = null
    var clearFlag = false
    lateinit var content: Array<List<String>>
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        mActivity = this
        AppController.tempmainCatArrayList.clear()
        AppController.tempsubCatArrayList.clear()
        AppController.tempsizeCatArrayList.clear()
        AppController.tempbrandCatArrayList.clear()
        AppController.temppriceCatArrayList.clear()
        AppController.tempcolorCatArrayList.clear()
        AppController.tempofferCatArrayList.clear()
        initialiseUI()
        setArrayLists()
        setCategory()
        setActionBar()
    }

    fun setArrayLists() {
        typeArrayList = ArrayList()
        categoryArrayList = ArrayList()
        sizeArrayList = ArrayList()
        tempArrayListSize = ArrayList()
        colorArrayList = ArrayList()
        priceArrayList = ArrayList()
        tempArrayListBrand = ArrayList()
        tempArrayListPrice = ArrayList()
        tempArrayListColor = ArrayList()
        tempArrayListCategory = ArrayList()
        tempArrayListMainCategory = ArrayList()
        offerModels = ArrayList()
        tempofferModels = ArrayList()
        try {
            val offerObjArray = JSONArray(
                getJsonOfferResponse(this@FilterActivity)
            )
            for (i in 0 until offerObjArray.length()) {
                val responseObj = offerObjArray.getJSONObject(i)
                offerModels!!.add(getOffersObjectValues(responseObj))
            }
            val typeObjArray = JSONArray(
                getJsonTypeResponse(this@FilterActivity)
            )
            for (i in 0 until typeObjArray.length()) {
                val responseObj = typeObjArray.getJSONObject(i)
                typeArrayList!!.add(getTypeObjectValues(responseObj))
            }
            val sizeObjArray = JSONArray(
                getJsonSizeResponse(this@FilterActivity)
            )
            for (i in 0 until sizeObjArray.length()) {
                val responseObj = sizeObjArray.getJSONObject(i)
                sizeArrayList!!.add(getSizeObjectValues(responseObj))
            }
            val colorObjArray = JSONArray(
                getJsonColorResponse(this@FilterActivity)
            )
            for (i in 0 until colorObjArray.length()) {
                val responseObj = colorObjArray.getJSONObject(i)
                colorArrayList!!.add(getColorObjectValues(responseObj))
            }
            val priceObjArray = JSONArray(
                getJsonPriceResponse(this@FilterActivity)
            )
            for (i in 0 until priceObjArray.length()) {
                val responseObj = priceObjArray.getJSONObject(i)
                priceArrayList!!.add(getPriceObjectValues(responseObj))
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }

    @Throws(JSONException::class)
    fun getOffersObjectValues(jobject: JSONObject): OfferModel {
        val offerModel = OfferModel()
        offerModel.id = jobject.getString(JSON_SETTINGS_OFFERID)
        offerModel.name = jobject.getString(JSON_SETTINGS_OFFER)
        offerModel.offerBanner = jobject.getString(JSON_SETTINGS_OFFERIMAGE)
        return offerModel
    }

    @Throws(JSONException::class)
    fun getTypeObjectValues(jobject: JSONObject): BrandTypeModel {
        val brandModel = BrandTypeModel()
        brandModel.id = jobject.getString(JSON_SETTINGS_BRANDID)
        brandModel.name = jobject.getString(JSON_SETTINGS_BRANDNAME)
        return brandModel
    }

    @Throws(JSONException::class)
    fun getCategoryObjectValues(jobject: JSONObject): CategoryModel {
        val categoryModel = CategoryModel()
        categoryModel.id = jobject.getString(JSON_SETTINGS_CATEGORYID)
        categoryModel.name = jobject.getString(JSON_SETTINGS_CATEGORYNAME)
        categoryModel.parentId = jobject.getString(JSON_SETTINGS_PRODUCTID)
        return categoryModel
    }

    @Throws(JSONException::class)
    fun getSizeObjectValues(jobject: JSONObject): SizeModel {
        val sizeModel = SizeModel()
        sizeModel.id = jobject.getString(JSON_SETTINGS_SIZEID)
        sizeModel.name = jobject.getString(JSON_SETTINGS_SIZENAME)
        return sizeModel
    }

    @Throws(JSONException::class)
    fun getColorObjectValues(jobject: JSONObject): ColorModel {
        val colorModel = ColorModel()
        colorModel.id = jobject.getString(JSON_SETTINGS_COLORID)
        colorModel.colorcode = jobject.getString(JSON_SETTINGS_COLORCODE)
        colorModel.colorName = jobject.getString("color_name")
        return colorModel
    }

    @Throws(JSONException::class)
    fun getPriceObjectValues(jobject: JSONObject): PriceModel {
        val priceModel = PriceModel()
        priceModel.id = jobject.getString(JSON_SETTINGS_PRICEID)
        priceModel.from_range = jobject.getString(JSON_SETTINGS_PRICEFROM)
        priceModel.to_range = jobject.getString(JSON_SETTINGS_PRICETO)
        return priceModel
    }

    private fun initialiseUI() {
        mListFilter = findViewById<View>(R.id.listFilter) as ListView
        relApply = findViewById<View>(R.id.relApply) as RelativeLayout
        relClear = findViewById<View>(R.id.relClear) as RelativeLayout
        relApply!!.setOnClickListener {
            selectedOptions
            saveListingOption(mActivity!!, "2")
            finish()
        }
        relClear!!.setOnClickListener {
            tempArrayListBrand!!.clear()
            tempArrayListCategory!!.clear()
            tempArrayListColor!!.clear()
            tempArrayListMainCategory!!.clear()
            tempArrayListPrice!!.clear()
            tempArrayListSize!!.clear()
            tempofferModels!!.clear()
            AppController.tempmainCatArrayList.clear()
            AppController.tempsubCatArrayList.clear()
            AppController.tempsizeCatArrayList.clear()
            AppController.tempbrandCatArrayList.clear()
            AppController.temppriceCatArrayList.clear()
            AppController.tempcolorCatArrayList.clear()
            AppController.tempofferCatArrayList.clear()
            saveFilterDataCategory(mActivity!!, "")
            saveFilterDataSubCategory(mActivity!!, "")
            saveFilterDataSize(mActivity!!, "")
            saveFilterDataBrand(mActivity!!, "")
            saveFilterDataColor(mActivity!!, "")
            saveFilterDataPrice(mActivity!!, "")
            saveFilterDataOffer(mActivity!!, "")
            clearFlag = true
            setSubCategory()
            clearFlag = false
            setSize()
            setBrand()
            setPrice()
            setColor()
            setOffers()
            setCategory()
        }
        mListFilterContent = findViewById<View>(R.id.listFilterContent) as ListView
        mListFilter!!.adapter = FilterAdapter(mActivity!!, mTextString)
        mListFilter!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> // TODO Auto-generated method stub
                if (position == 0) {
                    setCategory()
                } else if (position == 1) {
                    setSubCategory()
                } else if (position == 2) {
                    setSize()
                } else if (position == 3) {
                    setBrand()
                } else if (position == 4) {
                    setPrice()
                } else if (position == 5) {
                    setColor()
                } else if (position == 6) {
                    setOffers()
                }
            }
        mListFilterContent!!.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> // TODO Auto-generated method stub
                val selectedFromList = mListFilterContent!!
                    .getItemAtPosition(position) as String
            }
    }

    val selectedOptions: Unit
        get() {
            var responseCategory = ""
            var responseSubCategory = ""
            var responseSize = ""
            var responseBrand = ""
            var responsePrice = ""
            var responseColor = ""
            var responseOffer = ""
            if (AppController.tempmainCatArrayList.size > 0) {
                for (i in AppController.tempmainCatArrayList.indices) {
                    responseCategory = (responseCategory
                            + AppController.tempmainCatArrayList[i] + ",")
                }
                if (responseCategory.endsWith(",")) {
                    responseCategory = responseCategory.substring(
                        0,
                        responseCategory.length - 1
                    )
                }
                saveFilterDataCategory(
                    mActivity!!,
                    responseCategory
                )
            }
            if (AppController.tempsubCatArrayList.size > 0) {
                for (i in AppController.tempsubCatArrayList.indices) {
                    responseSubCategory = (responseSubCategory
                            + AppController.tempsubCatArrayList[i] + ",")
                }
                if (responseSubCategory.endsWith(",")) {
                    responseSubCategory = responseSubCategory.substring(
                        0,
                        responseSubCategory.length - 1
                    )
                }
                saveFilterDataSubCategory(
                    mActivity!!,
                    responseSubCategory
                )
            }
            if (AppController.tempsizeCatArrayList.size > 0) {
                for (i in AppController.tempsizeCatArrayList.indices) {
                    responseSize = (responseSize
                            + AppController.tempsizeCatArrayList[i] + ",")
                }
                if (responseSize.endsWith(",")) {
                    responseSize = responseSize.substring(
                        0,
                        responseSize.length - 1
                    )
                }
                saveFilterDataSize(mActivity!!, responseSize)
            }
            if (AppController.tempofferCatArrayList.size > 0) {
                for (i in AppController.tempofferCatArrayList.indices) {
                    responseOffer = (responseOffer
                            + AppController.tempofferCatArrayList[i] + ",")
                }
                if (responseOffer.endsWith(",")) {
                    responseOffer = responseOffer.substring(
                        0,
                        responseOffer.length - 1
                    )
                }
                saveFilterDataOffer(mActivity!!, responseOffer)
            }
            if (AppController.tempbrandCatArrayList.size > 0) {
                for (i in AppController.tempbrandCatArrayList.indices) {
                    responseBrand = (responseBrand
                            + AppController.tempbrandCatArrayList[i] + ",")
                }
                if (responseBrand.endsWith(",")) {
                    responseBrand = responseBrand.substring(
                        0,
                        responseBrand.length - 1
                    )
                }
                saveFilterDataBrand(mActivity!!, responseBrand)
            }
            if (AppController.temppriceCatArrayList.size > 0) {
                for (i in AppController.temppriceCatArrayList.indices) {
                    responsePrice = (responsePrice
                            + AppController.temppriceCatArrayList[i] + ",")
                }
                if (responsePrice.endsWith(",")) {
                    responsePrice = responsePrice.substring(
                        0,
                        responsePrice.length - 1
                    )
                }
                saveFilterDataPrice(mActivity!!, responsePrice)
            }
            if (AppController.tempcolorCatArrayList.size > 0) {
                for (i in AppController.tempcolorCatArrayList.indices) {
                    responseColor = (responseColor
                            + AppController.tempcolorCatArrayList[i] + ",")
                }
                if (responseColor.endsWith(",")) {
                    responseColor = responseColor.substring(
                        0,
                        responseColor.length - 1
                    )
                }
                saveFilterDataColor(mActivity!!, responseColor)
            }
            if (responseCategory == "1" || responseCategory == "2"
                || responseCategory == "3"
                || responseCategory == "4"
                || responseCategory == "5"
                || responseCategory == "31"
                || responseCategory == "33"
            ) {
                println("alambana cat id---1")
                setCatId(mActivity!!, responseSubCategory)
                setParentCatId(mActivity!!, responseCategory)
            } else {
                setCatId(mActivity!!, responseSubCategory)
                setParentCatId(mActivity!!, responseCategory)
            }
        }

    private fun setCategory() {
        // TODO Auto-generated method stub
        val respMainCategory = getMainCategory(this@FilterActivity)
        val mainCategoryArrayList = ArrayList<CategoryModel>()
        var categoryObjArray: JSONArray? = null
        try {
            categoryObjArray = JSONArray(respMainCategory)
            for (i in 0 until categoryObjArray.length()) {
                val responseObj = categoryObjArray.getJSONObject(i)
                val model = getCategoryObjectValues(responseObj)
                if (model.parentId.equals("0", ignoreCase = true)) {
                    mainCategoryArrayList.add(model)
                }
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
        val adapter = FilterCategoryMainContentAdapter(
            mActivity!!,  /*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 */
            mainCategoryArrayList, tempArrayListMainCategory!!
        )
        mListFilterContent!!.adapter = adapter
    }

    private fun setSubCategory() {
        val sortCategory: Array<SortCategory> = SplashActivity.sortCategoryGlobal
            ?.sortCategorys!!
        val filterArrayList = ArrayList<CategoryModel>()
        filterArrayList.clear()
        if (tempArrayListMainCategory!!.size == 0 && !clearFlag) {
            showtoast(mActivity, 18)
        }
        for (i in sortCategory.indices) {
            for (categoryModel in sortCategory[i]
                .categoryModels) {
                for (categoryModelMain in tempArrayListMainCategory!!) {
                    if (categoryModel.parentId ==
                        categoryModelMain.id
                    ) filterArrayList.add(categoryModel)
                }
            }
        }
        val adapter = FilterCategoryContentAdapter(
            mActivity!!,  /*
                 * sortCategory[DashboardFActivity.categorySelectionPosition]
                 * .getCategoryModels()
                 */
            filterArrayList, tempArrayListCategory!!
        )
        mListFilterContent!!.adapter = adapter
    }

    private fun setSize() {
        val adapter = FilterSizeContentAdapter(
            mActivity!!, sizeArrayList!!, tempArrayListSize!!
        )
        mListFilterContent!!.adapter = adapter
    }

    private fun setBrand() {
        val adapter = FilterBrandContentAdapter(
            mActivity!!, typeArrayList!!, tempArrayListBrand!!
        )
        mListFilterContent!!.adapter = adapter
    }

    private fun setPrice() {
        val adapter = FilterPriceContentAdapter(
            mActivity!!, priceArrayList!!, tempArrayListPrice!!
        )
        mListFilterContent!!.adapter = adapter
    }

    private fun setColor() {
        val adapter = FilterColorContentAdapter(
            mActivity!!, colorArrayList!!, tempArrayListColor!!
        )
        mListFilterContent!!.adapter = adapter
    }

    private fun setOffers() {
        val adapter = FilterOfferAdapter(
            mActivity!!,
            offerModels!!, tempofferModels!!
        )
        mListFilterContent!!.adapter = adapter
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}