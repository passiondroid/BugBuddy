package com.passiondroid.bugbuddy.data

import com.passiondroid.bugbuddy.data.models.AttachmentInfo
import com.passiondroid.bugbuddy.data.models.IssueMeta
import com.passiondroid.bugbuddy.data.models.IssueRequest
import com.passiondroid.bugbuddy.data.models.IssueResponse
import okhttp3.MultipartBody
import retrofit2.http.*


/**
 * Created by Arif Khan on 28/02/20.
 */
internal interface JiraApi {

    @GET("rest/api/latest/issue/createmeta")
    suspend fun getIssueMetaData(): IssueMeta

    @POST("rest/api/latest/issue")
    suspend fun createIssue(@Body issueRequest: IssueRequest): IssueResponse

    @Headers("X-Atlassian-Token:nocheck")
    @Multipart
    @POST("rest/api/latest/issue/{issueId}/attachments")
    suspend fun uploadAttachment(@Path("issueId") issueId: String, @Part file: MultipartBody.Part): List<AttachmentInfo>
}