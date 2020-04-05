package com.passiondroid.bugbuddy.data.models


import com.google.gson.annotations.SerializedName

data class IssueResponse(
    @SerializedName("id")
    val id: String?,
    @SerializedName("key")
    val key: String,
    @SerializedName("self")
    val self: String?
)