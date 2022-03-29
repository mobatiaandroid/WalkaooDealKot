package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.model.HistoryModel
import com.mobatia.vkcsalesapp.model.TransactionModel
import java.util.*

class TransactionHistoryAdapter constructor(
    var mContext: Activity,
    var listTransaction: List<TransactionModel>
) : BaseExpandableListAdapter(), VKCUrlConstants {
    var productList: ArrayList<HistoryModel>? = null
    var positionValue: Int = 0
    public override fun getGroupCount(): Int {
        return listTransaction.size
    }

    public override fun getChildrenCount(groupPosition: Int): Int {
        productList = listTransaction.get(groupPosition).listHistory
        //return listTransaction.get(positionValue).getListHistory().size();
        return productList?.size!!
    }

    public override fun getGroup(groupPosition: Int): Any {
        return listTransaction.get(groupPosition)
    }

    public override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return productList!!.get(childPosition)
    }

    public override fun getGroupId(groupPosition: Int): Long {
        return 0
    }

    public override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return 0
    }

    public override fun hasStableIds(): Boolean {
        return false
    }

    public override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View, parent: ViewGroup
    ): View {
        var convertView: View = convertView
        if (convertView == null) {
            val infalInflater: LayoutInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(
                R.layout.item_history_parent,
                null
            )
        }
        val textUser: TextView = convertView.findViewById<View>(R.id.textUser) as TextView
        val textPoints: TextView = convertView.findViewById<View>(R.id.textPoints) as TextView
        val textIcon: TextView = convertView.findViewById<View>(R.id.textIcon) as TextView
        textPoints.setText(listTransaction.get(groupPosition).totalPoints + " Coupons")
        textUser.setText(listTransaction.get(groupPosition).userName)
        positionValue = groupPosition
        if (isExpanded) {
            textIcon.setText("-")
        } else {
            textIcon.setText("+")
        }
        return convertView
    }

    internal class ViewHolder constructor() {
        var textType: TextView? = null
        var textPoints: TextView? = null
        var textToUser: TextView? = null
        var textDate: TextView? = null
    }

    public override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View, parent: ViewGroup
    ): View {
        /*System.out.println("Group Position:"+groupPosition+"Child Psoitiomn:"+childPosition);
		System.out.println("Count:"+listTransaction.get(groupPosition).getListHistory().size());*/
        var viewHolder: ViewHolder? = null
        var v: View = convertView
        if (convertView == null) {
            val inflater: LayoutInflater = mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_history, null)
            viewHolder = ViewHolder()
            v.setTag(viewHolder)
        }
        viewHolder = v.getTag() as ViewHolder?
        viewHolder!!.textType = v.findViewById<View>(R.id.textType) as TextView?
        viewHolder.textPoints = v.findViewById<View>(R.id.textPoints) as TextView?
        viewHolder.textToUser = v.findViewById<View>(R.id.textToUser) as TextView?
        viewHolder.textDate = v.findViewById<View>(R.id.textDate) as TextView?
        //childPosition=childPosition-1;
        if (childPosition % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            v.setBackgroundColor(
                mContext.getResources().getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            v.setBackgroundColor(
                mContext.getResources().getColor(
                    R.color.list_row_color_white
                )
            )
        }
        //	String date=productList.get(childPosition).getDateValue();
        viewHolder.textType!!.setText(productList!!.get(childPosition).type)
        viewHolder.textPoints!!.setText(productList!!.get(childPosition).points)
        if (productList!!.get(childPosition).to_name.length > 0) {
            if (productList!!.get(childPosition).to_role.length > 0) {
                viewHolder.textToUser!!.setText(
                    (productList!!.get(childPosition)
                        .to_name
                            + " / "
                            + productList!!.get(childPosition).to_role)
                )
            } else {
                /*viewHolder.textToUser.setText(productList.get(childPosition)
						.getTo_name());*/
                viewHolder.textToUser!!.setText(
                    "INV: " + productList!!.get(childPosition)
                        .invoiceNo
                )
            }
        } else {
            viewHolder.textToUser!!.setText("")
        }
        //System.out.println("Position Value:"+childPosition);
        viewHolder.textDate!!.setText(productList!!.get(childPosition).dateValue)
        return v
    }

    public override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}