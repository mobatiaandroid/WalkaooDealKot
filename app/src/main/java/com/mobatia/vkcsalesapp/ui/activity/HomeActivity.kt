/*
package com.mobatia.vkcsalesapp.ui.activity

import android.content.Context
import android.content.res.TypedArray
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataBrand
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataCategory
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataColor
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataOffer
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataPrice
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.saveFilterDataSize
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var mContext: AppCompatActivity
    private val navMenuIcons: TypedArray? = null
    private var navMenuTitles: Array<String> = arrayOf(String())
    private var categoryIdList: Array<String> = arrayOf(String())
    private val navDrawerItems: ArrayList<MenuNavigationModel>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(activity_main_toolbar)
        mContext = this
        val intent = intent.extras
        navMenuTitles = intent?.getStringArray("MAINCATEGORYNAMELIST") as Array<String>
        categoryIdList = intent?.getStringArray("MAINCATEGORYIDLIST") as Array<String>

        saveFilterDataCategory(mContext, "")
        saveFilterDataSize(mContext, "")
        saveFilterDataBrand(mContext, "")
        saveFilterDataColor(mContext, "")
        saveFilterDataPrice(mContext, "")
        saveFilterDataOffer(mContext, "")
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawerLayout,
            activity_main_toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        ) {
            override fun onDrawerClosed(drawerView: View) {
                // Triggered once the drawer closes
                super.onDrawerClosed(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }

            override fun onDrawerOpened(drawerView: View) {
                // Triggered once the drawer opens
                super.onDrawerOpened(drawerView)
                try {
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

    }

    override fun onBackPressed() {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun displayScreen(id: Int) {

        */
/* supportFragmentManager
             .beginTransaction()
             .replace(R.id.relativeLayout, fragment)
             .commit()*//*

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {


        return true
    }
}


*/
