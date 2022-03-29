/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ProductListAdapter
import com.mobatia.vkcsalesapp.appdialogs.SortDialog
import com.mobatia.vkcsalesapp.appdialogs.SortDialog.SortOptionSelectionListener
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getBrandIdForSearch
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataBrand
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataColor
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataSize
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getIDsForOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getListingOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getProductListSortOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSubCategoryId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveListType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveProductListSortOption
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_COST
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_ID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_CATEGORY_NAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_COLOR_ID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_COLOR_IMAGE
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
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORCODE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCUtils.checkInternetConnection
import com.mobatia.vkcsalesapp.manager.VKCUtils.hideKeyBoard
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.*
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import com.mobatia.vkcsalesapp.ui.activity.FilterActivity
import org.json.JSONObject
import java.util.*

/**
 * Bibin
 */
// Currently not used....WishListFragment
class WishListFragment : Fragment(), VKCUrlConstants {
    private var listView: ListView? = null
    private var gridProductList: GridView? = null
    var productModels: ArrayList<ProductModel>? = null

    // private ProductModel mProductModel;
    // private ArrayList<ProductModel> mProductList;
    private var mActivity: Activity? = null
    private var mRelFilter: RelativeLayout? = null
    private var mRelSortBy: RelativeLayout? = null
    private var mRelList: RelativeLayout? = null
    private var mRootView: View? = null
    private var viewFilter: View? = null
    private var viewSortBy: View? = null
    private var viewList: View? = null
    private var relShare: RelativeLayout? = null
    private var imgSearch: ImageView? = null
    private var edtSearch: EditText? = null
    var flag = true
    private var tvList: TextView? = null
    private var imgList: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_wishlist, null)
        mActivity = activity
        initialiseUI()

        // System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));
        saveListType(activity!!, "ProductList")
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

        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private fun initialiseUI() {
        listView = mRootView!!.findViewById<View>(R.id.list) as ListView
        gridProductList = mRootView!!.findViewById<View>(R.id.gridProducts) as GridView
        mRelFilter = mRootView!!.findViewById<View>(R.id.relFilter) as RelativeLayout
        mRelSortBy = mRootView!!.findViewById<View>(R.id.relSortBy) as RelativeLayout
        mRelList = mRootView!!.findViewById<View>(R.id.relList) as RelativeLayout
        viewFilter = mRootView!!.findViewById(R.id.viewFilter) as View
        viewSortBy = mRootView!!.findViewById(R.id.viewSortBy) as View
        viewList = mRootView!!.findViewById(R.id.viewList) as View
        tvList = mRootView!!.findViewById<View>(R.id.tvList) as TextView
        imgList = mRootView!!.findViewById<View>(R.id.imgList) as ImageView
        relShare = mRootView!!.findViewById<View>(R.id.relShare) as RelativeLayout
        val relSearchHeader = mRootView!!
            ?.findViewById<View>(R.id.relSearchHeader) as RelativeLayout
        val manager = SearchHeaderManager(activity!!)
        manager.getSearchHeader(relSearchHeader)
        imgSearch = manager.searchImage
        edtSearch = manager.editText
        manager.searchAction(activity!!, object : SearchActionInterface {
            override fun searchOnTextChange(key: String?) {
                // TODO Auto-generated method stub
                if (edtSearch!!.text.toString() != "") {
                    ProductListFragment.setFilter(
                        edtSearch!!.text
                            .toString()
                    )
                    hideKeyBoard(mActivity!!)
                }
            }
        }, edtSearch!!.text.toString())
        // imgSearch.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        //
        // ProductListFragment.setFilter(edtSearch.getText().toString());
        //
        // }
        // });
        relShare!!.setOnClickListener { // TODO Auto-generated method stub
            shareIntent("http://vkcgroup.com/")
        }
        mRelFilter!!.setOnClickListener {
            viewFilter!!.visibility = View.VISIBLE
            viewSortBy!!.visibility = View.GONE
            viewList!!.visibility = View.GONE
            val intent = Intent(activity, FilterActivity::class.java)
            startActivity(intent)
        }
        mRelList!!.setOnClickListener { // TODO Auto-generated method stub
            viewFilter!!.visibility = View.GONE
            viewSortBy!!.visibility = View.GONE
            viewList!!.visibility = View.VISIBLE
            flag = if (tvList!!.text == "LIST") {
                true
            } else {
                false
            }
            setList()
        }
        mRelSortBy!!.setOnClickListener {
            viewFilter!!.visibility = View.GONE
            viewSortBy!!.visibility = View.VISIBLE
            viewList!!.visibility = View.GONE
            showDialog("Sort By")
        }
    }

    private fun shareIntent(link: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(
            Intent.createChooser(
                emailIntent, activity
                    ?.getResources()?.getString(R.string.app_name)
            )
        )
    }// TODO Auto-generated method stub// TODO Auto-generated method stub// Toast.makeText(getActivity(), "Brand : " + dataType, 1000)

    // .show();
    // String values[]={dataCategory,"","",""};
// Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
    // .show();
// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
    // .show();
// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
    // .show();
    // CustomToast toast = new CustomToast(mActivity);
    // toast.show(19);
    val products: Unit
        get() {
            var dataCategory: String = ""
            var dataColor: String = ""
            var dataSize: String = ""
            var dataType: String = ""
            var dataOffer: String = ""
            if (getListingOption(activity!!) == "0") {
                dataCategory = DashboardFActivity.categoryId
            } else if (getListingOption(activity!!) ==
                "1"
            ) {
                dataCategory = getIDsForOffer(activity!!)
            } else if (getListingOption(activity!!) ==
                "2"
            ) {
                dataCategory = getFilterDataCategory(activity!!)
                if (dataCategory!!.length == 0) {
                    // CustomToast toast = new CustomToast(mActivity);
                    // toast.show(19);
                    dataCategory = DashboardFActivity.categoryId
                }
            } else if (getListingOption(activity!!) ==
                "4"
            ) {
                dataCategory = getIDsForOffer(activity!!)
                // Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
                // .show();
            } else if (getListingOption(activity!!) ==
                "5"
            ) {
                dataCategory = getSubCategoryId(activity!!)
                // Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
                // .show();
            }
            dataSize = if (getFilterDataSize(activity!!) != "") {
                getFilterDataSize(activity!!)
            } else {
                ""
            }
            dataColor = if (getFilterDataColor(activity!!) != "") {
                getFilterDataSize(activity!!)
            } else {
                ""
            }
            dataType = if (getListingOption(activity!!) == "4") ({
                getBrandIdForSearch(activity!!)
                // Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
                // .show();
            }).toString() else {
                if (getFilterDataBrand(activity!!) != "") {
                    getFilterDataBrand(activity!!)
                } else {
                    ""
                }

                // Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
                // .show();
            }
            dataOffer = if (getFilterDataOffer(activity!!) != "") {
                getFilterDataOffer(activity!!)
            } else {
                ""
            }
            val name = arrayOf(
                "category_id", "color_id", "size_id", "type_id",
                "content", "offer_id"
            )
            val values = arrayOf(
                dataCategory, dataColor, dataSize, dataType, "",
                dataOffer
            )

            val manager = VKCInternetManager(
                VKCUrlConstants.PRODUCT_DETAIL_URL
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        successResponse?.let { parseResponse(it) }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "08012015 Errror$failureResponse")
                    }
                })
        }

    private fun parseResponse(response: String) {
        productModels = ArrayList()
        try {
            val jsonObjectresponse = JSONObject(response)
            val jsonArrayresponse = jsonObjectresponse
                .getJSONArray(JSON_TAG_SETTINGS_RESPONSE)
            for (j in 0 until jsonArrayresponse.length()) {
                val jsonObjectZero = jsonArrayresponse.getJSONObject(j)
                val productModel = ProductModel()
                // categoryname
                // productcost
                // productid
                // productname
                // productquantity
                // productoffer
                productModel.setmSapId(
                    jsonObjectZero
                        .optString("productSapId")
                )
                productModel.categoryId = jsonObjectZero
                    .optString(JSON_CATEGORY_ID)
                productModel.categoryName = jsonObjectZero
                    .optString(JSON_CATEGORY_NAME)
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
                    .optString(JSON_PRODUCT_TIMESTAMP)
                productModel.setmProductOff(
                    jsonObjectZero
                        .optString(JSON_PRODUCT_OFFER)
                )

                // product_color
                // product_image
                // product_size
                // product_type
                val productColorArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_COLOR)
                val productImageArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_IMAGE)
                val productSizeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_SIZE)
                val productTypeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_TYPE)
                val colorModels = ArrayList<ColorModel>()
                for (i in 0 until productColorArray.length()) {
                    val colorModel = ColorModel()
                    val jsonObject = productColorArray.getJSONObject(i)
                    colorModel.id = jsonObject
                        .optString(JSON_SETTINGS_COLORID)
                    colorModel.colorcode = jsonObject
                        .optString(JSON_SETTINGS_COLORCODE)
                    colorModels.add(colorModel)
                }
                productModel.productColor = colorModels

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
                    sizeModel.name = jsonObject
                        .optString(JSON_SETTINGS_SIZENAME)
                    sizeModels.add(sizeModel)
                }
                productModel.setmProductSize(sizeModels)
                // /////
                val brandTypeModels = ArrayList<BrandTypeModel>()
                for (i in 0 until productTypeArray.length()) {
                    val typeModel = BrandTypeModel()
                    val jsonObject = productTypeArray.getJSONObject(i)
                    typeModel.id = jsonObject.optString(JSON_SETTINGS_BRANDID)
                    typeModel.name = jsonObject
                        .optString(JSON_SETTINGS_BRANDNAME)
                    brandTypeModels.add(typeModel)
                }
                productModel.productType = brandTypeModels
                // /////////
                Log.v("LOG", "9122014 " + "productModels.add(productModel)")
                productModels!!.add(productModel)

                // if (j % 2 == 1) {
                // productModels.add(model);
                // }
                // model = productModel;
                // if (j % 2 == 0) {
                // productModels.add(model);
                // }
                // model = productModel;
                // productModels.add(productModel);
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setList()
        /*
		 * type=1;list type=2;grid
		 */
        // if(flag==true){
        //
        // gridProductList.setVisibility(View.GONE);
        // listView.setVisibility(View.VISIBLE);
        // tvList.setText("GRID");
        // System.out.println("23122014:Flag::"+flag);
        // listAdapter = new ProductListAdapter(getActivity(), productModels,1);
        // /*type=1;list
        // type=2;grid*/
        // listView.setAdapter(listAdapter);
        //
        // }else{
        // gridProductList.setVisibility(View.VISIBLE);
        // listView.setVisibility(View.GONE);
        // tvList.setText("LIST");
        // System.out.println("23122014:Flag::"+flag);
        // listAdapter = new ProductListAdapter(getActivity(), productModels,2);
        //
        // gridProductList.setAdapter(listAdapter);
        // }
    }

    fun setList() {
        if (productModels!!.size == 0) {
            // CustomToast toast = new CustomToast(mActivity);
            // toast.show(17);
            showtoast(mActivity, 17)
        }
        if (flag == true) {
            gridProductList!!.visibility = View.GONE
            listView!!.visibility = View.VISIBLE
            tvList!!.text = "GRID"
            imgList!!.setImageResource(R.drawable.grid)
            println("23122014:Flag::$flag")
            listAdapter = ProductListAdapter(
                activity!!, productModels!!,
                1
            )
            /*
			 * type=1;list type=2;grid
			 */listView!!.adapter = listAdapter
        } else {
            gridProductList!!.visibility = View.VISIBLE
            listView!!.visibility = View.GONE
            tvList!!.text = "LIST"
            imgList!!.setImageResource(R.drawable.list)
           // println("23122014:Flag::$flag")
            listAdapter = ProductListAdapter(
                activity!!, productModels!!,
                2
            )
            gridProductList!!.adapter = listAdapter
        }
        if (listAdapter != null) {
            val option = getProductListSortOption(mActivity!!)
            if (option == "0") {
                listAdapter!!.doSort(0)
            } else if (option == "1") {
                listAdapter!!.doSort(1)
            } else if (option == "2") {
                listAdapter!!.doSort(2)
            } else if (option == "3") {
                listAdapter!!.doSort(3)
            } else if (option == "4") {
                listAdapter!!.doSort(4)
            }
        }
    }

    var model: ProductModel? = null
    var sortDialog: SortDialog? = null
    private fun showDialog(str: String) {
        sortDialog = SortDialog(
            activity!!, str,
            object : SortOptionSelectionListener {
                override fun selectedOption(option: String?) {

                    if (option == "Popularity") {
                        listAdapter!!.doSort(0)
                        saveProductListSortOption(
                            mActivity!!, "0"
                        )
                    } else if (option == "Price(Low to High)") {
                        listAdapter!!.doSort(1)
                        saveProductListSortOption(
                            mActivity!!, "1"
                        )
                    } else if (option == "Price(High to Low)") {
                        listAdapter!!.doSort(2)
                        saveProductListSortOption(
                            mActivity!!, "2"
                        )
                    } else if (option == "New Arrivals") {
                        listAdapter!!.doSort(3)
                        saveProductListSortOption(
                            mActivity!!, "3"
                        )
                    } else if (option == "Discount") {
                        listAdapter!!.doSort(4)
                        saveProductListSortOption(
                            mActivity!!, "4"
                        )
                    }
                }
            })
        sortDialog!!.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        sortDialog!!.setCancelable(true)
        sortDialog!!.show()
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        // System.out.println("11122104:"+AppPrefenceManager.getFilterDataCategory(mActivity));
        super.onResume()
        edtSearch!!.setText("")
        if (checkInternetConnection(activity!!)) {
            products
        } else {
            // CustomToast toast = new CustomToast(getActivity());
            // toast.show(0);
            showtoast(activity, 0)
        }
    }

    companion object {
        fun setFilter(key: String?) {
            // Toast.makeText(DashboardFActivity.dashboardFActivity, key,
            // 1000).show();
            if (key != null) {
                listAdapter!!.filter(key)
            }
        }

        var listAdapter: ProductListAdapter? = null
    }
}