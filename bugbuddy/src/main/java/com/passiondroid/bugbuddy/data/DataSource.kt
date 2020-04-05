package com.passiondroid.bugbuddy.data

import com.passiondroid.bugbuddy.data.models.AttachmentInfo
import com.passiondroid.bugbuddy.data.models.IssueMeta
import com.passiondroid.bugbuddy.data.models.IssueRequest
import com.passiondroid.bugbuddy.data.models.IssueResponse
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Created by Arif Khan on 28/02/20.
 */
internal interface DataSource {

    suspend fun createIssue(issueRequest: IssueRequest): IssueResponse

    suspend fun getIssueMetaData(): IssueMeta

    suspend fun uploadAttachment(issueId: String, file: File): List<AttachmentInfo>
}