package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ProductImages
import java.util.*

/**
 * @author Bibin
 */
class ViewpagerAdapter(
    mActivity: Activity,
    mImageList: ArrayList<ProductImages>
) : PagerAdapter() {
    var mActivity: Activity
    var mImageList: ArrayList<ProductImages>
    var mInflater: LayoutInflater? = null
    private val view: View? = null

    // TODO Auto-generated method stub

    override fun isViewFromObject(view: View, jobject: Any): Boolean {
        // TODO Auto-generated method stub
        return view === jobject as RelativeLayout
    }

    override fun getCount(): Int {
        return mImageList?.size
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        // TODO Auto-generated method stub
        mInflater = mActivity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView: View = mInflater!!.inflate(
            R.layout.custom_viepagerlayout,
            container, false
        )
        val img = itemView.findViewById<View>(R.id.imageView) as ImageView
        img.scaleType = ImageView.ScaleType.CENTER_CROP
        val progressBar = itemView
            .findViewById<View>(R.id.progressBar1) as ProgressBar
        VKCUtils.setImageFromUrl(
            mActivity, mImageList[position]
                .imageName, img, progressBar
        )
        img.setOnClickListener {
            // TODO Auto-generated method stub
            val dialogBuilder = AlertDialog.Builder(mActivity)
            val inflater: LayoutInflater = mActivity.getLayoutInflater()
            val dialogView: View = inflater.inflate(R.layout.layout_image_zoom, null)
            dialogBuilder.setView(dialogView)
            val webView = dialogView.findViewById<View>(R.id.imageLarge) as WebView
            val imageClose = dialogView.findViewById<View>(R.id.imageClose) as ImageView
            webView.settings.builtInZoomControls = true
            webView.settings.displayZoomControls = false
            webView.setInitialScale(250)
            webView.isScrollbarFadingEnabled = true
            webView.loadUrl(
                mImageList[position]
                    .imageName
            )
            val alertDialog = dialogBuilder.create()
            alertDialog.show()
            imageClose.setOnClickListener { // TODO Auto-generated method stub
                alertDialog.dismiss()
            }
        }
        (container as ViewPager).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, jobject: Any) {
        // Remove viewpager_item.xml from ViewPager
        (container as ViewPager).removeView(jobject as RelativeLayout)
    }

    init {
        this.mActivity = mActivity
        this.mImageList = mImageList
    }
}