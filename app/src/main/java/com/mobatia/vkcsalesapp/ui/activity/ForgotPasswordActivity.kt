package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCUtils

class ForgotPasswordActivity : AppCompatActivity(), View.OnClickListener, VKCUrlConstants {
    var editEmail: EditText? = null
    var buttonSubmit: Button? = null
    var mContext: Activity? = null
    protected override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        mContext = this
        initialiseUI()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initialiseUI() {
        val abar: ActionBar = getSupportActionBar()!!
        val viewActionBar: View = getLayoutInflater().inflate(
            R.layout.actionbar_title, null
        )
        val params = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER
        )
        val textviewTitle: TextView = viewActionBar
            .findViewById<View>(R.id.actionbar_textview) as TextView
        textviewTitle.setText("Forgot Password")
        abar.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        setActionBar()
        buttonSubmit = findViewById<View>(R.id.buttonSubmit) as Button?
        editEmail = findViewById<View>(R.id.editEmail) as EditText?
        buttonSubmit!!.setOnClickListener(this)
    }

    fun setActionBar() {
        val actionBar: ActionBar = getSupportActionBar()!!
        actionBar.setSubtitle("")
        actionBar.setTitle("")
        actionBar.setBackgroundDrawable(ColorDrawable(getResources().getColor(R.color.white)))
        actionBar.show()
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back)
        getSupportActionBar()?.setHomeButtonEnabled(true)
    }// TODO Auto-generated method stub

    // parseJSON(successResponse);
    private val myPassword: Unit
        private get() {
            var manager: VKCInternetManager? = null
            val name = arrayOf("email")
            val value = arrayOf<String>(editEmail?.getText().toString())
            manager = VKCInternetManager(VKCUrlConstants.Companion.LIST_MY_DEALERS_SALES_HEAD_URL)
            manager.getResponsePOST(mContext, name, value,
                object : VKCInternetManager.ResponseListener {
                    override fun responseSuccess(successResponse: String?) {

                        // parseJSON(successResponse);
                    }

                    override fun responseFailure(failureResponse: String?) {
                        // TODO Auto-generated method stub
                    }
                })
        }

    protected override fun onRestart() {
// TODO Auto-generated method stub
        super.onRestart()
    }

    protected override fun onResume() {
// TODO Auto-generated method stub
        super.onResume()
    }

    override fun onBackPressed() {
// TODO Auto-generated method stub
        super.onBackPressed()
        finish()
    }

    override fun onClick(v: View) {
        // TODO Auto-generated method stub
        if (v === buttonSubmit) {
            if (editEmail?.getText().toString().trim { it <= ' ' }.length > 0) {
                myPassword
            } else {
                VKCUtils.showtoast(mContext, 53)
            }
        }
    }
}