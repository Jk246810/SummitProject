package com.example.testapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore

class MainActivity : WearableActivity() {
    private val CHANNEL_ID = "channel_id_example1"
    private val notificationID = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        createNotificationChannel()

        checkBattery()

        setAmbientEnabled()
    }
    private fun checkBattery(){
        val docRef = db.collection("users")
            .document(auth.currentUser.uid)
        docRef.addSnapshotListener{ snapshot, e ->
            if (e != null) {
                Toast.makeText(this, "listen failed", Toast.LENGTH_LONG).show()
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {

                val device = snapshot.data?.get("CtmReady")
                Log.d("TAG", "Current data: ${device}")
                if(device == false){
                    //This is where the notification will be triggered
                    sendNotification()
                }
            } else {
                Toast.makeText(this, "data: null", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun createNotificationChannel(){
         val name = "Notification Title"
        val descriptionText = "Notification description"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    private fun sendNotification(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("CTM Low Battery")
            .setContentText("The CTM needs to be charged")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(this)){
            notify(notificationID, builder.build())
        }
    }
    fun sendClicked(view: View){
        val medicationField = findViewById<EditText>(R.id.medication)
        val med = medicationField.text.toString()

        val dosageField = findViewById<EditText>(R.id.dosage)
        val dosage = dosageField.text.toString()

      
        val medication = hashMapOf(
                "medication" to med,
                "dosage" to dosage.toInt()
        )

            db.collection("users")
                    .document(auth.currentUser.uid)
                    .collection("medications")
                    .add(medication)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data Successfully registered", Toast.LENGTH_LONG).show()
                        medicationField.text.clear()
                        dosageField.text.clear()

                    }
                    .addOnFailureListener { Toast.makeText(this, "Data not registered", Toast.LENGTH_LONG).show() }

    }

}