package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel

class DispatchedListAdapter(
    var mActivity: Activity,
    var listModel: List<SubDealerOrderListModel>
) : BaseAdapter() {
    var mLayoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return listModel.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return listModel
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    internal class ViewHolder {
        var textOrderNo: TextView? = null
        var textName: TextView? = null
        var textAddress: TextView? = null
        var textPhone: TextView? = null
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        var v = view
        if (view == null) {
            val inflater = mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.custom_subdealer_list, null)
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
        viewHolder = ViewHolder()
        viewHolder.textOrderNo = v
            .findViewById<View>(R.id.textViewOrderNO) as TextView
        viewHolder.textName = v.findViewById<View>(R.id.textViewName) as TextView
        viewHolder.textAddress = v
            .findViewById<View>(R.id.textViewAddress) as TextView
        viewHolder.textPhone = v.findViewById<View>(R.id.textViewPhone) as TextView
        v.tag = viewHolder
        val orderList = listModel[position]
        if (orderList != null) {
            val status_value = orderList.status
            var status = ""
            if (status_value == "0") {
                status = "Pending"
            } else if (status_value == "1") {
                status = "Pending dispatch"
            } else if (status_value == "2") {
                status = "Pending dispatch"
            } else if (status_value == "3") {
                status = "Rejected"
            } else if (status_value == "4") {
                status = "Dispatched"
            }
            viewHolder.textOrderNo!!.text = orderList.orderid
            viewHolder.textName!!.text = orderList.orderDate
            viewHolder.textAddress!!.text = orderList.totalqty
            viewHolder.textPhone!!.text = status
        }
        return v
    }
}