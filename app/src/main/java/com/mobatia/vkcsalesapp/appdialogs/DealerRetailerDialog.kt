/**
 *
 */
package com.mobatia.vkcsalesapp.appdialogs

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.mobatia.vkcsalesapp.R


class DealerRetailerDialog(
    a: FragmentActivity, strArray: Array<String?>,
    text: TextView, Title: String, onItemSelectListener: OnDialogItemSelectListener
) : Dialog(a) {
    var mActivity: Activity
    var d: Dialog? = null
    var text: TextView
    var stringArray: Array<String?>
    var dialogTitle: String
    var onItemSelectListener: OnDialogItemSelectListener
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_state_district_place)
        init()
    }

    private fun init() {
        val txtTitle = findViewById<View>(R.id.txtTitle) as TextView
        val list = findViewById<View>(R.id.listView) as ListView
        list.adapter = DealerRetailer(stringArray, mActivity)
        txtTitle.text = dialogTitle
        list.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> // TODO Auto-generated method stub
                text.text = stringArray[position]
                onItemSelectListener.itemSelected(position)
                dismiss()
            }
    }

    interface OnDialogItemSelectListener {
        fun itemSelected(position: Int)
    }

    inner class DealerRetailer(var states: Array<String?>, var mActivity: Activity) :
        BaseAdapter() {
        var mLayoutInflater: LayoutInflater
        override fun getCount(): Int {
            // TODO Auto-generated method stub
            return states.size
        }

        override fun getItem(position: Int): Any {
            // TODO Auto-generated method stub
            return states
        }

        override fun getItemId(position: Int): Long {
            // TODO Auto-generated method stub
            return 0
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var view: View? = null
            view = convertView
                ?: mLayoutInflater.inflate(
                    R.layout.state_district_place_single_text, null
                )
            val wraperClass: WraperClass = WraperClass(view)
            wraperClass.nameText.text = states[position]
            return view!!
        }

        private inner class WraperClass(view: View?) {
            var view: View? = null
            val nameText: TextView
                get() = view!!.findViewById<View>(R.id.textViewName) as TextView

            init {
                this.view = view
                // TODO Auto-generated constructor stub
            }
        }

        init {
            mLayoutInflater = LayoutInflater.from(mActivity)
        }
    }

    init {
        // TODO Auto-generated constructor stub
        mActivity = a
        this.text = text
        stringArray = strArray
        dialogTitle = Title
        this.onItemSelectListener = onItemSelectListener
    }
}