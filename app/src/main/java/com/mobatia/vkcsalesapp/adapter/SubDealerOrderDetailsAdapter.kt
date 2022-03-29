package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel
import java.util.*

class SubDealerOrderDetailsAdapter     // this.subdealersModels = subdealersModels;
// System.out.println("Sub Dealer Details" + subdealersModels.size());
    (
    var mActivity: Activity,
    subdealersModels: ArrayList<SubDealerOrderDetailModel?>?
) : BaseAdapter() {
    // ArrayList<SubDealerOrderDetailModel> subdealersModels;
    var mLayoutInflater: LayoutInflater? = null
    var value: String? = null
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return AppController.subDealerOrderDetailList.size
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
        val toast = CustomToast(mActivity)
        val mLayoutInflater = mActivity
            .layoutInflater
        if (convertView == null) {
            view = mLayoutInflater.inflate(
                R.layout.subdealer_order_detail,
                null
            )
            val approvedqty = view
                .findViewById<View>(R.id.textViewApprovedQuantity) as TextView
            val txtProductId = view
                .findViewById<View>(R.id.textViewProductId) as TextView
            val txtDescription = view
                .findViewById<View>(R.id.textViewDescription) as TextView
            val txtQuantity = view
                .findViewById<View>(R.id.textViewQuantity) as TextView
            val txtDate = view
                .findViewById<View>(R.id.textViewSubOrderDate) as TextView
            val edtQuantity = view
                .findViewById<View>(R.id.edtQuantity) as EditText
            val txtCase = view.findViewById<View>(R.id.textViewCase) as TextView
            if (getUserType(mActivity) == "7") {
                edtQuantity.visibility = View.GONE
                approvedqty.visibility = View.GONE
                txtCase.visibility = View.GONE
            } else {
                edtQuantity.visibility = View.VISIBLE
                approvedqty.visibility = View.VISIBLE
                txtCase.visibility = View.VISIBLE
            }
            val updateButton = view
                .findViewById<View>(R.id.updateButton) as RelativeLayout
            txtProductId.text = ("Product Id : "
                    + AppController.subDealerOrderDetailList[position]
                .productId)
            txtDescription.text = ("Description : "
                    + AppController.subDealerOrderDetailList[position]
                .description)
            txtQuantity.text = ("Order Quantity :"
                    + AppController.subDealerOrderDetailList[position]
                .quantity)
            txtCase.text = "Case : " + AppController.subDealerOrderDetailList[position].caseDetail
            edtQuantity.setText(AppController.subDealerOrderDetailList[position].quantity)
            txtDate.text = ("Order Date : "
                    + AppController.subDealerOrderDetailList[position]
                .orderDate)

            /*
			 * VKCUtils.formatDateWithInput(
			 * AppController.subDealerOrderDetailList
			 * .get(position).getOrderDate(), "dd MMM yyyy",
			 * "yyyy-MM-dd hh:mm:ss"));
			 */edtQuantity.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(
                    s: CharSequence, start: Int,
                    before: Int, count: Int
                ) {
                    // TODO Auto-generated method stub
                    AppController.status = 2
                }

                override fun beforeTextChanged(
                    s: CharSequence, start: Int,
                    count: Int, after: Int
                ) {
                    // TODO Auto-generated method stub
                }

                override fun afterTextChanged(s: Editable) {
                    // TODO Auto-generated method stub

                    AppController.status = 2;

                    try {

                        if (edtQuantity.getText().toString().trim().equals("")) {
                            value = "0";
                        } else {
                            value = edtQuantity.getText().toString().trim();
                        }
                        if (Integer.valueOf(value) > Integer
                                .valueOf(
                                    AppController.subDealerOrderDetailList
                                        .get(position).quantity
                                )
                        ) {
                            Log.i(
                                "Edit Valuee", ""
                                        + edtQuantity.getText().toString()
                            );


                            toast.show(27);
                            AppController.isSubmitError = true;
                            AppController.listErrors.add(
                                AppController.isSubmitError.toString()
                            );

                        } else if (Integer.valueOf(value) < Integer
                                .valueOf(
                                    AppController.subDealerOrderDetailList
                                        .get(position).quantity
                                )
                            && Integer.valueOf(value) >= 0
                        ) {
                            if (Integer.valueOf(value) > 0) {
                                AppController.TempSubDealerOrderDetailList.get(
                                    position
                                ).quantity = value;
                                AppController.listErrors.clear();
                            } else {
                                toast.show(29);
                                AppController.isSubmitError = true;
                                AppController.listErrors.add(AppController.isSubmitError.toString());
                            }
                        } else {
                            AppController.isSubmitError = false;
                            AppController.listErrors.clear();
                        }

                    } catch (e: Exception) {

                    }
                }
            })
            /*
			 * updateButton.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { // TODO Auto-generated
			 * method stub
			 * 
			 * } });
			 */
        } else {
            view = convertView
        }

        /*
		 * setText( "Order Date : " + VKCUtils.formatDateWithInput(
		 * subdealersModels.get(position).getOrderDate(), "dd MMM yyyy",
		 * "yyyy-MM-dd hh:mm:ss"));
		 */if (position % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            view!!.setBackgroundColor(
                mActivity.resources.getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            view!!.setBackgroundColor(
                mActivity.resources.getColor(
                    R.color.list_row_color_white
                )
            )
        }
        return view
    }
}