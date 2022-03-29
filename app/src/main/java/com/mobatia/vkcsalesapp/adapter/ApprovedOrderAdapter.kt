package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.model.Pending_Order_Model
import java.util.*

class ApprovedOrderAdapter(
    var mActivity: Activity,
    var listPending: ArrayList<Pending_Order_Model>
) : BaseAdapter() {
    var mInflater: LayoutInflater
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return listPending.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val toast = CustomToast(mActivity)
        var view: View? = null
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_pending_order, null)
            val approvedQty = view.findViewById<View>(R.id.txtApproved) as TextView
            val prodId = view.findViewById<View>(R.id.txtProdId) as TextView
            val size = view.findViewById<View>(R.id.txtSize) as TextView
            val qty = view.findViewById<View>(R.id.txtQuantity) as TextView
            val textColor = view.findViewById<View>(R.id.txtColor) as TextView
            prodId.text = listPending[position]
                .productId
            size.text = listPending[position]
                .caseDetail
            qty.text = listPending[position]
                .quantity
            textColor.text = listPending[position].color_name
            approvedQty.text = listPending[position].approved_qty
        } else {
            view = convertView
        }
        val rel1 = view!!.findViewById<View>(R.id.rel1) as RelativeLayout
        val rel2 = view.findViewById<View>(R.id.rel2) as RelativeLayout
        val rel3 = view.findViewById<View>(R.id.rel3) as RelativeLayout
        val rel4 = view.findViewById<View>(R.id.rel4) as RelativeLayout
        val rel5 = view.findViewById<View>(R.id.rel5) as RelativeLayout

        /* Bibin Edited */if (getUserType(mActivity) == "7") {
            rel5.visibility = View.GONE
        } else {
            rel5.visibility = View.VISIBLE
        }
        if (position % 2 == 0) {
            rel1.setBackgroundColor(Color.rgb(219, 188, 188))
            rel2.setBackgroundColor(Color.rgb(219, 188, 188))
            rel3.setBackgroundColor(Color.rgb(219, 188, 188))
            rel4.setBackgroundColor(Color.rgb(219, 188, 188))
            rel5.setBackgroundColor(Color.rgb(219, 188, 188))
        } else {
            rel1.setBackgroundColor(Color.rgb(208, 208, 208))
            rel2.setBackgroundColor(Color.rgb(208, 208, 208))
            rel3.setBackgroundColor(Color.rgb(208, 208, 208))
            rel4.setBackgroundColor(Color.rgb(208, 208, 208))
            rel5.setBackgroundColor(Color.rgb(208, 208, 208))
        }
        if (getUserType(mActivity) != "7") {
        }
        return view
    }

    companion object {
        var value: String? = null
    }

    init {
        mInflater = LayoutInflater.from(mActivity)
    }
}