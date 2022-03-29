/**
 *
 */
package com.mobatia.vkcsalesapp.ui.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ProductListNew_Adapter
import com.mobatia.vkcsalesapp.appdialogs.SortDialog
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomProgressBar
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManagerWithoutDialog
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ProductModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import com.mobatia.vkcsalesapp.ui.activity.FilterActivity
import org.json.JSONArray
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author archana.s
 */
@SuppressLint("NewApi")
class ProductListNew_Fragment : Fragment(), VKCUrlConstants {
    private val mContainerId = 0
    var pDialog: CustomProgressBar? = null
    private var listView: ListView? = null
    private var gridProductList: GridView? = null
    var productModels: ArrayList<ProductModel> = ArrayList<ProductModel>()
    var fragmenListFragment: ProductListFragment? = null
    private var mActivity: Activity? = null
    var mFragmentTransaction: FragmentTransaction? = null
    private val exitFlag = true
    var total_records = "0"
    var tolal_pages = "0"
    var current_page = "0"
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
    var flag = false
    private var tvList: TextView? = null
    private var imgList: ImageView? = null
    private val count = 0
    var cat_id = ""
    var parent_cat_id = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_productlist, null)
        mActivity = activity
        productModels.clear()
        initialiseUI()
        activity?.let { AppPrefenceManager.saveProductListSortOption(it, "") }
        activity?.let { AppPrefenceManager.saveListType(it, "ProductList") }
        return view
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
        val relSearchHeader: RelativeLayout = mRootView
            ?.findViewById<View>(R.id.relSearchHeader) as RelativeLayout
        relSearchHeader.setVisibility(View.VISIBLE)
        val manager = activity?.let { SearchHeaderManager(it) }
        manager?.getSearchHeader(relSearchHeader)
        imgSearch = manager?.searchImage
        edtSearch = manager?.editText
        AppController.isCart = false
        AppController.isClickedCartAdapter = false
        if (activity?.let { VKCUtils.checkInternetConnection(it) } == true) {
            // getProducts(mPageNumber);
            getProducts(1, "")
            edtSearch?.setText("")
        } else {
            val toast = activity?.let { CustomToast(it) } // toast.show(0);
            VKCUtils.showtoast(mActivity, 0)
        }
        manager?.searchAction(activity!!, object : SearchHeaderManager.SearchActionInterface {
            override fun searchOnTextChange(key: String?) {
                // TODO Auto-generated method stub
                if (edtSearch?.getText().toString() != "") {

                    /*
					 * ProductListFragment.setFilter(edtSearch.getText()
					 * .toString());
					 */
                    /*
					 * getProducts(1, edtSearch.getText().toString());
					 * VKCUtils.hideKeyBoard(mActivity);
					 */
                    listAdapter?.filter(edtSearch?.getText().toString())
                }
            }
        }, edtSearch?.getText().toString())
        relShare?.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
            shareIntent("http://vkcgroup.com/")
        })
        mRelFilter?.setOnClickListener(View.OnClickListener {
            viewFilter!!.visibility = View.VISIBLE
            viewSortBy!!.visibility = View.GONE
            viewList!!.visibility = View.GONE
            val intent = Intent(activity, FilterActivity::class.java)
            startActivity(intent)
        })
        mRelList?.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
            viewFilter!!.visibility = View.GONE
            viewSortBy!!.visibility = View.GONE
            viewList!!.visibility = View.VISIBLE
            flag = if (tvList?.getText() == "LIST") {
                true
            } else {
                false
            }
            setList()
        })
        mRelSortBy?.setOnClickListener(View.OnClickListener {
            viewFilter!!.visibility = View.GONE
            viewSortBy!!.visibility = View.VISIBLE
            viewList!!.visibility = View.GONE
            showDialog("Sort By")
        })
    }

    private fun shareIntent(link: String) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.setType("text/plain")
        emailIntent.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(
            Intent.createChooser(
                emailIntent, activity
                    ?.getResources()?.getString(R.string.app_name)
            )
        )
    }

    private fun getProducts(pageNumber: Int, key: String) // int pageNumber
    {
        var dataCategory = ""
        var dataColor = ""
        var dataSize = ""
        var dataType = ""
        var dataOffer = ""
        var dataTypePrice = ""

        if (activity?.let { AppPrefenceManager.getListingOption(it) } == "0") {
            dataCategory = DashboardFActivity.categoryId
            if (dataCategory == "1" || dataCategory == "2"
                || dataCategory == "3" || dataCategory == "4"
                || dataCategory == "5"
                || dataCategory == "31"
                || dataCategory == "33" || dataCategory == "67"
            ) {
                /*
				 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				 * AppPrefenceManager.setCatId(mActivity, "");
				 */
                parent_cat_id = dataCategory
                cat_id = ""
                /*
				 * AppPrefenceManager.setParentCatId(mActivity, pa);
				 * AppPrefenceManager.setCatId(mActivity, "");
				 */
            } else {
                /*
				 * AppPrefenceManager.setParentCatId(mActivity, "");
				 * AppPrefenceManager.setCatId(mActivity, dataCategory);
				 */
                parent_cat_id = ""
                cat_id = dataCategory
            }
            /*
			 * parent_cat_id=DashboardFActivity.categoryId;
			 * System.out.println("Id---"+8989+parent_cat_id);
			 */
        } else if (activity?.let { AppPrefenceManager.getListingOption(it) } ==
            "1"
        ) {
            dataCategory = AppPrefenceManager.getIDsForOffer(activity!!).toString()
            /*
			 * if(dataCategory.equals("1")||(dataCategory.equals("2"))||
			 * (dataCategory
			 * .equals("3"))||(dataCategory.equals("4"))||(dataCategory
			 * .equals("5"))||
			 * (dataCategory.equals("31"))||(dataCategory.equals(
			 * "33"))||dataCategory.equals("67")){
			 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
			 * AppPrefenceManager.setCatId(mActivity, "");
			 * parent_cat_id=dataCategory; cat_id=""; }else{
			 * AppPrefenceManager.setParentCatId(mActivity, "");
			 * AppPrefenceManager.setCatId(mActivity, dataCategory);
			 * parent_cat_id=""; cat_id=dataCategory; }
			 */parent_cat_id = dataCategory
            cat_id = ""
        } else if (activity?.let { AppPrefenceManager.getListingOption(it) } ==
            "2"
        ) {
            dataCategory = activity?.let {
                AppPrefenceManager
                    .getFilterDataCategory(it).toString()
            }.toString()
            parent_cat_id = activity?.let { AppPrefenceManager.getParentCatId(it).toString() }.toString()
            cat_id = activity?.let { AppPrefenceManager.getCatId(it) }.toString()
            println("$parent_cat_id:demo1:$cat_id")
        } else if (activity?.let { AppPrefenceManager.getListingOption(it) } ==
            "4"
        ) {
            dataCategory = AppPrefenceManager.getIDsForOffer(activity!!).toString()
            parent_cat_id = dataCategory
            cat_id = ""
            /*
			 * if(dataCategory.equals("1")||(dataCategory.equals("2"))||
			 * (dataCategory
			 * .equals("3"))||(dataCategory.equals("4"))||(dataCategory
			 * .equals("5"))||
			 * (dataCategory.equals("31"))||(dataCategory.equals(
			 * "33"))||dataCategory.equals("67")){
			 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
			 * AppPrefenceManager.setCatId(mActivity, "");
			 * parent_cat_id=dataCategory; cat_id=""; }else{
			 * parent_cat_id=dataCategory;
			 * 
			 * } // Toast.makeText(getActivity(), "Data : " + dataCategory,
			 * 1000) // .show();
			 */
        } else if (activity?.let { AppPrefenceManager.getListingOption(it) }
            == "5"
        ) {
            dataCategory = AppPrefenceManager.getSubCategoryId(activity!!).toString()
            if (dataCategory == "1" || dataCategory == "2"
                || dataCategory == "3" || dataCategory == "4"
                || dataCategory == "5"
                || dataCategory == "31"
                || dataCategory == "33" || dataCategory == "67"
            ) {
                /*
				 * AppPrefenceManager.setParentCatId(mActivity, dataCategory);
				 * AppPrefenceManager.setCatId(mActivity, "");
				 */
                parent_cat_id = dataCategory
                cat_id = ""
            } else {
                /*
				 * AppPrefenceManager.setParentCatId(mActivity, "");
				 * AppPrefenceManager.setCatId(mActivity, dataCategory);
				 */
                parent_cat_id = ""
                cat_id = dataCategory
            }
            // Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
            // .show();
        }
        dataSize = if (activity?.let { AppPrefenceManager.getFilterDataSize(it) } != "") ({
            activity?.let { AppPrefenceManager.getFilterDataSize(it) }
        }).toString() else {
            ""
        }
        dataColor = if (activity?.let { AppPrefenceManager.getFilterDataColor(it) } != "") ({
            activity?.let { AppPrefenceManager.getFilterDataColor(it) }
        }).toString() else {
            ""
        }
        dataType = if (activity?.let { AppPrefenceManager.getListingOption(it) } == "4") ({
            AppPrefenceManager.getBrandIdForSearch(activity!!)
            // Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
            // .show();
        }).toString() else ({
            if (activity?.let { AppPrefenceManager.getFilterDataBrand(it) } != "") {
                activity?.let { AppPrefenceManager.getFilterDataBrand(it) }
            } else {
                ""
            }

            // Toast.makeText(getActivity(), "Brand : " + dataType, 1000)
            // .show();
        }).toString()
        println("Datatype-$dataType")
        dataTypePrice = if (activity?.let { AppPrefenceManager.getFilterDataPrice(it) } != "") ({
            activity?.let {
                AppPrefenceManager
                    .getFilterDataPrice(it)
            }
        }).toString() else {
            ""
        }
        if (activity?.let { AppPrefenceManager.getFilterDataOffer(it) } != "") {
            dataOffer = activity?.let { AppPrefenceManager.getFilterDataOffer(it).toString() }.toString()
        } else if (AppPrefenceManager.getOfferIDs(activity!!) != ""
            && (AppPrefenceManager.getFilterDataOffer(activity!!)
                    == "")
        ) {
            dataOffer = AppPrefenceManager.getOfferIDs(activity!!).toString()
            parent_cat_id = ""
            cat_id = ""
        } else {
            dataOffer = ""
        }
        println("parent_cat_id::$parent_cat_id")
        println("cat_id::$cat_id")
        val name = arrayOf(
            "parent_category_id", "category_id", "color_id",
            "size_id", "type_id", "content", "offer_id", "currentpage",
            "price_range"
        )
        val values = arrayOf(
            parent_cat_id, cat_id, dataColor, dataSize,
            dataType, key, dataOffer, pageNumber.toString(),
            dataTypePrice
        ) // ,
        // String.valueOf(pageNumber)
        for (i in name.indices) {
            println("values---" + name[i] + "-" + values[i])
        }
        val manager = VKCInternetManagerWithoutDialog(
            VKCUrlConstants.PRODUCT_DETAIL_NEW_URL
        )
        pDialog = CustomProgressBar(
            activity,
            R.drawable.loading
        )
        pDialog!!.show()
        manager.getResponsePOST(
            activity, name, values,
            object : VKCInternetManagerWithoutDialog.ResponseListenerWithoutDialog {
                override fun responseSuccess(successResponse: String) {
                    // TODO Auto-generated method stub
                    // Log.v("LOG", "12012015 success" + successResponse);
                    parseResponse(successResponse)
                    pDialog!!.dismiss()
                }

                override fun responseFailure(failureResponse: String) {
                    // TODO Auto-generated method stub
                    pDialog!!.dismiss()
                }
            })
    }

    private fun parseResponse(response: String) {
        try {
            val jsonObjectresponse = JSONObject(response)
            total_records = jsonObjectresponse.getString("total_records")
            tolal_pages = jsonObjectresponse.getString("tolal_pages")
            current_page = jsonObjectresponse.getString("current_page")
            val jsonArrayresponse: JSONArray = jsonObjectresponse
                .getJSONArray(JSON_TAG_SETTINGS_RESPONSE)
            // /productModels=new ArrayList<ProductNewModel>();
            // productModels.clear();
            val tempProductArrayList: ArrayList<ProductModel> = ArrayList<ProductModel>()
            if (jsonArrayresponse.length() > 0) {
                for (j in 0 until jsonArrayresponse.length()) {
                    val obj: JSONObject = jsonArrayresponse.getJSONObject(j)
                    val model = ProductModel()
                    model.setmProductName(obj.getString("name"))
                    model.id = obj.getString("id")
                    val format = SimpleDateFormat(
                        "yyyy-MM-dd HH:mm:ss"
                    )
                    try {
                        val date = format.parse(obj.getString("timestamp"))
                        println("converted date=-$date")
                        model.timeStampP = date
                    } catch (e: ParseException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    model.productDescription = obj.getString("description")
                    model.parent_category = obj.getString("parent_category")
                    model.categoryId = obj.getString("category_id")
                    model.type = obj.getString("type")
                    model.size = obj.getString("size")
                    model.productViews = obj.getString("views")
                    model.setmProductPrize(obj.getString("cost"))
                    model.setmProductOff(obj.getString("offer"))
                    model.image_name = obj.getString("image_name")
                    model.productquantity = obj.getString("orderqty")
                    productModels.add(model)
                }
            } else {
                VKCUtils.showtoast(mActivity, 17)
                productModels.clear()
            }

            // productModels.addAll(tempProductArrayList);
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setList()
    }

    fun setList() {
        if (productModels.size == 0) {
            // CustomToast toast = new CustomToast(mActivity);
            // toast.show(17);
            VKCUtils.showtoast(mActivity, 17)
        }
        if (flag == true) {
            gridProductList?.setVisibility(View.GONE)
            listView!!.visibility = View.VISIBLE
            tvList?.setText("GRID")
            imgList!!.setImageResource(R.drawable.grid)
            val currentPage = current_page.toInt()
            val totalPage = tolal_pages.toInt()
            listAdapter = activity?.let {
                ProductListNew_Adapter(
                    it,
                    productModels, 1, currentPage, totalPage,
                    object : ProductListNew_Adapter.ScrollingAdapterinterface {
                        override fun calledInterface(position: Int) {
                            // TODO Auto-generated method stub
                            if (currentPage == totalPage) {
                            } else {
                                getProducts(position + 1, "")
                            }
                            // pos=2*position-1;
                        }
                    })
            }
            listAdapter?.notifyDataSetChanged()
            listView!!.adapter = listAdapter
            if (currentPage == 1) {
                listView!!.setSelection(0)
            } else {
                listView!!.setSelection((currentPage - 1) * 20)
            }
        } else {
            gridProductList?.setVisibility(View.VISIBLE)
            listView!!.visibility = View.GONE
            tvList?.setText("LIST")
            imgList!!.setImageResource(R.drawable.list)
            println("23122014:Flag::$flag")
            /*
			 * listAdapter = new ProductListAdapterNew(getActivity(),
			 * productModels, 2);
			 * 
			 * gridProductList.setAdapter(listAdapter);
			 * gridProductList.setSelection
			 * (AppController.selectedProductPosition);
			 */
            val currentPage = current_page.toInt()
            val totalPage = tolal_pages.toInt()
            listAdapter = activity?.let {
                ProductListNew_Adapter(
                    it,
                    productModels, 2, currentPage, totalPage,
                    object : ProductListNew_Adapter.ScrollingAdapterinterface {
                        override fun calledInterface(position: Int) {
                            // TODO Auto-generated method stub
                            if (currentPage == totalPage) {
                            } else {
                                getProducts(position + 1, "")
                            }
                            // pos=2*position-1;
                        }
                    })
            }
            listAdapter?.notifyDataSetChanged()
            gridProductList?.setAdapter(listAdapter)
            if (currentPage == 1) {
                gridProductList?.setSelection(0)
            } else {
                gridProductList?.setSelection((currentPage - 1) * 20)
            }
        }
        if (listAdapter != null) {
            val option: String? = mActivity?.let {
                AppPrefenceManager
                    .getProductListSortOption(it)
            }
            if (option == "0") {
                listAdapter?.doSort(0)
            } else if (option == "1") {
                listAdapter?.doSort(1)
            } else if (option == "2") {
                listAdapter?.doSort(2)
            } else if (option == "3") {
                listAdapter?.doSort(3)
            } else if (option == "4") {
                listAdapter?.doSort(4)
            } else if (option == "5") {
                listAdapter?.doSort(5)
            }
        }
    }

    var model: ProductModel? = null
    var sortDialog: SortDialog? = null
    private fun showDialog(str: String) {
        sortDialog = SortDialog(
            activity!!, str,
            object : SortDialog.SortOptionSelectionListener {
                override fun selectedOption(option: String?) {
                    // TODO Auto-generated method stub
                    if (option == "Popularity") {
                        listAdapter?.doSort(0)
                        activity?.let {
                            AppPrefenceManager.saveProductListSortOption(
                                it, "0"
                            )
                        }
                    } else if (option == "Price(Low to High)") {
                        listAdapter?.doSort(1)
                        activity?.let {
                            AppPrefenceManager.saveProductListSortOption(
                                it, "1"
                            )
                        }
                    } else if (option == "Price(High to Low)") {
                        listAdapter?.doSort(2)
                        activity?.let {
                            AppPrefenceManager.saveProductListSortOption(
                                it, "2"
                            )
                        }
                    } else if (option == "New Arrivals") {
                        listAdapter?.doSort(3)
                        activity?.let {
                            AppPrefenceManager.saveProductListSortOption(
                                it, "3"
                            )
                        }
                    } else if (option == "Discount") {
                        listAdapter?.doSort(4)
                        activity?.let {
                            AppPrefenceManager.saveProductListSortOption(
                                it, "4"
                            )
                        }
                    } else if (option == "Most Order") {
                        listAdapter?.doSort(5)
                        activity?.let {
                            AppPrefenceManager.saveProductListSortOption(
                                it, "5"
                            )
                        }
                    }
                }
            })
        sortDialog?.getWindow()?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        sortDialog?.setCancelable(true)
        sortDialog?.show()
    }

    /*
	 * @Override public void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); edtSearch.setText(""); if
	 * (VKCUtils.checkInternetConnection(getActivity())) { getProducts(); } else
	 * { // CustomToast toast = new CustomToast(getActivity()); //
	 * toast.show(0); VKCUtils.showtoast(mActivity, 0); } }
	 */
    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
        // edtSearch.setText("");
        //productModels.clear();

        /*if (VKCUtils.checkInternetConnection(getActivity())) {
			// getProducts(mPageNumber);
			getProducts(1, "");
			edtSearch.setText("");
		} else {
			CustomToast toast = new CustomToast(getActivity()); // toast.show(0);
			VKCUtils.showtoast(mActivity, 0);
		}*/
    }

    override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy()
        if (pDialog?.isShowing()!!) {
            pDialog?.dismiss()
        }
    }

    companion object {
        fun setFilter(key: String?) {
            // Toast.makeText(DashboardFActivity.dashboardFActivity, key,
            // 1000).show();
            if (key != null) {
                listAdapter?.filter(key)
            }
        }

        var listAdapter: ProductListNew_Adapter? = null
    }
}