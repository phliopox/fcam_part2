package com.ph.fastcam_part2.chap3

import com.google.gson.annotations.SerializedName

data class UserDto(

    @SerializedName("total_count")
    val totalCount : Int,
    @SerializedName("items")
    val items:List<User>
)

data class User(
    val id : Int,
    @SerializedName("login")
    val username :String
)
