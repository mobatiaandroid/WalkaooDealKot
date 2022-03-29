package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.RedeemReportModel
import com.mobatia.vkcsalesapp.ui.activity.RedeemReportDetailActivity
import java.util.*

class RedeemReportsAdapter(
    mActivity: Activity,
    listModel: ArrayList<RedeemReportModel>
) : BaseAdapter() {
    var mActivity: Activity
    var mLayoutInflater: LayoutInflater? = null
    var listModel: ArrayList<RedeemReportModel>
    override fun getCount(): Int {
        return listModel?.size!!
    }

    // TODO Auto-generated method stub

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return listModel.get(position)
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    internal class ViewHolder {
        var textCustId: TextView? = null
        var textCustName: TextView? = null
        var textCustPlace: TextView? = null
        var textCustNobile: TextView? = null
        var textView: TextView? = null
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        var v = view
        if (view == null) {
            val inflater: LayoutInflater = mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_redeem_report_list, null)
            viewHolder = ViewHolder()
            viewHolder.textCustId = v
                .findViewById<View>(R.id.txtViewCustId) as TextView
            viewHolder.textCustName = v.findViewById<View>(R.id.txtViewCustName) as TextView
            viewHolder.textCustPlace = v
                .findViewById<View>(R.id.txtViewPlace) as TextView
            viewHolder.textCustNobile = v
                .findViewById<View>(R.id.txtViewMobile) as TextView
            viewHolder.textView = v
                .findViewById<View>(R.id.txtViewDetail) as TextView
            v.tag = viewHolder
        } else {
            viewHolder = v.tag as ViewHolder
        }
        if (position % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            v.setBackgroundColor(
                mActivity.getResources().getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            v.setBackgroundColor(
                mActivity.getResources().getColor(
                    R.color.list_row_color_white
                )
            )
        }
        viewHolder!!.textCustId?.setText(listModel[position].custId)
        viewHolder.textCustName?.setText(listModel[position].custName)
        viewHolder.textCustPlace?.setText(listModel[position].custPlace)
        viewHolder.textCustNobile?.setText(listModel[position].custMobile)
        viewHolder.textView?.setOnClickListener(View.OnClickListener { // TODO Auto-generated method stub
            val intent = Intent(mActivity, RedeemReportDetailActivity::class.java)
            intent.putExtra("position", position.toString())
            intent.putExtra("cust_id", listModel[position].custId)
            mActivity.startActivity(intent)
        })

/*
			viewHolder.textDate.setText(orderList.getOrderDate());
			viewHolder.textName.setText(orderList.getName());
			System.out.println("Adapter Order Id"
					+ listModel.get(position).getOrderid());
			viewHolder.textAddress.setText(orderList.getTotalqty());
			viewHolder.textPhone.setText(status);
		*/return v
    }

    init {
        this.mActivity = mActivity
        this.listModel = listModel
        //this.notifyDataSetChanged();

        // mLayoutInflater = LayoutInflater.from(mActivity);
    }
}