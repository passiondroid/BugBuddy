package com.passiondroid.bugbuddy.sample

import android.app.Application
import com.facebook.stetho.Stetho
import com.passiondroid.bugbuddy.BugBuddy

/**
 * Created by Arif Khan on 26/02/20.
 */
class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()
        BugBuddy
            .application(this)
            .username("khan_arif@live.in")
            .apiToken("JgszCPsTg5GgdAPK2eBZC2DB")
            .jiraEndpoint("https://passiondroid.atlassian.net/")
            .reportBugOnShake(true)

        if(BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }
}