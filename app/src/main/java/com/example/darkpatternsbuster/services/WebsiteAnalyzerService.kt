package com.example.darkpatternsbuster.services

import android.accessibilityservice.AccessibilityService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.accessibility.AccessibilityEvent
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

class WebsiteAnalyzerService : AccessibilityService() {


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if(rootInActiveWindow == null)
            return
        val rootNodeInfo: AccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(rootInActiveWindow)

    }

    override fun onInterrupt() {

    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
    }
}