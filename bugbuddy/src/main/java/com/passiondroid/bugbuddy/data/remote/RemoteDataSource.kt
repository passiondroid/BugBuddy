package com.passiondroid.bugbuddy.data.remote

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import com.passiondroid.bugbuddy.BugBuddy
import com.passiondroid.bugbuddy.data.DataSource
import com.passiondroid.bugbuddy.data.JiraApi
import com.passiondroid.bugbuddy.data.models.AttachmentInfo
import com.passiondroid.bugbuddy.data.models.IssueMeta
import com.passiondroid.bugbuddy.data.models.IssueRequest
import com.passiondroid.bugbuddy.data.models.IssueResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException


/**
 * Created by Arif Khan on 28/02/20.
 */
internal class RemoteDataSource(private val jiraEndpoint: String): DataSource {

    private val webservice by lazy {
        Retrofit.Builder()
            .baseUrl(jiraEndpoint)
            .client(OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor())
                .addNetworkInterceptor(StethoInterceptor())
                .build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(JiraApi::class.java)
    }

    override suspend fun createIssue(issueRequest: IssueRequest): IssueResponse {
        return webservice.createIssue(issueRequest)
    }

    override suspend fun getIssueMetaData(): IssueMeta {
       return webservice.getIssueMetaData()
    }

    override suspend fun uploadAttachment(issueId: String, file: File): List<AttachmentInfo> {
        val requestFile = RequestBody.create(MediaType.parse("image/jpg"),file)
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        return webservice.uploadAttachment(issueId, body)
    }

    class BasicAuthInterceptor :
        Interceptor {
        private lateinit var credentials: String
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            credentials = if(!BugBuddy.getPassword().isNullOrEmpty()) {
                Credentials.basic(BugBuddy.getUsername(), BugBuddy.getPassword()!!)
            }else if(!BugBuddy.getApiToken().isNullOrEmpty()){
                Credentials.basic(BugBuddy.getUsername(), BugBuddy.getApiToken()!!)
            }else {
                throw IllegalArgumentException("Illegal Arguments. Provide atleast Password or ApiToken")
            }
            val request: Request = chain.request()
            val authenticatedRequest: Request = request.newBuilder()
                .header("Authorization", credentials).build()
            return chain.proceed(authenticatedRequest)
        }

    }
}