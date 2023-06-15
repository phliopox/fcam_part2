package com.ph.fastcam_part2.chap1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySecondBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(this@SecondActivity)
        }
/*
        binding.backToLastButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.frameLayout,WebToonFragment())
                commit()
            }
        }
        binding.changeTabNameButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                //replace()
            }
        }*/

    }

    override fun onBackPressed() {
        /* 현재 액티비티 위에 fragment 를 띄웠기 때문에 back 버튼 클릭시 fragment 내부에서 back 버튼 동작이 실행되는 것이 아니라
        앱이 종료되어버림(나의 경우 이전 activity 인 mainActivity 로 이동) -> super 호출 막기*/
        //super.onBackPressed()

        //val currentFragment = supportFragmentManager.fragments[0]
        val currentFragment = supportFragmentManager.fragments[binding.viewPager.currentItem]
        if(currentFragment is WebToonFragment){
            if(currentFragment.canGoBack()){
                currentFragment.goBack()
            }else{
                super.onBackPressed() // webView 에 뒤로갈 페이지가 없을경우에만 mainActivity 로 이동
            }
        }
    }
}