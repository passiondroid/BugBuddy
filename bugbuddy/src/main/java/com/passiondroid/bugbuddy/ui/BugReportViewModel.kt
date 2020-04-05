package com.passiondroid.bugbuddy.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.passiondroid.bugbuddy.data.Repository
import com.passiondroid.bugbuddy.data.models.IssueMeta
import com.passiondroid.bugbuddy.data.models.IssueRequest
import com.passiondroid.bugbuddy.data.models.IssueResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.lang.Exception

/**
 * Created by Arif Khan on 29/02/20.
 */
class BugReportViewModel: ViewModel() {

    private val repository: Repository by lazy { Repository() }
    val isssueMetaLiveData = MutableLiveData<IssueMeta>()
    var issueResponse: IssueResponse? = null
    val issueStateLiveData = MutableLiveData<IssueState>()

    enum class IssueState {
        CreatingIssue,
        UploadingAttachment,
        IssueCreationFailed,
        UploadingAttachmentFailed,
        IssueCreated
    }

    fun getIssueMetaData() {
        viewModelScope.launch {
            val issueMetadata = repository.getIssueMetaData()
            withContext(Dispatchers.Main) {
                isssueMetaLiveData.value = issueMetadata
            }
        }
    }

    fun createIssue(issueRequest: IssueRequest, imagePath: String) {
        viewModelScope.launch {
            try {
                sendState(IssueState.CreatingIssue)
                val deferred1 = viewModelScope.async(Dispatchers.IO) {
                    repository.createIssue(issueRequest)
                }
                issueResponse = deferred1.await()
            }catch (ex: Exception) {
                sendState(IssueState.IssueCreationFailed)
                ex.printStackTrace()
            }
            try {
                sendState(IssueState.UploadingAttachment)
                issueResponse?.let {
                    viewModelScope.async(Dispatchers.IO) {
                        repository.uploadAttachment(it.key, File(imagePath))
                    }.await()
                }
            }catch (ex: Exception) {
                ex.printStackTrace()
                sendState(IssueState.UploadingAttachmentFailed)
            }
            issueResponse?.let {
                sendState(IssueState.IssueCreated)
            }
        }
    }

    private suspend fun sendState(state: IssueState){
        withContext(Dispatchers.Main){
            issueStateLiveData.value = state
        }
    }
}