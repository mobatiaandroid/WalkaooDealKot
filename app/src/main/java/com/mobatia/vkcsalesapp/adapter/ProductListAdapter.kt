package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ProductImages
import com.mobatia.vkcsalesapp.model.ProductModel
import com.mobatia.vkcsalesapp.model.SizeModel
import java.util.*

class ProductListAdapter : BaseAdapter, VKCUrlConstants {
    var mContext: Context
    var mInflater: LayoutInflater? = null
    private val view: View? = null
    private var type: Int = 0
    var imageUrls: ArrayList<ProductImages>? = null

    constructor(mcontext: Context) {
        mContext = mcontext
    }

    constructor(
        mcontext: Context,
        mProductsList: ArrayList<ProductModel>, type: Int
    ) {
        mContext = mcontext
        mProductsListTemp = ArrayList<ProductModel>()
        this.type = type
        mInflater = LayoutInflater.from(mContext)
        mProductsListTemp!!.addAll(mProductsList)
        imageUrls = ArrayList<ProductImages>()
    }

    override fun getCount(): Int {
        return mProductsList?.size!!
    }

    // TODO Auto-generated method stub

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
        if (convertView == null) {
            if (type == 1) {
                view = mInflater?.inflate(
                    R.layout.custom_listlayout, parent,
                    false
                )!!
            } else {
                view = mInflater?.inflate(
                    R.layout.custom_gridlayout, parent,
                    false
                )!!
            }
        } else {
            view = convertView
        }
        imageUrls = mProductsList!!.get(position).productImages
        /*System.out.println("img urls 21052015 size " + imageUrls.size());
		for (int i = 0; i < imageUrls.size(); i++) {
			System.out.println("img urls 21052015 "
					+ imageUrls.get(i).getImageName());
		}*/

        // if (convertView == null)
        run({

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
        })
        // else
        run({})
        // holder.imageView.setBackgroundResource(R.drawable.sandal);
        holder!!.imageView!!.setScaleType(ImageView.ScaleType.CENTER_CROP)
        val progressBar: ProgressBar = view
            .findViewById<View>(R.id.progressBar1) as ProgressBar
        if (imageUrls!!.size > 0) {
            VKCUtils.setImageFromUrlGrid(
                mContext as Activity?, imageUrls!!.get(0)
                    .imageName, holder!!.imageView, progressBar
            )
        } else {
            // holder.imageView.setBackgroundResource(R.drawable.transparent_bg);
        }
        holder!!.txtProductName?.setText(
            mProductsList!!.get(position)
                .getmProductName()
        )
        holder!!.txtProductSize?.setText(
            ("Size: "
                    + getProductSizeList(
                mProductsList!!.get(position)
                    .getmProductSize()
            ))
        )
        // ₹
        holder!!.txtProductItemNumber?.setText(
            ("Item Number: "
                    + mProductsList!!.get(position).id)
        )
        holder!!.txtProductPrice?.setText(
            (" ₹ "
                    + mProductsList!!.get(position).getmProductPrize())
        )
        // holder.ratingBar.setRating(4);
        holder!!.txtProductOff?.setText(
            ("Offer: "
                    + mProductsList!!.get(position).getmProductOff() + " %")
        )
        view.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {

                //Bibin Edited 24/11/2016
                AppController.product_id = mProductsList!!.get(position).getmProductName() //
                AppController.selectedProductPosition = position
                AppController.category_id = mProductsList!!.get(position).categoryId
                val intent: Intent = Intent(
                    mContext,
                    ProductDetailActivity::class.java
                )

                //intent.putExtra("MODEL", mProductsList.get(position));
                mContext.startActivity(intent)
            }
        })
        return view
    }

    // Filter Class
    fun filter(charText: String) {
        var charText: String = charText
        charText = charText.toLowerCase(Locale.getDefault())
        mProductsList!!.clear()
        if (charText.length == 0) {
            mProductsList!!.addAll((mProductsListTemp)!!)
        } else {
            for (product: ProductModel in mProductsListTemp!!) {

                /*if ((product.getmProductName().contains(charText))
						|| (product.getmProductPrize().contains(charText))
						|| (product.getmProductSize().contains(charText))
						|| (product.getProductColor().contains(charText))
						|| (product.getProductDescription().contains(charText))
						|| (product.getmProductOff().contains(charText))) {
					mProductsList.add(product);
				}*/
                if (((product.getmProductName().contains(charText))
                            || (product.getmProductPrize().contains(charText))
                            || (product.getmProductSize().contains(charText))
                            || (product.productDescription.contains(charText))
                            || (product.getmProductOff().contains(charText)))
                ) {
                    mProductsList!!.add(product)
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun getProductSizeList(sizeModels: ArrayList<SizeModel>): String {
        var size: String = ""
        for (sizeModel: SizeModel in sizeModels) {
            size = size + sizeModel.name + ","
        }
        if (size.length != 0) {
            return size.substring(0, size.length - 1)
        } else {
            return ""
        }
    }

    inner class ViewHolder constructor() {
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
        notifyDataSetChanged()
    }

    private fun inItComparator(sortOption: Int) {
        myComparator = object : Comparator<ProductModel> {
            override fun compare(obj1: ProductModel?, obj2: ProductModel?): Int {
                // obj1.getmProductPrize().compareTo(obj2.getmProductPrize());
                when (sortOption) {
                    0 -> {
                        return obj2?.let {
                            obj1?.productViews?.compareTo(
                                it?.productViews
                            )
                        }!!
                    }
                    1 -> {
                        run({
                            // low to high
                            try {
                                return (obj1?.getmProductPrize()?.toInt()?.minus(
                                    obj2?.getmProductPrize()
                                        ?.toInt()!!
                                )!!)
                            } catch (ex: Exception) {
                                return obj2?.getmProductPrize()?.let {
                                    obj1?.getmProductPrize()?.compareTo(
                                        it
                                    )
                                }!!
                            }
                        })
                        run({

                            /*
					 * return obj2.getmProductPrize().compareTo(
					 * obj1.getmProductPrize());
					 */try {
                            return (obj2?.getmProductPrize()?.toInt()!! - obj1?.getmProductPrize()
                                ?.toInt()!!)
                        } catch (ex: Exception) {
                            return obj1?.getmProductPrize()?.let {
                                obj2?.getmProductPrize()?.compareTo(
                                    it
                                )
                            }!!
                        }
                        })
                        run({
                            // Want to remove
                            return obj2?.productquantity?.let {
                                obj1?.productquantity?.compareTo(
                                    it
                                )
                            }!!
                        })
                    }
                    2 -> {
                        run({
                            try {
                                return (obj1?.getmProductPrize()
                                    ?.toInt()?.let {
                                        obj2?.getmProductPrize()?.toInt()?.minus(
                                            it
                                        )
                                    }!!)
                            } catch (ex: Exception) {
                                //Log.e("LOG 03232015", "" + ex.message)
                                return obj1?.getmProductPrize()?.let {
                                    obj2?.getmProductPrize()?.compareTo(
                                        it
                                    )
                                }!!
                            }
                        })
                        run({
                            return obj2?.productquantity?.let {
                                obj1?.productquantity?.compareTo(
                                    it
                                )
                            }!!
                        })
                    }
                    3 -> {
                        return obj2?.productquantity?.let {
                            obj1?.productquantity?.compareTo(
                                it
                            )
                        }!!
                    }
                    4 -> {
                        return obj1?.getmProductOff()?.let {
                            obj2?.getmProductOff()?.compareTo(
                                it
                            )
                        }!!
                    }
                    5 -> {

                        // return obj1.getmProductOrder().compareTo(
                        // obj2.getmProductOrder());
                        var reVal: Int = 0
                        if (obj1?.getmProductOrder() == obj2?.getmProductOrder()) {
                            reVal = 0
                        }
                        if (obj1?.getmProductOrder()!! < obj2?.getmProductOrder()!!) {
                            reVal = +1
                        }
                        if (obj1?.getmProductOrder() > obj2?.getmProductOrder()!!) {
                            reVal = -1
                        }
                        return reVal
                    }
                    else -> {
                        return obj2?.getmProductName()?.let {
                            obj1?.getmProductName()?.compareTo(
                                it
                            )
                        }!!
                    }
                }
            }


        }
    }

    var myComparator: Comparator<ProductModel>? = null

    companion object {
        var mProductsList: ArrayList<ProductModel>? = null
        var mProductsListTemp: ArrayList<ProductModel>? = null
    }
}