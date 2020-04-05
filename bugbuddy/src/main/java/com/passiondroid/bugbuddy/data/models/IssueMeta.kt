package com.passiondroid.bugbuddy.data.models


import com.google.gson.annotations.SerializedName

data class IssueMeta(
    @SerializedName("expand")
    val expand: String?,
    @SerializedName("projects")
    val projects: List<Project>?
) {
    data class Project(
        @SerializedName("avatarUrls")
        val avatarUrls: AvatarUrls?,
        @SerializedName("id")
        val id: String?,
        @SerializedName("issuetypes")
        val issuetypes: List<Issuetype>?,
        @SerializedName("key")
        val key: String?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("self")
        val self: String?
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

        data class Issuetype(
            @SerializedName("description")
            val description: String?,
            @SerializedName("iconUrl")
            val iconUrl: String?,
            @SerializedName("id")
            val id: String?,
            @SerializedName("name")
            val name: String?,
            @SerializedName("scope")
            val scope: Scope?,
            @SerializedName("self")
            val self: String?,
            @SerializedName("subtask")
            val subtask: Boolean?
        ) {
            data class Scope(
                @SerializedName("project")
                val project: Project?,
                @SerializedName("type")
                val type: String?
            ) {
                data class Project(
                    @SerializedName("id")
                    val id: String?
                )
            }
        }
    }
}