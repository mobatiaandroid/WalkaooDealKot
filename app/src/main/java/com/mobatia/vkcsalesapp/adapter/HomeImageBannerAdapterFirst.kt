package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.HomeImageBannerModel
import com.mobatia.vkcsalesapp.model.ProductModel
import java.util.*

class HomeImageBannerAdapterFirst : PagerAdapter {
    var mActivity: Activity
    var layout: View? = null
    var pagenumber: TextView? = null
    var click: Button? = null
    var mType: Int
    var displayHeight = 0
    var displayWidth = 0
    var mFeedbackFormModels: ArrayList<HomeImageBannerModel>
    var mProductsList: ArrayList<ProductModel>? = null
    var mCount: Int = 0

    constructor(
        mainActivity: Activity,
        arg1: ArrayList<HomeImageBannerModel>, type: Int
    ) {
        mActivity = mainActivity
        mFeedbackFormModels = arg1
        mType = type
        setDisplayParam(mActivity)
        setListSize()
    }

    constructor(
        mainActivity: Activity,
        arg1: ArrayList<HomeImageBannerModel>,
        mProductsList: ArrayList<ProductModel>?, type: Int
    ) {
        mActivity = mainActivity
        mFeedbackFormModels = arg1
        mType = type
        this.mProductsList = mProductsList
        setDisplayParam(mActivity)
        setListSize()
    }

    private fun setListSize() {
        if (mType == 0) {
            mCount = mFeedbackFormModels.size
        } else {
            if (mFeedbackFormModels.size % 3 == 0) {
                mCount = mFeedbackFormModels.size / 3
            } else {
                mCount = (mFeedbackFormModels.size / 3) + 1
            }
        }
    }

    private fun setDisplayParam(activity: Activity) {
        val displayManagerScale = activity?.let {
            DisplayManagerScale(
                it
            )
        }
        displayHeight = displayManagerScale?.deviceHeight!!
        displayWidth = displayManagerScale?.deviceWidth!!
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun instantiateItem(container: View, position: Int): Any {
        // TODO Auto-generated method stub
        val inflater: LayoutInflater = mActivity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (mType == 0) {
            layout = inflater.inflate(R.layout.custom_home_banner_pager, null)
            setViews(layout, position)
            (container as ViewPager).addView(layout, 0)
        } else {
            layout = inflater.inflate(
                R.layout.custom_home_banner_pager_three_image, null
            )
            setBottomImages(layout, position)
            (container as ViewPager).addView(layout, 0)
        }
        return layout!!
    }

    private fun setBottomImages(collection: View?, position: Int): View {
        val images: LinearLayout = collection
            ?.findViewById<View>(R.id.linearImage) as LinearLayout
        val wraperClassFooter: WraperClassFooter = collection?.let { WraperClassFooter(it) }
        var imageUrl1: String = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg"
        var imageUrl3: String = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg"
        var imageUrl2: String = "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg"
        if (position == 0) {
            imageUrl1 = mFeedbackFormModels[position].getBannerUrl()
            imageUrl2 = mFeedbackFormModels[position + 1].getBannerUrl()
            imageUrl3 = mFeedbackFormModels[position + 2].getBannerUrl()
        } else {
            if (((position * 3)) <= mFeedbackFormModels.size) {
                imageUrl1 = mFeedbackFormModels[3 * position].getBannerUrl()
            }
            if ((((position * 3) + 1)) <= mFeedbackFormModels.size) {
                imageUrl2 = mFeedbackFormModels[3 * position + 1]
                    .getBannerUrl()
            }
            if ((((position * 3) + 2)) <= mFeedbackFormModels.size) {
                imageUrl3 = mFeedbackFormModels[3 * position + 1]
                    .getBannerUrl()
            }
        }
        wraperClassFooter.footerImage1.setOnClickListener(
            View.OnClickListener { // TODO Auto-generated method stub
                if (((position * 3) + 1) <= mProductsList!!.size) {
                    if (position > 0) {
                        val intent = Intent(
                            mActivity,
                            ProductDetailActivity::class.java
                        )
                        AppController.product_id = mProductsList!![position * 3].getmProductName()
                        AppController.category_id = mProductsList!![position * 3].getCategoryId()
                        intent.putExtra(
                            "MODEL",
                            mProductsList!![position * 3]
                        )

                        mActivity.startActivity(intent)
                    } else {
                        val intent = Intent(
                            mActivity,
                            ProductDetailActivity::class.java
                        )
                        AppController.product_id = mProductsList!![position].getmProductName()
                        AppController.category_id = mProductsList!![position].getCategoryId()
                        intent.putExtra(
                            "MODEL",
                            mProductsList!![position]
                        )
                        mActivity.startActivity(intent)
                    }
                }
            })
        wraperClassFooter.footerImage2.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    // TODO Auto-generated method stub
                    if (((position * 3) + 2) <= mProductsList!!.size) {
                        if (position > 0) {
                            AppController.product_id =
                                mProductsList!![(position * 3) + 1].getmProductName()
                            AppController.category_id =
                                mProductsList!![(position * 3) + 1].getCategoryId()
                            val intent = Intent(
                                mActivity,
                                ProductDetailActivity::class.java
                            )
                            intent.putExtra(
                                "MODEL",
                                mProductsList!![(position * 3) + 1]
                            )
                            mActivity.startActivity(intent)
                        } else {
                            AppController.product_id =
                                mProductsList!![position + 1].getmProductName()
                            AppController.category_id =
                                mProductsList!![position + 1].getCategoryId()
                            val intent = Intent(
                                mActivity,
                                ProductDetailActivity::class.java
                            )
                            intent.putExtra(
                                "MODEL",
                                mProductsList!![position + 1]
                            )
                            mActivity.startActivity(intent)
                        }
                    }
                }
            })
        wraperClassFooter.footerImage3.setOnClickListener(
            object : View.OnClickListener {
                override fun onClick(v: View) {
                    // TODO Auto-generated method stub
                    if (((position * 3) + 3) <= mProductsList!!.size) {
                        if (position > 0) {
                            AppController.product_id =
                                mProductsList!![(position * 3) + 2].getmProductName()
                            AppController.category_id =
                                mProductsList!![(position * 3) + 2].getCategoryId()
                            val intent = Intent(
                                mActivity,
                                ProductDetailActivity::class.java
                            )
                            intent.putExtra(
                                "MODEL",
                                mProductsList!![(position * 3) + 2]
                            )
                            mActivity.startActivity(intent)
                        } else {
                            AppController.product_id =
                                mProductsList!![position + 2].getmProductName()
                            AppController.category_id =
                                mProductsList!![position + 2].getCategoryId()
                            val intent = Intent(
                                mActivity,
                                ProductDetailActivity::class.java
                            )
                            intent.putExtra(
                                "MODEL",
                                mProductsList!![position + 2]
                            )
                            mActivity.startActivity(intent)
                        }
                    }
                }
            })
        VKCUtils.setImageFromUrlBaseTransprant(
            mActivity, imageUrl1,
            wraperClassFooter.footerImage1,
            wraperClassFooter.progressBar1
        )
        VKCUtils.setImageFromUrlBaseTransprant(
            mActivity, imageUrl2,
            wraperClassFooter.footerImage2,
            wraperClassFooter.progressBar2
        )
        VKCUtils.setImageFromUrlBaseTransprant(
            mActivity, imageUrl3,
            wraperClassFooter.footerImage3,
            wraperClassFooter.progressBar3
        )
        return images
    }

    internal inner class WraperClassFooter(var view: View) {
        val footerImage1: ImageView
            get() {
                val images1 = view
                    .findViewById<View>(R.id.footerImage1) as ImageView
                images1.layoutParams.width = displayWidth / 3
                images1.scaleType = ImageView.ScaleType.CENTER_CROP
                return images1
            }
        val footerImage2: ImageView
            get() {
                val images1 = view
                    .findViewById<View>(R.id.footerImage2) as ImageView
                images1.layoutParams.width = displayWidth / 3
                images1.scaleType = ImageView.ScaleType.CENTER_CROP
                return images1
            }
        val footerImage3: ImageView
            get() {
                val images1 = view
                    .findViewById<View>(R.id.footerImage3) as ImageView
                images1.layoutParams.width = displayWidth / 3
                images1.scaleType = ImageView.ScaleType.CENTER_CROP
                return images1
            }
        val progressBar1: ProgressBar
            get() = view.findViewById<View>(R.id.progressBar1) as ProgressBar
        val progressBar2: ProgressBar
            get() = view.findViewById<View>(R.id.progressBar2) as ProgressBar
        val progressBar3: ProgressBar
            get() = view.findViewById<View>(R.id.progressBar3) as ProgressBar
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
        val wraperClass: WraperClass = view?.let { WraperClass(it) }!!
        VKCUtils.setImageFromUrl(
            mActivity,
            mFeedbackFormModels[arg0].getBannerUrl() /* "http://dev.mobatia.com/vkc/media/uploads/slider_images/1.jpg" */,
            wraperClass.imageBanner, wraperClass.progressBar
        )
        view!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                // TODO Auto-generated method stub
                val intent = Intent(
                    mActivity,
                    ProductDetailActivity::class.java
                )
                intent.putExtra("MODEL", mProductsList!![arg0])
                AppController.product_id = mProductsList!![arg0].getmProductName()
                AppController.category_id = mProductsList!![arg0].getCategoryId()
                mActivity.startActivity(intent)
            }
        })
    }

    override fun destroyItem(arg0: View, arg1: Int, arg2: Any) {
        (arg0 as ViewPager).removeView(arg2 as View)
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === (arg1 as View)
    }

    override fun saveState(): Parcelable? {
        return null
    }
}