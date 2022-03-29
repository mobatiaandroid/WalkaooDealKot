package com.mobatia.vkcsalesapp.customview

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import com.mobatia.vkcsalesapp.R

class CustomProgressBar(context: Context?, resourceIdOfImage: Int) : Dialog(
    context!!, R.style.TransparentProgressDialog
) {
    private val iv: ImageView
    override fun show() {
        val anim = RotateAnimation(
            0.0f, 360.0f,
            Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF,
            .5f
        )
        anim.interpolator = LinearInterpolator()
        anim.repeatCount = Animation.INFINITE
        anim.duration = 1000
        iv.animation = anim
        iv.startAnimation(anim)
        super.show()
    }

    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        val params = LinearLayout.LayoutParams(
            ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT
        )
        iv = ImageView(context)
        iv.setImageResource(resourceIdOfImage)
        layout.addView(iv, params)
        addContentView(layout, params)
    }
}