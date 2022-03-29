package com.mobatia.vkcsalesapp.ui.activity

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.adapter.ArticleAdapter
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserId
import com.mobatia.vkcsalesapp.manager.VKCInternetManager
import com.mobatia.vkcsalesapp.manager.VKCInternetManager.ResponseListener
import com.mobatia.vkcsalesapp.model.ArticleModel
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class ArticleListActivity : AppCompatActivity(), VKCUrlConstants {
    private var mActivity: Activity? = null
    private var search_key: String? = null
    var listArticle: MutableList<ArticleModel>? = null
    var listViewArticle: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_article)
        mActivity = this
        val extras = intent.extras
        if (extras != null) {
            search_key = extras.getString("key")
        }
        initialiseUI()
        listViewArticle!!.onItemClickListener =
            AdapterView.OnItemClickListener { arg0, arg1, arg2, arg3 -> // TODO Auto-generated method stub
                AppController.articleNumber = listArticle!![arg2]
                    .article_no
            }
    }

    private fun initialiseUI() {
        // TODO Auto-generated method stub
        listArticle = ArrayList()
        val actionBar = supportActionBar
        actionBar!!.subtitle = ""
        actionBar.title = ""
        actionBar.show()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setHomeButtonEnabled(true)
        listViewArticle = findViewById<View>(R.id.listViewArticle) as ListView
        articleApi
    }

    // TODO Auto-generated method stub
    private val articleApi: Unit
        private get() {
            var manager: VKCInternetManager? = null
            listArticle!!.clear()
            val name = arrayOf("article_no")
            val value = arrayOf(getUserId(this))
            manager = VKCInternetManager(VKCUrlConstants.URL_ARTICLE_SEARCH_PRODUCT)
            manager.getResponsePOST(mActivity, name, value, object : ResponseListener {
                override fun responseSuccess(successResponse: String?) {
                    if (successResponse != null) {
                        parseMyDealerJSON(successResponse)
                    }
                }

                override fun responseFailure(failureResponse: String?) {
                    // TODO Auto-generated method stub
                }
            })
        }

    private fun parseMyDealerJSON(successResponse: String) {
        // TODO Auto-generated method stub
        try {
            val respObj = JSONObject(successResponse)
            val response = respObj.getJSONObject("response")
            val status = response.getString("status")
            if (status == "Success") {
                val respArray = response.getJSONArray("orderdetails")
                for (i in 0 until respArray.length()) {
                    val model = ArticleModel()
                    val obj = respArray.getJSONObject(i)
                    model.id = obj.getString("id")
                    model.article_no = obj.getString("article_no")
                    listArticle!!.add(model)
                }
                val adapter = ArticleAdapter(
                    mActivity!!,
                    listArticle!!
                )
                listViewArticle!!.adapter = adapter
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}