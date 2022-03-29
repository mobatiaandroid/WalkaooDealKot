package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.ColorModel
import java.util.*

class ColorGridAdapter : BaseAdapter {
    var mContext: Context
    var colorModels: ArrayList<ColorModel>? = null
    var mInflater: LayoutInflater
    var mDisplayManager: DisplayManagerScale? = null
    var width = 0
    var height = 0
    var type: Int
    var colorValue: String? = null

    constructor(mcontext: Context, colorModels: ArrayList<ColorModel>?, type: Int) {
        mContext = mcontext
        this.colorModels = colorModels
        this.type = type
        mInflater = LayoutInflater.from(mContext)
        displayScale
    }

    constructor(mcontext: Context, colorValue: String?, type: Int) {
        mContext = mcontext
        this.colorValue = colorValue
        this.type = type
        mInflater = LayoutInflater.from(mContext)
        displayScale
    }

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return if (type == 0) {
            1
        } else {
            colorModels!!.size
        }
    }


    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    var checkBoxTemp: CheckBox? = null

    @SuppressLint("NewApi")
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val holderView: View
        holderView = convertView ?: mInflater.inflate(
            R.layout.custom_color_grid, parent,
            false
        )
        val gd = GradientDrawable()
        // gd.setColor(Color.parseColor(colorModels.get(position).getColorcode()));
        if (type == 1) {
            gd.setColor(
                VKCUtils.parseColor(
                    colorModels!![position]
                        ?.colorcode.toString()
                )
            )
        } else {
            gd.setColor(VKCUtils.parseColor(colorValue!!))
        }
        gd.setCornerRadius(50f)
        val relativeMain: RelativeLayout = ViewHolder()
            .getColorView(holderView)
        relativeMain.setBackground(gd)
        val lnrHolder: LinearLayout = ViewHolder().getLinearView(holderView)
        lnrHolder.getLayoutParams().height = (height * 1.8).toInt()
        val checkBox: CheckBox = ViewHolder().getCheckBox(holderView)
        if (type == 1) {
            checkBox.visibility = View.VISIBLE
        } else {
            checkBox.visibility = View.GONE
        }
        checkBox.setOnClickListener { v -> // TODO Auto-generated method stub
            if (checkBoxTemp != null) {
                checkBoxTemp!!.isChecked = false
            }
            checkBoxTemp = v as CheckBox
            if (checkBox.isChecked) {
                ProductDetailActivity?.selectedFromColorList = colorModels!![position].colorcode
                ProductDetailActivity?.selectedIDFromColorList = colorModels!![position].id
            }
        }
        return holderView
    }

    val displayScale: Unit
        get() {
            mDisplayManager = DisplayManagerScale(mContext)
            width = mDisplayManager!!.deviceWidth
            height = mDisplayManager!!.deviceHeight
        }

    inner class ViewHolder {
        /**
         * @return the view
         */
        fun getColorView(holderView: View): RelativeLayout {
            return holderView.findViewById<View>(R.id.viewColor) as RelativeLayout
        }

        fun getCheckBox(holderView: View): CheckBox {
            return holderView.findViewById<View>(R.id.checkBoxColor) as CheckBox
        }

        fun getLinearView(holderView: View): LinearLayout {
            return holderView.findViewById<View>(R.id.lnrHolder) as LinearLayout
        }
    }
}