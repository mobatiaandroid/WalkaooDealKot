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
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ProductListAdapter
import com.mobatia.vkcsalesapp.appdialogs.SortDialog
import com.mobatia.vkcsalesapp.appdialogs.SortDialog.SortOptionSelectionListener
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getBrandIdForSearch
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataBrand
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataColor
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataSize
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getIDsForOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getListingOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getOfferIDs
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getProductListSortOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSelectedUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getSubCategoryId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
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
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORCODE
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_COLORID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZEID
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_SETTINGS_SIZENAME
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCUtils.checkInternetConnection
import com.mobatia.vkcsalesapp.manager.VKCUtils.hideKeyBoard
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.ColorModel
import com.mobatia.vkcsalesapp.model.ProductImages
import com.mobatia.vkcsalesapp.model.ProductModel
import com.mobatia.vkcsalesapp.model.SizeModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import com.mobatia.vkcsalesapp.ui.activity.FilterActivity
import org.json.JSONObject
import java.util.*

/**
 * @author archana.s
 */
@SuppressLint("NewApi")
class ProductListFragment : Fragment(), VKCUrlConstants // , OnScrollListener
{
    private var listView: ListView? = null
    private var gridProductList: GridView? = null
    var productModels: ArrayList<ProductModel>? = null

    // private ProductModel mProductModel;
    // private ArrayList<ProductModel> mProductList;
    private var mPageNumber = 0
    private var mActivity: Activity? = null
    private var mrootView: View? = null
    private var mRelFilter: RelativeLayout? = null
    private var mRelSortBy: RelativeLayout? = null
    private var mRelList: RelativeLayout? = null
    private var viewFilter: View? = null
    private var viewSortBy: View? = null
    private var viewList: View? = null
    private var relShare: RelativeLayout? = null
    private var imgSearch: ImageView? = null
    private var edtSearch: EditText? = null
    var flag = false
    private var tvList: TextView? = null
    private var imgList: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mrootView = inflater.inflate(R.layout.fragment_productlist, null)
        mActivity = activity
        initialiseUI()

        // System.out.println("05012015:listing option:"+AppPrefenceManager.getListingOption(mActivity));
        saveListType(activity!!, "ProductList")
        return mrootView

        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private fun initialiseUI() {
        listView = mrootView!!.findViewById<View>(R.id.list) as ListView
        gridProductList = mrootView!!.findViewById<View>(R.id.gridProducts) as GridView
        mRelFilter = mrootView!!.findViewById<View>(R.id.relFilter) as RelativeLayout
        mRelSortBy = mrootView!!.findViewById<View>(R.id.relSortBy) as RelativeLayout
        mRelList = mrootView!!.findViewById<View>(R.id.relList) as RelativeLayout
        viewFilter = mrootView!!.findViewById(R.id.viewFilter) as View
        viewSortBy = mrootView!!.findViewById(R.id.viewSortBy) as View
        viewList = mrootView!!.findViewById(R.id.viewList) as View
        tvList = mrootView!!.findViewById<View>(R.id.tvList) as TextView
        imgList = mrootView!!.findViewById<View>(R.id.imgList) as ImageView
        relShare = mrootView!!.findViewById<View>(R.id.relShare) as RelativeLayout
        val relSearchHeader = mrootView!!
            ?.findViewById<View>(R.id.relSearchHeader) as RelativeLayout
        val manager = SearchHeaderManager(activity!!)
        manager.getSearchHeader(relSearchHeader)
        imgSearch = manager.searchImage
        edtSearch = manager.editText
        AppController.isCart = false
        // listView.setOnScrollListener(this);
        /*
		 * listView.setOnScrollListener(new OnScrollListener() {
		 * 
		 * @Override public void onScrollStateChanged(AbsListView view, int
		 * scrollState) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void onScroll(AbsListView view, int
		 * firstVisibleItem, int visibleItemCount, int totalItemCount) { // TODO
		 * Auto-generated method stub System.out.println("Scroll detected");
		 * if(firstVisibleItem + visibleItemCount >= totalItemCount){
		 * getProducts(mPageNumber+1); } } });
		 */manager.searchAction(activity!!, object : SearchActionInterface {
            override fun searchOnTextChange(key: String?) {
                // TODO Auto-generated method stub
                if (edtSearch!!.text.toString() != "") {
                    setFilter(
                        edtSearch!!.text
                            .toString()
                    )
                    hideKeyBoard(mActivity!!)
                }
            }
        }, edtSearch!!.text.toString())
        mPageNumber = 0

        /*
         * if (VKCUtils.checkInternetConnection(getActivity())) {
         * //getProducts(mPageNumber); getProducts(); } else { CustomToast toast
         * = new CustomToast(getActivity()); // toast.show(0);
         * VKCUtils.showtoast(mActivity, 0); }
         */
        // imgSearch.setOnClickListener(new OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        //
        // ProductListFragment.setFilter(edtSearch.getText().toString());
        //
        // }
        // });
        /*
         * gridProductList.setOnItemClickListener(new OnItemClickListener() {
         * 
         * @Override public void onItemClick(AdapterView<?> arg0, View arg1, int
         * pos, long arg3) { // TODO Auto-generated method stub //
         * AppController.selectedProductPosition=pos; } });
         */relShare!!.setOnClickListener { // TODO Auto-generated method stub
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
    }// TODO Auto-generated method stub

    // Log.v("LOG", "08012015 Errror" + failureResponse);
// TODO Auto-generated method stub
    // Log.v("LOG", "12012015 success" + successResponse);
// , String.valueOf(pageNumber)// ,"currentpage"// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
    // .show();
// Toast.makeText(getActivity(), "Data : " + dataCategory, 1000)
    // .show();
// CustomToast toast = new CustomToast(mActivity);
    // toast.show(19);
// DashboardFActivity.categoryId);
    // int pageNumber
    val products: Unit
        get() {

            // DashboardFActivity.categoryId);
            var dataCategory: String = ""
            var dataColor: String = ""
            var dataSize: String = ""
            var dataType: String = ""
            var dataOffer: String = ""
            var custId: String = ""
            var custType: String = ""

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
            dataSize = if (getFilterDataSize(activity!!) != "") ({
                getFilterDataSize(activity!!)
            }).toString() else {
                ""
            }
            dataColor = if (getFilterDataColor(activity!!) != "") ({
                getFilterDataSize(activity!!)
            }).toString() else {
                ""
            }
            dataType = if (getListingOption(activity!!) == "4") ({
                getBrandIdForSearch(activity!!)
            }).toString() else ({
                if (getFilterDataBrand(activity!!) != "") {
                    getFilterDataBrand(activity!!)
                } else {
                    ""
                }
            }).toString()
            dataOffer = if (getFilterDataOffer(activity!!) != "") ({
                getFilterDataOffer(activity!!)
            }).toString() else {
                ""
            }
            if (getUserType(activity!!) == "7") {
                custType = "2"
                custId = getSelectedUserId(activity!!)
            } else if (getUserType(activity!!) == "6") {
                custType = "1"
                custId = getCustomerId(activity!!)
            } else if (getUserType(activity!!) == "4") {
                custType = getCustomerCategory(activity!!)
                custId = getSelectedUserId(activity!!)
            }
            if (getListingOption(mActivity!!) == "1") {
                dataOffer = getOfferIDs(mActivity!!)
            }
            val name = arrayOf(
                "category_id", "color_id", "size_id", "type_id",
                "content", "offer_id", "customerId", "customerType"
            ) // ,"currentpage"
            val values = arrayOf(
                dataCategory, dataColor, dataSize, dataType, "",
                dataOffer, custId, custType
            ) // , String.valueOf(pageNumber)

            val manager = VKCInternetManager(
                VKCUrlConstants.PRODUCT_DETAIL_URL
            )
            manager.getResponsePOST(
                activity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "12012015 success" + successResponse);
                        if (successResponse != null) {
                            parseResponse(successResponse)
                        }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                        // Log.v("LOG", "08012015 Errror" + failureResponse);
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
                productModel.productdescription = jsonObjectZero
                    .optString("productdescription")
                productModel
                    .setmSapId(jsonObjectZero.optString("productSapId"))
                productModel.categoryId = jsonObjectZero
                    .optString("categoryid")
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
                var orderCount = 0
                orderCount = try {
                    jsonObjectZero
                        .optString(JSON_PRODUCT_ORDER).toInt()
                } catch (e: Exception) {
                    0
                }
                productModel.setmProductOrder(orderCount)
                // product_color
                // product_image
                // product_size
                // product_type
                val productColorArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_COLOR)
                val productNewArrivalArray = jsonObjectZero
                    .getJSONArray("new_arrivals")
                val productPendingQtyArray = jsonObjectZero
                    .getJSONArray("pendingQty")
                val productImageArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_IMAGE)
                /*System.out.println("size product image "
						+ productImageArray.length());*/
                val productSizeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_SIZE)
                val productTypeArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_TYPE)
                val productCaseArray = jsonObjectZero
                    .getJSONArray(JSON_PRODUCT_CASE)
                /*ArrayList<PendingQuantityModel> pendingArray = new ArrayList<PendingQuantityModel>();
				for (int i = 0; i < productPendingQtyArray.length(); i++) {

					PendingQuantityModel pendingModel = new PendingQuantityModel();
					JSONObject jsonObject = productColorArray.getJSONObject(i);
					pendingModel.setColor(jsonObject.optString("color"));
					pendingModel.setColorid(jsonObject.optString("colorid"));
					pendingModel.setPendingQty(jsonObject
							.optString("pendingQty"));
					pendingModel.setSize(jsonObject.optString("size"));
					pendingModel.setSizeid(jsonObject.optString("sizeid"));
					pendingArray.add(pendingModel);

				}
				productModel.setmPendingQty(pendingArray);*/

                /*ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
				for (int i = 0; i < productColorArray.length(); i++) {

					ColorModel colorModel = new ColorModel();
					JSONObject jsonObject = productColorArray.getJSONObject(i);
					colorModel.setId(jsonObject
							.optString(JSON_SETTINGS_COLORID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));
					colorModel.setColorName(jsonObject.optString("color_name"));
					colorModels.add(colorModel);

				}
				productModel.setProductColor(colorModels);*/

                // ////////////
                val productImages = ArrayList<ProductImages>()
                for (i in 0 until productImageArray.length()) {
                    val images = ProductImages()
                    val jsonObject = productImageArray.getJSONObject(i)
                    images.id = jsonObject.optString(JSON_SETTINGS_COLORID)
                    images.imageName = (VKCUrlConstants.BASE_URL
                            + jsonObject.optString(JSON_COLOR_IMAGE))
                    images.productName = jsonObject.optString("product_name")
                    val colorModel = ColorModel()
                    colorModel.id = jsonObject.optString(JSON_COLOR_ID)
                    colorModel.colorcode = jsonObject
                        .optString(JSON_SETTINGS_COLORCODE)
                    images.colorModel = colorModel
                    productImages.add(images)
                }
                productModel.productImages = productImages

                /*ArrayList<ProductImages> newArrivals = new ArrayList<ProductImages>();
				for (int i = 0; i < productNewArrivalArray.length(); i++) {

					ProductImages images = new ProductImages();
					JSONObject jsonObject = productNewArrivalArray
							.getJSONObject(i);
					images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
					images.setImageName(BASE_URL
							+ jsonObject.optString(JSON_COLOR_IMAGE));
					images.setProductName(jsonObject.optString("product_name"));
					ColorModel colorModel = new ColorModel();
					colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));
					images.setColorModel(colorModel);
					images.setCatId(jsonObject.optString("categoryid"));
					System.out.println("Cat Id :"
							+ jsonObject.optString("categoryid"));
					newArrivals.add(images);

				}
				productModel.setmNewArrivals(newArrivals);*/
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
                /*ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
				for (int i = 0; i < productTypeArray.length(); i++) {

					BrandTypeModel typeModel = new BrandTypeModel();
					JSONObject jsonObject = productTypeArray.getJSONObject(i);
					typeModel
							.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
					typeModel.setName(jsonObject
							.optString(JSON_SETTINGS_BRANDNAME));
					typeModel.setImgUrl(jsonObject
							.optString(JSON_BRAND_IMAGENAME));

					brandTypeModels.add(typeModel);

				}
				productModel.setProductType(brandTypeModels);*/

                /*ArrayList<CaseModel> caseModels = new ArrayList<CaseModel>();
				for (int i = 0; i < productCaseArray.length(); i++) {

					CaseModel caseModel = new CaseModel();
					JSONObject jsonObject = productCaseArray.getJSONObject(i);
					caseModel.setId(jsonObject.optString(JSON_SETTINGS_CASEID));
					caseModel.setName(jsonObject
							.optString(JSON_SETTINGS_CASENAME));

					caseModels.add(caseModel);

				}
				productModel.setmProductCases(caseModels);*/productModels!!.add(productModel)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setList()
    }

    fun setList() {
        if (productModels!!.size == 0) {
            showtoast(mActivity, 17)
        }
        if (flag == true) {
            gridProductList!!.visibility = View.GONE
            listView!!.visibility = View.VISIBLE
            tvList!!.text = "GRID"
            imgList!!.setImageResource(R.drawable.grid)
            listAdapter = ProductListAdapter(
                activity!!, productModels!!,
                1
            )
            listView!!.adapter = listAdapter
        } else {
            gridProductList!!.visibility = View.VISIBLE
            listView!!.visibility = View.GONE
            tvList!!.text = "LIST"
            imgList!!.setImageResource(R.drawable.list)
            println("23122014:Flag::$flag")
            listAdapter = ProductListAdapter(
                activity!!, productModels!!,
                2
            )
            gridProductList!!.adapter = listAdapter
            gridProductList!!.setSelection(AppController.selectedProductPosition)
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
            } else if (option == "5") {
                listAdapter!!.doSort(5)
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
                    } else if (option == "Most Order") {
                        listAdapter!!.doSort(5)
                        saveProductListSortOption(
                            mActivity!!, "5"
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
        super.onResume()
        edtSearch!!.setText("")
        if (checkInternetConnection(activity!!)) {
            // getProducts(mPageNumber);
            products
        } else {
            val toast = CustomToast(activity!!) // toast.show(0);
            showtoast(mActivity, 0)
        }
    } /*
	 * @Override public void onScroll(AbsListView view, int firstVisibleItem,
	 * int visibleItemCount, int totalItemCount) { // TODO Auto-generated method
	 * stub System.out.println("Moved Scroll"); if (firstVisibleItem +
	 * visibleItemCount >= totalItemCount) { getProducts(mPageNumber + 1); } }
	 * 
	 * @Override public void onScrollStateChanged(AbsListView view, int
	 * scrollState) { // TODO Auto-generated method stub
	 * 
	 * }
	 */

    /*
	 * @SuppressLint("ClickableViewAccessibility")
	 * 
	 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO
	 * Auto-generated method stub int X = (int) event.getX(); int Y = (int)
	 * event.getY(); int eventaction = event.getAction(); switch (eventaction) {
	 * case MotionEvent.ACTION_DOWN: break; case MotionEvent.ACTION_UP:
	 * System.out.println("Moved up");
	 * 
	 * break; } return false; }
	 */
    companion object {
        @JvmStatic
        fun setFilter(key: String?) {
            // Toast.makeText(DashboardFActivity.dashboardFActivity, key,
            // 1000).show();
            listAdapter!!.filter(key!!)
        }

        var listAdapter: ProductListAdapter? = null
    }
}