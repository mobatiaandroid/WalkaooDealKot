package com.mobatia.vkcsalesapp.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveCustomerId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveDealerCount
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveIsCredit
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveLoginStatusFlag
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveStateCode
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveUserName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveUserType
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setLoginCustomerName
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.setLoginPlace
import com.mobatia.vkcsalesapp.manager.DataBaseManager
import com.mobatia.vkcsalesapp.manager.DisplayManagerScale
import com.mobatia.vkcsalesapp.model.MembersModel
import com.mobatia.vkcsalesapp.ui.activity.DashboardFActivity
import java.util.*

class MemberAdapter(var mActivity: Activity, var listModel: ArrayList<MembersModel>) :
    BaseAdapter(), VKCDbConstants {
    var mLayoutInflater: LayoutInflater? = null
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return listModel.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return listModel
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return position.toLong()
    }

    internal class ViewHolder {
        var textName: TextView? = null
        var textSwitch: TextView? = null
    }

    @SuppressLint("ResourceAsColor")
    override fun getView(position: Int, view: View, parent: ViewGroup): View {
        var viewHolder: ViewHolder? = null
        var v = view
        if (view == null) {
            val inflater = mActivity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            v = inflater.inflate(R.layout.item_group_member, null)
            viewHolder = ViewHolder()
            viewHolder.textName = v.findViewById<View>(R.id.textName) as TextView
            viewHolder.textSwitch = v.findViewById<View>(R.id.textSwitch) as TextView
            v.tag = viewHolder
        } else {
            viewHolder = v.tag as ViewHolder
        }
        if (position % 2 == 1) {
            // view.setBackgroundColor(Color.BLUE);
            v.setBackgroundColor(
                mActivity.resources.getColor(
                    R.color.list_row_color_grey
                )
            )
        } else {
            v.setBackgroundColor(
                mActivity.resources.getColor(
                    R.color.list_row_color_white
                )
            )
        }
        viewHolder.textName!!.text = listModel[position].customer_name
        viewHolder!!.textSwitch!!.setOnClickListener { // TODO Auto-generated method stub
            if (listModel[position].login == "failed") {
                val appExitDialog: UnableSwitchDialog =
                    UnableSwitchDialog(
                        mActivity
                    )
                appExitDialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                appExitDialog.setCancelable(true)
                appExitDialog.show()
            } else {
                val appExitDialog: SwitchDialog =
                    SwitchDialog(
                        mActivity,
                        position
                    )
                appExitDialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT)
                )
                appExitDialog.setCancelable(true)
                appExitDialog.show()
            }
        }
        return v
    }

    /**/
    inner class SwitchDialog     // TODO Auto-generated constructor stub
        (a: Activity?, var position: Int) : Dialog(a!!),
        View.OnClickListener {
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_switch)
            init()
        }

        private fun init() {
            val disp = DisplayManagerScale(mActivity)
            val displayH = disp.deviceHeight
            val displayW = disp.deviceWidth
            val relativeDate = findViewById<View>(R.id.datePickerBase) as RelativeLayout

            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);
            val buttonSet = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener {
                saveUserType(
                    mActivity,
                    listModel[position].role_id
                )
                saveCustomerId(
                    mActivity,
                    listModel[position].cust_id
                )
                if (listModel[position].dist_name != null) {
                    setLoginPlace(
                        mActivity, listModel[position].dist_name
                    )
                }
                if (listModel[position].customer_name != null) {
                    setLoginCustomerName(
                        mActivity,
                        listModel[position].customer_name
                    )
                }
                saveUserId(
                    mActivity,
                    listModel[position].user_id
                )
                // Save Dealer Count
                val mDealerCount = listModel[position]
                    .dealer_count
                saveDealerCount(mActivity, mDealerCount)
                val isCredit = listModel[position].credit_view
                saveIsCredit(mActivity, isCredit)
                // dealerCount = Integer.parseInt(mDealerCount);
                val mUserName = listModel[position].user_name
                // if(AppPrefenceManager.getLoginStatusFlag(mActivity).equals("false")){
                if (getUserName(mActivity) != "") {
                    if (getUserName(mActivity) != mUserName) {
                        clearDb()
                    } else {
                    }
                }
                // }
                saveUserName(
                    mActivity,
                    listModel[position].user_name
                )
                saveStateCode(
                    mActivity,
                    listModel[position].state_code
                )
                saveLoginStatusFlag(mActivity, "true")
                dismiss()
                mActivity.startActivity(
                    Intent(
                        mActivity,
                        DashboardFActivity::class.java
                    )
                )
                mActivity.finish()
            }
            val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener { dismiss() }
        }

        override fun onClick(v: View) {
            dismiss()
        }
    }

    inner class UnableSwitchDialog(a: Activity?) : Dialog(a!!),
        View.OnClickListener {
        var position: Int = 0
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_unable_to_switch)
            init()
        }

        private fun init() {
            val disp = DisplayManagerScale(mActivity)
            val displayH = disp.deviceHeight
            val displayW = disp.deviceWidth
            val relativeDate = findViewById<View>(R.id.datePickerBase) as RelativeLayout

            // relativeDate.getLayoutParams().height = (int) (displayH * .65);
            // relativeDate.getLayoutParams().width = (int) (displayW * .90);
            val buttonSet = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener { dismiss() }
        }

        override fun onClick(v: View) {
            dismiss()
        }

        /*init {
            // TODO Auto-generated constructor stub
            position = position
        }*/
    }

    fun clearDb() {
        val databaseManager = DataBaseManager(mActivity)
        databaseManager.removeDb(VKCDbConstants.TABLE_SHOPPINGCART)
    }
}