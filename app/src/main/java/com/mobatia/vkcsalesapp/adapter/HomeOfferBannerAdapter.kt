package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.OfferModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import java.util.*

class HomeOfferBannerAdapter(
    mActivity: Activity,
    offerModels: ArrayList<OfferModel> /*, DisplayView displayView*/
) : PagerAdapter() {
    var mActivity: Activity
    var layout: View? = null
    var displayHeight = 0
    var displayWidth = 0
    var offerModels: ArrayList<OfferModel>
    private fun setDisplayParam(activity: Activity) {
        val mDisplayManager = DisplayManagerScale(activity)
        displayHeight = mDisplayManager.deviceHeight
        displayWidth = mDisplayManager.deviceWidth
    }

    override fun getCount(): Int {
        return offerModels.size
    }


    override fun instantiateItem(container: View, position: Int): Any {
        // TODO Auto-generated method stub
        val inflater: LayoutInflater = mActivity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        layout = inflater
            .inflate(R.layout.custom_home_offer_banner_pager, null)
        setViews(layout, position)
        (container as ViewPager).addView(layout, 0)
        return layout!!
    }

    internal inner class WraperClass(var view: View) {
        val imageBanner: ImageView
            get() {
                val imageView: ImageView
                imageView = view.findViewById<View>(R.id.bannerImageView) as ImageView
                imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                return imageView
            }
        val progressBar: ProgressBar
            get() = view.findViewById<View>(R.id.progressBar) as ProgressBar
    }

    private fun setViews(view: View?, arg0: Int) {
        val wraperClass: WraperClass = WraperClass(view!!)
        VKCUtils.setImageFromUrl(
            mActivity, offerModels[arg0]
                .offerBanner, wraperClass.imageBanner, wraperClass
                .progressBar
        )
        view!!.setOnClickListener {
            DashboardFActivity.dashboardFActivity.setDisplayView()
            AppPrefenceManager.saveListingOption(mActivity, "1")
            AppPrefenceManager.saveOfferIDs(mActivity, offerModels[arg0].id)
        }
    }

    override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(arg2 as View)
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1 as View
    }

    override fun saveState(): Parcelable? {
        return null
    }

    init {
        this.mActivity = mActivity
        this.offerModels = offerModels
        setDisplayParam(mActivity)
    }
}