package com.mobatia.vkcsalesapp.appdialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.*
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getProductListSortOption

class SortDialog     // TODO Auto-generated constructor stub
    (
    var mActivity: Activity, var TEXTTYPE: String,
    var optionSelectionListener: SortOptionSelectionListener
) : Dialog(mActivity), View.OnClickListener {
    var d: Dialog? = null
    var optionRadioGroup: RadioGroup? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_sortby)
        init()
    }

    private fun init() {
        optionRadioGroup = findViewById<View>(R.id.myRadioGroup) as RadioGroup
        val relativeSort = findViewById<View>(R.id.SortByBase) as RelativeLayout
        val radioPopularity = findViewById<View>(R.id.radioPopularity) as RadioButton
        val radioPriceLow = findViewById<View>(R.id.radioPriceLow) as RadioButton
        val radioPriceHigh = findViewById<View>(R.id.radioPriceHigh) as RadioButton
        val radioArrivals = findViewById<View>(R.id.radioArrivals) as RadioButton
        val radioDiscount = findViewById<View>(R.id.radioDiscount) as RadioButton
        val radioMostOrder = findViewById<View>(R.id.radioMostOrder) as RadioButton
        val option = getProductListSortOption(mActivity)
        if (option == "0") {
            radioPopularity.isChecked = true
        } else if (option == "1") {
            radioPriceLow.isChecked = true
        } else if (option == "2") {
            radioPriceHigh.isChecked = true
        } else if (option == "3") {
            radioArrivals.isChecked = true
        } else if (option == "4") {
            radioDiscount.isChecked = true
        } else if (option == "5") {
            radioMostOrder.isChecked = true
        }
        val txtTitle = findViewById<View>(R.id.txtTitle) as TextView
        txtTitle.text = TEXTTYPE
        val buttonSet = findViewById<View>(R.id.buttonSet) as Button
        buttonSet.setOnClickListener {
            val selectedId = optionRadioGroup!!.checkedRadioButtonId
            val radioButton = findViewById<View>(selectedId) as RadioButton
            optionSelectionListener.selectedOption(
                radioButton.text
                    .toString()
            )
            dismiss()
        } // alrtDbldr.cancel();

        val buttonCancel = findViewById<View>(R.id.buttonCancel) as Button
        buttonCancel.setOnClickListener { dismiss() }
    }

    override fun onClick(v: View) {
        dismiss()
    }

    fun setSortOptionSelection(
        options: Array<String?>?,
        optionSelectionListener: SortOptionSelectionListener
    ) {
        this.optionSelectionListener = optionSelectionListener
    }

    interface SortOptionSelectionListener {
        fun selectedOption(option: String?)
    }
}