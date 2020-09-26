package com.duke.elliot.kim.kotlin.youbroker.my_page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.databinding.FragmentMyPageTabBinding
import com.duke.elliot.kim.kotlin.youbroker.utility.showToast
import com.google.firebase.auth.FirebaseAuth

class MyPageTabFragment: Fragment() {

    private lateinit var binding: FragmentMyPageTabBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_tab, container, false)

        // TODO Temporary button
        binding.buttonSignOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            showToast(requireContext(), "로그아웃")
        }

        return binding.root
    }
}