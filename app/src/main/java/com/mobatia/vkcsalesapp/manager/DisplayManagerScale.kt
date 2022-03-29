package com.mobatia.vkcsalesapp.manager


import android.app.Activity
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Point

class DisplayManagerScale(var cont: Context) {
    var TAB_COUNT_DIN = 0f

    @get:SuppressLint("NewApi")
    val deviceHeight: Int
        get() {
            val localPoint = Point()
            val localWindowManager = (cont as Activity)
                .windowManager
            localWindowManager.defaultDisplay.getSize(localPoint)
            return localPoint.y
        }

    @get:SuppressLint("NewApi")
    val deviceWidth: Int
        get() {
            val localPoint = Point()
            val localWindowManager = (cont as Activity)
                .windowManager
            localWindowManager.defaultDisplay.getSize(localPoint)
            return localPoint.x
        }
    val displayDensity: Float
        get() = cont.resources.displayMetrics.density
    val displayscale: Float
        get() {
            val f = cont.resources.displayMetrics.density
            if (f <= 0.75) TAB_COUNT_DIN = 3.0f
            if (f <= 1.0) TAB_COUNT_DIN = 3.0f else if (f <= 1.5) TAB_COUNT_DIN =
                4.0f else if (f <= 2.0) TAB_COUNT_DIN = 5.0f else if (f <= 3.0) TAB_COUNT_DIN = 6.0f
            return TAB_COUNT_DIN
        }
}