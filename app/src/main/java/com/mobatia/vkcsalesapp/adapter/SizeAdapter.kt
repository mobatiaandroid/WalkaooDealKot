package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.model.QuickSizeModel
import java.util.*

class SizeAdapter constructor(
    mContext: Activity,
    sizeArrayList: ArrayList<QuickSizeModel>?
) : BaseAdapter() {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private val sizeArrayList: ArrayList<QuickSizeModel>? = null
    var tempArrayList: ArrayList<QuickSizeModel>? = null
    public override fun getCount(): Int {
        // TODO Auto-generated method stub
        // return filterListString.length;
        return AppController.arrayListSize.size
    }

    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return AppController.arrayListSize.get(position)
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        var view: View? = null
        val chechBoxType: CheckBox? = null
        var holder: ViewHolder? = null
        if (convertView == null) {
            holder = ViewHolder()
            view = mInflater.inflate(R.layout.item_size_adapter, null)
            holder.checkboxSize = view
                .findViewById<View>(R.id.checkBoxSize) as CheckBox?
            holder.sizeName = view
                .findViewById<View>(R.id.textSizeName) as TextView?
            view.setTag(holder)
        } else {
            holder = view!!.getTag() as ViewHolder?
        }

        // gd.setStroke(strokeWidth, strokeColor);
//System.out.println("Size Name: "+sizeArrayList.get(position).getSizeName());
        //holder.checkboxSize.setText(sizeArrayList.get(position).getSizeName());
        holder!!.sizeName!!.setText(AppController.arrayListSize.get(position).sizeName)
        holder.checkboxSize!!.setOnCheckedChangeListener(object :
            CompoundButton.OnCheckedChangeListener {
            public override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    //AppController.sizeArrayList.get(position).setSelected(isChecked);
                    AppController.arrayListSize.get(position).setSelected(true)
                } else {
                    AppController.arrayListSize.get(position).setSelected(false)
                }
            }
        })

        /* To retain a checkbox selection state */
        /*
		 * if (tempArrayList.contains(sizeArrayList.get(position))) {
		 * chechBoxType.setChecked(true); //
		 * Log.v("LOG","04112014 "+filterArrayList.get(position).getName() // );
		 * } else { chechBoxType.setChecked(false); //
		 * Log.v("LOG","04112014 "+filterArrayList.get(position).getName() // );
		 * }
		 */return (view)!!
    }

    internal class ViewHolder constructor() {
        var checkboxSize: CheckBox? = null
        var sizeName: TextView? = null
    }

    init {
        this.mContext = mContext
        if (sizeArrayList != null) {
            AppController.arrayListSize = sizeArrayList
        }
        mInflater = LayoutInflater.from(mContext)
    }
}