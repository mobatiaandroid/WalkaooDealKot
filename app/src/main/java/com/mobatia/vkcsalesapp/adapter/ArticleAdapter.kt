package com.mobatia.vkcsalesapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.model.ArticleModel
import java.util.ArrayList

class ArticleAdapter(
    var mActivity: Activity,
    articleArrayList: List<ArticleModel>
) : BaseAdapter() {
    var mInflater: LayoutInflater
    var ArticleArrayList: List<ArticleModel> = ArrayList()
    override fun getCount(): Int {
        // TODO Auto-generated method stub
        return ArticleArrayList.size
    }

    override fun getItem(position: Int): Any {
        // TODO Auto-generated method stub
        return ArticleArrayList.get(position)
    }

    override fun getItemId(position: Int): Long {
        // TODO Auto-generated method stub
        return 0
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        var view: View? = null
        if (convertView == null) {
            view = mInflater.inflate(R.layout.article_list_item, null)
            val article = view.findViewById<View>(R.id.txt_articleNo) as TextView
            article.text = ArticleArrayList[position].article_no
        } else {
            view = convertView
        }
        return view!!
    }

    init {
        ArticleArrayList = articleArrayList
        mInflater = LayoutInflater.from(mActivity)
    }
}