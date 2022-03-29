package com.mobatia.vkcsalesapp.appdialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.RelativeLayout
import com.mobatia.vkcsalesapp.R

class AppExitDialog     // TODO Auto-generated constructor stub
    (var mActivity: Activity, var TEXTTYPE: String) : Dialog(mActivity), View.OnClickListener {
    var d: Dialog? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_app_exit)
        init()
    }

    private fun init() {
        val relativeDate = findViewById<View>(R.id.datePickerBase) as RelativeLayout
        val buttonSet = findViewById<View>(R.id.buttonOk) as Button
        buttonSet.setOnClickListener {
            dismiss()
            mActivity.finish()
        } // alrtDbldr.cancel();

        val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
        buttonCancel.setOnClickListener { dismiss() }
    }

    override fun onClick(v: View) {
        dismiss()
    }
}