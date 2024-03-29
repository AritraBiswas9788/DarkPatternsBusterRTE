package com.example.darkpatternsbuster.activities

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.darkpatternsbuster.R
import com.koushikdutta.ion.Ion
import io.github.cdimascio.essence.Essence
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors


class CheckerActivity : AppCompatActivity() {
    private lateinit var button: Button
    private lateinit var textView: TextView
    private lateinit var editText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checker)

        button = findViewById(R.id.button)
        textView = findViewById(R.id.Text)
        editText = findViewById(R.id.editText)

        button.setOnClickListener {
            var url = editText.text.toString()
            url = "https://$url"
            ContentScrapper.getHTMLData(url,object : ContentScrapper.ScrapListener{
                override fun onResponse(html: String?) {
                    if(html != null) {
                        //Toast.makeText(applicationContext, "done.", Toast.LENGTH_SHORT).show()
                        Log.i("debuggerCheck","done.")
                        Log.i("debuggerCheck",html)
                        //textView.text=html

                        val data = Essence.extract(html)
                        Log.i("debuggerCheck", data.toString())
                        //textView.text=data.toString()


                    } else {
                        //Toast.makeText(applicationContext,"Not found",Toast.LENGTH_LONG).show()
                        Log.i("debuggerCheck","not found.")
                    }
                }
            })
            /*Ion.with(this).load(url).asString()
                .setCallback { exception: java.lang.Exception?, result: String ->
                    Toast.makeText(this, "done.", Toast.LENGTH_SHORT).show()
                    Log.i("debuggerCheck",result)
                    textView.text = result
                    val data = Essence.extract(result)
                    Log.i("debuggerCheck", data.toString())
                    textView.text=data.text
                    //doTaskInBackGround(url)
                    //textView.text=html
                }*/

        }
    }


        fun doTaskInBackGround(url: String) {
            val executor = Executors.newSingleThreadExecutor()
            val handler = Handler(Looper.getMainLooper())
            executor.execute {
                var str = ""
                try {
                    str = getHtml(url)!!
                } catch (e: Exception) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
                }
                handler.post {
                    setAnswer(str)
                    Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun setAnswer(str: String) {
            textView.text = str
        }


        @Throws(IOException::class)
        fun getHtml(url: String?): String? {
            // Build and set timeout values for the request.
            val connection = URL(url).openConnection()
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.connect()

            // Read and store the result line by line then return the entire string.
            val `in` = connection.getInputStream()
            val reader = BufferedReader(InputStreamReader(`in`))
            val html = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                html.append(line)
            }
            `in`.close()
            return html.toString()
        }

    object ContentScrapper {
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
