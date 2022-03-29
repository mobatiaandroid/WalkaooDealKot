package com.mobatia.vkcsalesapp.appdialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import com.mobatia.vkcsalesapp.R

class PushNotificationDialog     // TODO Auto-generated constructor stub
    (var mActivity: Activity) : Dialog(mActivity), View.OnClickListener {
    var d: Dialog? = null
    private var textView1: TextView? = null
    private var dialogLstnr: DialogListener? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        //println("onCreate dialog")
    }

    fun init(message: String?, dL: DialogListener?) {
        dialogLstnr = dL
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_push_notification)
        textView1 = findViewById<View>(R.id.textView1) as TextView
        textView1!!.text = message
        val relativeDate = findViewById<View>(R.id.datePickerBase) as RelativeLayout
        val buttonSet = findViewById<View>(R.id.buttonOk) as Button
        buttonSet.setOnClickListener {
            dismiss()
            dialogLstnr!!.dismissDialog()
        }
        val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
        buttonCancel.setOnClickListener { dismiss() }
    }

    override fun onClick(v: View) {
        dismiss()
    }

    interface DialogListener {
        fun dismissDialog()
    }
}