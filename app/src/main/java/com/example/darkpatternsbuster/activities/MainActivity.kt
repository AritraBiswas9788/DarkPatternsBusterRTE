package com.example.darkpatternsbuster.activities

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.darkpatternsbuster.R
import java.io.InputStream
import java.net.HttpURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var checkWeb: CheckBox
    private  val channelid="Message Channel"
    private val Notification_id=100
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //notification()
        checkWeb = findViewById(R.id.WebCheck)
        checkWeb.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Automating Messages is riskier due to WhatsApp security policies. \nThis involves a process where every selected contact will be opened and the message will be sent one by one over a period of time. \nThis requires an additional permission known as Accessibility. \nDo you still want to enable this setting?")
            builder.setTitle("DISCLAIMER")
            builder.apply {
                setPositiveButton("YES") { _, _ ->
                    showToast("Redirects to accessibility")
                    //createAlertDialog("Navigate To the Installed apps section and enable My WhatsApp Accessibility for WhatsApp Automation to work.","STEPS for Accessibility")
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder.setMessage("Navigate To the Installed apps section and enable My WhatsApp Accessibility for WhatsApp Automation to work.")
                    builder.setTitle("STEPS for Accessibility")
                    builder.apply {
                        setPositiveButton("OK") { dialog, id ->
                            val intentAccess = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                            intentAccess.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intentAccess)

                            /*val intent = Intent(context, CheckerActivity::class.java)
                            context.startActivity(intent)*/
                        }
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                }
                setNegativeButton("NO") { _: DialogInterface, _: Int ->
                    checkWeb.isChecked = false
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun notification()
    {
        val Drawable =ResourcesCompat.getDrawable(resources,R.drawable.gc,null)
        val bitmapdrawable= Drawable as BitmapDrawable
        val largeicon= bitmapdrawable.bitmap

        val nm=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification= Notification.Builder(this).setLargeIcon(largeicon).setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentText("New Dark Pattern Link Found").setSubText("Dark Pattern").setChannelId(channelid).build()
        nm.createNotificationChannel(NotificationChannel(channelid,"New Channel",NotificationManager.IMPORTANCE_HIGH))
        nm.notify(Notification_id,notification)

    }

    private fun showToast(message:String)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}