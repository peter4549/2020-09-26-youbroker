package com.duke.elliot.kim.kotlin.youbroker.partners

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.duke.elliot.kim.kotlin.youbroker.R

class PartnersTabFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // TODO replace with binding
        val view = inflater.inflate(R.layout.fragment_promotions_tab, container, false)
        return view
    }
}