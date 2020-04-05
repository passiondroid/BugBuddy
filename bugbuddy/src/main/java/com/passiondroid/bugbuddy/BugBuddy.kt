package com.passiondroid.bugbuddy

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.SensorManager
import android.text.format.DateFormat
import android.view.View
import com.passiondroid.bugbuddy.ui.BugReportActivity
import com.squareup.seismic.ShakeDetector
import java.io.File
import java.io.FileOutputStream
import java.util.*


/**
 * Created by Arif Khan on 26/02/20.
 */
object BugBuddy {

    val IMAGE_PATH = "IMAGE_PATH"
    private lateinit var application: Application
    private lateinit var jiraEndpoint: String
    private lateinit var username: String
    private var password: String? = null
    private var apiToken: String? = null
    private var activity: Activity? = null

    fun application(application: Application): BugBuddy {
        this.application = application
        registerActivityLifecycleListener(application)
        return this
    }

    fun jiraEndpoint(jiraEndpoint: String): BugBuddy {
        this.jiraEndpoint = jiraEndpoint
        return this
    }

    fun username(username: String): BugBuddy {
        this.username = username
        return this
    }

    fun password(password: String): BugBuddy {
        this.password = password
        return this
    }

    fun apiToken(apiToken: String): BugBuddy {
        this.apiToken = apiToken
        return this
    }

    fun getJiraEndpoint(): String {
        return jiraEndpoint
    }

    fun getUsername(): String {
        return username
    }

    fun getPassword(): String? {
        return password
    }

    fun getApiToken(): String? {
        return apiToken
    }

    fun reportBugOnShake(value: Boolean): BugBuddy {
        if(value) {
            val sensorManager =
                application.getSystemService(Context.SENSOR_SERVICE) as SensorManager
            ShakeListenerService.onShake = {
                val path = takeScreenshot(application)
                val intent = Intent(application, BugReportActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra(IMAGE_PATH, path)
                application.startActivity(intent)
            }
            val sd = ShakeDetector(ShakeListenerService)
            sd.start(sensorManager)
        }
        return this
    }

    fun reportBug() {
        val path = takeScreenshot(application)
        val intent = Intent(application, BugReportActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(IMAGE_PATH,path)
        application.startActivity(intent)
    }

    private fun takeScreenshot(application: Application): String {
        var path = ""
        val now = Date()
        DateFormat.format("hh:mm:ss.SSS", now)
        try { // image naming and path  to include sd card  appending name you choose for file
            val mPath: String = application.filesDir.toString() + "/" + now + ".jpg"
            // create bitmap screen capture
            val v1: View? = activity?.window?.decorView?.rootView
            v1?.let {
                v1.isDrawingCacheEnabled = true
                val bitmap: Bitmap = Bitmap.createBitmap(v1.drawingCache)
                v1.isDrawingCacheEnabled = false
                val imageFile = File(mPath)
                val outputStream = FileOutputStream(imageFile)
                val quality = 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                outputStream.flush()
                outputStream.close()
                path = mPath
            }
        } catch (e: Throwable) { // Several error may come out with file handling or DOM
            e.printStackTrace()
        }
        return path
    }

    private fun registerActivityLifecycleListener(application: Application) {
        application.registerActivityLifecycleCallbacks(object: ActivityCallbackListener {
            override fun onActivityResumed(currentActivity: Activity) {
                activity = currentActivity
            }
        })
    }

}