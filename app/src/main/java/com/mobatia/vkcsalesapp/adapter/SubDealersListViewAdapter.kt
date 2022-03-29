package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.model.DealersShopModel
import java.util.*

class SubDealersListViewAdapter constructor(
    dealersModels: ArrayList<DealersShopModel>,
    mActivity: Activity
) : BaseAdapter() {
    var mActivity: Activity
    var position: Int = 0
    var mLayoutInflater: LayoutInflater? = null
    public override fun getCount(): Int {
        // TODO Auto-generated method stub
        return AppController.dealersModels.size
    }

    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return AppController.dealersModels
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    internal class ViewHolder constructor() {
        var name: TextView? = null
        var address: TextView? = null
        var phone: TextView? = null
        var check_box: CheckBox? = null
    }

    public override fun getView(position: Int, Convertview: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        val mInflater: LayoutInflater = mActivity
            .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var view: View = Convertview
        if (view == null) {
            view = mInflater
                .inflate(R.layout.custom_subdealers_list_view, null)
            viewHolder = ViewHolder()
            viewHolder.name = view.findViewById<View>(R.id.textViewName) as TextView?
            viewHolder.address = view
                .findViewById<View>(R.id.textViewAddress) as TextView?
            viewHolder.phone = view.findViewById<View>(R.id.textViewPhone) as TextView?
            viewHolder.check_box = view
                .findViewById<View>(R.id.checkbox_dealer) as CheckBox?
            viewHolder.check_box!!.setOnCheckedChangeListener(null)
            viewHolder.check_box!!
                .setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
                    public override fun onCheckedChanged(
                        buttonView: CompoundButton,
                        isChecked: Boolean
                    ) {
                        val idValue: String = AppController.dealersModels.get(
                            position
                        ).id
                        val getPosition: Int = buttonView.getTag() as Int
                        AppController.dealersModels.get(getPosition)
                            .setSelected(isChecked)
                    }
                })
            view.setTag(viewHolder)
            view.setTag(R.id.textViewName, viewHolder.name)
            view.setTag(R.id.textViewAddress, viewHolder.address)
            view.setTag(R.id.textViewPhone, viewHolder.phone)
            view.setTag(R.id.checkbox_dealer, viewHolder.check_box)
        } else {
            viewHolder = view.getTag() as ViewHolder?
        }
        viewHolder!!.check_box!!.setTag(position)
        viewHolder.name!!.setText(
            AppController.dealersModels.get(position)
                .name
        )
        viewHolder.address!!.setText(
            AppController.dealersModels.get(position)
                .address
        )
        viewHolder.phone!!.setText(
            AppController.dealersModels.get(position)
                .phone
        )
        viewHolder.check_box!!.setChecked(
            AppController.dealersModels.get(
                position
            ).isSelected()
        )
        return view
    }

    init {
        AppController.dealersModels = dealersModels
        this.mActivity = mActivity
    }
}