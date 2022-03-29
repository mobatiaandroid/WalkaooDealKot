package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ReportDetailAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController

class RedeemReportDetailActivity : AppCompatActivity(), View.OnClickListener, VKCUrlConstants {
    var mContext: Activity? = null
    var listViewDetails: ListView? = null
    var position = 0
    var cust_id: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_redeem_report_detail)
        mContext = this
        initialiseUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // title/icon
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialiseUI() {
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
        textviewTitle.text = "Report Detail"
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        val intent = intent
        cust_id = intent.extras!!.getString("cust_id")
        listViewDetails = findViewById<View>(R.id.listViewRedeemReportDetail) as ListView
        for (i in AppController.listRedeemReport.indices) {
            if (AppController.listRedeemReport[i].custId
                == cust_id
            ) {
                position = i
                break
            }
        }
        val adapter = ReportDetailAdapter(
            mContext!!,
            AppController.listRedeemReport[position]
                .listReportDetail
        )
        listViewDetails!!.adapter = adapter
    }

    fun setActionBar() {
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.white)))
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.back)
        supportActionBar!!.setHomeButtonEnabled(true)
    }

    override fun onRestart() {
        // TODO Auto-generated method stub
        super.onRestart()
    }

    override fun onResume() {
        // TODO Auto-generated method stub
        super.onResume()
    }

    override fun onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View) {}
}