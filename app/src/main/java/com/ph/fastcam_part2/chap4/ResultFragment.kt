package com.ph.fastcam_part2.chap4

import android.content.Intent
import android.net.Uri
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
import androidx.recyclerview.widget.RecyclerView
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.chap4.ApiClient.retrofit
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
    private lateinit var repoAdapter : RepoAdapter

    private var page = 0
    private var hasMore = true
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
        val linearLayoutManager = LinearLayoutManager(requireContext())

        repoAdapter = RepoAdapter{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.htmlUrl))
            startActivity(intent)
        }

        binding.repoRecyclerView.apply {
            adapter = repoAdapter
            layoutManager = linearLayoutManager
        }

        listRepo(userName, page)

        //페이징 처리
        binding.repoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                // 마지막 아이템에 관한 포지션 정보를 linearLayoutManager 가 가지고있다.
                val lastVisiblePosition =
                    linearLayoutManager.findLastCompletelyVisibleItemPosition()
                val totalCount = linearLayoutManager.itemCount
                // 완전 맨끝 말고 마지막에서 2번째 정도 전의 아이템으로 커스텀 .. (더 미리 불러오도록)
                if (lastVisiblePosition -2 >= totalCount - 3 && hasMore) {
                    page += 1
                    listRepo(userName, page)
                }
            }

        })
    }

    private fun listRepo(userName: String, page: Int) {
        val githubService = ApiClient.retrofit.create(GithubApi::class.java)

        githubService.listRepos(userName, page).enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                hasMore = response.body()?.count() ==30
                repoAdapter.submitList(repoAdapter.currentList + response.body().orEmpty())
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                Toast.makeText(requireContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "GitRepoFragment - onFailure: ${t.message}"); }
        })

    }
}