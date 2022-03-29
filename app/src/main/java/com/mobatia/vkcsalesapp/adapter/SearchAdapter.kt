/**
 *
 */
package com.mobatia.vkcsalesapp.adapter

import android.content.Context
import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import android.widget.TextView
import com.mobatia.vkcsalesapp.R

/**
 * @author Anshad
 */
class SearchAdapter constructor(
    context: Context?,
    var cursorSearch: Cursor,
    private val items: List<String>
) : CursorAdapter(context, cursorSearch, false) {
    private var text: TextView? = null
    public override fun newView(context: Context, cursor: Cursor, parent: ViewGroup): View {
        // TODO Auto-generated method stub
        val inflater: LayoutInflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.seach_row_item, parent, false)
        text = view.findViewById<View>(R.id.tvsearchItemRow) as TextView?
        return view
    }

    public override fun bindView(view: View, context: Context, cursor: Cursor) {
        // TODO Auto-generated method stub
        text!!.setText(items.get(cursor.getPosition()))
    }

    public override fun getCount(): Int {
        // TODO Auto-generated method stub
        return cursor.getCount()
    }
}