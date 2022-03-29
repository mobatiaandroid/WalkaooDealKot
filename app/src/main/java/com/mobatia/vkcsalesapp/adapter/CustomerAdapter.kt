package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.CustomerModel
import java.util.*

class CustomerAdapter(
    var mActivity: Activity,
    var listModel: ArrayList<CustomerModel>
) : BaseAdapter() {
    var mLayoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return listModel.size
    }

    override fun getItem(position: Int): ArrayList<CustomerModel> {
        // TODO Auto-generated method stub
        return listModel
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    internal class ViewHolder {
        var textName: TextView? = null
        var textRole: TextView? = null
        var textPhone: TextView? = null
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        var v = view
        if (view == null) {
            val inflater = mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_cust_list, null)
            viewHolder = ViewHolder()
            viewHolder.textName = v
                .findViewById<View>(R.id.textName) as TextView
            viewHolder.textPhone = v.findViewById<View>(R.id.textPhone) as TextView
            viewHolder.textRole = v
                .findViewById<View>(R.id.textRole) as TextView
            v.tag = viewHolder
        } else {
            viewHolder = v.tag as ViewHolder
        }
        if (position % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            v.setBackgroundColor(
                mActivity.resources.getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            v.setBackgroundColor(
                mActivity.resources.getColor(
                    R.color.list_row_color_white
                )
            )
        }
        viewHolder.textName!!.text = listModel[position].name
        viewHolder!!.textPhone!!.text = listModel[position].phone
        viewHolder.textRole!!.text = listModel[position].role
        return v
    }
}