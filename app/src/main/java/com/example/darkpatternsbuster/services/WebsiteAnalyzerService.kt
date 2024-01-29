package com.example.darkpatternsbuster.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import java.util.LinkedList
import java.util.Queue


class WebsiteAnalyzerService : AccessibilityService() {


    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (rootInActiveWindow == null)
            return
        val rootNodeInfo: AccessibilityNodeInfoCompat = AccessibilityNodeInfoCompat.wrap(rootInActiveWindow)
        //Log.i("ServiceChecker", "Accessibility Event Triggered")
        if (event != null) {
            if (AccessibilityEvent.eventTypeToString(event.eventType).contains("WINDOW")) {
                val nodeInfo = event.source?.getChild(0)?.getChild(0)
                Log.i("ServiceChecker", nodeInfo?.className.toString())
                //nodeDfs(nodeInfo)
                getUrlsFromViews(nodeInfo)
                if(nodeInfo!=null && nodeInfo.text!=null) {
                    val x = nodeInfo.text.toString()
                    Log.d("ServiceChecker", x)
                }
                else
                    if(nodeInfo?.text==null)
                         Log.i("ServiceChecker", "null text Found")
                    else
                        Log.i("ServiceChecker", "null root Found")
            }
        }
    }

    fun nodeDfs(info: AccessibilityNodeInfo?) {
        if (info == null) return
        if (info.text != null && info.text.length > 0) {

            val x = (info.text.toString() + " class: " + info.className)
            Log.i("ServiceChecker", x)
        }
        for (i in 0 until info.childCount) {
            val child = info.getChild(i)
            nodeDfs(child)
            child?.recycle()
        }
    }

    fun getUrlsFromViews(info: AccessibilityNodeInfo?) {
        try {
            if (info == null) return
            if (info.text != null && info.text.isNotEmpty()) {
                val capturedText = info.text.toString()
                Log.d("ServiceChecker2", capturedText)
                if (capturedText.contains("https://") || capturedText.contains("http://") || capturedText.contains(
                        ".com"
                    ) || capturedText.contains(".ac.in")
                ) {


                    val x = (info.text.toString() + " class: " + info.className)
                    Log.d("ServiceChecker1", x)

                }
            }
            for (i in 0 until info.childCount) {
                val child = info.getChild(i)
                getUrlsFromViews(child)
                child?.recycle()
            }
        }
//            val queue: Queue<AccessibilityNodeInfo?> = LinkedList<AccessibilityNodeInfo?>()
//            queue.add(info)
//            Log.d("ServiceChecker2", info!!.text.toString())
//            while (queue.isNotEmpty()) {
//                val temp = queue.peek()
//                queue.remove()
//                Log.d("ServiceChecker2", temp!!.text.toString())
////                if (temp == null) return
//                if (temp?.text != null && temp.text.isNotEmpty()) {
//                    val capturedText = temp.text.toString()
//                    Log.d("ServiceChecker2", capturedText)
//                    if (capturedText.contains("https://") || capturedText.contains("http://") || capturedText.contains(
//                            ".com"
//                        ) || capturedText.contains(".ac.in")
//                    ) {
//
//
//                        val x = (temp.text.toString() + " class: " + temp.className)
//                        Log.d("ServiceChecker1", x)
////                        break
//
//                    }
//                }
//                if (temp == null) continue
//                for (i in 0 until temp!!.childCount) {
//                    val child = temp!!.getChild(i)
//                    queue.add(child)
////                    child?.recycle()
//                }
//                temp.recycle()
//
//            }
//        }
        catch (ex: StackOverflowError) {
            ex.printStackTrace()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
    override fun onInterrupt() {

    }

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.i("ServiceChecker", "Service-Connected")
    }
}