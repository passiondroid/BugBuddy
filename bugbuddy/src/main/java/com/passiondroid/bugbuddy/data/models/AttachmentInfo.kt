package com.passiondroid.bugbuddy.data.models


import com.google.gson.annotations.SerializedName

data class AttachmentInfo(
    @SerializedName("author")
    val author: Author?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("created")
    val created: String?,
    @SerializedName("filename")
    val filename: String?,
    @SerializedName("mimeType")
    val mimeType: String?,
    @SerializedName("self")
    val self: String?,
    @SerializedName("size")
    val size: Int?,
    @SerializedName("thumbnail")
    val thumbnail: String?
) {
    data class Author(
        @SerializedName("active")
        val active: Boolean?,
        @SerializedName("avatarUrls")
        val avatarUrls: AvatarUrls?,
        @SerializedName("displayName")
        val displayName: String?,
        @SerializedName("emailAddress")
        val emailAddress: String?,
        @SerializedName("key")
        val key: String?,
        @SerializedName("locale")
        val locale: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("self")
        val self: String?,
        @SerializedName("timeZone")
        val timeZone: String?
    ) {
        data class AvatarUrls(
            @SerializedName("16x16")
            val x16: String?,
            @SerializedName("24x24")
            val x24: String?,
            @SerializedName("32x32")
            val x32: String?,
            @SerializedName("48x48")
            val x48: String?
        )
    }
}