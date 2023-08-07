package com.ph.fastcam_part2.chap4

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.databinding.Chap4Binding
import com.ph.fastcam_part2.databinding.Chap4ResultBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultFragment : Fragment() {
    private lateinit var binding: Chap4ResultBinding
    private val repoAdapter = RepoAdapter()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = Chap4ResultBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userName = arguments?.getString("username") ?: return
        binding.usernameTextView.text = userName

        binding.repoRecyclerView.apply {
            adapter = repoAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        listRepo(userName)

    }

    private fun listRepo(userName: String) {
        val githubService = retrofit.create(GithubApi::class.java)

        githubService.listRepos(userName).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                repoAdapter.submitList(response.body())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Toast.makeText(requireContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "GitRepoFragment - onFailure: ${t.message}");            }
        })

    }
}