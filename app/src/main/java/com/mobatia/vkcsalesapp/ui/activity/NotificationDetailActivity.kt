package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R

class NotificationDetailActivity : AppCompatActivity() {
    var mDate: String? = null
    var mMessage: String? = null
    var textMessage: TextView? = null
    var textDate: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_detail)
        val abar = supportActionBar
        val viewActionBar = layoutInflater.inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.text = "Notification Detail"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        val intent = intent
        mMessage = intent.extras!!.getString("MESSAGE")
        mDate = intent.extras!!.getString("MESSAGE_DATE")
        initUI()
    }

    private fun initUI() {
        // TODO Auto-generated method stub
        textMessage = findViewById<View>(R.id.textMessageDetail) as TextView
        textDate = findViewById<View>(R.id.textMessageDate) as TextView
        textMessage!!.text = mMessage
        textDate!!.text = mDate
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        // Enable action bar icon_luncher as toggle Home Button
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }
}