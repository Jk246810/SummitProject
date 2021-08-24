package com.example.testapp


import android.content.Intent
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View


class OptionsList : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_list)
    }

    fun medicationSegue(view: View){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun recordingSegue(view: View){
        val intent = Intent(this, StopRecording::class.java)
        startActivity(intent)
        finish()
    }
}