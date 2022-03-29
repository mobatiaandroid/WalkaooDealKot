package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.BrandBannerModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import java.util.*

class HomeBrandBannerTwoAdapter constructor(
    mainActivity: Activity,
    brandBannerModels: ArrayList<BrandBannerModel>, type: Int
) : PagerAdapter() {
    var mActivity: Activity
    var layout: View? = null
    var mType: Int
    var displayHeight: Int = 0
    var displayWidth: Int = 0
    var brandBannerModels: ArrayList<BrandBannerModel>
    private var count: Int = 0
    override fun getCount(): Int {
        return brandBannerModels.size
    }

    private fun setListSize() {
        if (mType == 0) {
            count = brandBannerModels.size
        } else {
            if (brandBannerModels.size % 2 == 0) {
                count = brandBannerModels.size / 2
            } else {
                count = (brandBannerModels.size / 2) + 1
            }
        }
    }

    private fun setDisplayParam(activity: Activity) {
        val mDisplayManager: DisplayManagerScale = DisplayManagerScale(activity)
        displayHeight = mDisplayManager.deviceHeight
        displayWidth = mDisplayManager.deviceWidth
    }

    public override fun instantiateItem(container: View, position: Int): Any {
        // TODO Auto-generated method stub
        val inflater: LayoutInflater = mActivity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mType == 0) {
            layout = inflater.inflate(R.layout.custom_home_banner_pager, null)
            setViews(layout, position)
            (container as ViewPager).addView(layout, 0)
        } else {
            layout = inflater.inflate(
                R.layout.custom_home_brand_banner_pager_three_image, null
            )
            setBottomImages(layout, position)
            (container as ViewPager).addView(layout, 0)
        }
        return layout!!
    }

    private fun setBottomImages(collection: View?, position: Int): View {
        val images: LinearLayout = collection
            ?.findViewById<View>(R.id.linearImage) as LinearLayout
        val wraperClassFooter: WraperClassFooter = WraperClassFooter(collection!!)
        var imageUrl1: String? = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg"
        var imageUrl2: String? = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg"
        if (position == 0) {
            imageUrl1 = brandBannerModels.get(position).brandBannerTwo
            imageUrl2 = brandBannerModels.get(position + 1).brandBannerTwo
        } else {
            if (((position * 2)) <= brandBannerModels.size) {
                imageUrl1 = brandBannerModels.get(position * 2).brandBannerTwo
            }
            if ((((position * 2) + 1)) < brandBannerModels.size) {
                imageUrl2 = brandBannerModels.get((position * 2) + 1)
                    .brandBannerTwo
            }
        }
        wraperClassFooter.footerImage1.setOnClickListener(
            object : View.OnClickListener {
                public override fun onClick(v: View) {
                    // TODO Auto-generated method stub
                    if (position == 0) {
                        DashboardFActivity.dashboardFActivity
                            .setDisplayView()
                        AppPrefenceManager
                            .saveListingOption(mActivity, "4")
                        AppPrefenceManager.saveIDsForOffer(
                            mActivity,
                            ""
                        )
                        AppPrefenceManager.saveBrandIdForSearch(
                            mActivity,
                            brandBannerModels.get(position).id
                        )
                    } else {
                        DashboardFActivity.dashboardFActivity
                            .setDisplayView()
                        AppPrefenceManager
                            .saveListingOption(mActivity, "4")
                        AppPrefenceManager.saveIDsForOffer(
                            mActivity,
                            ""
                        )
                        AppPrefenceManager
                            .saveBrandIdForSearch(
                                mActivity,
                                brandBannerModels.get(position * 2)
                                    .id
                            )
                    }
                }
            })
        wraperClassFooter.footerImage2.setOnClickListener(
            object : View.OnClickListener {
                public override fun onClick(v: View) {
                    // TODO Auto-generated method stub
                    if (position == 0) {
                        DashboardFActivity.dashboardFActivity.setDisplayView()
                        AppPrefenceManager.saveListingOption(mActivity, "4")
                        AppPrefenceManager.saveIDsForOffer(
                            mActivity,
                            ""
                        )
                        AppPrefenceManager.saveBrandIdForSearch(
                            mActivity,
                            brandBannerModels.get(position + 1).id
                        )
                    } else {
                        DashboardFActivity.dashboardFActivity.setDisplayView()
                        AppPrefenceManager.saveListingOption(mActivity, "4")
                        AppPrefenceManager.saveIDsForOffer(
                            mActivity,
                            ""
                        )
                        AppPrefenceManager.saveBrandIdForSearch(
                            mActivity,
                            brandBannerModels.get((position * 2) + 1).id
                        )
                    }
                }
            })
        if (imageUrl1 != null) {
            VKCUtils.setImageFromUrlBaseTransprant(
                mActivity, imageUrl1,
                wraperClassFooter.footerImage1,
                wraperClassFooter.progressBar1
            )
        }
        if (imageUrl2 != null) {
            VKCUtils.setImageFromUrlBaseTransprant(
                mActivity, imageUrl2,
                wraperClassFooter.footerImage2,
                wraperClassFooter.progressBar2
            )
        }
        return images
    }

    internal inner class WraperClassFooter constructor(var view: View) {
        val footerImage1: ImageView
            get() {
                val images1: ImageView = view
                    .findViewById<View>(R.id.footerImage1) as ImageView
                images1.getLayoutParams().width = displayWidth / 2
                images1.setScaleType(ImageView.ScaleType.FIT_CENTER)
                return images1
            }
        val footerImage2: ImageView
            get() {
                val images1: ImageView = view
                    .findViewById<View>(R.id.footerImage2) as ImageView
                images1.getLayoutParams().width = displayWidth / 2
                images1.setScaleType(ImageView.ScaleType.FIT_CENTER)
                return images1
            }
        val progressBar1: ProgressBar
            get() {
                return view.findViewById<View>(R.id.progressBar1) as ProgressBar
            }
        val progressBar2: ProgressBar
            get() {
                return view.findViewById<View>(R.id.progressBar2) as ProgressBar
            }
    }

    internal inner class WraperClass constructor(var view: View) {
        val imageBanner: ImageView
            get() {
                val imageView: ImageView
                imageView = view.findViewById<View>(R.id.bannerImageView) as ImageView
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP)
                return imageView
            }
        val progressBar: ProgressBar
            get() {
                return view.findViewById<View>(R.id.progressBar) as ProgressBar
            }
    }

    private fun setViews(view: View?, arg0: Int) {
        val wraperClass: WraperClass = WraperClass(view!!)
        VKCUtils.setImageFromUrl(
            mActivity,
            brandBannerModels.get(arg0)
                .brandBannerTwo /* "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg" */,
            wraperClass.imageBanner, wraperClass.progressBar
        )
        view!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {}
        })
    }

    public override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(arg2 as View?)
    }

    public override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === (arg1 as View)
    }

    public override fun saveState(): Parcelable? {
        return null
    }

    init {
        mActivity = mainActivity
        this.brandBannerModels = brandBannerModels
        mType = type
        setDisplayParam(mActivity)
        setListSize()
    }
}