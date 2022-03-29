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
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.CreditPaymentModel
import java.util.*

/**
 * Bibin
 */
class CreditPaymentStatusAdapter(
    var mActivity: Activity,
    var creditStatusModels: ArrayList<CreditPaymentModel>
) : BaseAdapter() {
    var mInflater: LayoutInflater
    override fun getCount(): Int {
        return creditStatusModels.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View? = null
        view = convertView ?: mInflater
            .inflate(R.layout.custom_creditpayment_status, null)
        val wraperClass: WraperClass = WraperClass(view)
        wraperClass.customerNameTxt.text = (
                "Customer name :  "
                        + creditStatusModels[position].getmName())
        wraperClass.customerPlaceTxt.text = "Place :  " + creditStatusModels[position].city
        wraperClass.pendingOrderOneTxt.text = creditStatusModels[position].getmType1100()
        wraperClass.pendingOrderTwoTxt.text = creditStatusModels[position].getmType1200()
        wraperClass.pendingOrderThreeTxt.text = creditStatusModels[position].getmType1300()
        wraperClass.pendingOrderFourTxt.text = creditStatusModels[position].getmType1400()
        wraperClass.pendingOrderFiveTxt.text = creditStatusModels[position].getmType1500()
        wraperClass.pendingOrderSixTxt.text = creditStatusModels[position].getmType1600()
        wraperClass.pendingOrderSevenTxt.text = creditStatusModels[position].getmType2000()
        wraperClass.totalBalance.text = creditStatusModels[position].getmTotalBalance()
        wraperClass.totalDue.text = creditStatusModels[position].getmTotalDue()
        return view!!
    }

    private inner class WraperClass(view: View?) {
        var view: View? = null
        val customerNameTxt: TextView
            get() = view!!.findViewById<View>(R.id.textViewName) as TextView
        val customerPlaceTxt: TextView
            get() = view!!.findViewById<View>(R.id.textViewPlace) as TextView
        val pendingOrderOneTxt: TextView
            get() = view!!.findViewById<View>(R.id.textViewPendingOrdOneData) as TextView
        val pendingOrderTwoTxt: TextView
            get() = view!!.findViewById<View>(R.id.textViewPendingOrdTwoData) as TextView
        val pendingOrderThreeTxt: TextView
            get() = view
                ?.findViewById<View>(R.id.textViewPendingOrdThreeData) as TextView
        val pendingOrderFourTxt: TextView
            get() = view
                ?.findViewById<View>(R.id.textViewPendingOrdFourData) as TextView
        val pendingOrderFiveTxt: TextView
            get() = view
                ?.findViewById<View>(R.id.textViewPendingOrdFiveData) as TextView
        val pendingOrderSixTxt: TextView
            get() = view!!.findViewById<View>(R.id.textViewPendingOrdSixData) as TextView
        val pendingOrderSevenTxt: TextView
            get() = view!!.findViewById<View>(R.id.textViewPendingOrdSevenData) as TextView
        val totalBalance: TextView
            get() = view!!.findViewById<View>(R.id.textViewTotalBalanceData) as TextView
        val totalDue: TextView
            get() = view!!.findViewById<View>(R.id.textViewTotalDueData) as TextView

        init {
            this.view = view
            // TODO Auto-generated constructor stub
        }
    }

    init {
        mInflater = LayoutInflater.from(mActivity)
    }
}