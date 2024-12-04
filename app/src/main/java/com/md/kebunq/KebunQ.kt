package com.md.kebunq

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger

class KebunQ : Application() {
    override fun onCreate() {
        super.onCreate()
        FacebookSdk.sdkInitialize(this)
        AppEventsLogger.activateApp(this)
    }
}