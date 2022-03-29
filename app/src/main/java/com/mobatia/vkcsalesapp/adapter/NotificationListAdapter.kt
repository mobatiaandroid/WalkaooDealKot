package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.manager.VKCUtils
import com.mobatia.vkcsalesapp.model.NotificationListModel
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class NotificationListAdapter constructor(
    list: ArrayList<NotificationListModel>,
    activity: Activity
) : BaseAdapter(), VKCUrlConstants {
    var listNotification: ArrayList<NotificationListModel>
    var mInflater: LayoutInflater? = null
    var activity: Activity

    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return listNotification.size
    }

    // TODO Auto-generated method stub

    public override fun getItem(position: Int): NotificationListModel {
        // TODO Auto-generated method stub
        return listNotification.get(position)
    }

    public override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    public override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        var holder: ViewHolder? = null
        val view: View
        mInflater = LayoutInflater.from(activity)
        if (convertView == null) {
            view = mInflater?.inflate(
                R.layout.item_notification_list, parent,
                false
            )!!
        } else {
            view = convertView
        }
        holder = ViewHolder()
        holder!!.imageDelete = view.findViewById<View>(R.id.imageDelete) as ImageView?
        holder.txtMessage = view.findViewById<View>(R.id.textMessage) as TextView?
        holder.txtDate = view.findViewById<View>(R.id.textDate) as TextView?
        holder.txtMessage?.setText(listNotification.get(position).message)
        holder.txtDate?.setText(listNotification.get(position).messageDate)
        holder.imageDelete!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                // TODO Auto-generated method stub
                showDeleteDialog(position)
            }
        })
        return view
    }

    inner class ViewHolder constructor() {
        var imageDelete: ImageView? = null
        var txtMessage: TextView? = null
        var txtDate: TextView? = null
    }

    private fun getNotificationList() {
        listNotification.clear()
        val name = arrayOf("userid", "role")
        val values = arrayOf(
            getUserId(activity),
            getUserType(activity)
        )
        val manager = VKCInternetManager(
            VKCUrlConstants.NOTIFICATION_LIST_URL
        )
        /*
		 * for (int i = 0; i < name.length; i++) { Log.v("LOG",
		 * "12012015 name : " + name[i]); Log.v("LOG", "12012015 values : " +
		 * values[i]);
		 *
		 * }
		 */manager.getResponsePOST(activity, name, values, object : ResponseListener {
            override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponse(successResponse)
                }
            }

            override fun responseFailure(failureResponse: String?) {
                VKCUtils.showtoast(activity, 17)
            }
        })
    }
    /*
		 * for (int i = 0; i < name.length; i++) { Log.v("LOG",
		 * "12012015 name : " + name[i]); Log.v("LOG", "12012015 values : " +
		 * values[i]);
		 * 
		 * }
		 */


    /*
	 * private void setAdapter() { mStatusList.setAdapter(new
	 * SubDealerOrderListAdapter(subDealerModels, getActivity())); Log.i("",
	 * ""); }
	 */
    private fun parseResponse(result: String) {
        try {
            val arrayOrders: JSONArray? = null
            val jsonObjectresponse: JSONObject = JSONObject(result)
            // JSONObject response =
            // jsonObjectresponse.getJSONObject("response");
            val status: String = jsonObjectresponse.getString("status")
            if ((status == "200")) {
                val arrayNotification: JSONArray = jsonObjectresponse
                    .getJSONArray("data")
                for (i in 0 until arrayNotification.length()) {
                    val jobject: JSONObject = arrayNotification.getJSONObject(i)
                    val model: NotificationListModel = NotificationListModel()
                    model.message = jobject.getString("message")
                    model.messageDate = jobject.getString("date")
                    model.messageId = jobject.getString("id")
                    listNotification.add(model)
                }
                val adapter: NotificationListAdapter = NotificationListAdapter(
                    listNotification, activity
                )
                adapter.notifyDataSetChanged()
                AppController.mNotificationList?.setAdapter(adapter)
            }
        } catch (e: Exception) {
        }
    }

    private fun deleteListItem(id: String) {
        listNotification.clear()
        val name: Array<String> = arrayOf("id")
        val values: Array<String> = arrayOf(id)
        val manager: VKCInternetManager = VKCInternetManager(
            VKCUrlConstants.Companion.NOTIFICATION_DELETE_URL
        )
        manager.getResponsePOST(activity, name, values, object :
            VKCInternetManager.ResponseListener {
            public override fun responseSuccess(successResponse: String?) {
                if (successResponse != null) {
                    parseResponseDelete(successResponse)
                }
            }

            public override fun responseFailure(failureResponse: String?) {
                VKCUtils.showtoast(activity, 17)
            }
        })
    }

    private fun parseResponseDelete(result: String) {
        try {
            val jsonObjectresponse: JSONObject = JSONObject(result)
            val status: String = jsonObjectresponse.getString("status")
            if ((status == "200")) {
                getNotificationList()
            }
        } catch (e: Exception) {
        }
    }

    private fun showDeleteDialog(position: Int) {
        val appDeleteDialog: DeleteAlert = DeleteAlert(activity, position)
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
        var position: Int
        override fun onCreate(savedInstanceState: Bundle) {
            super.onCreate(savedInstanceState)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.dialog_delete_notification)
            init()
        }

        private fun init() {
            val relativeDate: RelativeLayout =
                findViewById<View>(R.id.datePickerBase) as RelativeLayout
            val buttonSet: Button = findViewById<View>(R.id.buttonOk) as Button
            buttonSet.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    dismiss()
                    deleteListItem(
                        listNotification.get(position)
                            .messageId
                    )
                }
            })
            val buttonCancel: Button = findViewById<View>(R.id.buttonCancel) as Button
            buttonCancel.setOnClickListener(object : View.OnClickListener {
                public override fun onClick(v: View) {
                    dismiss()
                }
            })
        }

        public override fun onClick(v: View) {
            dismiss()
        }

        init {
            // TODO Auto-generated constructor stub
            mActivity = a
            this.position = position
        }
    }

    init {
        // TODO Auto-generated constructor stub
        listNotification = list
        this.activity = activity
    }
}