package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.NavDrawerItem
import java.io.Serializable
import java.util.*

class NavDrawerListAdapter constructor(
    private val context: Context,
    private val navDrawerItems: ArrayList<NavDrawerItem>
) : BaseAdapter(), Serializable {
    public override fun getCount(): Int {
        return navDrawerItems.size
    }

    public override fun getItem(position: Int): Any {
        return navDrawerItems.get(position)
    }

    public override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView: View = convertView
        if (convertView == null) {
            val mInflater: LayoutInflater = context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = mInflater.inflate(R.layout.drawer_list_item, null)
        }
        val imgIcon: ImageView = convertView.findViewById<View>(R.id.icon) as ImageView
        val txtTitle: TextView = convertView.findViewById<View>(R.id.title) as TextView
        val divider: View = convertView.findViewById(R.id.viewDivider) as View
        imgIcon.setImageResource(navDrawerItems.get(position).icon)
        if (navDrawerItems.get(position).title.contains("Version")) {
            divider.setVisibility(View.GONE)
            txtTitle.setTextColor(context.getResources().getColor(R.color.vkcred))
            txtTitle.setText(navDrawerItems.get(position).title)
        } else {
            txtTitle.setText(navDrawerItems.get(position).title)
            divider.setVisibility(View.VISIBLE)
        }
        return convertView
    }
}