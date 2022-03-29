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
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.model.DispatchedDetailModel
import java.util.*

class DispatchedListDetailAdapter(
    var mActivity: Activity,
    var listDespatch: ArrayList<DispatchedDetailModel>
) : BaseAdapter() {
    var mInflater: LayoutInflater
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return listDespatch.size
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
        var view = convertView
        val holder: ViewHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.item_dispatched_detail, null)
            holder = ViewHolder()
            holder.despatchedQty = view
                .findViewById<View>(R.id.txtDespatchedQuantity) as TextView
            holder.prodId = view.findViewById<View>(R.id.txtProdId) as TextView
            holder.size = view.findViewById<View>(R.id.txtSize) as TextView
            holder.approved_qty = view
                .findViewById<View>(R.id.txtApprovedQty) as TextView
            holder.textColor = view.findViewById<View>(R.id.txtColor) as TextView
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        holder.despatchedQty!!.text = listDespatch[position].quantity
        holder.prodId!!.text = listDespatch[position].productId
        holder.size!!.text = listDespatch[position].caseDetail
        holder.approved_qty!!.text = listDespatch[position].approved_qty
        holder.textColor!!.text = listDespatch[position].color_name
        val rel1 = view.findViewById<View>(R.id.rel1) as RelativeLayout
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
        return view
    }

    internal class ViewHolder {
        var despatchedQty: TextView? = null
        var prodId: TextView? = null
        var size: TextView? = null
        var approved_qty: TextView? = null
        var textColor: TextView? = null
    }

    companion object {
        var value: String? = null
    }

    init {
        mInflater = LayoutInflater.from(mActivity)
    }
}