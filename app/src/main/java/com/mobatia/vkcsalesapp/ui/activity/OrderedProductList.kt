package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ColorGridAdapter
import com.mobatia.vkcsalesapp.customview.HorizontalListView
import com.mobatia.vkcsalesapp.model.ColorModel
import java.util.*

class OrderedProductList : AppCompatActivity() {
    private var mDealersListView: ListView? = null
    private var mSalesAdapter: SalesOrderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
    }

    protected fun getLayoutResourceId(): Int {
        // TODO Auto-generated method stub
        return R.layout.activity_subdealer_order_update
    }
    private fun init(savedInstanceState: Bundle?) {
        mDealersListView = findViewById<View>(R.id.dealersListView) as ListView
        mSalesAdapter = SalesOrderAdapter(this@OrderedProductList)
        mDealersListView!!.adapter = mSalesAdapter
    }
}

internal class SalesOrderAdapter(mActivity: AppCompatActivity) : BaseAdapter() {
    var mActivity: Activity
    var mInflater: LayoutInflater
    var colorArrayList = ArrayList<ColorModel>()
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return 1
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
        var view: View? = null
        if (convertView == null) {
            view = mInflater.inflate(R.layout.custom_ordered_product_list, null)
            val prodId = view.findViewById<View>(R.id.txtProdId) as TextView
            prodId.text = "1207"
            val size = view.findViewById<View>(R.id.txtSize) as TextView
            size.text = "8,9"
            val qty = view.findViewById<View>(R.id.txtQuantity) as TextView
            qty.text = "50"
            val relColor = view
                .findViewById<View>(R.id.listViewColor) as HorizontalListView
            val model1 = ColorModel()
            model1.colorcode = "#000000"
            val model2 = ColorModel()
            model2.colorcode = "#0000FF"
            val model3 = ColorModel()
            model3.colorcode = "#a52a2a"
            colorArrayList.add(0, model1)
            colorArrayList.add(1, model2)
            colorArrayList.add(2, model3)
            relColor.adapter = ColorGridAdapter(
                mActivity, colorArrayList,
                0
            )
        } else {
            view = convertView
        }
        return view!!
    }

    init {
        this.mActivity = mActivity
        mInflater = LayoutInflater.from(mActivity)
    }
}