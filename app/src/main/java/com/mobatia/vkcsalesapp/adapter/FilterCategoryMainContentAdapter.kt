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
import com.mobatia.vkcsalesapp.model.CategoryModel
import java.util.*

class FilterCategoryMainContentAdapter constructor(
    mContext: Activity,
    filterArrayList: ArrayList<CategoryModel>,
    tempArrayList: ArrayList<CategoryModel>
) : BaseAdapter() {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private val filterArrayList: ArrayList<CategoryModel>
    var tempArrayList: ArrayList<CategoryModel>
    public override fun getCount(): Int {
        // TODO Auto-generated method stub
        // return filterListString.length;
        return filterArrayList.size
    }

    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return filterArrayList.get(position)
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val view: View
        if (convertView == null) {
            view = mInflater.inflate(R.layout.custom_filtercontent, null)
        } else {
            view = convertView
        }
        val chechBoxType: CheckBox = view
            .findViewById<View>(R.id.checkBoxType) as CheckBox
        chechBoxType.setText(filterArrayList.get(position).name)
        chechBoxType.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(arg0: View) {
                val isChecked: Boolean = chechBoxType.isChecked()
                if (isChecked) {
                    tempArrayList.add(filterArrayList.get(position))
                    AppController.tempmainCatArrayList.add(filterArrayList.get(position).id!!)
                } else {

                    //tempArrayList.remove(filterArrayList.get(position));
                    removeCategoryModel(filterArrayList.get(position))
                    AppController.tempmainCatArrayList.remove(filterArrayList.get(position).id)
                }
            }
        })
        if (tempArrayList.contains(filterArrayList.get(position))) {
            chechBoxType.setChecked(true)
        } else {
            chechBoxType.setChecked(false)
        }
        return view
    }

    private fun removeCategoryModel(categoryModel: CategoryModel) {
        for (i in tempArrayList.indices) {
            if ((categoryModel.name == tempArrayList.get(i).name)) {
                tempArrayList.removeAt(i)
            }
        }
    }

    init {
        this.mContext = mContext
        this.filterArrayList = filterArrayList
        this.tempArrayList = tempArrayList
        mInflater = LayoutInflater.from(mContext)
    }
}