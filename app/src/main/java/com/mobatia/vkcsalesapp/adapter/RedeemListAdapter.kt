package com.mobatia.vkcsalesapp.adapterimport

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.model.GiftListModel
import com.mobatia.vkcsalesapp.model.GiftUserModel
import java.util.*

class RedeemListAdapter constructor(
    var mContext: Activity,
    var listGift: ArrayList<GiftListModel>
) : BaseExpandableListAdapter(), VKCUrlConstants {
    var giftUserList: ArrayList<GiftUserModel>? = null
    var positionValue: Int = 0
    public override fun getGroupCount(): Int {
        return listGift.size
    }

    public override fun getChildrenCount(groupPosition: Int): Int {
        giftUserList = listGift.get(groupPosition).listGiftUser
        // return listTransaction.get(positionValue).getListHistory().size();
        return giftUserList?.size!!
    }

    public override fun getGroup(groupPosition: Int): Any {
        return giftUserList!!.get(groupPosition)
    }

    public override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return giftUserList!!.get(childPosition)
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
        val textPoints: TextView = convertView
            .findViewById<View>(R.id.textPoints) as TextView
        val textIcon: TextView = convertView.findViewById<View>(R.id.textIcon) as TextView
        textPoints.setText("Mobile :" + listGift.get(groupPosition).phone)
        textUser.setText(listGift.get(groupPosition).name)
        positionValue = groupPosition
        if (isExpanded) {
            textIcon.setText("-")
        } else {
            textIcon.setText("+")
        }
        return convertView
    }

    internal class ViewHolder constructor() {
        var textGiftType: TextView? = null
        var textGiftQuantity: TextView? = null
        var textGiftName: TextView? = null // ImageView imageGift;
    }

    public override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View, parent: ViewGroup
    ): View {
        /*
		 * System.out.println("Group Position:"+groupPosition+"Child Psoitiomn:"+
		 * childPosition);
		 * System.out.println("Count:"+listTransaction.get(groupPosition
		 * ).getListHistory().size());
		 */
        var viewHolder: RedeemListAdapter.ViewHolder? = null
        var v: View = convertView
        if (convertView == null) {
            val inflater: LayoutInflater = mContext
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_gift, null)
            viewHolder = RedeemListAdapter.ViewHolder()
            viewHolder.textGiftName = v.findViewById<View>(R.id.textGiftName) as TextView?
            viewHolder.textGiftType = v.findViewById<View>(R.id.textGiftType) as TextView?
            viewHolder.textGiftQuantity = v.findViewById<View>(R.id.textGiftQuantity) as TextView?
            v.setTag(viewHolder)
        } else {
            viewHolder = v.getTag() as RedeemListAdapter.ViewHolder?
        }

        // viewHolder.imageGift = (ImageView) v.findViewById(R.id.imageGift);

        // childPosition=childPosition-1;
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
        // String date=productList.get(childPosition).getDateValue();
        viewHolder!!.textGiftName?.setText(
            giftUserList!!.get(childPosition)
                .gift_title
        )
        viewHolder.textGiftType?.setText(
            giftUserList!!.get(childPosition)
                .gift_type
        )
        viewHolder.textGiftQuantity?.setText(
            giftUserList!!.get(childPosition)
                .quantity
        )
        /*
		 * if (!giftUserList.get(childPosition).getGift_image().equals("")) {
		 * Picasso.with(mContext)
		 * .load(giftUserList.get(childPosition).getGift_image())
		 * .placeholder(R.drawable.gift).into(viewHolder.imageGift); } else {
		 * 
		 * }
		 */
        // System.out.println("Position Value:"+childPosition);
        return v
    }

    public override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return false
    }
}