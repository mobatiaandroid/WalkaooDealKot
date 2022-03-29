package com.mobatia.vkcsalesapp.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.NavDrawerItem
import java.util.*

class NavDrawerExpandableListAdapter     // TODO Auto-generated constructor stub
    (
    var mContext: Context,
    var navDrawerItems: ArrayList<NavDrawerItem>,
    var hashMapNavDrawerItemChild: HashMap<NavDrawerItem, ArrayList<NavDrawerItem>>,
    var onExploreListener: OnExploreListener
) : BaseExpandableListAdapter() {
    override fun getGroupCount(): Int {
        // TODO Auto-generated method stub
        return navDrawerItems.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        // TODO Auto-generated method stub
        return if (hashMapNavDrawerItemChild[navDrawerItems[groupPosition]] != null
        ) hashMapNavDrawerItemChild[navDrawerItems[groupPosition]]!!.size else {
            0
        }
    }

    override fun getGroup(groupPosition: Int): Any {
        // TODO Auto-generated method stub
        return navDrawerItems[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        // TODO Auto-generated method stub
        return hashMapNavDrawerItemChild[navDrawerItems[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        // TODO Auto-generated method stub
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        // TODO Auto-generated method stub
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        // TODO Auto-generated method stub
        return false
    }

    override fun getGroupView(
        groupPosition: Int, isExpanded: Boolean,
        convertView: View, parent: ViewGroup
    ): View {
        var convertView = convertView
        val headerTitle = getGroup(groupPosition) as NavDrawerItem
        if (convertView == null) {
            val infalInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_group, null)
        }
        val imgIcon = convertView.findViewById<View>(R.id.icon) as ImageView
        val lblListHeader = convertView
            .findViewById<View>(R.id.lblListHeader) as TextView
        val explore = convertView
            .findViewById<View>(R.id.explore) as TextView
        val exploreRl = convertView
            .findViewById<View>(R.id.exploreRl) as RelativeLayout
        val textBaseRl = convertView
            .findViewById<View>(R.id.textBaseRl) as RelativeLayout
        if (groupPosition <= 9) {
            imgIcon.setImageResource(headerTitle.icon)
            imgIcon.visibility = View.VISIBLE
        } else {
            imgIcon.visibility = View.INVISIBLE
            if (headerTitle.title.equals("Locate us", ignoreCase = true)) {
                imgIcon.visibility = View.VISIBLE
                imgIcon.setImageResource(R.drawable.location)
            } else if (headerTitle.title.equals("Contact us", ignoreCase = true)) {
                imgIcon.visibility = View.VISIBLE
                imgIcon.setImageResource(R.drawable.brand)
            } else if (headerTitle.title.contains("Version")) {
                if (groupPosition == 0) {
                    imgIcon.visibility = View.INVISIBLE
                }
                // imgIcon.setVisibility(View.INVISIBLE);
            } else {
                imgIcon.setImageResource(R.drawable.brand)
            }
        }
        lblListHeader.setTypeface(null, Typeface.BOLD)
        lblListHeader.text = headerTitle.title
        if (navDrawerItems[groupPosition].id.toInt() < 0) {
            exploreRl.visibility = View.INVISIBLE
        } else {
            exploreRl.visibility = View.VISIBLE
        }
        exploreRl.setOnClickListener {
            if (explore.text.toString() == "+") {
                onExploreListener.onExpandGroup(groupPosition)
                explore.text = "-"
            } else {
                onExploreListener.onCollapseGrope(groupPosition)
                explore.text = "+"
            }
        }
        textBaseRl.setOnClickListener { onItemClickListener!!.onItemSelected(navDrawerItems[groupPosition].id) }
        return convertView
    }

    override fun getChildView(
        groupPosition: Int, childPosition: Int,
        isLastChild: Boolean, convertView: View, parent: ViewGroup
    ): View {
        var convertView = convertView
        val childText = getChild(
            groupPosition,
            childPosition
        ) as NavDrawerItem
        if (convertView == null) {
            val infalInflater = mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = infalInflater.inflate(R.layout.list_item, null)
        }
        val txtListChild = convertView
            .findViewById<View>(R.id.lblListItem) as TextView
        txtListChild.text = childText.title
        convertView.setOnClickListener {
            onItemClickListener!!.onItemSelected(
                hashMapNavDrawerItemChild[navDrawerItems[groupPosition]]
                    ?.get(childPosition)?.id
            )
        }
        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        // TODO Auto-generated method stub
        return true
    }

    var onItemClickListener: OnItemClickListener? = null
    @JvmName("setOnItemClickListener1")
    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemSelected(id: String?)
    }

    interface OnExploreListener {
        fun onExpandGroup(position: Int)
        fun onCollapseGrope(position: Int)
    }
}