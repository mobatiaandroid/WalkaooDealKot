package com.mobatia.vkcsalesapp.adapterimport

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.SubDealerOrderListModel
import java.util.*

class PendingOrderListAdapter     // mLayoutInflater = LayoutInflater.from(mActivity);
constructor(
    var mActivity: Context,
    var subdealersModels: ArrayList<SubDealerOrderListModel>
) : BaseAdapter() {
    var mLayoutInflater: LayoutInflater? = null
    var status: String? = null
    public override fun getCount(): Int {
        // TODO Auto-generated method stub
        return subdealersModels.size
    }

    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View? = null
        val inflater: LayoutInflater = (mActivity as Activity).getLayoutInflater()
        if (convertView == null) {
            view = inflater.inflate(R.layout.pending_list_row, null)
            val textOrderNo: TextView = view
                .findViewById<View>(R.id.textViewOrderNO) as TextView
            val textName: TextView = view.findViewById<View>(R.id.textViewName) as TextView
            val textAddress: TextView = view
                .findViewById<View>(R.id.textViewAddress) as TextView
            val textStatus: TextView = view
                .findViewById<View>(R.id.textViewStatus) as TextView
            textOrderNo.setText(subdealersModels.get(position).orderid)
            textName.setText(
                ("Dealer:  "
                        + subdealersModels.get(position).name)
            )
            textAddress.setText(
                ("Place:  "
                        + subdealersModels.get(position).address)
            )
            if ((subdealersModels.get(position).status == "0")) {
                status = "Pending"
            } else if ((subdealersModels.get(position).status == "1")) {
                status = "Approved"
            } else if ((subdealersModels.get(position).status == "2")) {
                status = "Partially Approved"
            } else if ((subdealersModels.get(position).status == "3")) {
                status = "Rejected"
            }
            textStatus.setText(
                ("Status: "
                        + status)
            )
        } else {
            view = convertView
        }
        if (position % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            view!!.setBackgroundColor(
                mActivity.getResources().getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            view!!.setBackgroundColor(
                mActivity.getResources().getColor(
                    R.color.list_row_color_white
                )
            )
        }
        return (view)
    }
}