package com.duke.elliot.kim.kotlin.youbroker.tab

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.duke.elliot.kim.kotlin.youbroker.R
import com.duke.elliot.kim.kotlin.youbroker.utility.OkCancelDialogFragment
import com.duke.elliot.kim.kotlin.youbroker.databinding.FragmentTabBinding
import com.duke.elliot.kim.kotlin.youbroker.my_page.MyPageTabFragment
import com.duke.elliot.kim.kotlin.youbroker.partners.PartnersTabFragment
import com.duke.elliot.kim.kotlin.youbroker.profile.ProfileHelper.profileNotCreated
import com.duke.elliot.kim.kotlin.youbroker.profile.ProfileHelper.requestProfileCreation
import com.duke.elliot.kim.kotlin.youbroker.promotion.PromotionsTabFragment
import com.duke.elliot.kim.kotlin.youbroker.sign_in.SignInActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class TabFragment: Fragment() {

    private lateinit var binding: FragmentTabBinding
    private lateinit var tabIconResourceIds: Array<Int>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_tab,
            container,
            false
        )

        tabIconResourceIds = arrayOf(
            R.drawable.ic_sharp_library_books_24,
            R.drawable.ic_sharp_people_24,
            R.drawable.ic_sharp_account_circle_24
        )

        initializeTabLayoutViewPager()

        return binding.root
    }

    private fun initializeTabLayoutViewPager() {
        binding.viewPager.adapter = FragmentStateAdapter(requireActivity())
        binding.viewPager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.tag = position
            tab.setIcon(tabIconResourceIds[position])
        }.attach()

        val linearLayout = binding.tabLayout.getChildAt(0) as LinearLayout

        @SuppressLint("ClickableViewAccessibility")
        for (i in 0 until linearLayout.childCount) {
            linearLayout.getChildAt(i).setOnTouchListener { _, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_UP) {
                    if (i != PROMOTIONS_TAB_INDEX) {
                        when {
                            notSignedIn() -> requestSignIn()
                            profileNotCreated() -> requestProfileCreation()
                            else -> binding.viewPager.setCurrentItem(i, true)
                        }
                    } else
                        binding.viewPager.setCurrentItem(i, true)
                }
                true
            }
        }
    }

    private fun notSignedIn() = FirebaseAuth.getInstance().currentUser == null

    private fun requestSignIn() {
        val okCancelDialogFragment =
            OkCancelDialogFragment(getString(R.string.sign_in),
                getString(R.string.request_sign_in_message)
            ).apply {
                setOkButtonOnClick {
                    this.startActivity(Intent(requireContext(), SignInActivity::class.java))
                    this.dismiss()
                }
            }

        okCancelDialogFragment.show(requireActivity().supportFragmentManager, tag)
    }

    class FragmentStateAdapter(fragmentActivity: FragmentActivity):
        androidx.viewpager2.adapter.FragmentStateAdapter(fragmentActivity) {
        private val pageCount = 3

        override fun getItemCount(): Int = pageCount

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> PromotionsTabFragment()
                1 -> PartnersTabFragment()
                2 -> MyPageTabFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

    companion object {
        private const val PROMOTIONS_TAB_INDEX = 0
    }
}