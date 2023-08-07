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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ph.fastcam_part2.R
import com.ph.fastcam_part2.TAG
import com.ph.fastcam_part2.databinding.Chap4Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GitRepoFragment : Fragment() {
    private lateinit var binding: Chap4Binding
    private lateinit var userAdapter :UserAdapter
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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

/*

        val githubService = retrofit.create(GithubApi::class.java)
        githubService.listRepos("square").enqueue(object :Callback<List<Repo>>{
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                Log.d(TAG, "GitRepoFragment - onResponse ListRepo: ${response.body().toString()}");
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
            }

        })
*/

        userAdapter = UserAdapter{ findNavController().navigate(R.id.action_gitRepoFragment_to_resultFragment,
            bundleOf("username" to it.username))}

        binding.userRecyclerView.apply {
            adapter = userAdapter
        }

        var editTextFlow : Flow<CharSequence?>
        //https://medium.com/jaesung-dev/android-debounce%EC%99%80-throttle-%EC%9D%B4%ED%95%B4%ED%95%98%EA%B8%B0-e6da12d18d26
        //debouncing
        /*  binding.searchEditText.addTextChangedListener(object  :TextWatcher{
              override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =Unit
              override fun onTextChanged(searchTerm: CharSequence?, start: Int, before: Int, count: Int) {
                  val trim = searchTerm.toString().trim()
                  if(trim.isEmpty()) return

              /*CoroutineScope(Dispatchers.Main).launch {
                      delay(300)
                      Log.d(TAG, "GitRepoFragment - onTextChanged: $searchTerm");
                      searchUser(trim)
                  }*/
              }

              override fun afterTextChanged(s: Editable?) =Unit

          }){ searchTerm->
              val trim = searchTerm.toString().trim()

              searchTerm.
              editTextFlow = trim as Flow<CharSequence?>
              if(trim.isNotEmpty()){
                  runBlocking {
                      launch {
                          delay(300)
                          searchUser(trim)

                      }
                  }
              }

          }*/

        CoroutineScope(Dispatchers.IO).launch{
            val searchEditText = binding.searchEditText
            val editTextToFlow = editTextToFlow(searchEditText)
            editTextToFlow.debounce(500)
                .filter { it?.length!! > 0}
                .onEach {
                    searchUser(it.toString())
                }.launchIn(this)

        }
    }

    private fun searchUser(query: String) {
        val githubService = retrofit.create(GithubApi::class.java)

        githubService.searchUsers(query).enqueue(object : Callback<UserDto> {
            override fun onResponse(call: Call<UserDto>, response: Response<UserDto>) {
                userAdapter.submitList(response.body()?.items)
            }

            override fun onFailure(call: Call<UserDto>, t: Throwable) {
                Toast.makeText(requireContext(), "오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "GitRepoFragment - onFailure: ${t.message}");
            }
        })

    }
    private suspend fun editTextToFlow(editText: EditText) :Flow<CharSequence?>{
        return callbackFlow<CharSequence?> {
            val listener = object : TextWatcher{
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) =Unit

                override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                    trySend(text)
                }

                override fun afterTextChanged(s: Editable?)=Unit

            }
            editText.addTextChangedListener(listener)
            awaitClose{
                editText.removeTextChangedListener(listener)
            }

        }.onStart {
            Log.d(TAG, "textChangesToFlow() / onStart 발동")
            emit(editText.text)
        }


    }
}