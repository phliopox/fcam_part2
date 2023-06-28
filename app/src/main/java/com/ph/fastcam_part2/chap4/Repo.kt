package com.ph.fastcam_part2.chap4

import com.google.gson.annotations.SerializedName

data class Repo(
    val id: Long,
    val name: String,
    val description: String,
    val language: String?,
    @SerializedName("stargazers_count")
    val starCont: Int,
    @SerializedName("forks_count")
    val forkCount: Int,
    @SerializedName("html_url")
    val htmlUrl : String


)
