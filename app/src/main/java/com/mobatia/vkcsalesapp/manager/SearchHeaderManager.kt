/**
 *
 */
package com.mobatia.vkcsalesapp.manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView.OnEditorActionListener
import com.mobatia.vkcsalesapp.R

/**
 * Bibin
 */
class SearchHeaderManager(private var context: Context) {
    /** The relative params.  */
    private var relativeParams: RelativeLayout.LayoutParams? = null
    var headerView: View? = null
     var inflater: LayoutInflater? = null
    var searchImage: ImageView? = null
        private set
    var editText: EditText? = null
        private set
    var searchInterface: SearchActionInterface? = null
    var height = 0
    var width = 0
    fun getSearchHeader(layout: RelativeLayout) {
        initialiseUI()
        relativeParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        relativeParams!!.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        layout.addView(headerView, relativeParams)
    }

    fun initialiseUI() {
        inflater = LayoutInflater.from(context)
        headerView = inflater?.inflate(R.layout.layout_searchmanager, null)
        var relHeader = headerView
            ?.findViewById<View>(R.id.relSearchHeader) as RelativeLayout
        relHeader.layoutParams.height = (height * 0.09).toInt()
        relHeader.layoutParams.width = height
        searchImage = headerView?.findViewById<View>(R.id.imgSearch) as ImageView
        editText = headerView?.findViewById<View>(R.id.edtSearch) as EditText
        editText!!.setOnClickListener { // TODO Auto-generated method stub
//				Toast.makeText(context, "ontouch", 1000).show();
            editText!!.isFocusableInTouchMode = true
        }
        editText!!.setOnTouchListener { v, event -> // TODO Auto-generated method stub
            editText!!.isFocusableInTouchMode = true
            false
        }
    }

    val deviceHeights: Unit
        get() {
            val displayScale = DisplayManagerScale(context)
            height = displayScale.deviceHeight
            width = displayScale.deviceWidth
        }

    fun searchAction(
        context: Context,
        searchInterface: SearchActionInterface?, key: String?
    ) {
        this.context = context
        this.searchInterface = searchInterface
        searchWithKey(key)
        // searchInterface.searchOnTextChange(key);
    }

    fun searchWithKey(key: String?) {
        editText
            ?.setOnEditorActionListener(OnEditorActionListener { v, actionId, event -> // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchInterface!!.searchOnTextChange(key)
                    return@OnEditorActionListener true
                }
                false
            })
    }

    interface SearchActionInterface {
        fun searchOnTextChange(key: String?)
    }

    init {
        deviceHeights
    }
}