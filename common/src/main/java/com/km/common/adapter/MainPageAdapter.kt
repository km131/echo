package com.km.common.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.lang.IndexOutOfBoundsException

class MainPagerAdapter(fragment: Fragment,fragmentMap:Map<Int,()->Fragment>) : FragmentStateAdapter(fragment) {
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = fragmentMap

    override fun getItemCount(): Int = tabFragmentsCreators.size


    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}
