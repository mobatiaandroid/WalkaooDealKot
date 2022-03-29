package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.ReportDetailModel
import java.util.*

class ReportDetailAdapter     // this.notifyDataSetChanged();
// mLayoutInflater = LayoutInflater.from(mActivity);
    (
    var mActivity: Activity,
    var listModel: ArrayList<ReportDetailModel>
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
        var textGiftName: TextView? = null
        var textGiftQty: TextView? = null
        var textRewardPoints: TextView? = null
        var textTotalCoupons: TextView? = null
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        var v = view
        if (view == null) {
            val inflater = mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_report_detail, null)
            viewHolder = ViewHolder()
            viewHolder.textGiftName = v
                .findViewById<View>(R.id.txtViewGift) as TextView
            viewHolder.textGiftQty = v
                .findViewById<View>(R.id.txtViewGiftQty) as TextView
            viewHolder.textRewardPoints = v
                .findViewById<View>(R.id.txtViewRedeemPoints) as TextView
            viewHolder.textTotalCoupons = v
                .findViewById<View>(R.id.txtViewTotalCoupons) as TextView
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
        viewHolder!!.textGiftName!!.text = listModel[position].gift_name
        viewHolder.textGiftQty!!.text = listModel[position].gift_qty
        viewHolder.textRewardPoints
            ?.setText(listModel[position].rwd_points)
        viewHolder.textTotalCoupons!!.text = listModel[position]
            .tot_coupons
        return v
    }
}