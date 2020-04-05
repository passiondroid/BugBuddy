package com.passiondroid.bugbuddy.data.models


import com.google.gson.annotations.SerializedName

data class IssueRequest(
    @SerializedName("fields")
    val fields: Fields?
) {
    data class Fields(
        @SerializedName("description")
        val description: String?,
        @SerializedName("issuetype")
        val issuetype: Issuetype?,
        @SerializedName("project")
        val project: Project?,
        @SerializedName("summary")
        val summary: String?
    ) {
        data class Issuetype(
            @SerializedName("id")
            val id: String?
        )

        data class Project(
            @SerializedName("id")
            val id: String?
        )
    }
}