/**
 *
 */
package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ColorModel
import java.util.*

class FilterColorContentAdapter(
    mContext: Activity,
    filterArrayList: ArrayList<ColorModel>,
    tempArrayList: ArrayList<ColorModel>
) : BaseAdapter() {
    var mContext: Context
     var mInflater: LayoutInflater
     lateinit var filterListString: Array<String>
    private val filterArrayList: ArrayList<ColorModel>
    var tempArrayList: ArrayList<ColorModel>
    override fun getCount(): Int {
        return filterArrayList.size
    }

    // TODO Auto-generated method stub
    // return filterListString.length;


    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return filterArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val view: View
        view = convertView ?: mInflater.inflate(R.layout.custom_filtercolor, null)
        val gd = GradientDrawable()
        gd.setCornerRadius(50f)
        gd.setAlpha(100)
        val relativeMain: RelativeLayout = view
            .findViewById<View>(R.id.viewColor) as RelativeLayout
        val hexColorTextView: TextView = view
            .findViewById<View>(R.id.hexColorTextView) as TextView
        relativeMain.setBackgroundDrawable(gd)
        val chechBoxType = view
            .findViewById<View>(R.id.checkBoxType) as CheckBox
        if (filterArrayList[position].colorcode?.startsWith("#") == true) {
            gd.setColor(
                VKCUtils.parseColor(
                    filterArrayList[position]
                        .colorcode!!
                )
            )
            hexColorTextView.setText(
                filterArrayList[position]
                    .colorName
            )
        } else {
            chechBoxType.text = "No color"
            hexColorTextView.setText("")
        }
        chechBoxType.setOnClickListener {
            val isChecked = chechBoxType.isChecked
            if (isChecked) {
                tempArrayList.add(filterArrayList[position])
                AppController.tempcolorCatArrayList.add(filterArrayList[position].id!!)
            } else {
                removeColorModel(filterArrayList[position])
                AppController.tempcolorCatArrayList.remove(filterArrayList[position].id)
            }
        }
        if (tempArrayList.contains(filterArrayList[position])) {
            chechBoxType.isChecked = true
        } else {
            chechBoxType.isChecked = false
            // Log.v("LOG","04112014 "+filterArrayList.get(position).getName()
            // );
        }
        //
        return view
    }

    private fun removeColorModel(mColorModel: ColorModel) {
        for (i in tempArrayList.indices) {
            if (mColorModel.colorcode == tempArrayList[i].colorcode) {
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