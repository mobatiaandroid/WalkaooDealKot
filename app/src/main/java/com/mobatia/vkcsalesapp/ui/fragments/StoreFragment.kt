package com.mobatia.vkcsalesapp.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.mobatia.vkcsalesapp.R

class StoreFragment  // this Fragment will be called from MainActivity
    : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.store_activity_fragment, container, false)
    }
}