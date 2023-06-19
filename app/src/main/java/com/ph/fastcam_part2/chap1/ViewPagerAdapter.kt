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
                WebToonFragment(position,"https://comic.naver.com/webtoon?tab=mon").apply {
                    listener = secondActivity
                }
            }
            1->{
                WebToonFragment(position,"https://comic.naver.com/webtoon?tab=tue").apply {
                    listener = secondActivity
                }
            }
            else ->{
                WebToonFragment(position,"https://comic.naver.com/webtoon?tab=wed").apply {
                    listener = secondActivity
                }
            }
        }
    }
}