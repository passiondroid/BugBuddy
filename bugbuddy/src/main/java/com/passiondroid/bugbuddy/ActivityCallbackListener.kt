package com.passiondroid.bugbuddy

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * Created by Arif Khan on 27/02/20.
 */
interface ActivityCallbackListener: Application.ActivityLifecycleCallbacks {

    override fun onActivityPaused(p0: Activity) = Unit

    override fun onActivityStarted(p0: Activity) = Unit

    override fun onActivityDestroyed(p0: Activity) = Unit

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) = Unit

    override fun onActivityStopped(p0: Activity) = Unit

    override fun onActivityCreated(p0: Activity, p1: Bundle?) = Unit

    override fun onActivityResumed(activity: Activity) = Unit
}

//class ActivityCallbackListener: ActivityCallback {
//
//    var activity: Activity? = null
//
//    override fun onActivityResumed(activity: Activity) {
//        super.onActivityResumed(activity)
//        this.activity = activity
//    }
//}