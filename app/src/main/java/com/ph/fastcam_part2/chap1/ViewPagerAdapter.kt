package com.ph.fastcam_part2.chap1

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(private val secondActivity: SecondActivity) : FragmentStateAdapter(secondActivity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                WebToonFragment()
            }
            1->{
                Web2Fragment()
            }
            else ->{
                WebToonFragment()
            }
        }
    }
}