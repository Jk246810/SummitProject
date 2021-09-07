package com.example.testapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.example.testapp.databinding.ActivityNotificationScreenBinding

class NotificationScreen : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_screen)
        var notificationField = findViewById<TextView>(R.id.notification)
        var msg = "Notifications: make sure the CTM is connected and the INS is charged before continuing!"
        notificationField.setText(msg)
    }

    fun backButtonClicked(view: View){
        val intent = Intent(this, OptionsList::class.java)
        startActivity(intent)
        finish()
    }
}