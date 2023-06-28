package com.ph.fastcam_part2.chap3

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.databinding.Chap4Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitRepoFragment : Fragment() {
    private lateinit var binding : Chap4Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Chap4Binding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val githubService = retrofit.create(GithubApi::class.java)
        githubService.listRepos("square").enqueue(object :Callback<List<Repo>>{
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d(TAG, "GitRepoFragment - onResponse ListRepo: ${response.body().toString()}");
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
            }

        })

        githubService.searchUsers("squar").enqueue(object : Callback<UserDto>{
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                Log.d(TAG, "GitRepoFragment - onResponse SearchUser: ${response.body().toString()}");
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Log.d(TAG, "GitRepoFragment - onFailure: ${t.message}");
            }
        })
    }
}