package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.model.OfferModel
import java.util.*

class FilterOfferAdapter(
    mContext: Activity,
    offerModels: ArrayList<OfferModel>,
    tempArrayList: ArrayList<OfferModel>
) : BaseAdapter() {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private val offerModels: ArrayList<OfferModel>
    var tempOfferModels: ArrayList<OfferModel>
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        //return filterListString.length;
        return offerModels.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return offerModels[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val view: View
        view = convertView ?: mInflater.inflate(R.layout.custom_filtercontent, null)
        val chechBoxType = view.findViewById<View>(R.id.checkBoxType) as CheckBox
        chechBoxType.text = offerModels[position].name + "%"
        chechBoxType.setOnClickListener {
            val isChecked = chechBoxType.isChecked
            if (isChecked) {
                tempOfferModels.add(offerModels[position])
                AppController.tempofferCatArrayList.add(offerModels[position].id!!)
            } else {
                removeOfferModel(offerModels[position])
                AppController.tempofferCatArrayList.remove(offerModels[position].id)
            }
        }
        if (tempOfferModels.contains(offerModels[position])) {
            chechBoxType.isChecked = true
        } else {
            chechBoxType.isChecked = false
        }
        return view
    }

    private fun removeOfferModel(mOfferModel: OfferModel) {
        for (i in tempOfferModels.indices) {
            if (mOfferModel.name == tempOfferModels[i].name) {
                tempOfferModels.removeAt(i)
            }
        }
    }

    init {
        this.mContext = mContext
        this.offerModels = offerModels
        tempOfferModels = tempArrayList
        mInflater = LayoutInflater.from(mContext)
    }
}