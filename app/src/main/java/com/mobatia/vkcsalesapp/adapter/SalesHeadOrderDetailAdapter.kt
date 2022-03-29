package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.customview.CustomToast
import com.mobatia.vkcsalesapp.customview.HorizontalListView
import com.mobatia.vkcsalesapp.model.CartModel
import com.mobatia.vkcsalesapp.model.ColorModel
import java.util.*

class SalesHeadOrderDetailAdapter constructor(var mActivity: Activity) : BaseAdapter() {
    var mInflater: LayoutInflater
    var cartArrayList: ArrayList<CartModel>? = null
    var colorArrayList: ArrayList<ColorModel> = ArrayList()
    var imgClose: ImageView? = null
    var linearLayout: LinearLayout? = null
    private val mTxtViewQty: TextView? = null
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
            view = mInflater.inflate(
                R.layout.custom_order_list_4_item,
                null
            )
            val prodId: TextView = view.findViewById<View>(R.id.txtProdId) as TextView
            val size: TextView = view.findViewById<View>(R.id.txtSize) as TextView
            val qty: TextView = view.findViewById<View>(R.id.txtQuantity) as TextView
            val textColor: TextView = view.findViewById<View>(R.id.txtColor) as TextView
            val relColor: HorizontalListView = view
                .findViewById<View>(R.id.listViewColor) as HorizontalListView
            // imgClose = (ImageView) view.findViewById(R.id.imgClose);
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
            textColor.setText(
                AppController.subDealerOrderDetailList.get(position)
                    .color_name
            )
            // color.setText(cartArrayList.get(position).getProdColor());
            /*relColor.setAdapter(new ColorGridAdapter(mActivity,
						AppController.subDealerOrderDetailList.get(position)
								.getColor_code(), 0));*/
        } else {
            view = convertView
        }


        /*
		 * AppController.subDealerOrderDetailList.get(position) .getColorId(),
		 * 0));
		 */
        val rel1: RelativeLayout = view!!.findViewById<View>(R.id.rel1) as RelativeLayout
        val rel2: RelativeLayout = view.findViewById<View>(R.id.rel2) as RelativeLayout
        val rel3: RelativeLayout = view.findViewById<View>(R.id.rel3) as RelativeLayout
        val rel4: RelativeLayout = view.findViewById<View>(R.id.rel4) as RelativeLayout
        //RelativeLayout rel5 = (RelativeLayout) view.findViewById(R.id.rel5);
        if (position % 2 == 0) {
            rel1.setBackgroundColor(Color.rgb(219, 188, 188))
            rel2.setBackgroundColor(Color.rgb(219, 188, 188))
            rel3.setBackgroundColor(Color.rgb(219, 188, 188))
            rel4.setBackgroundColor(Color.rgb(219, 188, 188))
            //rel5.setBackgroundColor(Color.rgb(219, 188, 188));
        } else {
            rel1.setBackgroundColor(Color.rgb(208, 208, 208))
            rel2.setBackgroundColor(Color.rgb(208, 208, 208))
            rel3.setBackgroundColor(Color.rgb(208, 208, 208))
            rel4.setBackgroundColor(Color.rgb(208, 208, 208))
            //rel5.setBackgroundColor(Color.rgb(208, 208, 208));
        }
        return (view)
    }

    companion object {
        var value: String? = null
    }

    init {
        // this.cartArrayList = cartArrayList;
        // this.linearLayout = linearLayout;
        // mTxtViewQty = txtViewQty;
        mInflater = LayoutInflater.from(mActivity)
    }
}