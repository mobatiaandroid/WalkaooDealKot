package com.mobatia.vkcsalesapp.ui.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobatia.vkcsalesapp.R
import com.mobatia.vkcsalesapp.constants.VKCUrlConstants
import com.mobatia.vkcsalesapp.controller.AppController

class LocationFragment : Fragment(), VKCUrlConstants, OnMapReadyCallback {
    // this Fragment will be called from MainActivity
    private var mRootView: View? = null

    //MapView mapView;
    var googleMap: GoogleMap? = null
    private val myContext: FragmentActivity? = null
    var mapFragment: SupportMapFragment? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mRootView = inflater.inflate(
            R.layout.location_fragment, container,
            false
        )
        AppController.isCart = false
        val abar = (activity as AppCompatActivity?)!!.supportActionBar
        val viewActionBar = activity!!.layoutInflater.inflate(
            R.layout.actionbar_imageview, null
        )
        val params = ActionBar.LayoutParams( // Center the textview in the ActionBar !
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER
        )
        abar!!.setCustomView(viewActionBar, params)
        abar.setDisplayShowCustomEnabled(true)
        mapFragment = SupportMapFragment.newInstance()
        mapFragment!!.getMapAsync { googleMap ->
            val vkc = LatLng(11.20377, 75.82870)
            googleMap.addMarker(
                MarkerOptions().position(vkc)
                    .title("VKC")
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(vkc))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f))
        }

        ///getChildFragmentManager().beginTransaction().replace(R.id.mapview, mapFragment).commit();
        //init(mRootView, savedInstanceState);
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        childFragmentManager.beginTransaction().replace(R.id.mapview, mapFragment!!).commit()
    }

    override fun onResume() {
        //	mapView.onResume();
        //mapFragment.getMapAsync(this);
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        childFragmentManager.beginTransaction().replace(R.id.llMap, LocationFragment()).commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        //mapView.onDestroy();
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //mapView.onLowMemory();
    }

    override fun onMapReady(map: GoogleMap) {
        MapsInitializer.initialize(activity!!)
        googleMap = map
        val vkc = LatLng(11.20377, 75.82870)
        googleMap!!.addMarker(
            MarkerOptions().position(vkc)
                .title("VKC")
        )
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(vkc))
    }
}