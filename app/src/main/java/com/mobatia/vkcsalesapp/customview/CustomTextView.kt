/**
 *
 */
package com.mobatia.vkcsalesapp.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import java.io.Serializable

/**
 * Bibin
 */
class CustomTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs),
    Serializable {
    fun init() {

        //setTypeface(null, Typeface.BOLD);
    }

    init {
        // TODO Auto-generated constructor stub

        //super.setTextColor(Color.WHITE);
        init()
    }
}