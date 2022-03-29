package com.mobatia.vkcsalesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.mobatia.vkcsalesapp.R

/**
 * @author archana
 */
class FilterAdapter(private val mContext: Context, private val filterListString: Array<String>) :
    BaseAdapter() {
    private val mInflater: LayoutInflater
    var mFilterImageList: IntArray
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return filterListString.size
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
        // TODO Auto-generated method stub
        val view: View
        view = convertView ?: mInflater.inflate(R.layout.custom_filterlayout, null)
        val imgFilter = view.findViewById<View>(R.id.imgOption) as ImageView
        val txtFilter = view.findViewById<View>(R.id.txtOption) as TextView
        imgFilter.setImageResource(mFilterImageList[position])
        txtFilter.text = filterListString[position]
        return view
    }

    init {
        val ImageList = intArrayOf(
            R.drawable.category, R.drawable.category, R.drawable.men,
            R.drawable.brand, R.drawable.price, R.drawable.men, R.drawable.offers
        )
        mFilterImageList = ImageList
        mInflater = LayoutInflater.from(mContext)
    }
}