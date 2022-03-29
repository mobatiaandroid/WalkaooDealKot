/**
 *
 */
package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ProductImages
import java.util.*

/**
 * Bibin
 */
class ListImageAdapter(
    mActivity: Activity,
    mImageArrayList: ArrayList<ProductImages>
) : BaseAdapter() {
    var mActivity: Activity
    var mImageArrayList: ArrayList<ProductImages>
    var mInflater: LayoutInflater? = null
    var displayManagerScale: DisplayManagerScale? = null
    var viewWidth = 0
    var viewHeight = 0
    var dimension = 0f
    var imageWidthHeight: Int
    override fun getCount(): Int {
        return mImageArrayList.size!!
    }

    // TODO Auto-generated method stub

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return mImageArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val mInflater: LayoutInflater = mActivity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View
        view = convertView ?: mInflater.inflate(R.layout.custom_griditem, null)
        val img = view.findViewById<View>(R.id.imageView) as ImageView
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        val progressBar = view
            .findViewById<View>(R.id.progressBar1) as ProgressBar
        VKCUtils.setImageFromUrl(
            mActivity, mImageArrayList[position]
                .imageName, img, progressBar
        )
        val textName: TextView = view.findViewById<View>(R.id.textProductName) as TextView
        textName.setText(mImageArrayList[position].productName)
        return view
    }

    init {
        this.mActivity = mActivity
        this.mImageArrayList = mImageArrayList
        val displayManagerScale = DisplayManagerScale(mActivity)
        imageWidthHeight = displayManagerScale.deviceWidth
    }
}