package com.mobatia.vkcsalesapp.ui.activity

import android.Manifest.permission
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getLoginStatusFlag
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveBrandBannerResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveJsonColorResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveJsonOfferResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveJsonPriceResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveJsonSizeResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveJsonTypeResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveMainCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveNewArrivalBannerResponse
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.savePopularProductSliderResponse
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_BRANDID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_BRANDNAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_CATEGORYID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_CATEGORYNAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORCODE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRICEFROM
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRICEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRICETO
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_PRODUCTID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_ARRIVALS
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_BRAND
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_CATEGORY
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_COLOR
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_OFFER
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_POPULAR
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_PRICE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_SIZE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_TYPE
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SplashActivity : AppCompatActivity(), VKCUrlConstants,
    VKCDbConstants {
    // Splash screen timer
    val SPLASH_TIME_OUT = 3000
    var typeArrayList: ArrayList<BrandTypeModel?>? = null
    var sizeArrayList: ArrayList<SizeModel?>? = null
    var colorArrayList: ArrayList<ColorModel?>? = null
    var priceArrayList: ArrayList<PriceModel?>? = null
    var categoryArrayList: ArrayList<CategoryModel>? = null
    val googleregserviceintent: Intent? = null

    //private String deviceId = "";
    val PERMISSION_REQUEST_CODE = 200
    var mActivity: Activity? = null
    var mIsError = false
    var bundle: Bundle? = null
    var type: String? = null
    var message: String? = null
    val view: View? = null

    companion object {
        lateinit var sortCategoryGlobal: SortCategory

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mActivity = this
        initUI()
        AppController.cartArrayListSelected.clear()

        if (checkPermission()) {
            if (VKCUtils.checkInternetConnection(mActivity as SplashActivity)) {
                getSettingsApi()
                //startService();
            } else {
                mIsError = true
                // CustomToast toast = new CustomToast(mActivity);
                // toast.show(0);
                VKCUtils.showtoast(mActivity, 0)
            }
        } else {
            VKCUtils.showtoast(mActivity, 59)
            requestPermission()
        }

    }

    /*******************************************************
     * Method name : showPushDialog Description : show Push notification message
     * in Dialog Parameters : message Return type : void Date : 29-May-2015
     * Author : Vandana Surendranath
     */


    fun initUI() {
        val splashImageView = findViewById(R.id.splashImageView) as ImageView
        splashImageView.scaleType = ScaleType.CENTER_CROP
    }


    fun getSettingsApi() {
        //  System.out.println("settings api");
        val manager = VKCInternetManager(VKCUrlConstants.SETTINGS_URL)
        //  Log.v("LOG", "04122014 CACHE " + manager.getResponseCache());
        manager.getResponse(this, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                // TODO Auto-generated method stub
                // Log.v("LOG", "26122014 " + successResponse);
                intiArray()
                // successResponse
                // =VKCUtils.getrespnse(VKCSplashActivity.this,"settings");
                if (successResponse != null) {
                    parseJSON(successResponse)
                }
                // goTODashBoard();
            }

            override fun responseFailure(failureResponse: String?) {
                // TODO Auto-generated method stub
                // Log.v("LOG", "04122014FAIL " + failureResponse);
                mIsError = true
                goToHome(mIsError)
                // intiArray();
                // parseJSON(manager.getResponseCache());
                // goTODashBoard();
            }
        })
    }

    open fun goToHome(flag: Boolean) {
        if (!flag) {
            loadSplash()
        } else {
            // CustomToast toast = new CustomToast(mActivity);
            // toast.show(0);
            VKCUtils.showtoast(mActivity, 0)
            finish()
        }
        // CustomToast toast = new CustomToast(mActivity);
        // toast.show(0);
        // finish();
    }

    open fun intiArray() {
        typeArrayList = ArrayList()
        categoryArrayList = ArrayList()
        sizeArrayList = ArrayList()
        colorArrayList = ArrayList()
        priceArrayList = ArrayList()
    }

    open fun parseJSON(successResponse: String) {
        try {

            // System.out.println("28122014 " + successResponse);
            val jsonObject = JSONObject(successResponse)
            val jsonArray = jsonObject
                .getJSONArray(JSON_TAG_SETTINGS_RESPONSE)
            val objResponse = jsonArray.getJSONObject(0)
            val categoryObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_CATEGORY)
            saveMainCategory(
                this@SplashActivity,
                categoryObjArray.toString()
            )
            for (i in 0 until categoryObjArray.length()) {
                val responseObj = categoryObjArray.getJSONObject(i)
                getCategoryObjectValues(responseObj)?.let { categoryArrayList!!.add(it) }
            }
            val offerObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_OFFER)
            saveJsonOfferResponse(
                this@SplashActivity,
                offerObjArray
            )
            val typeObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_TYPE)
            saveJsonTypeResponse(
                this@SplashActivity,
                typeObjArray
            )
            for (i in 0 until typeObjArray.length()) {
                val responseObj = typeObjArray.getJSONObject(i)
                typeArrayList!!.add(getTypeObjectValues(responseObj))
            }
            val sizeObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_SIZE)
            saveJsonSizeResponse(
                this@SplashActivity,
                sizeObjArray
            )
            for (i in 0 until sizeObjArray.length()) {
                val responseObj = sizeObjArray.getJSONObject(i)
                sizeArrayList!!.add(getSizeObjectValues(responseObj))
            }
            /*  for (int i = 0; i < sizeArrayList.size(); i++) {
                Log.v("05122014", "LOG " + sizeArrayList.get(i).getName());
            }
*/
            val colorObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_COLOR)
            saveJsonColorResponse(
                this@SplashActivity,
                colorObjArray
            )
            for (i in 0 until colorObjArray.length()) {
                val responseObj = colorObjArray.getJSONObject(i)
                colorArrayList!!.add(getColorObjectValues(responseObj))
            }
            /*   for (int i = 0; i < colorArrayList.size(); i++) {
                Log.v("05122014", "LOG " + colorArrayList.get(i).getColorcode());
            }*/
            val priceObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_PRICE)
            saveJsonPriceResponse(
                this@SplashActivity,
                priceObjArray
            )
            for (i in 0 until priceObjArray.length()) {
                val responseObj = priceObjArray.getJSONObject(i)
                priceArrayList!!.add(getPriceObjectValues(responseObj))
            }
            /*  for (int i = 0; i < priceArrayList.size(); i++) {
                Log.v("05122014", "LOG "
                        + priceArrayList.get(i).getFrom_range() + ","
                        + priceArrayList.get(i).getTo_range());
            }
*/
            val bannerObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_ARRIVALS)
            saveNewArrivalBannerResponse(
                this@SplashActivity, bannerObjArray.toString()
            )

            // JSONArray topSliderObjArray = objResponse
            // .getJSONArray("top_slider");
            // AppPrefenceManager.saveTopSliderResponse(VKCSplashActivity.this,
            // topSliderObjArray.toString());
            val bottomSliderObjArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_POPULAR)
            /*  System.out.println("28122014 bottomSliderObjArray"
                    + bottomSliderObjArray.toString());*/savePopularProductSliderResponse(
                this@SplashActivity, bottomSliderObjArray.toString()
            )

            // brand banner
            val brandBannerArray = objResponse
                .getJSONArray(JSON_TAG_SETTINGS_BRAND)
            saveBrandBannerResponse(
                this@SplashActivity,
                brandBannerArray.toString()
            )

            /* Log.v("LOG",
                    "20141228 BrandBannerResponse"
                            + brandBannerArray.toString());*/
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            mIsError = true
        }

        goToHome(mIsError)

    }

    @Throws(JSONException::class)
    fun getCategoryObjectValues(jobject: JSONObject): CategoryModel? {
        val categoryModel = CategoryModel()
        categoryModel.id = jobject.getString(JSON_SETTINGS_CATEGORYID)
        categoryModel.name = jobject.getString(JSON_SETTINGS_CATEGORYNAME)
        categoryModel.parentId = jobject.getString(JSON_SETTINGS_PRODUCTID)
        return categoryModel
    }

    @Throws(JSONException::class)
    fun getTypeObjectValues(jobject: JSONObject): BrandTypeModel? {
        val brandModel = BrandTypeModel()
        brandModel.id = jobject.getString(JSON_SETTINGS_BRANDID)
        brandModel.name = jobject.getString(JSON_SETTINGS_BRANDNAME)
        return brandModel
    }

    @Throws(JSONException::class)
    fun getSizeObjectValues(jobject: JSONObject): SizeModel? {
        val sizeModel = SizeModel()
        sizeModel.id = jobject.getString(JSON_SETTINGS_SIZEID)
        sizeModel.name = jobject.getString(JSON_SETTINGS_SIZENAME)
        return sizeModel
    }

    @Throws(JSONException::class)
    fun getColorObjectValues(jobject: JSONObject): ColorModel? {
        val colorModel = ColorModel()
        colorModel.id = jobject.getString(JSON_SETTINGS_COLORID)
        colorModel.colorcode = jobject.getString(JSON_SETTINGS_COLORCODE)
        return colorModel
    }

    @Throws(JSONException::class)
    fun getPriceObjectValues(jobject: JSONObject): PriceModel? {
        val priceModel = PriceModel()
        priceModel.id = jobject.getString(JSON_SETTINGS_PRICEID)
        priceModel.from_range = jobject.getString(JSON_SETTINGS_PRICEFROM)
        priceModel.to_range = jobject.getString(JSON_SETTINGS_PRICETO)
        return priceModel
    }

    open fun getCategoryNameList(): Array<String?> {
        val stringsList = ArrayList<String>()
        for (categoryModel in categoryArrayList!!) {
            if (categoryModel.parentId == "0") {
                stringsList.add(categoryModel.name)
            }
        }
        val strings = arrayOfNulls<String>(stringsList.size)
        for (i in stringsList.indices) {
            strings[i] = stringsList[i]
        }
        return strings
    }

    open fun getCategoryIdList(): Array<String?> {
        val stringsList = ArrayList<String>()
        for (categoryModel in categoryArrayList!!) {
            if (categoryModel.parentId == "0") {
                stringsList.add(categoryModel.id)
            }
        }
        val strings = arrayOfNulls<String>(stringsList.size)
        for (i in stringsList.indices) {
            strings[i] = stringsList[i]
        }
        return strings
    }

    open fun sortCategory() {
        val mainCategoryName = getCategoryNameList()
        val mainCategoryId = getCategoryIdList()
        val category = arrayOfNulls<SortCategory>(mainCategoryId.size)
        sortCategoryGlobal = SortCategory()
        for (i in category.indices) {
            val categoryModels = ArrayList<CategoryModel>()
            for (categoryModel in categoryArrayList!!) {
                if (categoryModel.parentId == mainCategoryId[i]) {
                    categoryModels.add(categoryModel)
                }
            }
            category[i] = SortCategory()
            category[i]!!.categoryModels = categoryModels
        }
        sortCategoryGlobal!!.sortCategorys = category
    }


    open fun loadSplash() {
        Handler().postDelayed({
            if (getLoginStatusFlag(mActivity!!) == "") {
                AppController.navMenuTitles = getCategoryNameList()
                AppController.categoryIdList = getCategoryIdList()
                val i = Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
                i.putExtra("MAINCATEGORYNAMELIST", getCategoryNameList())
                i.putExtra("MAINCATEGORYIDLIST", getCategoryIdList())
                sortCategory()
                startActivity(i)
                finish()
            } else if (getLoginStatusFlag(mActivity!!)
                == "true"
            ) {
                AppController.navMenuTitles = getCategoryNameList()
                AppController.categoryIdList = getCategoryIdList()
                AppController.userType = getUserType(mActivity!!)
                val mIntent = Intent(
                    this@SplashActivity,
                    DashboardFActivity::class.java
                )
                mIntent.putExtra(
                    "MAINCATEGORYNAMELIST",
                    getCategoryNameList()
                )
                mIntent.putExtra("MAINCATEGORYIDLIST", getCategoryIdList())
                mIntent.putExtra(
                    "USERTYPE",
                    getUserType(mActivity!!)
                )
                sortCategory()
                startActivity(mIntent)
                finish()
            } else {
                AppController.navMenuTitles = getCategoryNameList()
                AppController.categoryIdList = getCategoryIdList()
                val i = Intent(
                    this@SplashActivity,
                    LoginActivity::class.java
                )
                i.putExtra("MAINCATEGORYNAMELIST", getCategoryNameList())
                i.putExtra("MAINCATEGORYIDLIST", getCategoryIdList())
                sortCategory()
                startActivity(i)
                finish()
            }
        }, SPLASH_TIME_OUT.toLong())
    }

    open fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(mActivity!!, permission.READ_PHONE_STATE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    open fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                permission.READ_PHONE_STATE,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    /*    fun onRequestPermissionsResult(
           requestCode: Int,
           permissions: Array<String?>?,
           grantResults: IntArray
       ) {
           when (requestCode) {
               PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                   val phoneAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                   val readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                   val writeAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED
                   if (phoneAccepted) {
                       if (VKCUtils.checkInternetConnection(this@SplashActivity)) {
                           getSettingsApi()
                           // startService();
                       } else {
                           mIsError = true
                           VKCUtils.showtoast(mActivity, 0)
                       }
                   } else {
                       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                           if (shouldShowRequestPermissionRationale(permission.READ_PHONE_STATE)) {
                               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                   requestPermissions(
                                       arrayOf(
                                           permission.READ_PHONE_STATE,
                                           permission.READ_EXTERNAL_STORAGE,
                                           permission.WRITE_EXTERNAL_STORAGE
                                       ),
                                       PERMISSION_REQUEST_CODE
                                   )
                               }
                           } else {
                               requestPermission()
                           }
                       } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                               this@SplashActivity,
                               permission.READ_PHONE_STATE
                           )
                       ) {
                           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                               ActivityCompat.requestPermissions(
                                   this@SplashActivity, arrayOf(
                                       permission.READ_PHONE_STATE,
                                       permission.READ_EXTERNAL_STORAGE,
                                       permission.WRITE_EXTERNAL_STORAGE
                                   ),
                                   PERMISSION_REQUEST_CODE
                               )
                           }
                       } else {
                           val i = Intent(
                               Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                               Uri.parse("package:" + BuildConfig.APPLICATION_ID)
                           )
                           startActivity(i)
                       }
                   }
               }
           }
       }*/


}
