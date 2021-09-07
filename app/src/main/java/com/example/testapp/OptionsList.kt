package com.example.testapp


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.sql.Timestamp
import java.text.SimpleDateFormat


const val PERMISSION_REQUEST_HEART_RATE = 0


class OptionsList : WearableActivity(), SensorEventListener, ActivityCompat.OnRequestPermissionsResultCallback{


    private lateinit var sensorManager: SensorManager
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val sdf: SimpleDateFormat = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options_list)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        getHeartRatePermissions()

    }


    fun getHeartRatePermissions(){
        if(checkSelfPermission(Manifest.permission.BODY_SENSORS) == PackageManager.PERMISSION_GRANTED){
            getHeartRate()
        }else{
            requestHeartRatePermissions()
        }
    }

    fun requestHeartRatePermissions(){
            requestPermissions(arrayOf(Manifest.permission.BODY_SENSORS), PERMISSION_REQUEST_HEART_RATE)
    }
    fun getHeartRate(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val mHeartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)

        sensorManager.registerListener(this, mHeartRateSensor, 900000000);
    }
    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        System.out.println("onAccuracyChanged - accuracy: " + accuracy);
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_HEART_RATE) {
            logHeartRate(event.values[0])
        }
    }
    fun logHeartRate(heartRate: Float){
        val timestamp = Timestamp(System.currentTimeMillis())
        val recordedRate = hashMapOf(
            "Heart Rate" to heartRate,
            "Date" to sdf.format(timestamp)
        )
        db.collection("Users_Collection")
            .document(auth.currentUser.uid)
            .collection("Heart_Rate_Collection")
            .add(recordedRate)
            .addOnSuccessListener {
                //Toast.makeText(this, "Data registered", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { Toast.makeText(this, "Data not registered", Toast.LENGTH_LONG).show() }

    }


    override fun onResume() {
        super.onResume()
        sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)?.also { proximity ->
            sensorManager.registerListener(this, proximity, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

   override fun onPause() {
        super.onPause()

        // Don't receive any more updates from either sensor.
        sensorManager.unregisterListener(this)
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