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
import com.mobatia.vkcsalesapp.model.CartModel
import com.mobatia.vkcsalesapp.model.ColorModel
import com.mobatia.vkcsalesapp.model.SubDealerOrderDetailModel
import java.util.*

class SubDealerOrderAdapter(
    var mActivity: Activity,
    listSubdealer: ArrayList<SubDealerOrderDetailModel>
) : BaseAdapter() {
    var mInflater: LayoutInflater
    var cartArrayList: ArrayList<CartModel>? = null
    var colorArrayList: ArrayList<ColorModel> = ArrayList()
    var imgClose: ImageView? = null
    var linearLayout: LinearLayout? = null
    private val mTxtViewQty: TextView? = null
    var edtQty: EditText? = null
    var value: String? = null
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
            view = mInflater.inflate(R.layout.custom_order_list_4_item, null)
        } else {
            view = convertView
        }
        val prodId: TextView = view!!.findViewById<View>(R.id.txtProdId) as TextView
        val size: TextView = view.findViewById<View>(R.id.txtSize) as TextView
        val qty: TextView = view.findViewById<View>(R.id.txtQuantity) as TextView
        val color: TextView = view.findViewById<View>(R.id.txtColor) as TextView
        // edtQty = (EditText) view.findViewById(R.id.edtQty);
        // TextView color=(TextView)view.findViewById(R.id.txtColor);
        /*
		 * HorizontalListView relColor = (HorizontalListView) view
		 * .findViewById(R.id.listViewColor);
		 */
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
        color.setText(
            AppController.subDealerOrderDetailList.get(position)
                .color_name
        )
        // color.setText(cartArrayList.get(position).getProdColor());
        /*
		 * relColor.setAdapter(new ColorGridAdapter(mActivity,
		 * AppController.subDealerOrderDetailList.get(position)
		 * .getColor_code(), 0));
		 */
        /*
		 * AppController.subDealerOrderDetailList.get(position) .getColorId(),
		 * 0));
		 */
        val rel1: RelativeLayout = view.findViewById<View>(R.id.rel1) as RelativeLayout
        val rel2: RelativeLayout = view.findViewById<View>(R.id.rel2) as RelativeLayout
        val rel3: RelativeLayout = view.findViewById<View>(R.id.rel3) as RelativeLayout
        val rel4: RelativeLayout = view.findViewById<View>(R.id.rel4) as RelativeLayout
        /*
		 * RelativeLayout rel5 = (RelativeLayout) view.findViewById(R.id.rel5);
		 * 
		 * Bibin Edited if
		 * (AppPrefenceManager.getUserType(mActivity).equals("7")) {
		 * rel5.setVisibility(View.GONE); } else {
		 * rel5.setVisibility(View.VISIBLE); }
		 */if (position % 2 == 0) {
            rel1.setBackgroundColor(Color.rgb(219, 188, 188))
            rel2.setBackgroundColor(Color.rgb(219, 188, 188))
            rel3.setBackgroundColor(Color.rgb(219, 188, 188))
            rel4.setBackgroundColor(Color.rgb(219, 188, 188))
            // rel5.setBackgroundColor(Color.rgb(219, 188, 188));
        } else {
            rel1.setBackgroundColor(Color.rgb(208, 208, 208))
            rel2.setBackgroundColor(Color.rgb(208, 208, 208))
            rel3.setBackgroundColor(Color.rgb(208, 208, 208))
            rel4.setBackgroundColor(Color.rgb(208, 208, 208))
            // rel5.setBackgroundColor(Color.rgb(208, 208, 208));
        }
        /*
		 * if (AppController.isEditable) { edtQty.setEnabled(true); } else {
		 * edtQty.setEnabled(false); } edtQty.addTextChangedListener(new
		 * TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int start, int
		 * before, int count) { // TODO Auto-generated method stub
		 * AppController.status = 2;
		 * 
		 * }
		 * 
		 * @Override public void beforeTextChanged(CharSequence s, int start,
		 * int count, int after) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void afterTextChanged(Editable s) { // TODO
		 * Auto-generated method stub AppController.status = 2;
		 * 
		 * try {
		 * 
		 * if (edtQty.getText().toString().trim().equals("")) { value = "0"; }
		 * else { value = edtQty.getText().toString().trim(); } if
		 * (Integer.valueOf(value) > Integer
		 * .valueOf(AppController.subDealerOrderDetailList
		 * .get(position).getQuantity())) { Log.i("Edit Valuee", "" +
		 * edtQty.getText().toString()); Log.i("List Values", "" +
		 * AppController.subDealerOrderDetailList .get(position).getQuantity());
		 * 
		 * toast.show(27); AppController.isSubmitError = true;
		 * AppController.listErrors.add(String
		 * .valueOf(AppController.isSubmitError));
		 * 
		 * } else if (Integer.valueOf(value) < Integer
		 * .valueOf(AppController.subDealerOrderDetailList
		 * .get(position).getQuantity()) && Integer.valueOf(value) >= 0) { if
		 * (Integer.valueOf(value) > 0) {
		 * AppController.TempSubDealerOrderDetailList.get(
		 * position).setQuantity(value); AppController.listErrors.clear(); }
		 * else { toast.show(29); AppController.isSubmitError = true;
		 * AppController.listErrors.add(String
		 * .valueOf(AppController.isSubmitError)); } } else {
		 * AppController.isSubmitError = false;
		 * AppController.listErrors.clear(); } System.out.println("Quantity is"
		 * + AppController.subDealerOrderDetailList.get(
		 * position).getQuantity()); } catch (Exception e) {
		 * System.out.println("Error found" + e); } }
		 * 
		 * });
		 */return (view)
    }

    init {
        // this.cartArrayList = cartArrayList;
        // this.linearLayout = linearLayout;
        // mTxtViewQty = txtViewQty;
        mInflater = LayoutInflater.from(mActivity)
    }
}