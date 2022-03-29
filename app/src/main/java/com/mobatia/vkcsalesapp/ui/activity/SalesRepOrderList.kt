package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.SalesRepOrderListViewAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel
import java.util.*

class SalesRepOrderList : AppCompatActivity(), VKCUrlConstants {
    var salesRepOrderListView: ListView? = null
    var salesRepOrderModels = ArrayList<SalesRepOrderModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salesrep_order_list)
        setActionBar()
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        salesRepOrderListView = findViewById<View>(R.id.salesRepOrderListView) as ListView
        salesRepOrderListView!!.adapter = SalesRepOrderListViewAdapter(
            salesRepOrderModels, this@SalesRepOrderList
        )
        setItemSelectListener(salesRepOrderListView)
    }

    private fun setItemSelectListener(salesRepOrderListView2: ListView?) {
        // TODO Auto-generated method stub
        salesRepOrderListView
            ?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id -> // TODO Auto-generated method stub
                val intent = Intent(this@SalesRepOrderList, OrderedProductList::class.java)
                startActivity(intent)
            })
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}