/**
 *
 */
package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.model.CaseModel
import java.util.*

/**
 * @author Archana S
 */
class SizeGridAdapter constructor(var mContext: Context, caseModels: ArrayList<CaseModel>?) :
    BaseAdapter() {
    var caseModels: ArrayList<CaseModel>?
    var mInflater: LayoutInflater
    private val view: View? = null
    var mDisplayManager: DisplayManagerScale? = null
    var width: Int = 0
    var height: Int = 0
    override fun getCount(): Int {
        return caseModels?.size!!
    }

    // TODO Auto-generated method stub


    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    var checkBoxTemp: CheckBox? = null

    @SuppressLint("NewApi")
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val holderView: View
        if (convertView == null) {
            holderView = mInflater.inflate(
                R.layout.custom_size_grid, parent,
                false
            )
        } else {
            holderView = convertView
        }
        val relativeMain: RelativeLayout = ViewHolder().getColorView(holderView)
        val txtSize: TextView = ViewHolder().getTextView(holderView)
        txtSize.setText(caseModels!!.get(position).name)
        val lnrHolder: LinearLayout = ViewHolder().getLinearView(holderView)
        lnrHolder.getLayoutParams().height = (height * 1.8).toInt()

        // Toast.makeText(mContext,
        // "Height:"+height+","+lnrHolder.getLayoutParams().height,
        // 1000).show();
        val checkBox: CheckBox = ViewHolder().getCheckBox(holderView)
        checkBox.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                // TODO Auto-generated method stub
                if (checkBoxTemp != null) {
                    checkBoxTemp!!.setChecked(false)
                }
                checkBoxTemp = v as CheckBox?
                if (checkBox.isChecked()) {
                    ProductDetailActivity.selectedFromSizeList =
                        caseModels!!.get(position).name
                    ProductDetailActivity.selectedIDFromSizeList =
                        caseModels!!.get(position).id
                    //AppController.size=caseModels.get(position).getName();
                }
            }
        })
        return holderView
    }

    val displayScale: Unit
        get() {
            mDisplayManager = DisplayManagerScale(mContext)
            width = mDisplayManager?.deviceWidth!!
            height = mDisplayManager?.deviceHeight!!
        }

    inner class ViewHolder constructor() {
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

        fun getTextView(holderView: View): TextView {
            return holderView.findViewById<View>(R.id.textView1) as TextView
        }
    }

    init {
        this.caseModels = caseModels
        mInflater = LayoutInflater.from(mContext)
        displayScale
    }
}