package com.example.darkpatternsbuster.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.darkpatternsbuster.R

class MainActivity : AppCompatActivity() {
    private lateinit var checkWeb: CheckBox
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkWeb = findViewById(R.id.WebCheck)
        checkWeb.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Automating Messages is riskier due to WhatsApp security policies. \nThis involves a process where every selected contact will be opened and the message will be sent one by one over a period of time. \nThis requires an additional permission known as Accessibility. \nDo you still want to enable this setting?")
            builder.setTitle("DISCLAIMER")
            builder.apply {
                setPositiveButton("YES") { dialog, id ->
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
                        }
                    }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()

                }
                setNegativeButton("NO") { dialogInterface: DialogInterface, i: Int ->
                    checkWeb.isChecked = false
                }
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
    private fun showToast(message:String)
    {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }
}