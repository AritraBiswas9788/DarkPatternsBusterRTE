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
    private var visitedUrlList:ArrayList<String> = arrayListOf()
    private var urlWebsite:String = ""

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
                nodeFindDfs(nodeInfo)

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

    fun nodeFindDfs(info: AccessibilityNodeInfo?) {
        if (info == null) return
        if (info.text != null && info.text.length > 0) {

            if (info.viewIdResourceName == "com.android.chrome:id/url_bar") {
                val x = info.text.toString()
                Log.i("" +
                        "", x)
                if(!visitedUrlList.contains(x)) {
                    visitedUrlList.add(x)
                    urlWebsite = info.viewIdResourceName
                    //getHtmlView("https://$urlWebsite")
                }
            }
        }
        for (i in 0 until info.childCount) {
            val child = info.getChild(i)
            nodeDfs(child)
            child?.recycle()
        }

    }

    /*private fun getHtmlView(urlWebsite: String?) {
        Ion.with(applicationContext).load(urlWebsite).asString()
            .setCallback { exception: java.lang.Exception?, result: String? ->
                if(result!=null) {
                    Toast.makeText(this, "done.", Toast.LENGTH_SHORT).show()
                    Log.i("debuggerCheck", result.substring(0, 150))
                    //textView.text = result
                    val data = Essence.extract(result)
                    Log.i("debuggerCheck", data.toString().substring(0, 150))
                    //textView.text = data.text
                }
                else
                {
                    Log.i("debuggerCheck", "Null found")
                }
                }

        }*/

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