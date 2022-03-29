/**
 *
 */
package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ListImageModel
import java.util.*

/**
 * Bibin
 */
class ListBottomImageAdapter(
    mActivity: Activity,
    mImageArrayList: ArrayList<ListImageModel>,
    height: Int
) : BaseAdapter() {
    var mActivity: Activity
    var mImageArrayList: ArrayList<ListImageModel>
    var mInflater: LayoutInflater
    var mDisplaymanager: DisplayManagerScale
    var mViewHeight: Int
    override fun getCount(): Int {
        return mImageArrayList.size
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
        val view: View
        view = convertView ?: mInflater.inflate(R.layout.custom_bottomgriditem, null)
        val relContent: RelativeLayout = view.findViewById<View>(R.id.relContent) as RelativeLayout
        relContent.getLayoutParams().height = (mViewHeight * 0.90).toInt()
        val img = view.findViewById<View>(R.id.imageView) as ImageView
        val txtName: TextView = view.findViewById<View>(R.id.txtName) as TextView
        val txtPrice: TextView = view.findViewById<View>(R.id.txtPrice) as TextView
        val progressBar = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        VKCUtils.setImageFromUrl(
            mActivity, mImageArrayList[position].url,
            img, progressBar
        )
        return view
    }

    init {
        mDisplaymanager = DisplayManagerScale(mActivity)
        this.mActivity = mActivity
        this.mImageArrayList = mImageArrayList
        mInflater = LayoutInflater.from(mActivity)
        mViewHeight = height
    }
}