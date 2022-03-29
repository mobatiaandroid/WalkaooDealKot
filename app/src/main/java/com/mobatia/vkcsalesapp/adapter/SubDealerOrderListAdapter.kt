package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel

/*                                                           Duplication Issue Corrected                  */
class SubDealerOrderListAdapter     //this.notifyDataSetChanged();
//System.out.println("Length" + listModel.size());
// mLayoutInflater = LayoutInflater.from(mActivity);
    (
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
        return listModel.get(position)
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
        var textDate: TextView? = null
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        var v = view
        if (view == null) {
            val inflater = mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.custom_subdealer_list, null)
            viewHolder = ViewHolder()
            viewHolder.textOrderNo = v
                .findViewById<View>(R.id.textViewOrderNO) as TextView
            viewHolder.textName = v.findViewById<View>(R.id.textViewName) as TextView
            viewHolder.textAddress = v
                .findViewById<View>(R.id.textViewAddress) as TextView
            viewHolder.textPhone = v
                .findViewById<View>(R.id.textViewPhone) as TextView
            viewHolder.textDate = v
                .findViewById<View>(R.id.textViewOrderDate) as TextView
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
        val orderList = listModel[position]
        if (orderList != null) {
            val status_value = orderList.status
            var status = ""
            if (status_value == "0") {
                status = "Pending"
            } else if (status_value == "1") {
                status = "Approved"
            } else if (status_value == "2") {
                status = "Pending dispatch"
            } else if (status_value == "3") {
                status = "Rejected"
            } else if (status_value == "4") {
                status = "Dispatched"
            }
            if (status_value == "1" || status_value == "4") {
                viewHolder!!.textOrderNo
                   ?.setText(orderList.parent_order_id)
            } else {
                viewHolder!!.textOrderNo
                    ?.setText(orderList.orderid)
            }
            if (getUserType(mActivity) == "4") {
                viewHolder.textDate!!.visibility = View.GONE
            } else {
                viewHolder.textDate!!.visibility = View.VISIBLE
                viewHolder.textDate!!.text = orderList.orderDate
            }
            viewHolder.textName!!.text = orderList.name
            viewHolder.textAddress!!.text = orderList.totalqty
            viewHolder.textPhone!!.text = status
        }
        return v
    }
}