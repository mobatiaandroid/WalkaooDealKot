package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.ProgressBar
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.VKCUtils.setImageFromUrl
import com.mobatia.vkcsalesapp.model.ProductModel
import com.mobatia.vkcsalesapp.model.SizeModel
import java.util.*

class ProductListNew_Adapter : BaseAdapter, VKCUrlConstants {
    var mContext: Context
    var mInflater: LayoutInflater? = null
    private val view: View? = null
    private var type = 0
    private var currentPage = 0
    private var totalPage = 0
    var mProductsList: ArrayList<ProductModel>? = null
    private var adapterinterface: ScrollingAdapterinterface? = null

    //private int type;
    constructor(mcontext: Context) {
        mContext = mcontext
    }

    /*public ProductListAdapterNew(Context mcontext,
			ArrayList<ProductModel> mProductsList, int type) {

		this.mContext = mcontext;
		this.mProductsList = mProductsList;
		mProductsListTemp = new ArrayList<ProductModel>();
		this.type = type;
		mInflater = LayoutInflater.from(mContext);
		mProductsListTemp.addAll(mProductsList);
		imageUrls = new ArrayList<ProductImages>();
	}*/
    constructor(
        mcontext: Context,
        mProductsList: ArrayList<ProductModel>,
        type: Int,
        currentPage: Int,
        totalPage: Int,
        adapterinterface: ScrollingAdapterinterface?
    ) {
        mContext = mcontext
        this.mProductsList = mProductsList
        mProductsListTemp = ArrayList()
        this.type = type
        //mInflater = LayoutInflater.from(mContext);
        mProductsListTemp!!.addAll(mProductsList)
        //imageUrls = new ArrayList<ProductImages>();
        this.adapterinterface = adapterinterface
        this.currentPage = currentPage
        this.totalPage = totalPage
    }

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return mProductsList!!.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        var holder: ViewHolder? = null
        val view: View
        val mInflater = mContext
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (convertView == null) {
            if (type == 1) {
                view = mInflater.inflate(
                    R.layout.custom_listlayout, parent,
                    false
                )
            } else {
                view = mInflater.inflate(
                    R.layout.custom_gridlayout, parent,
                    false
                )
            }
        } else {
            view = convertView
        }

        //imageUrls = mProductsList.get(position).getProductImages();
        /*System.out.println("img urls 21052015 size " + imageUrls.size());
        for (int i = 0; i < imageUrls.size(); i++) {
            System.out.println("img urls 21052015 "
                    + imageUrls.get(i).getImageName());
        }*/

        // if (convertView == null)
        run {

            // view = mInflater.inflate(R.layout.custom_listlayout, parent,
            // false);
            holder = ViewHolder()
            holder!!.imageView = view.findViewById<View>(R.id.imageView) as ImageView?
            holder!!.txtProductName = view
                .findViewById<View>(R.id.txtProductName) as TextView?
            holder!!.txtProductSize = view
                .findViewById<View>(R.id.txtProductSize) as TextView?
            holder!!.txtProductItemNumber = view
                .findViewById<View>(R.id.txtProductItemNumber) as TextView?
            holder!!.txtProductPrice = view
                .findViewById<View>(R.id.txtProductPrice) as TextView?
            holder!!.txtProductOff = view
                .findViewById<View>(R.id.txtProductOff) as TextView?
        }
        // else
        run {}
        // holder.imageView.setBackgroundResource(R.drawable.sandal);
        holder!!.imageView!!.scaleType = ScaleType.CENTER_CROP
        val progressBar = view
            .findViewById<View>(R.id.progressBar1) as ProgressBar
        if (mProductsList!!.size != 0 && mProductsList!!.get(position).image_name != "") {
            setImageFromUrl(
                mContext as Activity,
                VKCUrlConstants.BASE_URL + mProductsList!![position].image_name,
                holder!!.imageView,
                progressBar
            )
        } else {
            // holder.imageView.setBackgroundResource(R.drawable.transparent_bg);
        }
        holder!!.txtProductName!!.text = mProductsList!!.get(position)
            .getmProductName()
        holder!!.txtProductSize!!.text = ("Size: "
                + mProductsList!!.get(position).size)
        // ₹
        holder!!.txtProductItemNumber!!.text = ("Item Number: "
                + mProductsList!!.get(position).id)
        holder!!.txtProductPrice!!.text = (" ₹ "
                + mProductsList!!.get(position).getmProductPrize())
        // holder.ratingBar.setRating(4);
        holder!!.txtProductOff!!.text = ("Offer: "
                + mProductsList!!.get(position).getmProductOff() + " %")
        view.setOnClickListener(View.OnClickListener { /*	Intent intent = new Intent(mContext,
						ProductDetailsActivityNew.class);
				AppController.selectedProductPosition = position;
				intent.putExtra("MODEL", mProductsList);
intent.putExtra("position", position);
				mContext.startActivity(intent);*/
            AppController.product_id = mProductsList!![position].getmProductName() //
            AppController.selectedProductPosition = position
            AppController.category_id = mProductsList!![position].categoryId
            val intent = Intent(
                mContext,
                ProductDetailActivity::class.java
            )

            //intent.putExtra("MODEL", mProductsList.get(position));
            mContext.startActivity(intent)
        })
        if (position == ((currentPage * 20) - 1) && currentPage <= totalPage) {
            adapterinterface!!.calledInterface(currentPage)
        }
        return view
    }

    // Filter Class
    fun filter(charText: String) {
        var charText = charText
        charText = charText.toLowerCase(Locale.getDefault())
        mProductsList!!.clear()
        if (charText.length == 0) {
            mProductsList!!.addAll((mProductsListTemp)!!)
        } else {
            for (product: ProductModel in mProductsListTemp!!) {
                if (((product.getmProductName().contains(charText))
                            || (product.getmProductPrize().contains(charText))
                            || (product.size.contains(charText)) /*|| (product.getProductColor().contains(charText))*/
                            || (product.type.contains(charText))
                            || (product.getmProductOff().contains(charText)))
                ) {
                    mProductsList!!.add(product)
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun getProductSizeList(sizeModels: ArrayList<SizeModel>): String {
        var size = ""
        for (sizeModel: SizeModel in sizeModels) {
            size = size + sizeModel.name + ","
        }
        return if (size.length != 0) {
            size.substring(0, size.length - 1)
        } else {
            ""
        }
    }

    inner class ViewHolder() {
        var imageView: ImageView? = null
        var txtProductName: TextView? = null
        var txtProductSize: TextView? = null
        var txtProductItemNumber: TextView? = null
        var txtProductPrice: TextView? = null
        var txtProductOff: TextView? = null
        var txtShop: TextView? = null // RatingBar ratingBar;
    }

    fun doSort(sortOption: Int) {
        inItComparator(sortOption)
        Collections.sort(mProductsList, myComparator)
        /*for (int i = 0; i < mProductsList.size(); i++) {
			System.out.println("23032015 "
					+ mProductsList.get(i).getmProductPrize());
		}*/notifyDataSetChanged()
    }

    private fun inItComparator(sortOption: Int) {
        myComparator = object : Comparator<ProductModel> {
            override fun compare(obj1: ProductModel, obj2: ProductModel): Int {
                // return
                // obj1.getmProductPrize().compareTo(obj2.getmProductPrize());
                when (sortOption) {
                    0 -> {
                        return obj1.productViews.compareTo(
                            obj2.productViews
                        )
                    }
                    1 -> {
                        run {
                            // low to high
                            try {
                                return (obj1.getmProductPrize().toInt() - obj2.getmProductPrize()
                                    .toInt())
                            } catch (ex: Exception) {
                                Log.e("LOG 03232015", "" + ex.message)
                                return obj1.getmProductPrize().compareTo(
                                    obj2.getmProductPrize()
                                )
                            }
                        }
                        run {
                            try {
                                return (obj2.getmProductPrize().toInt() - obj1.getmProductPrize()
                                    .toInt())
                            } catch (ex: Exception) {
                                Log.e("LOG 03232015", "" + ex.message)
                                return obj2.getmProductPrize().compareTo(
                                    obj1.getmProductPrize()
                                )
                            }
                        }
                        run {
                            /*return obj1.getProductquantity().compareTo(
							obj2.getProductquantity());*/return obj2.timeStampP.compareTo(
                            obj1.timeStampP
                        )
                        }
                    }
                    2 -> {
                        run {
                            try {
                                return (obj2.getmProductPrize().toInt() - obj1.getmProductPrize()
                                    .toInt())
                            } catch (ex: Exception) {
                                Log.e("LOG 03232015", "" + ex.message)
                                return obj2.getmProductPrize().compareTo(
                                    obj1.getmProductPrize()
                                )
                            }
                        }
                        run {
                            return obj2.timeStampP.compareTo(
                                obj1.timeStampP
                            )
                        }
                    }
                    3 -> {
                        return obj2.timeStampP.compareTo(
                            obj1.timeStampP
                        )
                    }
                    4 -> {
                        return obj2.getmProductOff().compareTo(
                            obj1.getmProductOff()
                        )
                    }
                    5 -> {
                        /*int reVal = 0;
					if (obj1.getmProductOrder() == obj2.getmProductOrder()) {
						reVal = 0;
					}
					if (obj1.getmProductOrder() < obj2.getmProductOrder()) {
						reVal = +1;
					}
					if (obj1.getmProductOrder() > obj2.getmProductOrder()) {
						reVal = -1;
					}

					return reVal;*/return obj2.productquantity.compareTo(
                            obj1.productquantity
                        )
                    }
                    else -> {
                        return obj1.getmProductName().compareTo(
                            obj2.getmProductName()
                        )
                    }
                }
            }
        }
    }

    var myComparator: Comparator<ProductModel>? = null

    interface ScrollingAdapterinterface {
        fun calledInterface(position: Int)
    }

    companion object {
        var mProductsList: ArrayList<ProductModel>? = null
        var mProductsListTemp: ArrayList<ProductModel>? = null
    }
}