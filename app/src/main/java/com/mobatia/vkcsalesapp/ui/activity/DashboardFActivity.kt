/*
package com.mobatia.vkcsalesapp.ui.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.database.MatrixCursor
import android.database.SQLException
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ExpandableListView
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.legacy.app.ActionBarDrawerToggle
import com.google.android.material.snackbar.Snackbar
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.SQLiteServices.DatabaseHelper
import com.mobatia.vkcsalesapp.adapter.NavDrawerExpandableListAdapter
import com.mobatia.vkcsalesapp.adapter.NavDrawerListAdapter
import com.mobatia.vkcsalesapp.adapter.SearchAdapter
import com.mobatia.vkcsalesapp.appdialogs.AppExitDialog
import com.mobatia.vkcsalesapp.constants.VKCDbConstants
import com.mobatia.vkcsalesapp.controller.AppController
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager
import com.mobatia.vkcsalesapp.manager.AppPrefenceManager.getUserType
import com.mobatia.vkcsalesapp.manager.DataBaseManager
import com.mobatia.vkcsalesapp.model.CategoryModel
import com.mobatia.vkcsalesapp.model.NavDrawerItem
import com.mobatia.vkcsalesapp.model.SortCategory
import com.mobatia.vkcsalesapp.ui.fragments.*
import java.io.IOException
import java.util.*

class DashboardFActivity : AppCompatActivity(), VKCDbConstants {
    private var mDrawerLayout: DrawerLayout? = null
    var lvExp: ExpandableListView? = null

    lateinit var mDrawerToggle: ActionBarDrawerToggle
    private var mDrawerTitle: CharSequence? = null
    private var mTitle: CharSequence? = null
    lateinit var navMenuTitles : Array<String>
    lateinit var categoryIdList: Array<String>
    lateinit var navMenuIcons: TypedArray
    private var navDrawerItems: ArrayList<NavDrawerItem>? = null
    private var adapter:NavDrawerListAdapter NavDrawerListAdapter? = null
    var mFragmentManager: FragmentManager? = null
    var mFragmentTransaction: FragmentTransaction? = null
    private var items: MutableList<String>? = null
    var searchView: SearchView? = null
    var userType: String? = null
    var mDealerCount = 0
    var mRoleId = 0
    var key = ""
    var mContext: Context? = null
    companion object {
        var categorySelectionPosition = -1
        private var mNavDrawerItemIndex = 0
        lateinit var dashboardFActivity: DashboardFActivity
        lateinit var categoryId:String

    }
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppController.product_id = ""
        setContentView(R.layout.activity_dashboard_f)
        mDrawerTitle = getTitle()
        mTitle = mDrawerTitle
        dashboardFActivity=this
         navMenuTitles= arrayOf<String>()
         categoryIdList=arrayOf<String>()
        AppPrefenceManager.saveFilterDataCategory(this, "")
        AppPrefenceManager.saveFilterDataSize(this, "")
        AppPrefenceManager.saveFilterDataBrand(this, "")
        AppPrefenceManager.saveFilterDataColor(this, "")
        AppPrefenceManager.saveFilterDataPrice(this, "")
        AppPrefenceManager.saveFilterDataOffer(this, "")
        setIconTitle()
        Thread.setDefaultUncaughtExceptionHandler(
            AppController(
                this, DashboardFActivity::class.java
            ) as Thread.UncaughtExceptionHandler?
        )
        checkDatabase()
        initArray()
         initUI(savedInstanceState)
        initProductDetails()
    }

    private fun setIconTitle() {
        navMenuTitles = intent.getStringArrayExtra("MAINCATEGORYNAMELIST")!!
        categoryIdList = intent.getStringArrayExtra("MAINCATEGORYIDLIST")!!
        userType = getUserType(this)
        mDealerCount = AppPrefenceManager
            .getDealerCount(this)?.toInt()!!
        mRoleId = if (getUserType(this) == "") {
            0
        } else {
            getUserType(this)!!.toInt()
        }
        navMenuIcons = getResources()
            .obtainTypedArray(R.array.nav_drawer_icons)
    }

    protected fun checkDatabase() {
        val myDbHelper = DatabaseHelper(
            this@DashboardFActivity,
            VKCDbConstants.DBNAME
        )
        try {
            myDbHelper.createDataBase()
        } catch (ioe: IOException) {
            throw Error("Unable to create database")
        }
        try {
            myDbHelper.openDataBase()
            myDbHelper.close()
        } catch (sqle: SQLException) {
            throw sqle
        }
    }

    private fun initArray() {
      //  println("21012015:$userType")
        navDrawerItems = ArrayList<NavDrawerItem>()

        // Home
        navMenuIcons
            ?.getResourceId(0, -1)?.let {
                NavDrawerItem(
                    "Home", it, "-1"
                )
            }?.let {
                navDrawerItems!!.add(
                    it
            )
            }
        for (i in navMenuTitles.indices) {
            navMenuIcons
                ?.getResourceId(i + 1, -1)?.let {
                    NavDrawerItem(
                        navMenuTitles[i], it, categoryIdList[i]
                    )
                }?.let {
                    navDrawerItems!!.add(
                        it
                    )
                }
            // System.out.println("NAVMENu"+navMenuTitles.length);
        }
        navDrawerItems!!.add(
            NavDrawerItem(
                "Locate Us", R.drawable.brand,
                "-3"
            )
        )
        navDrawerItems!!.add(
            NavDrawerItem(
                "Contact Us", R.drawable.location,
                "-5"
            )
        )

        // App Version
        navDrawerItems!!.add(
            NavDrawerItem(
                "Version : $version",
                R.color.transparent, "-7"
            )
        )
        // navDrawerItems.add(new NavDrawerItem("Version 1.2"));
        navMenuIcons?.recycle()
    }

    @SuppressLint("NewApi")
    private fun initUI(savedInstanceState: Bundle?) {
        mDrawerLayout = findViewById<View>(R.id.drawer_layout) as DrawerLayout?
        lvExp = findViewById<View>(R.id.lvExp) as ExpandableListView?
        val width: Int = getResources().getDisplayMetrics().widthPixels / 2
        val params: DrawerLayout.LayoutParams = lvExp
            ?.getLayoutParams() as DrawerLayout.LayoutParams
        params.width = width
        lvExp?.setLayoutParams(params)
        adapter = navDrawerItems?.let {
            NavDrawerListAdapter(
                getApplicationContext(),
                it
            )
        }
        val hashMapNavDrawerItemChild: HashMap<NavDrawerItem, ArrayList<NavDrawerItem>> =
            HashMap<NavDrawerItem, ArrayList<NavDrawerItem>>()
        val sortCategory: Array<SortCategory> = SplashActivity.sortCategoryGlobal
            ?.sortCategorys!!
        // println("13022015 SortCategory size:" + sortCategory.size)
        lvExp?.setGroupIndicator(null)
        val itemsHome: ArrayList<NavDrawerItem> = ArrayList<NavDrawerItem>()
        hashMapNavDrawerItemChild[navDrawerItems!![0]] = itemsHome
        for (j in sortCategory.indices) {
            val items: ArrayList<NavDrawerItem> = ArrayList<NavDrawerItem>()
            val filterArrayList: ArrayList<CategoryModel> = sortCategory[j]
                .categoryModels
            for (i in filterArrayList.indices) {
                val categoryModel: CategoryModel = filterArrayList[i]
                val drawerItem = NavDrawerItem()
                drawerItem.title = categoryModel.name
                drawerItem.id = categoryModel.id
                items.add(drawerItem)
            }
            hashMapNavDrawerItemChild[navDrawerItems!![j + 1]] = items
        }
        val expandableListAdapter = NavDrawerExpandableListAdapter(
            this, navDrawerItems!!, hashMapNavDrawerItemChild!!,
            object : NavDrawerExpandableListAdapter.OnExploreListener {
                override fun onExpandGroup(position: Int) {
                    // TODO Auto-generated method stub
                    lvExp?.expandGroup(position, true)
                }

                override fun onCollapseGrope(position: Int) {
                    // TODO Auto-generated method stub
                    lvExp?.collapseGroup(position)
                }
            })
        expandableListAdapter.setOnItemClickListener(object :
            NavDrawerExpandableListAdapter.OnItemClickListener {
            override fun onItemSelected(id: String?) {
                AppPrefenceManager.saveSubCategoryId(
                    this@DashboardFActivity,
                    id + ""
                )
                id?.toInt()?.let { goToProductList(it) }
            }
        })
        lvExp?.setAdapter(expandableListAdapter)
        lvExp?.setBackgroundColor(Color.parseColor("#DDFFFFFF"))
        setActionBar()

        mDrawerToggle = object : ActionBarDrawerToggle(
            this, mDrawerLayout,
            R.drawable.backbutton, R.string.app_name, R.string.app_name
        ) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                invalidateOptionsMenu()
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                invalidateOptionsMenu()

            }
        }

        mDrawerLayout!!.setDrawerListener(mDrawerToggle)
        mDrawerLayout?.closeDrawers()
        beginFragmentTransaction()
        // set
        if (savedInstanceState == null) {
            if (AppPrefenceManager.getDealerCount(this) == "0" && getUserType(
                    this
                ) == "7"
            ) // && mRoleId == 7
            {
                displayView(-4)
            } else {
                displayView(-1)
            }
        }
    }

    private fun goToProductList(position: Int) {
        // display view for selected item
        mDrawerLayout?.closeDrawers()
        mNavDrawerItemIndex = position
        AppPrefenceManager.saveListingOption(this@DashboardFActivity, "0")
        if (position == -1) {
            displayView(position)
        } else if (position == 1) {
            categorySelectionPosition = position - 1
            categoryId = categoryIdList[position - 1]
            displayView(position)
        } else if (position == 2) {
            categorySelectionPosition = position - 1
            categoryId = categoryIdList[position - 1]
            displayView(position)
        } else if (position == 3) {
            categorySelectionPosition = position - 1
            categoryId = categoryIdList[position - 1]
            displayView(position)
        } else if (position == 4) {
            categorySelectionPosition = position - 1
            categoryId = categoryIdList[position - 1]
            displayView(position)
        } else if (position == 5) {
            categorySelectionPosition = position - 1
            categoryId = categoryIdList[position - 1]
            displayView(position)
        } else if (position == -2) {
            AppPrefenceManager.saveListingOption(this@DashboardFActivity, "3")
            displayView(position)
        } else if (position == -4) {
            displayView(position)
        } else if (position == -3) {
            displayView(position)
        } else if (position == -5) {
            displayView(position)
        } else if (position == -7) {
        } else { // 09 /01/2015
            displayView(position)
            AppPrefenceManager.saveListingOption(this@DashboardFActivity, "5")
        }
    }

    @SuppressLint("NewApi")
    fun setActionBar() {
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setHomeButtonEnabled(true)
        val actionBar: ActionBar? = getSupportActionBar()
        actionBar?.setHomeAsUpIndicator(R.drawable.backbutton)
        actionBar?.setSubtitle("")
        actionBar?.setTitle("")
        actionBar?.show()
    }

    private var exitFlag = true
    private fun setHomeToFront() {
        beginFragmentTransaction()
        detachAllFragment()
        exitFlag = true
        if (mHomeFragment==null) {
            mFragmentTransaction!!.add(
                R.id.frame_container, HomeFragment(),
                "Home"
            )
        } else {
            mFragmentTransaction!!.attach(mHomeFragment!!)
        }
        mFragmentTransaction!!.commit()
        title = navMenuTitles[0]
        mDrawerLayout!!.closeDrawer(lvExp!!)
    }

    private fun showExirDialog(str: String) {
        val appExitDialog = AppExitDialog(
            this@DashboardFActivity, str
        )
        appExitDialog.getWindow()?.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        appExitDialog.setCancelable(true)
        appExitDialog.show()
    }

    override fun onBackPressed() {
        if (AppController.isDealerList) {
            displayView(-1)
        } else {
            if (exitFlag) {
                showExirDialog("")
            } else {
                setHomeToFront()
            }
        }
    }

    fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (!isConnected) {
            showSnack(isConnected)
        }
    }

    private inner class SlideMenuClickListener : AdapterView.OnItemClickListener {
        override fun onItemClick(
            parent: AdapterView<*>?, view: View, position: Int,
            id: Long
        ) {
            // display view for selected item
            mNavDrawerItemIndex = position
            AppPrefenceManager.saveListingOption(this@DashboardFActivity, "0")
            if (position == 0) {
                displayView(position)
            } else if (position == 1) {
                categorySelectionPosition = position - 1
                categoryId = categoryIdList[position - 1]
                displayView(position)
            } else if (position == 2) {
                categorySelectionPosition = position - 1
                categoryId = categoryIdList[position - 1]
                displayView(position)
            } else if (position == 3) {
                categorySelectionPosition = position - 1
                categoryId = categoryIdList[position - 1]
                displayView(position)
            } else if (position == 4) {
                categorySelectionPosition = position - 1
                categoryId = categoryIdList[position - 1]
                displayView(position)
            } else if (position == 5) {
                categorySelectionPosition = position - 1
                categoryId = categoryIdList[position - 1]
                displayView(position)
            } else if (position == navMenuTitles.size + 1) {
                AppPrefenceManager.saveListingOption(
                    this@DashboardFActivity,
                    "3"
                )
                categorySelectionPosition = position - 2
                categoryId = categoryIdList[position - 2]
                displayView(position)
            } else if (position == navMenuTitles.size + 2) {
                categorySelectionPosition = position - 3
                categoryId = categoryIdList[position - 3]
                displayView(position)
            }
            if (position == navMenuTitles.size + 3) {
                categorySelectionPosition = position - 4
                categoryId = categoryIdList[position - 4]
                displayView(position)
            }
        }
    }

    var mHomeFragment: HomeFragment? = null

    // StoreFragment mStoreFActivity;
    var mProductFragment: ProductListNew_Fragment? = null
    var mVkcDealersListView: VKCDealersListView? = null

    // ProductListFragment mProductFragment;
    var mSearchFragment: SearchListFragment? = null
    var mLocationFragment: LocationFragment? = null
    var mComplaintFragment: ComplaintFragment? = null
    var mFeedbackFragment: FeedbackFragment? = null
    var mSalesOrderFragment: SalesOrderFragment? = null
    var mSubdealerOrderFragment: SubDealerOrderList? = null
    var mPendingOrderFragment: SubdealerOrderStatusFragment? = null
    var dealersRetailersAndDateFilter: DealersRetailersAndDateFilter? = null
    var mContactUsFragment: ContactUsFragment? = null
    var creditPaymentStatusFragment: CreditPaymentFragment? = null
    var mRecentOrderFragment: RecentOrdersFragment? = null
    var salesOrderStatusListFragment: SalesOrderStatusListFragment? = null
    var msalesHeadOrderFragment: SalesHeadOrderList? = null
    var mNotificationsFragment: NotificationListFragment? = null
    var mQuickOrderFragment: Quick_Order_Fragment? = null
    var mSchemePoints: SchemePointsFragment? = null
    var mTransactionHistory: TransactionHistoryFragment? = null
    var mCustomersFragment: MyCustomersFragment? = null
    var mRedeemListFragment: RedeemedListFragment? = null
    var mRedeemReportFragment: RedeemReportFragment? = null
    var mGiftRewardFragment: GiftRewardReportFragment? = null
    private fun beginFragmentTransaction() {
        mFragmentManager = getSupportFragmentManager()
        mHomeFragment = mFragmentManager!!
            .findFragmentByTag("Home") as HomeFragment?
        mProductFragment = mFragmentManager!!
            .findFragmentByTag("Category") as ProductListNew_Fragment?
        mCustomersFragment = mFragmentManager!!
            .findFragmentByTag("Customers") as MyCustomersFragment?
        mSearchFragment = mFragmentManager!!
            .findFragmentByTag("Search") as SearchListFragment?
        mLocationFragment = mFragmentManager!!
            .findFragmentByTag("Locate Us") as LocationFragment?
        mContactUsFragment = mFragmentManager!!
            .findFragmentByTag("Contact Us") as ContactUsFragment?
        mComplaintFragment = mFragmentManager!!
            .findFragmentByTag("Complaint") as ComplaintFragment?
        mFeedbackFragment = mFragmentManager!!
            .findFragmentByTag("FeedBack") as FeedbackFragment?
        mSalesOrderFragment = mFragmentManager!!
            .findFragmentByTag("Sales") as SalesOrderFragment?
        mSubdealerOrderFragment = mFragmentManager!!
            .findFragmentByTag("SubDealerOrderList") as SubDealerOrderList?
        dealersRetailersAndDateFilter = mFragmentManager!!
            .findFragmentByTag("DealersRetailersAndDateFilter") as DealersRetailersAndDateFilter?
        creditPaymentStatusFragment = mFragmentManager!!
            .findFragmentByTag("CreditPaymentStatusFragment") as CreditPaymentFragment?
        salesOrderStatusListFragment = mFragmentManager!!
            .findFragmentByTag("SalesOrder") as SalesOrderStatusListFragment?
        mPendingOrderFragment = mFragmentManager!!
            .findFragmentByTag("PendingOrder") as SubdealerOrderStatusFragment?
        mRecentOrderFragment = mFragmentManager!!
            .findFragmentByTag("RecentOrder") as RecentOrdersFragment?
        msalesHeadOrderFragment = mFragmentManager!!
            .findFragmentByTag("SalesHead") as SalesHeadOrderList?
        mNotificationsFragment = mFragmentManager!!
            .findFragmentByTag("Notifications") as NotificationListFragment?
        mQuickOrderFragment = mFragmentManager!!
            .findFragmentByTag("QuickOrder") as Quick_Order_Fragment?
        mSchemePoints = mFragmentManager!!
            .findFragmentByTag("SchemePoints") as SchemePointsFragment?
        mTransactionHistory = mFragmentManager!!
            .findFragmentByTag("History") as TransactionHistoryFragment?
        mRedeemListFragment = mFragmentManager!!
            .findFragmentByTag("RedeemList") as RedeemedListFragment?
        mRedeemReportFragment = mFragmentManager!!
            .findFragmentByTag("RedeemReport") as RedeemReportFragment?
        mGiftRewardFragment = mFragmentManager!!
            .findFragmentByTag("GiftReward") as GiftRewardReportFragment?
        mVkcDealersListView = mFragmentManager!!
            .findFragmentByTag("VKC dealers") as VKCDealersListView?
        mFragmentTransaction = mFragmentManager!!.beginTransaction()


    }

    private fun detachAllFragment() {
        if (mHomeFragment != null) {
            mFragmentTransaction!!.detach(mHomeFragment!!)
        }
        if (mProductFragment != null) {
            mFragmentTransaction!!.detach(mProductFragment!!)
            AppPrefenceManager.saveProductListSortOption(
                this@DashboardFActivity, "0"
            )
        }
        if (mSearchFragment != null) {
            mFragmentTransaction!!.detach(mSearchFragment!!)
        }
        if (mLocationFragment != null) {
            mFragmentTransaction!!.detach(mLocationFragment!!)
        }
        if (mVkcDealersListView != null) {
            mFragmentTransaction!!.detach(mVkcDealersListView!!)
        }
        if (mComplaintFragment != null) {
            mFragmentTransaction!!.detach(mComplaintFragment!!)
        }
        if (mFeedbackFragment != null) {
            mFragmentTransaction!!.detach(mFeedbackFragment!!)
        }
        if (mSalesOrderFragment != null) {
            mFragmentTransaction!!.detach(mSalesOrderFragment!!)
        }
        if (mSalesOrderFragment != null) {
            mFragmentTransaction!!.detach(mSalesOrderFragment!!)
        }
        if (dealersRetailersAndDateFilter != null) {
            mFragmentTransaction!!.detach(dealersRetailersAndDateFilter!!)
        }
        if (creditPaymentStatusFragment != null) {
            mFragmentTransaction!!.detach(creditPaymentStatusFragment!!)
        }
        if (salesOrderStatusListFragment != null) {
            mFragmentTransaction!!.detach(salesOrderStatusListFragment!!)
        }
        if (mSubdealerOrderFragment != null) {
            mFragmentTransaction!!.detach(mSubdealerOrderFragment!!)
        }
        if (mPendingOrderFragment != null) {
            mFragmentTransaction!!.detach(mPendingOrderFragment!!)
        }
        if (mContactUsFragment != null) {
            mFragmentTransaction!!.detach(mContactUsFragment!!)
        }
        if (mRecentOrderFragment != null) {
            mFragmentTransaction!!.detach(mRecentOrderFragment!!)
        }
        if (msalesHeadOrderFragment != null) {
            mFragmentTransaction!!.detach(msalesHeadOrderFragment!!)
        }
        if (mNotificationsFragment != null) {
            mFragmentTransaction!!.detach(mNotificationsFragment!!)
        }
        if (mQuickOrderFragment != null) {
            mFragmentTransaction!!.detach(mQuickOrderFragment!!)
        }
        if (mSchemePoints != null) {
            mFragmentTransaction!!.detach(mSchemePoints!!)
        }
        if (mTransactionHistory != null) {
            mFragmentTransaction!!.detach(mTransactionHistory!!)
        }
        if (mCustomersFragment != null) {
            mFragmentTransaction!!.detach(mCustomersFragment!!)
        }
        if (mRedeemListFragment != null) {
            mFragmentTransaction!!.detach(mRedeemListFragment!!)
        }
        if (mRedeemReportFragment != null) {
            mFragmentTransaction!!.detach(mRedeemReportFragment!!)
        }
        if (mGiftRewardFragment != null) {
            mFragmentTransaction!!.detach(mGiftRewardFragment!!)
        }
    }

    interface DisplayVIewListener {
        fun setDisplayViewListener(i: Int)
    }

    fun setDisplayView() {
        displayView(2)
    }

    fun goToSearchWithKey(key: String) {
        this.key = key
        displayView(-2)
    }

    fun getcategoryIdList(): Array<String> {
        Log.v("LOG", "22122014 getcategoryIdList in display view")
        return categoryIdList
    }

    fun displayView(position: Int) {
        // update the main content with called Fragment
        beginFragmentTransaction()
        detachAllFragment()
        val fragment: Fragment? = null
        exitFlag = if (position == 0) {
            true
        } else {
            false
        }
        if (position == -1) {
            if (mHomeFragment == null) {
                val homeFragment = HomeFragment()
                val bundle = Bundle()
                bundle.putString("NAME", "VALUE")
                homeFragment.setArguments(bundle)
                mFragmentTransaction!!.add(
                    R.id.frame_container, homeFragment,
                    "Home"
                )
                exitFlag = true
            } else {
                mFragmentTransaction!!.attach(mHomeFragment!!)
            }
        } else if (position == -2) {
            if (mSearchFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SearchListFragment(), "Search"
                )
            } else {
                mFragmentTransaction!!.attach(mSearchFragment!!)
            }
        } else if (position == -3) {
            if (mLocationFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll")
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    LocationFragment(), "Locate Us"
                )
            } else {
                mFragmentTransaction!!.attach(mLocationFragment!!)
            }
        } else if (position == -5) {
            if (mContactUsFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    ContactUsFragment(), "Contact Us"
                )
            } else {
                mFragmentTransaction!!.attach(mContactUsFragment!!)
            }
        } else if (position == -4) {
            if (mVkcDealersListView == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    VKCDealersListView(), "VKC dealers"
                )
            } else {
                mFragmentTransaction!!.attach(mVkcDealersListView!!)
            }
        } else if (position == R.id.feedback) {
            if (mFeedbackFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    FeedbackFragment(), "FeedBack"
                )
            } else {
                mFragmentTransaction!!.attach(mFeedbackFragment!!)
            }
        } else if (position == R.id.notifications) {
            if (mNotificationsFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    NotificationListFragment(), "Notifications"
                )
            } else {
                mFragmentTransaction!!.attach(mNotificationsFragment!!)
            }
        } else if (position == R.id.complaint) {
            if (mComplaintFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    ComplaintFragment(), "Complaint"
                )
            } else {
                mFragmentTransaction!!.attach(mComplaintFragment!!)
            }
        } else if (position == R.id.quick_order) {
            if (mQuickOrderFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    Quick_Order_Fragment(), "QuickOrder"
                )
            } else {
                mFragmentTransaction!!.attach(mQuickOrderFragment!!)
            }
        } else if (position == R.id.schemePoints) {
            if (mSchemePoints == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SchemePointsFragment(), "SchemePoints"
                )
            } else {
                mFragmentTransaction!!.attach(mSchemePoints!!)
            }
        } else if (position == R.id.logout) {
            AppPrefenceManager.saveUserType(this@DashboardFActivity, "")
            AppPrefenceManager.saveUserId(this@DashboardFActivity, "")
            AppPrefenceManager.saveIsCredit(this@DashboardFActivity, "no")
            AppPrefenceManager.saveLoginStatusFlag(this@DashboardFActivity, "false")
            AppPrefenceManager.saveSelectedUserName(this@DashboardFActivity, "")
            AppPrefenceManager.saveSelectedUserId(this@DashboardFActivity, "")
            AppPrefenceManager.saveCustomerCategory(this@DashboardFActivity, "")
            AppPrefenceManager.setIsCallPendingAPI(this@DashboardFActivity, true)
            AppPrefenceManager.saveIsGroupMember(this@DashboardFActivity, "0")
            AppPrefenceManager.saveCustomerId(this@DashboardFActivity, "")
            AppController.cartArrayListSelected.clear()
            AppController.cartArrayList.clear()
            clearDb()
            val intent = Intent(
                this@DashboardFActivity,
                SplashActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        } else if (position == R.id.switchAccount) {
            startActivity(
                Intent(
                    this@DashboardFActivity,
                    GroupMemberActivity::class.java
                )
            )
            finish()
        } else if (position == R.id.create_new) {
            if (mSalesOrderFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SalesOrderFragment(), "Sales"
                ) // For Order submit
            } else {
                mFragmentTransaction!!.attach(mSalesOrderFragment!!)
            }
        } else if (position == R.id.salesHeadOrders) {
            if (msalesHeadOrderFragment == null) {
                Log.d("Fragment Lifecycle", "Fragment nulll")
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SalesHeadOrderList(), "SalesHead"
                ) // For Order
            } else {
                mFragmentTransaction!!.attach(msalesHeadOrderFragment!!)
            }
        } else if (position == R.id.credit) {
            if (creditPaymentStatusFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    CreditPaymentFragment(),
                    "CreditPaymentStatusFragment"
                )
            } else {
                mFragmentTransaction!!.attach(creditPaymentStatusFragment!!)
            }
        } else if (position == R.id.redeemList) {
            if (mRedeemListFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    RedeemedListFragment(), "RedeemList"
                ) // For Order
                // submit
            } else {
                mFragmentTransaction!!.attach(mRedeemListFragment!!)
            }
        } else if (position == R.id.salesorder) {
            if (salesOrderStatusListFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SalesOrderStatusListFragment(), "SalesOrder"
                )
            } else {
                mFragmentTransaction!!.attach(salesOrderStatusListFragment!!)
            }
        } else if (position == R.id.subdealerorder) {
            if (mSubdealerOrderFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SubDealerOrderList(), "SubDealerOrderList"
                )
            } else {
                mFragmentTransaction!!.attach(mSubdealerOrderFragment!!)
            }
        } else if (position == R.id.pendingorder) {
            if (mSubdealerOrderFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SubdealerOrderStatusFragment(), "PendingOrder"
                )
            } else {
                mFragmentTransaction!!.attach(mPendingOrderFragment!!)
            }
        } else if (position == R.id.salesmngmt) {
            if (salesOrderStatusListFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SalesOrderStatusListFragment(), "SalesOrder"
                )
            } else {
                mFragmentTransaction!!.attach(salesOrderStatusListFragment!!)
            }
        } else if (position == R.id.transactionHistory) {
            if (mTransactionHistory == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    TransactionHistoryFragment(), "History"
                )
            } else {
                mFragmentTransaction!!.attach(mTransactionHistory!!)
            }
        } else if (position == R.id.redeemReport) {
            if (mRedeemReportFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    RedeemReportFragment(), "RedeemReport"
                )
            } else {
                mFragmentTransaction!!.attach(mRedeemReportFragment!!)
            }
        } else if (position == R.id.giftRewardReport) {
            if (mGiftRewardFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    GiftRewardReportFragment(), "GiftReward"
                )
            } else {
                mFragmentTransaction!!.attach(mGiftRewardFragment!!)
            }
        } else if (position == R.id.recentOrder) {
            if (mRecentOrderFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    RecentOrdersFragment(), "RecentOrder"
                )
            } else {
                mFragmentTransaction!!.attach(mRecentOrderFragment!!)
            }
        } else if (position == R.id.myCustomers) {
            if (mCustomersFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    MyCustomersFragment(), "Customers"
                )
            } else {
                mFragmentTransaction!!.attach(mCustomersFragment!!)
            }
        } else if (position == 22) {
            if (mSalesOrderFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    SalesOrderFragment(), "Sales"
                ) // For Order submit
            } else {
                mFragmentTransaction!!.attach(mSalesOrderFragment!!)
            }
        } else if (position == 11) {
            if (mProductFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    ProductListNew_Fragment(), "Category"
                )
            } else {
                mFragmentTransaction!!.attach(mProductFragment!!)
            }
        } else {
            if (mProductFragment == null) {
                mFragmentTransaction!!.add(
                    R.id.frame_container,
                    ProductListNew_Fragment(), "Category"
                )
            } else {
                mFragmentTransaction!!.attach(mProductFragment!!)
            }
        }
        mFragmentTransaction!!.commit()
        if (position < navMenuTitles.size && position > 0) {
            setTitle(navMenuTitles[position])
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.main, menu)
        val userName = menu.findItem(R.id.userName)
        val s = SpannableString(
            AppPrefenceManager.getUserName(this)
        )
        s.setSpan(
            ForegroundColorSpan(Color.parseColor("#cb181f")), 0,
            s.length, 0
        )
        AppPrefenceManager
            .getUserName(this)?.length?.let {
                s.setSpan(
                    StyleSpan(Typeface.BOLD), 0, it, Spannable.SPAN_PARAGRAPH
            )
            }
        userName.setTitle(s)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else when (item.itemId) {
            R.id.home -> {
                if (mDrawerLayout?.isDrawerOpen(mDrawerLayout!!) == true) {
                    mDrawerLayout?.closeDrawer(mDrawerLayout!!)
                } else {
                    mDrawerLayout?.openDrawer(GravityCompat.START)
                }
                true
            }
            R.id.complaint -> {
                displayView(R.id.complaint)
                true
            }
            R.id.feedback -> {
                displayView(R.id.feedback)
                true
            }
            R.id.logout -> {
                mNavDrawerItemIndex = R.id.logout
                displayView(R.id.logout)
                true
            }
            R.id.create_new -> {
                mNavDrawerItemIndex = R.id.create_new
                displayView(R.id.create_new)
                true
            }
            R.id.credit -> {
                mNavDrawerItemIndex = R.id.credit
                displayView(R.id.credit)
                true
            }
            R.id.salesorder -> {
                mNavDrawerItemIndex = R.id.salesorder
                displayView(R.id.salesorder)
                true
            }
            R.id.salesmngmt -> {
                mNavDrawerItemIndex = R.id.salesmngmt
                displayView(R.id.salesmngmt)
                true
            }
            R.id.subdealerorder -> {
                mNavDrawerItemIndex = R.id.subdealerorder
                displayView(R.id.subdealerorder)
                true
            }
            R.id.pendingorder -> {
                mNavDrawerItemIndex = R.id.pendingorder
                displayView(R.id.pendingorder)
                true
            }
            R.id.recentOrder -> {
                mNavDrawerItemIndex = R.id.recentOrder
                displayView(R.id.recentOrder)
                true
            }
            R.id.notifications -> {
                mNavDrawerItemIndex = R.id.notifications
                displayView(R.id.notifications)
                true
            }
            R.id.quick_order -> {
                mNavDrawerItemIndex = R.id.quick_order
                displayView(R.id.quick_order)
                true
            }
            R.id.salesHeadOrders -> {
                mNavDrawerItemIndex = R.id.salesHeadOrders
                displayView(R.id.salesHeadOrders)
                true
            }
            R.id.schemePoints -> {
                mNavDrawerItemIndex = R.id.schemePoints
                displayView(R.id.schemePoints)
                true
            }
            R.id.transactionHistory -> {
                mNavDrawerItemIndex = R.id.transactionHistory
                displayView(R.id.transactionHistory)
                true
            }
            R.id.myCustomers -> {
                mNavDrawerItemIndex = R.id.myCustomers
                displayView(R.id.myCustomers)
                true
            }
            R.id.redeemReport -> {
                mNavDrawerItemIndex = R.id.redeemReport
                displayView(R.id.redeemReport)
                true
            }
            R.id.giftRewardReport -> {
                mNavDrawerItemIndex = R.id.giftRewardReport
                displayView(R.id.giftRewardReport)
                true
            }
            R.id.redeemList -> {
                mNavDrawerItemIndex = R.id.redeemList
                displayView(R.id.redeemList)
                true
            }
            R.id.switchAccount -> {
                mNavDrawerItemIndex = R.id.switchAccount
                displayView(R.id.switchAccount)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (mContext?.let {
                AppPrefenceManager.getIsCredit(it)
                    .equals("yes", ignoreCase = true)
            } == true
        ) {
            menu.findItem(R.id.credit).isVisible = true
        } else {
            menu.findItem(R.id.credit).isVisible = false
        }
        if (mContext?.let { getUserType(it) } == "4") {
            menu.findItem(R.id.salesmngmt).isVisible = false
            menu.findItem(R.id.salesorder).isVisible = false
            menu.findItem(R.id.subdealerorder).isVisible = false
            menu.findItem(R.id.pendingorder).isVisible = false
            menu.findItem(R.id.recentOrder).isVisible = false
            menu.findItem(R.id.recentOrder).isVisible = false
            // Bibin
            menu.findItem(R.id.schemePoints).isVisible = false
            menu.findItem(R.id.transactionHistory).isVisible = false
            menu.findItem(R.id.myCustomers).isVisible = false
            menu.findItem(R.id.redeemList).isVisible = false
            menu.findItem(R.id.redeemReport).isVisible = false
            menu.findItem(R.id.giftRewardReport).isVisible = false
            menu.findItem(R.id.salesHeadOrders).isVisible = true
        } else if (mContext?.let { getUserType(it) } ==
            "6"
        ) {
            // menu.findItem(R.id.salesorder).setTitle("SalesManagement");
            menu.findItem(R.id.recentOrder).isVisible = false
            menu.findItem(R.id.subdealerorder).isVisible = true
            menu.findItem(R.id.pendingorder).isVisible = false
            menu.findItem(R.id.salesHeadOrders).isVisible = false
        } else if (mContext?.let { getUserType(it) } ==
            "7"
        ) {
            menu.findItem(R.id.recentOrder).isVisible = true
            menu.findItem(R.id.salesorder).isVisible = false
            menu.findItem(R.id.pendingorder).isVisible = true
            menu.findItem(R.id.subdealerorder).isVisible = false
            menu.findItem(R.id.salesHeadOrders).isVisible = false
            menu.findItem(R.id.schemePoints).isVisible = false
            menu.findItem(R.id.transactionHistory).isVisible = false
            menu.findItem(R.id.myCustomers).isVisible = false
            menu.findItem(R.id.redeemList).isVisible = false
            menu.findItem(R.id.redeemReport).isVisible = false
            menu.findItem(R.id.giftRewardReport).isVisible = false
        } else {
            menu.findItem(R.id.subdealerorder).isVisible = false
            menu.findItem(R.id.pendingorder).isVisible = false
            menu.findItem(R.id.recentOrder).isVisible = false
            menu.findItem(R.id.salesHeadOrders).isVisible = false
        }
        if (mContext?.let { AppPrefenceManager.getIsGroupMember(it) } == "1") {
            menu.findItem(R.id.switchAccount).isVisible = true
        } else {
            menu.findItem(R.id.switchAccount).isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    // History
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private fun loadHistory(query: String, flagNoItem: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            // Cursor
            val columns = arrayOf("_id", "text")
            val temp = arrayOf<Any>(0, "VKC Sandals")
            val cursor = MatrixCursor(columns)
            if (!flagNoItem) {
                for (i in items!!.indices) {
                    if (items!![i].toLowerCase(Locale.getDefault())
                            .contains(query.toLowerCase(Locale.getDefault()))
                    ) {
                        temp[0] = i
                        temp[1] = items!![i]
                        // replaced s with i as s not used anywhere.
                        cursor.addRow(temp)
                    }
                }
            }

            // SearchView
            searchView!!.suggestionsAdapter = SearchAdapter(
                this, cursor,
                items!!
            )
            searchView!!.isVerticalScrollBarEnabled = false
        }
    }

    fun initProductDetails() {
        items = ArrayList()
        items?.add("VKC sandals")
        items?.add("VKC 2306 Sandals")
        items?.add("VKC Pride 3108 Sandals")
        items?.add("VKC 3311 Slippers")
        items?.add("VKC 3306 Slippers")
        items?.add("VKC 3385 Slippers")
        items?.add("VKC 3390 Slippers")
    }

    fun clearDb() {
        val databaseManager = mContext?.let {
            DataBaseManager(
                it
            )
        }
        databaseManager?.removeDb(VKCDbConstants.TABLE_SHOPPINGCART)
    }

    protected override fun onRestart() {
        // TODO Auto-generated method stub
        // AppController.isSelectedDealer=false;
        super.onRestart()
    }

    // TODO Auto-generated catch block
    private val version: String
        private get() {
            var packageinfo: PackageInfo? = null
            try {
                packageinfo = getPackageManager().getPackageInfo(
                    getPackageName(),
                    0
                )
            } catch (e: PackageManager.NameNotFoundException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }
            return packageinfo!!.versionName.toString()
        }

    protected override fun onStop() {
        // TODO Auto-generated method stub
        super.onStop()
    }

    private fun showSnack(isConnected: Boolean) {
        val message: String
        val color: Int
        if (isConnected) {
            message = "Good! Connected to Internet"
            color = Color.WHITE
        } else {
            message = "Sorry! Not connected to internet"
            color = Color.RED
        }
        val snackbar: Snackbar = Snackbar
            .make(findViewById<View>(R.id.fab), message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    protected override fun onResume() {
        super.onResume()
        // AppController.getInstance().setConnectivityListener(this);
    }


}*/
