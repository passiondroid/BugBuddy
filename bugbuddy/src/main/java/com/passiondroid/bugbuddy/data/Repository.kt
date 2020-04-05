package com.passiondroid.bugbuddy.data

import com.passiondroid.bugbuddy.BugBuddy
import com.passiondroid.bugbuddy.data.models.AttachmentInfo
import com.passiondroid.bugbuddy.data.models.IssueMeta
import com.passiondroid.bugbuddy.data.models.IssueRequest
import com.passiondroid.bugbuddy.data.models.IssueResponse
import com.passiondroid.bugbuddy.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import java.io.File

/**
 * Created by Arif Khan on 29/02/20.
 */
class Repository: DataSource {

    private val remoteDataSource: RemoteDataSource by lazy {
        RemoteDataSource(BugBuddy.getJiraEndpoint())
    }

    override suspend fun createIssue(issueRequest: IssueRequest): IssueResponse {
        return remoteDataSource.createIssue(issueRequest)
    }

    override suspend fun getIssueMetaData(): IssueMeta {
        return remoteDataSource.getIssueMetaData()
    }

    override suspend fun uploadAttachment(issueId: String, file: File): List<AttachmentInfo> {
        return remoteDataSource.uploadAttachment(issueId, file)
    }
}

