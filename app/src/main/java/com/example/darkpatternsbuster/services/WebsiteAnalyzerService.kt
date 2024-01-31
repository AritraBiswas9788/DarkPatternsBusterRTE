package com.example.darkpatternsbuster.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat
import com.example.darkpatternsbuster.activities.CheckerActivity
import io.github.cdimascio.essence.Essence
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.LinkedList
import java.util.Queue


class WebsiteAnalyzerService : AccessibilityService() {
    private var visitedUrlList: ArrayList<String> = arrayListOf()
    private var urlWebsite: String = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (rootInActiveWindow == null)
            return
        val rootNodeInfo: AccessibilityNodeInfoCompat =
            AccessibilityNodeInfoCompat.wrap(rootInActiveWindow)
        //Log.i("ServiceChecker", "Accessibility Event Triggered")
        if (event != null) {
            if (AccessibilityEvent.eventTypeToString(event.eventType).contains("WINDOW")) {
                val nodeInfo = event.source
                //Log.i("ServiceChecker", nodeInfo?.className.toString())
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
                var x = info.text.toString()
                if(!x.contains("https://")&&!x.contains("http://"))
                    x="https://$x"
                Log.i("ServiceChecker", x)

                if (!visitedUrlList.contains(x)) {
                    visitedUrlList.add(x)
                    urlWebsite = x
                    getHtmlView(x)
                }
            }
        }
        for (i in 0 until info.childCount) {
            val child = info.getChild(i)
            nodeFindDfs(child)
            child?.recycle()
        }

    }

    private fun getHtmlView(url: String) {
        HTMLFileReader.getHTMLData(url,object : HTMLFileReader.ScrapListener{
            override fun onResponse(html: String?) {
                if(html != null) {
                    //Toast.makeText(applicationContext, "done.", Toast.LENGTH_SHORT).show()
                    Log.i("ServiceChecker","done.")
                    Log.i("ServiceChecker",html)
                    //textView.text=html
                    val data = Essence.extract(html)
                    Log.i("ServiceChecker", data.toString())
                    //textView.text=data.toString()


                } else {
                    //Toast.makeText(applicationContext,"Not found",Toast.LENGTH_LONG).show()
                    Log.e("ServiceChecker","not found.")
                }
            }
        })
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

    object HTMLFileReader {
        fun getHTMLData(url: String, scrapListener: ScrapListener) {
            Thread {

                val google: URL?
                val `in`: BufferedReader?
                var input: String?
                val stringBuffer = StringBuffer()

                try {
                    google = URL(url)
                    `in` = BufferedReader(InputStreamReader(google.openStream()))
                    while (true) {
                        if (`in`.readLine().also { input = it } == null)
                            break
                        stringBuffer.append(input)
                    }
                    `in`.close()
                    scrapListener.onResponse(stringBuffer.toString())
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                    scrapListener.onResponse(null)
                }
            }.start()

        }


        interface ScrapListener {
            fun onResponse(html: String?)
        }
    }
}