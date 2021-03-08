package com.example.echo_kt.ui.main

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IndexOutOfBoundsException

const val HOME_PAGE_INDEX = 0
const val VIDEO_PAGE_INDEX = 1
const val MY_PAGE_INDEX = 2

class MainPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        HOME_PAGE_INDEX to { HomeFragment() },
        VIDEO_PAGE_INDEX to { VideoFragment() },
        MY_PAGE_INDEX to { MyFragment() }
    )

    override fun getItemCount(): Int {
        return tabFragmentsCreators.size
    }

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}
