package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.customview.HorizontalListView
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType

class OrderDetailAdapter constructor(var mActivity: Activity) : BaseAdapter() {
    var mInflater: LayoutInflater
    public override fun getCount(): Int {
        // TODO Auto-generated method stub
        return AppController.subDealerOrderDetailList.size
    }

    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val toast: CustomToast = CustomToast(mActivity)
        var view: View? = null
        if (convertView == null) {
            if ((getUserType(mActivity) == "7")) {
                view = mInflater.inflate(
                    R.layout.custom_order_list_4_item,
                    null
                )
                val prodId: TextView = view.findViewById<View>(R.id.txtProdId) as TextView
                val size: TextView = view.findViewById<View>(R.id.txtSize) as TextView
                val qty: TextView = view.findViewById<View>(R.id.txtQuantity) as TextView
                val relColor: HorizontalListView = view
                    .findViewById<View>(R.id.listViewColor) as HorizontalListView
                prodId.setText(
                    AppController.subDealerOrderDetailList.get(position)
                        .productId
                )
                size.setText(
                    AppController.subDealerOrderDetailList.get(position)
                        .caseDetail
                )
                qty.setText(
                    AppController.subDealerOrderDetailList.get(position)
                        .quantity
                )
                relColor.setAdapter(
                    ColorGridAdapter(
                        mActivity,
                        AppController.subDealerOrderDetailList.get(position)
                            .color_code, 0
                    )
                )
            } else {
                view = mInflater.inflate(R.layout.custom_order_list, null)
                val edtQty: EditText = view.findViewById<View>(R.id.edtQty) as EditText
                val prodId: TextView = view.findViewById<View>(R.id.txtProdId) as TextView
                val size: TextView = view.findViewById<View>(R.id.txtSize) as TextView
                val qty: TextView = view.findViewById<View>(R.id.txtQuantity) as TextView
                val textColor: TextView = view.findViewById<View>(R.id.txtColor) as TextView
                if (AppController.isEditable) {
                    edtQty.setEnabled(true)
                } else {
                    edtQty.setEnabled(false)
                }
                prodId.setText(
                    AppController.subDealerOrderDetailList.get(position)
                        .productId
                )
                size.setText(
                    AppController.subDealerOrderDetailList.get(position)
                        .caseDetail
                )
                if (!(getUserType(mActivity) == "7")) {
                    edtQty.setText(
                        AppController.subDealerOrderDetailList.get(position)
                            .quantity
                    )
                }
                qty.setText(
                    AppController.subDealerOrderDetailList.get(position)
                        .quantity
                )
                textColor.setText(
                    AppController.subDealerOrderDetailList.get(position).color_name
                )
                edtQty.addTextChangedListener(object : TextWatcher {
                    public override fun onTextChanged(
                        s: CharSequence?, start: Int, before: Int,
                        count: Int
                    ) {
                        // TODO Auto-generated method stub
                        AppController.status = 2
                        try {
                            if ((edtQty.getText().toString() == "")) {
                                value = "0"
                            } else {
                                value = edtQty.getText().toString()
                            }
                            if (Integer.valueOf(value) > Integer
                                    .valueOf(
                                        AppController.subDealerOrderDetailList
                                            .get(position).quantity
                                    )
                            ) {
                                toast.show(27)
                                AppController.isSubmitError = true
                                AppController.listErrors.add(
                                    java.lang.String
                                        .valueOf(AppController.isSubmitError)
                                )
                            } else if ((Integer.valueOf(value) < Integer
                                    .valueOf(
                                        AppController.subDealerOrderDetailList
                                            .get(position).quantity
                                    )
                                        && Integer.valueOf(value) >= 0)
                            ) {
                                if (Integer.valueOf(value) > 0) {
                                    AppController.subDealerOrderDetailList.get(
                                        position
                                    ).quantity = value
                                    AppController.listErrors.clear()
                                } else {
                                    toast.show(29)
                                    AppController.isSubmitError = true
                                    AppController.listErrors.add(
                                        java.lang.String
                                            .valueOf(AppController.isSubmitError)
                                    )
                                }
                            } else {
                                AppController.isSubmitError = false
                                AppController.listErrors.clear()
                            }
                        } catch (e: Exception) {
                        }
                    }

                    public override fun beforeTextChanged(
                        s: CharSequence?, start: Int, count: Int,
                        after: Int
                    ) {
                        // TODO Auto-generated method stub
                    }

                    public override fun afterTextChanged(s: Editable?) {
                        // TODO Auto-generated method stub
                        AppController.status = 2
                    }
                })
            }
        } else {
            view = convertView
        }
        val rel1: RelativeLayout = view!!.findViewById<View>(R.id.rel1) as RelativeLayout
        val rel2: RelativeLayout = view.findViewById<View>(R.id.rel2) as RelativeLayout
        val rel3: RelativeLayout = view.findViewById<View>(R.id.rel3) as RelativeLayout
        val rel4: RelativeLayout = view.findViewById<View>(R.id.rel4) as RelativeLayout
        val rel5: RelativeLayout = view.findViewById<View>(R.id.rel5) as RelativeLayout

        /* Bibin Edited */if ((getUserType(mActivity) == "7")) {
            rel5.setVisibility(View.GONE)
        } else {
            rel5.setVisibility(View.VISIBLE)
        }
        if (position % 2 == 0) {
            rel1.setBackgroundColor(Color.rgb(219, 188, 188))
            rel2.setBackgroundColor(Color.rgb(219, 188, 188))
            rel3.setBackgroundColor(Color.rgb(219, 188, 188))
            rel4.setBackgroundColor(Color.rgb(219, 188, 188))
            rel5.setBackgroundColor(Color.rgb(219, 188, 188))
        } else {
            rel1.setBackgroundColor(Color.rgb(208, 208, 208))
            rel2.setBackgroundColor(Color.rgb(208, 208, 208))
            rel3.setBackgroundColor(Color.rgb(208, 208, 208))
            rel4.setBackgroundColor(Color.rgb(208, 208, 208))
            rel5.setBackgroundColor(Color.rgb(208, 208, 208))
        }
        if (!(getUserType(mActivity) == "7")) {
        }
        return (view)
    }

    companion object {
        var value: String? = null
    }

    init {
        mInflater = LayoutInflater.from(mActivity)
    }
}