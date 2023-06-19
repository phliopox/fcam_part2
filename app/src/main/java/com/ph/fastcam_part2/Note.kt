package com.ph.fastcam_part2

import com.google.gson.annotations.SerializedName

data class Note (
    @SerializedName("success")
    private var success: Boolean?,
    @SerializedName("message")
    private var message: String?
)