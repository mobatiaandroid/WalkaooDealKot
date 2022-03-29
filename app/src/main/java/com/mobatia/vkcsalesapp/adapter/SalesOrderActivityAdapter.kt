package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.SQLiteServices.SQLiteAdapter
import com.mobatia.vkcsalesapp.activities.ProductDetailActivity
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.DataBaseManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.model.CartModel
import com.mobatia.vkcsalesapp.model.ColorModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class SalesOrderActivityAdapter : BaseAdapter, VKCDbConstants, VKCUrlConstants {
    var mActivity: Activity
    var mInflater: LayoutInflater
    var cartArrayList: ArrayList<CartModel>
    var colorArrayList: ArrayList<ColorModel> = ArrayList()
    var imgClose: ImageView? = null
    var linearLayout: LinearLayout
    private var mTxtViewQty: TextView
    private var mTxtTotalItem: TextView? = null
    private var mtextCartValue: TextView? = null
    private var databaseManager: DataBaseManager? = null
    private var cartPrice: Int = 0

    constructor(
        mActivity: Activity,
        cartArrayList: ArrayList<CartModel>, linearLayout: LinearLayout,
        txtViewQty: TextView, txtTotalItem: TextView?, cartValue: TextView?
    ) {
        this.mActivity = mActivity
        this.cartArrayList = cartArrayList
        this.linearLayout = linearLayout
        mTxtViewQty = txtViewQty
        mtextCartValue = cartValue
        mTxtTotalItem = txtTotalItem
        mInflater = LayoutInflater.from(mActivity)
        databaseManager = DataBaseManager(mActivity)
    }

    constructor(
        cartActivity: Activity,
        cartArrayList2: ArrayList<CartModel>, lnrTableHeaders: LinearLayout,
        txtQty: TextView
    ) {
        mActivity = cartActivity
        cartArrayList = cartArrayList2
        linearLayout = lnrTableHeaders
        mTxtViewQty = txtQty
        mInflater = LayoutInflater.from(mActivity)
        // TODO Auto-generated constructor stub
    }

    override fun getCount(): Int {
return cartArrayList.size    }
    public override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return position
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var view: View = convertView
        val holder: ViewHolder
        if (convertView == null) {
            view = mInflater.inflate(R.layout.custom_salesorder_list, null)
            holder = ViewHolder()
            holder.txtprodId = view.findViewById<View>(R.id.txtProdId) as TextView?
            holder.txtsize = view.findViewById<View>(R.id.txtSize) as TextView?
            holder.txtcolourValue = view.findViewById<View>(R.id.txtColor) as TextView?
            holder.Edtqty = view.findViewById<View>(R.id.txtQuantity) as EditText?
            view.setTag(holder)
        } else {
            holder = view.getTag() as ViewHolder
        }
        holder.Edtqty?.setImeOptions(EditorInfo.IME_ACTION_DONE)
        holder.Edtqty?.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            public override fun onEditorAction(
                v: TextView?,
                actionId: Int,
                event: KeyEvent?
            ): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Creating uri to be opened
                    val mAdapter: SQLiteAdapter = SQLiteAdapter(
                        mActivity,
                        VKCDbConstants.Companion.DBNAME
                    )
                    mAdapter.openToRead()
                    mAdapter.updateQuantity(
                        AppController.cartArrayList.get(position)
                            .getProdName(), AppController.cartArrayList
                            .get(position).getProdColor(),
                        AppController.cartArrayList.get(position)
                            .getProdSize(), holder.Edtqty?.getText()
                            .toString().trim({ it <= ' ' })
                    )
                    mAdapter.close()
                    AppController.cartArrayList.get(position).setProdQuantity(
                        holder.Edtqty?.getText().toString().trim({ it <= ' ' })
                    )
                    editApi(
                        AppController.cartArrayList.get(position).getPid(),
                        holder.Edtqty?.getText().toString()
                    )
                }
                return false
            }
        })
        holder.qtyWatcher = object : TextWatcher {
            public override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int,
                after: Int
            ) {
            }

            public override fun onTextChanged(
                s: CharSequence?, start: Int, before: Int,
                count: Int
            ) {
                try {
                    /*
					 * AppController.cartArrayList.get(position).setProdQuantity(
					 * holder.Edtqty.getText().toString());
					 */
                } catch (e: Exception) {
                }
            }

            public override fun afterTextChanged(s: Editable?) {
                // Update the quantity
                val text: String = holder.Edtqty?.getText().toString().trim({ it <= ' ' })
                if (((text == "") || (text
                            == "0"))
                ) {
                    //	getCartData();
                } else {
                    val mAdapter: SQLiteAdapter =
                        SQLiteAdapter(mActivity, VKCDbConstants.Companion.DBNAME)
                    mAdapter.openToRead()
                    mAdapter.updateQuantity(
                        AppController.cartArrayList.get(position).getProdName(),
                        AppController.cartArrayList.get(position).getProdColor(),
                        AppController.cartArrayList.get(position).getProdSize(),
                        holder.Edtqty?.getText().toString().trim({ it <= ' ' })
                    )
                    mAdapter.close()
                    AppController.cartArrayList.get(position).setProdQuantity(
                        holder.Edtqty?.getText().toString()
                    )
                    editApi(
                        AppController.cartArrayList.get(position).getPid(),
                        holder.Edtqty?.getText().toString()
                    )
                }
            }
        }
        if (holder.qtyWatcher != null) {
            holder.Edtqty?.removeTextChangedListener(holder.qtyWatcher)
        } else {
            holder.Edtqty?.addTextChangedListener(holder.qtyWatcher)
        }
        /*
		 * holder.Edtqty.addTextChangedListener(new TextWatcher() {
		 * 
		 * @Override public void onTextChanged(CharSequence s, int start, int
		 * before, int count) { // TODO Auto-generated method stub try {
		 * AppController.cartArrayList.get(position).setProdQuantity(
		 * holder.Edtqty.getText().toString()); } catch (Exception e) {
		 * 
		 * } }
		 * 
		 * @Override public void beforeTextChanged(CharSequence s, int start,
		 * int count, int after) { // TODO Auto-generated method stub
		 * 
		 * }
		 * 
		 * @Override public void afterTextChanged(Editable s) { // TODO
		 * Auto-generated method stub
		 * 
		 * } });
		 */
        // TextView color=(TextView)view.findViewById(R.id.txtColor);
        /*
		 * HorizontalListView relColor = (HorizontalListView) view
		 * .findViewById(R.id.listViewColor);
		 */holder.imgClose = view.findViewById<View>(R.id.imgClose) as ImageView?
        holder.txtprodId?.setText(
            AppController.cartArrayList.get(position)
                .getProdName()
        )
        holder.txtsize?.setText(
            AppController.cartArrayList.get(position)
                .getProdSize()
        )
        holder.Edtqty?.setText(
            AppController.cartArrayList.get(position)
                .getProdQuantity()
        )
        holder.txtcolourValue?.setText(
            AppController.cartArrayList.get(position)
                .getProdColor()
        )
        holder.txtprodId?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                // AppController.product_id =
                // AppController.cartArrayList.get(position).getProdId();
                AppController.product_id = AppController.cartArrayList.get(
                    position
                ).getProdName()
                AppController.p_id = AppController.cartArrayList.get(position)
                    .getProdName()
                AppController.category_id = AppController.cartArrayList.get(
                    position
                ).getCatId()
                AppController.isClickedCartItem = true
                /*
				 * String pid=AppController.cartArrayList.get(
				 * position).getPid(); String
				 * sapId=AppController.cartArrayList.get( position).getSapId();
				 * String prodId=AppController.cartArrayList.get(
				 * position).getProdId();
				 */AppController.delPosition = position
                AppController.listScrollTo = position
                // System.out.print("Del Posi :" + AppController.delPosition);
                mActivity.startActivity(
                    Intent(
                        mActivity,
                        ProductDetailActivity::class.java
                    )
                )
                //getCartData();
                // mActivity.finish();
            }
        })
        holder.txtcolourValue?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                // AppController.product_id =
                // AppController.cartArrayList.get(position).getProdId();
                AppController.product_id = AppController.cartArrayList.get(
                    position
                ).getProdName()
                AppController.category_id = AppController.cartArrayList.get(
                    position
                ).getCatId()
                AppController.isClickedCartItem = true
                /*
				 * AppController.p_id =
				 * AppController.cartArrayList.get(position) .getPid();
				 */AppController.p_id = AppController.cartArrayList.get(position)
                    .getProdName()
                AppController.delPosition = position
                AppController.listScrollTo = position
                mActivity.startActivity(
                    Intent(
                        mActivity,
                        ProductDetailActivity::class.java
                    )
                )
                //getCartData();
                // mActivity.finish();
            }
        })
        holder.txtsize?.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                // AppController.product_id =
                // AppController.cartArrayList.get(position).getProdId();
                AppController.product_id = AppController.cartArrayList.get(
                    position
                ).getProdName()
                AppController.category_id = AppController.cartArrayList.get(
                    position
                ).getCatId()
                AppController.delPosition = position
                AppController.listScrollTo = position
                AppController.p_id = AppController.cartArrayList.get(position)
                    .getProdName()
                AppController.isClickedCartItem = true
                mActivity.startActivity(
                    Intent(
                        mActivity,
                        ProductDetailActivity::class.java
                    )
                )
                //getCartData();
                // mActivity.finish();
            }
        })
        // color.setText(cartArrayList.get(position).getProdColor());
        /*
		 * relColor.setAdapter(new ColorGridAdapter(mActivity,
		 * cartArrayList.get( position).getProdColor(), 0));
		 */
        val rel1: RelativeLayout = view.findViewById<View>(R.id.rel1) as RelativeLayout
        val rel2: RelativeLayout = view.findViewById<View>(R.id.rel2) as RelativeLayout
        val rel3: RelativeLayout = view.findViewById<View>(R.id.rel3) as RelativeLayout
        val rel4: RelativeLayout = view.findViewById<View>(R.id.rel4) as RelativeLayout
        val rel5: RelativeLayout = view.findViewById<View>(R.id.rel5) as RelativeLayout
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
        holder.imgClose!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View?) {
                // TODO Auto-generated method stub
                showDeleteDialog(position)
            }
        })
        return view
    }

    private fun showDeleteDialog(position: Int) {
        val appDeleteDialog: DeleteAlert = DeleteAlert(mActivity, position)
        appDeleteDialog.getWindow()!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        appDeleteDialog.setCancelable(true)
        appDeleteDialog.show()
    }

    inner class DeleteAlert constructor(a: Activity, position: Int) : Dialog(a),
        View.OnClickListener, VKCDbConstants {
        var mActivity: Activity
        var d: Dialog? = null
        var mCheckBoxDis: CheckBox? = null
        var mImageView: ImageView? = null

        // public Button yes, no;
        var bUploadImage: Button? = null
        var TEXTTYPE: String? = null
        var mProgressBar: ProgressBar? = null
        var databaseManager: DataBaseManager? = null
        var position: Int
        var cartList: ArrayList<CartModel>? = null
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_delete_alert)
            init()
        }

        private fun init() {
            /*DisplayManagerScale disp = new DisplayManagerScale(mActivity);
			int displayH = disp.getDeviceHeight();
			int displayW = disp.getDeviceWidth();

			RelativeLayout relativeDate = (RelativeLayout) findViewById(R.id.datePickerBase);
*/
            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);
            val buttonSet: Button = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View?) {
                    val listDelete: ArrayList<String> = ArrayList()
                    listDelete.add(
                        AppController.cartArrayList.get(position)
                            .getPid()
                    )
                    deleteApi(listDelete)
                    databaseManager = DataBaseManager(mActivity)
                    databaseManager!!.removeFromDb(
                        VKCDbConstants.Companion.TABLE_SHOPPINGCART, "pid",
                        AppController.cartArrayList.get(position).getPid()
                    )
                    AppController.cartArrayList.removeAt(position)
                    notifyDataSetChanged()
                    //int totQty = 0;
                    /*	for (int i = 0; i < AppController.cartArrayList.size(); i++) {
						totQty = totQty
								+ Integer.parseInt(cartArrayList.get(i)
										.getProdQuantity());
					}*/
                    /*if (AppController.cartArrayList.size() > 0) {
						mTxtViewQty.setText("Total quantity :  " + "" + totQty);
						mTxtTotalItem.setText("Total Item : "
								+ AppController.cartArrayList.size());
					}*/

                    /*if (AppController.cartArrayList.size() == 0) {
						mTxtViewQty.setText("Total quantity :  " + "" + 0);
						mTxtTotalItem.setText("Total Item : " + 0);
						VKCUtils.showtoast(mActivity, 9);
						linearLayout.setVisibility(View.GONE);
						SalesOrderFragment.isCart = false;
					}
*/
                    //updateCartValue();
                    dismiss()
                }
            })
            val buttonCancel: Button = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View?) {
                    dismiss()
                }
            })
        }

        public override fun onClick(v: View) {
            dismiss()
        }

        init {
            // TODO Auto-generated constructor stub
            this.mActivity = a
            this.position = position
        }
    }// AppController.cartArrayList.add(setCartModel(cursor));

    /*
           if (AppController.cartArrayList.size() > 0) {

               mTxtTotalItem.setText("Total Item :  " + ""
                       + AppController.cartArrayList.size());

           } else {
               mTxtTotalItem.setText("Total Item : :  " + "" + 0);

           }*/
    // AppController.cartArrayList.clear();
    private val cartData: Unit
        private get() {
            // AppController.cartArrayList.clear();
            AppController.cartArrayListSelected.clear()
            val cols: Array<String> = arrayOf(
                VKCDbConstants.Companion.PRODUCT_ID,
                VKCDbConstants.Companion.PRODUCT_NAME,
                VKCDbConstants.Companion.PRODUCT_SIZEID,
                VKCDbConstants.Companion.PRODUCT_SIZE,
                VKCDbConstants.Companion.PRODUCT_COLORID,
                VKCDbConstants.Companion.PRODUCT_COLOR,
                VKCDbConstants.Companion.PRODUCT_QUANTITY,
                VKCDbConstants.Companion.PRODUCT_GRIDVALUE,
                "pid"
            )
            val cursor: Cursor? = databaseManager?.fetchFromDB(
                cols, VKCDbConstants.Companion.TABLE_SHOPPINGCART,

            )
            if (cursor?.getCount()!! > 0) {
                while (!cursor?.isAfterLast()!!) {
                    // AppController.cartArrayList.add(setCartModel(cursor));
                    val pid: String = cursor?.getString(1)!!
                    if ((pid == AppController.product_id)) {
                        val model: CartModel = CartModel()
                        model.setPid(cursor?.getString(8))
                        model.setProdColor(cursor?.getString(5))
                        model.setProdName(cursor?.getString(1))
                        model.setProdQuantity(cursor?.getString(6))
                        model.setProdSize(cursor?.getString(3))
                        AppController.cartArrayListSelected.add(model)
                    }
                    cursor?.moveToNext()
                }
                /*
                       if (AppController.cartArrayList.size() > 0) {
           
                           mTxtTotalItem.setText("Total Item :  " + ""
                                   + AppController.cartArrayList.size());
           
                       } else {
                           mTxtTotalItem.setText("Total Item : :  " + "" + 0);
           
                       }*/
            }
        }

    internal class ViewHolder constructor() {
        var txtprodId: TextView? = null
        var txtsize: TextView? = null
        var txtcolourValue: TextView? = null
        var Edtqty: EditText? = null
        var imgClose: ImageView? = null
        var qtyWatcher: TextWatcher? = null
    }

    fun deleteApi(listDelete: ArrayList<String>) {
        val name: Array<String> = arrayOf("ids")
        val values: Array<String> = arrayOf(listDelete.toString())
        val manager: VKCInternetManager = VKCInternetManager(
            VKCUrlConstants.Companion.DELETE_CART_ITEM_API
        )
        manager.getResponsePOST(mActivity, name, values,
            object : VKCInternetManager.ResponseListener {
                public override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    //	Log.v("LOG", "18022015 success" + successResponse);
                    try {
                        val obj: JSONObject = JSONObject(successResponse)
                        val status: String = obj.optString("status")
                        if ((status == "Success")) {
                            mTxtViewQty?.setText("Total Qty. :" + obj.optString("tot_qty"))
                            mtextCartValue?.setText("Cart Value: ₹" + obj.optString("cart_value"))
                            mTxtTotalItem?.setText("Total Item : " + obj.optString("tot_items"))
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    // parseResponse(successResponse);
                }

                public override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    Log.v("LOG", "18022015 Errror" + failureResponse)
                }
            })
    }

    fun editApi(id: String, qty: String) {
        val name: Array<String> = arrayOf("id", "quantity")
        val values: Array<String> = arrayOf(id, qty)
        val manager: VKCInternetManager = VKCInternetManager(
            VKCUrlConstants.Companion.EDIT_CART_ITEM_API
        )
        manager.getResponsePOST(mActivity, name, values,
            object : VKCInternetManager.ResponseListener {
                public override fun responseSuccess(successResponse: String?) {
                    // TODO Auto-generated method stub
                    Log.v("LOG", "18022015 success" + successResponse)
                    // parseResponse(successResponse);
                    //updateCartValue();
                    //setCartQuantity();
                    try {
                        val obj: JSONObject = JSONObject(successResponse)
                        val status: String = obj.optString("status")
                        if ((status == "Success")) {
                            mTxtViewQty.setText("Total Qty. :" + obj.optString("tot_qty"))
                            mtextCartValue?.setText("Cart Value: ₹" + obj.optString("cart_value"))
                            mTxtTotalItem?.setText("Total Item : " + obj.optString("tot_items"))
                        }
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
                    cartData
                }

                public override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                    Log.v("LOG", "18022015 Errror" + failureResponse)
                }
            })
    }

    fun setCartQuantity() {
        val cols: Array<String> = arrayOf(
            VKCDbConstants.Companion.PRODUCT_ID,
            VKCDbConstants.Companion.PRODUCT_NAME,
            VKCDbConstants.Companion.PRODUCT_SIZEID,
            VKCDbConstants.Companion.PRODUCT_SIZE,
            VKCDbConstants.Companion.PRODUCT_COLORID,
            VKCDbConstants.Companion.PRODUCT_COLOR,
            VKCDbConstants.Companion.PRODUCT_QUANTITY,
            VKCDbConstants.Companion.PRODUCT_GRIDVALUE
        )
        val cursor: Cursor? = databaseManager?.fetchFromDB(
            cols, VKCDbConstants.TABLE_SHOPPINGCART,
        )
        var mCount: Int = 0
        var cartount: Int = 0
        if (cursor?.moveToFirst() == true) {
            do {
                val count: String? = cursor?.getString(
                    cursor
                        ?.getColumnIndex("productqty")!!
                )
                cartount = count?.toInt()!!
                mCount = mCount + cartount
                // do what ever you want here
            } while (cursor?.moveToNext())
        }
        cursor?.close()
        mTxtViewQty.setText("Total Quantity : " + mCount.toString())
    }
}