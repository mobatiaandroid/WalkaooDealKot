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
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ProductListNew_Adapter
import com.mobatia.vkcsalesapp.adapter.ProductListNew_Adapter.ScrollingAdapterinterface
import com.mobatia.vkcsalesapp.appdialogs.SortDialog
import com.mobatia.vkcsalesapp.appdialogs.SortDialog.SortOptionSelectionListener
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataBrand
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataColor
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getFilterDataSize
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getIDsForOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getListingOption
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveListType
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager
import com.mobatia.vkcsalesapp.manager.SearchHeaderManager.SearchActionInterface
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCJsonTagConstants.JSON_TAG_SETTINGS_RESPONSE
import com.mobatia.vkcsalesapp.manager.VKCUtils.checkInternetConnection
import com.mobatia.vkcsalesapp.manager.VKCUtils.hideKeyBoard
import com.mobatia.vkcsalesapp.manager.VKCUtils.showtoast
import com.mobatia.vkcsalesapp.model.ProductModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import com.mobatia.vkcsalesapp.ui.activity.FilterActivity
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author archana.s
 */
@SuppressLint("NewApi")
class SearchListFragment : Fragment(), VKCUrlConstants {
    private var mRelFilter: RelativeLayout? = null
    private var mRelSortBy: RelativeLayout? = null
    private var mRelList: RelativeLayout? = null
    private var mRootView: View? = null
    private var viewList: View? = null
    private var viewFilter: View? = null
    private var viewSortBy: View? = null
    private var relShare: RelativeLayout? = null
    private var imgSearch: ImageView? = null
    var mLabel: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(R.layout.fragment_productlist, null)
        mActivity = activity
        productModels!!.clear()
        initialiseUI()
        saveListType(activity!!, "SearchList")
        return mRootView

        // return super.onCreateView(inflater, container, savedInstanceState);
    }

    private fun initialiseUI() {
        listView = mRootView!!.findViewById<View>(R.id.list) as ListView
        gridProductList = mRootView!!.findViewById<View>(R.id.gridProducts) as GridView
        mRelFilter = mRootView!!.findViewById<View>(R.id.relFilter) as RelativeLayout
        //mRelFilter.setVisibility(View.GONE);
        mRelSortBy = mRootView!!.findViewById<View>(R.id.relSortBy) as RelativeLayout
        viewList = mRootView!!.findViewById(R.id.viewList) as View
        mRelList = mRootView!!.findViewById<View>(R.id.relList) as RelativeLayout
        tvList = mRootView!!.findViewById<View>(R.id.tvList) as TextView
        imgList = mRootView!!.findViewById<View>(R.id.imgList) as ImageView
        viewFilter = mRootView!!.findViewById(R.id.viewFilter) as View
        viewSortBy = mRootView!!.findViewById(R.id.viewSortBy) as View
        relShare = mRootView!!.findViewById<View>(R.id.relShare) as RelativeLayout
        val relSearchHeader = mRootView
            ?.findViewById<View>(R.id.relSearchHeader) as RelativeLayout
        val manager = SearchHeaderManager(activity!!)
        manager.getSearchHeader(relSearchHeader)
        imgSearch = manager.searchImage
        edtSearch = manager.editText
        relShare!!.visibility = View.GONE
        manager.searchAction(activity!!, object : SearchActionInterface {
            override fun searchOnTextChange(key: String?) {

                // TODO Auto-generated method stub
                if (edtSearch!!.text.toString() != "") {
                    /*	AppPrefenceManager.saveFilterDataBrand(mActivity, "");
					AppPrefenceManager.saveFilterDataColor(mActivity, "");
					AppPrefenceManager.saveFilterDataOffer(mActivity, "");
					AppPrefenceManager.saveFilterDataPrice(mActivity, "");
					AppPrefenceManager.saveFilterDataSize(mActivity, "");
					AppPrefenceManager.setCatId(mActivity, "");
					AppPrefenceManager.setParentCatId(mActivity, "");*/
                    getSearchAPI(edtSearch!!.text.toString())
                    hideKeyBoard(mActivity!!)
                }
            }
        }, edtSearch!!.text.toString())
        relShare!!.setOnClickListener { // TODO Auto-generated method stub
            shareIntent("http://vkcgroup.com/")
        }
        mRelFilter!!.setOnClickListener {
            viewFilter!!.visibility = View.VISIBLE
            viewSortBy!!.visibility = View.GONE
            val intent = Intent(activity, FilterActivity::class.java)
            startActivity(intent)
        }
        mRelSortBy!!.setOnClickListener {
            viewFilter!!.visibility = View.GONE
            viewSortBy!!.visibility = View.VISIBLE
            showDialog("Sort By")
        }
        mRelList!!.setOnClickListener { // TODO Auto-generated method stub
            viewFilter!!.visibility = View.GONE
            viewSortBy!!.visibility = View.GONE
            viewList!!.visibility = View.VISIBLE
            if (tvList!!.text == "LIST") {
                flag = true
            } else {
                flag = false
            }
            if (productModels != null) {
                setList()
            } else {
            }
        }
        // getProducts();
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
    }

    var model: ProductModel? = null
    var sortDialog: SortDialog? = null

    private fun showDialog(str: String) {
        sortDialog = SortDialog(
            activity!!, str,
            object : SortOptionSelectionListener {
                override fun selectedOption(option: String?) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(getActivity(),option,
                    // Toast.LENGTH_SHORT).show();
                    if (listAdapter != null) {
                        if (option == "Popularity") {
                            listAdapter!!.doSort(0)
                        } else if (option == "Price(Low to High)") {
                            listAdapter!!.doSort(1)
                        } else if (option == "Price(High to Low)") {
                            listAdapter!!.doSort(2)
                        } else if (option == "New Arrivals") {
                            listAdapter!!.doSort(3)
                        } else if (option == "Discount") {
                            listAdapter!!.doSort(4)
                        }
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
        productModels!!.clear()
        /*if((AppController.flagForFilterApply.equals("1"))){
			productModels.clear();
		}*/if (DashboardFActivity.dashboardFActivity.key != "") {
            edtSearch!!.setText(DashboardFActivity.dashboardFActivity.key)
            getSearchAPI(edtSearch!!.text.toString())
            DashboardFActivity.dashboardFActivity.key = ""
        } else {
            /*AppPrefenceManager.saveFilterDataBrand(mActivity, "");
			AppPrefenceManager.saveFilterDataColor(mActivity, "");
			AppPrefenceManager.saveFilterDataOffer(mActivity, "");
			AppPrefenceManager.saveFilterDataPrice(mActivity, "");
			AppPrefenceManager.saveFilterDataSize(mActivity, "");*/
            getSearchAPI(edtSearch!!.text.toString())
            edtSearch!!.setText("")
        }
    }

    companion object {
        private var listView: ListView? = null
        var productModels: ArrayList<ProductModel>? = ArrayList()
        var total_records = "0"
        var tolal_pages = "0"
        var current_page = "0"

        // private ProductModel mProductModel;
        // private ArrayList<ProductModel> mProductList;
        private var mActivity: Activity? = null
        private var gridProductList: GridView? = null
        var flag = false
        private var tvList: TextView? = null
        private var imgList: ImageView? = null
        private var edtSearch: EditText? = null
        fun getSearchAPI(strKey: String) {
            Log.v("LOG", "15122014 getSearchAPI $strKey")
            if (checkInternetConnection(mActivity!!)) {
                productModels!!.clear()
                // getSearchProducts(strKey);
                getProducts(strKey, "1")
            } else {
                val toast = CustomToast(mActivity!!)
                toast.show(0)
            }
            // GetProductSearch getProductSearch=new GetProductSearch(strKey);
            // getProductSearch.execute();
        }

        //	private static void getSearchProducts(String key) {
        //
        //		String name[] = { "content" };
        //		String values[] = { key };
        //
        //		final VKCInternetManager manager = new VKCInternetManager(
        //				SEARCH_PRODUCT_URL);
        //
        //		manager.getResponsePOST(mActivity, name, values,
        //				new ResponseListener() {
        //
        //					@Override
        //					public void responseSuccess(String successResponse) {
        //						// TODO Auto-generated method stub
        //						Log.v("LOG", "12122014 success" + successResponse);
        //						parseResponse(successResponse);
        //
        //					}
        //
        //					@Override
        //					public void responseFailure(String failureResponse) {
        //						// TODO Auto-generated method stub
        //						Log.v("LOG", "9122014 Errror" + failureResponse);
        //					}
        //				});
        //	}
        private fun getProducts(key: String, pageno: String) {
            Log.v("LOG", "9122014 " + "getProducts" + DashboardFActivity.categoryId)
            var dataCategory: String = ""
            var dataColor: String = ""
            var dataSize: String = ""
            var dataType: String = ""
            var dataOffer: String = ""

            if (getListingOption(mActivity!!) == "0") {
                dataCategory = DashboardFActivity.categoryId
                //Toast.makeText(mActivity, "if listingoption:0"+DashboardFActivity.categoryId, 1000).show();
            } else if (getListingOption(mActivity!!) == "3") {
                dataCategory = getIDsForOffer(mActivity!!)
                //Toast.makeText(mActivity, "if listingoption:1"+DashboardFActivity.categoryId, 1000).show();
            } else if (getListingOption(mActivity!!) == "2") {
                dataCategory = getFilterDataCategory(mActivity!!)

                //Toast.makeText(mActivity, "if listingoption:2"+DashboardFActivity.categoryId, 1000).show();
            }
            dataSize = if (getFilterDataSize(mActivity!!) != "") {
                getFilterDataSize(mActivity!!)
            } else {
                ""
            }
            dataColor = if (getFilterDataColor(mActivity!!) != "") {
                getFilterDataSize(mActivity!!)
            } else {
                ""
            }
            dataType = if (getFilterDataBrand(mActivity!!) != "") {
                getFilterDataBrand(mActivity!!)
            } else {
                ""
            }
            dataOffer = if (getFilterDataOffer(mActivity!!) != "") {
                getFilterDataOffer(mActivity!!)
            } else {
                ""
            }

            /*String name[] = { "category_id", "color_id", "size_id", "type_id",
				"content", "offer_id" };
		String values[] = { dataCategory, dataColor, dataSize, dataType, key,
				dataOffer };*/
            val name = arrayOf(
                "parent_category_id", "category_id", "color_id", "size_id", "type_id",
                "content", "offer_id",
                "currentpage"
            )
            val values = arrayOf(
                "", "", dataColor, dataSize, dataType, key,
                dataOffer, pageno
            )

            // String values[]={dataCategory,"","",""};
            val manager = VKCInternetManager(
                VKCUrlConstants.PRODUCT_DETAIL_NEW_URL
            )

            manager.getResponsePOST(
                mActivity, name, values,
                object : ResponseListener {
                    override fun responseSuccess(successResponse: String?) {
                        // TODO Auto-generated method stub
                        if (successResponse != null) {
                            parseResponse(successResponse)
                        }
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                        Log.v("LOG", "9122014 Errror$failureResponse")
                    }
                })
        }

        /*@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		productModels = null;

	}*/
        /*private static void parseResponse(String response) {

		productModels = new ArrayList<ProductModel>();
		try {
			JSONObject jsonObjectresponse = new JSONObject(response);
			JSONArray jsonArrayresponse = jsonObjectresponse
					.getJSONArray(JSON_TAG_SETTINGS_RESPONSE);

			for (int j = 0; j < jsonArrayresponse.length(); j++) {

				JSONObject jsonObjectZero = jsonArrayresponse.getJSONObject(j);
				ProductModel productModel = new ProductModel();
				// categoryname
				// productcost
				// productid
				// productname
				// productquantity
				// productoffer
				productModel.setCategoryId(jsonObjectZero
						.optString(JSON_CATEGORY_ID));
				productModel.setCategoryName(jsonObjectZero
						.optString(JSON_CATEGORY_NAME));
				productModel.setmProductPrize(jsonObjectZero
						.optString(JSON_CATEGORY_COST));
				productModel.setId(jsonObjectZero.optString(JSON_PRODUCT_ID));
				productModel.setmProductName(jsonObjectZero
						.optString(JSON_PRODUCT_NAME));
				productModel.setProductquantity(jsonObjectZero
						.optString(JSON_PRODUCT_QTY));

				productModel.setProductDescription(jsonObjectZero
						.optString(JSON_PRODUCT_DESCRIPTION));

				productModel.setProductViews(jsonObjectZero
						.optString(JSON_PRODUCT_VIEWS));

				productModel.setTimeStamp(jsonObjectZero
						.optString(JSON_PRODUCT_TIMESTAMP));

				productModel.setmProductOff(jsonObjectZero
						.optString(JSON_PRODUCT_OFFER));

				// product_color
				// product_image
				// product_size
				// product_type
				JSONArray productColorArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_COLOR);
				JSONArray productImageArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_IMAGE);
				JSONArray productSizeArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_SIZE);
				JSONArray productTypeArray = jsonObjectZero
						.getJSONArray(JSON_PRODUCT_TYPE);

				ArrayList<ColorModel> colorModels = new ArrayList<ColorModel>();
				for (int i = 0; i < productColorArray.length(); i++) {

					ColorModel colorModel = new ColorModel();
					JSONObject jsonObject = productColorArray.getJSONObject(i);
					colorModel.setId(jsonObject
							.optString(JSON_SETTINGS_COLORID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));

					colorModels.add(colorModel);

				}
				productModel.setProductColor(colorModels);

				// ////////////
				ArrayList<ProductImages> productImages = new ArrayList<ProductImages>();
				for (int i = 0; i < productImageArray.length(); i++) {

					ProductImages images = new ProductImages();
					JSONObject jsonObject = productImageArray.getJSONObject(i);
					images.setId(jsonObject.optString(JSON_SETTINGS_COLORID));
					images.setImageName(BASE_URL
							+ jsonObject.optString(JSON_COLOR_IMAGE));
					ColorModel colorModel = new ColorModel();
					colorModel.setId(jsonObject.optString(JSON_COLOR_ID));
					colorModel.setColorcode(jsonObject
							.optString(JSON_SETTINGS_COLORCODE));
					images.setColorModel(colorModel);
					productImages.add(images);

				}
				productModel.setProductImages(productImages);
				// ///
				ArrayList<SizeModel> sizeModels = new ArrayList<SizeModel>();
				for (int i = 0; i < productSizeArray.length(); i++) {

					SizeModel sizeModel = new SizeModel();
					JSONObject jsonObject = productSizeArray.getJSONObject(i);
					sizeModel.setId(jsonObject.optString(JSON_SETTINGS_SIZEID));
					sizeModel.setName(jsonObject
							.optString(JSON_SETTINGS_SIZENAME));

					sizeModels.add(sizeModel);

				}
				productModel.setmProductSize(sizeModels);
				// /////
				ArrayList<BrandTypeModel> brandTypeModels = new ArrayList<BrandTypeModel>();
				for (int i = 0; i < productTypeArray.length(); i++) {

					BrandTypeModel typeModel = new BrandTypeModel();
					JSONObject jsonObject = productTypeArray.getJSONObject(i);
					typeModel
							.setId(jsonObject.optString(JSON_SETTINGS_BRANDID));
					typeModel.setName(jsonObject
							.optString(JSON_SETTINGS_BRANDNAME));

					brandTypeModels.add(typeModel);

				}
				productModel.setProductType(brandTypeModels);
				// /////////
				Log.v("LOG", "9122014 " + "productModels.add(productModel)");
				productModels.add(productModel);

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

		} catch (Exception e) {

		}

		setList();
		// listAdapter = new ProductListAdapter(mActivity, productModels,1);
		// listView.setAdapter(listAdapter);

	}
*/
        private fun parseResponse(response: String) {
            try {
                val jsonObjectresponse = JSONObject(response)
                total_records = jsonObjectresponse.getString("total_records")
                tolal_pages = jsonObjectresponse.getString("tolal_pages")
                current_page = jsonObjectresponse.getString("current_page")
                val jsonArrayresponse = jsonObjectresponse
                    .getJSONArray(JSON_TAG_SETTINGS_RESPONSE)
                ///productModels=new ArrayList<ProductNewModel>();
                //productModels.clear();
                val tempProductArrayList = ArrayList<ProductModel>()
                for (j in 0 until jsonArrayresponse.length()) {
                    val obj = jsonArrayresponse.getJSONObject(j)
                    val model = ProductModel()
                    model.setmProductName(obj.getString("name"))
                    model.id = obj.getString("id")
                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    try {
                        val date = format.parse(obj.getString("timestamp"))
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
                    productModels!!.add(model)
                }

                //productModels.addAll(tempProductArrayList);
            } catch (e: Exception) {
                e.printStackTrace()
            }
            setList()
        }

        fun setList() {
            println("30122014 Click function:flag::" + flag)
            if (productModels!!.size == 0) {
                showtoast(mActivity, 17)
            } else {
                if (flag == true) {
                    gridProductList!!.visibility = View.GONE
                    listView!!.visibility = View.VISIBLE
                    tvList!!.text = "GRID"
                    imgList!!.setImageResource(R.drawable.grid)
                    val currentPage = current_page.toInt()
                    val totalPage = tolal_pages.toInt()
                    listAdapter = ProductListNew_Adapter(
                        mActivity!!,
                        productModels!!, 1, currentPage, totalPage,
                        object : ScrollingAdapterinterface {
                            override fun calledInterface(position: Int) {
                                // TODO Auto-generated method stub
                                if (currentPage == totalPage) {
                                } else {
                                    getProducts(
                                        edtSearch!!.text.toString(),
                                        (position + 1).toString()
                                    )
                                }
                                // pos=2*position-1;
                            }
                        })
                    listAdapter!!.notifyDataSetChanged()
                    //listView.invalidateViews();
                    listView!!.adapter = listAdapter
                    if (currentPage == 1) {
                        listView!!.setSelection(0)
                    } else {
                        listView!!.setSelection((currentPage - 1) * 20)
                    }
                } else {
                    gridProductList!!.visibility = View.VISIBLE
                    listView!!.visibility = View.GONE
                    tvList!!.text = "LIST"
                    imgList!!.setImageResource(R.drawable.list)
                    /*listAdapter = new ProductListAdapter_New(mActivity, productModels, 2,"0");*/
                    val currentPage = current_page.toInt()
                    val totalPage = tolal_pages.toInt()
                    listAdapter = ProductListNew_Adapter(
                        mActivity!!,
                        productModels!!, 2, currentPage, totalPage,
                        object : ScrollingAdapterinterface {
                            override fun calledInterface(position: Int) {
                                // TODO Auto-generated method stub
                                if (currentPage == totalPage) {
                                } else {
                                    getProducts(
                                        edtSearch!!.text.toString(),
                                        (position + 1).toString()
                                    )
                                }
                                // pos=2*position-1;
                            }
                        })
                    listAdapter!!.notifyDataSetChanged()
                    //gridProductList.invalidateViews();
                    gridProductList!!.adapter = listAdapter
                    if (currentPage == 1) {
                        gridProductList!!.setSelection(0)
                    } else {
                        gridProductList!!.setSelection((currentPage - 1) * 20)
                    }
                }
            }
        }

        var listAdapter: ProductListNew_Adapter? = null
    }
}