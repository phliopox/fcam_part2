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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        when(view.id){
            R.id.chap1_btn ->{
                //  findNavController().navigate(R.id.action_chapterSelect_to_webToonFragment)
                startActivity(Intent(context,SecondActivity::class.java))
            }
            R.id.chap2_btn->{
                findNavController().navigate(R.id.action_chapterSelect_to_recodeFragment)
            }
            R.id.testInsert ->{
                val call: Call<Note> =
                    RetrofitClient().getApiService().soldInfoPost("test_kiosk","test_product","100", "test_singleset", "test_exp","200")

                call.enqueue(object : Callback<Note?> {
                    override fun onResponse(call: Call<Note?>, response: Response<Note?>) {

                        Log.d(TAG, "ChapterSelect - onResponse: 성공");
                    }
                    override fun onFailure(call: Call<Note?>, t: Throwable) {
                        Log.d(TAG, "ChapterSelect - onFailure: 실패");
                    }
                })
            }
        }
    }
}