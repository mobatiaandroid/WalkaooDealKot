package com.mobatia.vkcsalesapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.ui.activity.SalesRepOrderList

class DealersRetailersAndDateFilter : Fragment(), VKCUrlConstants {
    // this Fragment will be called from MainActivity
    private var mRootView: View? = null
    var imageViewSearch: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.dealers_retailers_and_date_filter, container, false
        )
        init(mRootView, savedInstanceState)
        AppController.isCart = false
        return mRootView
    }

    private fun init(v: View?, savedInstanceState: Bundle?) {
        imageViewSearch = v!!.findViewById<View>(R.id.imageViewSearch) as ImageView
        imageViewSearch!!.setOnClickListener(OnClickEvent())
    }

    internal inner class OnClickEvent : View.OnClickListener {
        override fun onClick(v: View) {
            val intent = Intent(activity, SalesRepOrderList::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
    }
}