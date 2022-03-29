/**
 *
 */
package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.SalesRepOrderModel
import java.util.*

/**
 * Bibin
 */
class SalesOrderDetailAdapter : BaseAdapter {
    var mActivity: Activity
    var dealersModels: ArrayList<SalesRepOrderModel>
    var mLayoutInflater: LayoutInflater

    constructor(
        dealersModels: ArrayList<SalesRepOrderModel>,
        mActivity: AppCompatActivity
    ) {
        this.dealersModels = dealersModels
        this.mActivity = mActivity
        mLayoutInflater = LayoutInflater.from(mActivity)
    }

    constructor(
        dealersModels: ArrayList<SalesRepOrderModel>,
        mActivity: Activity
    ) {
        this.dealersModels = dealersModels
        this.mActivity = mActivity
        mLayoutInflater = LayoutInflater.from(mActivity)
    }

    override fun getCount(): Int {
return dealersModels.size   }

    // TODO Auto-generated method stub

    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return dealersModels.get(position)
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View? = null
        if (convertView == null) {
            view = mLayoutInflater.inflate(
                R.layout.custom_sales_order_detail_adapter, null
            )
        } else {
            view = convertView
        }
        val wraperClass: WraperClass = WraperClass(view)
        wraperClass.orderNoText
            .setText(
                (
                        "Product Name : "
                                + dealersModels.get(position).getmPrdctName())
            )
        wraperClass.productName.setText(
            dealersModels.get(position).getmPrdctDesc()
        )
        wraperClass.customerName
            .setText(
                (
                        "Customer Name : "
                                + dealersModels.get(position).getmOrderQty())
            )
        wraperClass.orderQtyText.setText(
            (
                    "Order Quantity : "
                            + dealersModels.get(position).getmOrderQty())
        )
        wraperClass.pendingText.setText(
            (
                    "Pending Quantity : "
                            + dealersModels.get(position).getmPendingQty())
        )
        wraperClass.orderDateText.setText(
            (
                    "Order Date : "
                            + VKCUtils.formatDateWithInput(
                        dealersModels.get(position).getmOrderDate(),
                        "dd MMM yyyy", "yyyy-MM-dd hh:mm:ss"
                    ))
        )
        return view!!
    }

    private inner class WraperClass constructor(view: View?) {
        var view: View? = null
        val orderNoText: TextView
            get() {
                return view!!.findViewById<View>(R.id.textViewOrderNo) as TextView
            }
        val productName: TextView
            get() {
                return view!!.findViewById<View>(R.id.textViewPrdctNo) as TextView
            }
        val customerName: TextView
            get() {
                return view!!.findViewById<View>(R.id.textViewCustomerName) as TextView
            }
        val orderQtyText: TextView
            get() {
                return view!!.findViewById<View>(R.id.textViewOrderQty) as TextView
            }
        val pendingText: TextView
            get() {
                return view!!.findViewById<View>(R.id.textViewPending) as TextView
            }
        val orderDateText: TextView
            get() {
                return view!!.findViewById<View>(R.id.textViewOrderDate) as TextView
            }

        init {
            this.view = view
            // TODO Auto-generated constructor stub
        }
    }
}