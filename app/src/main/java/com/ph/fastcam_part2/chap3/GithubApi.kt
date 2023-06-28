package com.ph.fastcam_part2.chap3

import com.ph.fastcam_part2.GITHUB_TOKEN
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @Headers("Authorization: Bearer $GITHUB_TOKEN")
    @GET("users/{username}/repos")
    fun listRepos (@Path("username") username : String) : Call<List<Repo>>

    @Headers("Authorization: Bearer $GITHUB_TOKEN")
    @GET("search/users")
    fun searchUsers(@Query("q") query : String ):Call<UserDto>
}
