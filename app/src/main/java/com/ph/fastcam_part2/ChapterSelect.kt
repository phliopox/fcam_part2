package com.ph.fastcam_part2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ph.fastcam_part2.chap1.SecondActivity
import com.ph.fastcam_part2.databinding.FragmentChapterSelectorBinding

class ChapterSelect : Fragment(),View.OnClickListener {
    private lateinit var binding : FragmentChapterSelectorBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChapterSelectorBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.linear.children.forEach {
            it.setOnClickListener(this)
        }

    }

    override fun onClick(view: View) {
        Log.d(TAG, "ChapterSelect - onClick: ${view.id}");
        when(view.id){
            R.id.chap1_btn ->{
                //  findNavController().navigate(R.id.action_chapterSelect_to_webToonFragment)
                startActivity(Intent(context,SecondActivity::class.java))
            }
        }
    }
}