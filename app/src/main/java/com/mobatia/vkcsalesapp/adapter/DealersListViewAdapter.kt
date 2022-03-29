package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.DealersShopModel
import java.util.*

class DealersListViewAdapter(
    var dealersModels: ArrayList<DealersShopModel>,
    var mActivity: Activity
) : BaseAdapter() {
    var mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return dealersModels.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return dealersModels[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View? = null
        view = convertView
            ?: mLayoutInflater.inflate(
                R.layout.custom_dealers_list_view,
                null
            )
        val wraperClass: WraperClass = WraperClass(view)
        wraperClass.nameText.text = dealersModels[position].name
        wraperClass.addressText.text = dealersModels[position].address
        wraperClass.phoneText.text = dealersModels[position].phone
        return view!!
    }

    private inner class WraperClass(view: View?) {
        var view: View? = null
        val nameText: TextView
            get() = view!!.findViewById<View>(R.id.textViewName) as TextView
        val addressText: TextView
            get() = view!!.findViewById<View>(R.id.textViewAddress) as TextView
        val phoneText: TextView
            get() = view!!.findViewById<View>(R.id.textViewPhone) as TextView

        init {
            this.view = view
            // TODO Auto-generated constructor stub
        }
    }

    init {
        mLayoutInflater = LayoutInflater.from(mActivity)
    }
}