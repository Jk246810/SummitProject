package com.example.testapp

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.red
import android.support.wearable.activity.WearableActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var auth: FirebaseAuth
private lateinit var db: FirebaseFirestore

class StopRecording : WearableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stop_recording)

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val recordingVariable = db.collection("Users_Collection")
                .document(auth.currentUser.uid)
                .collection("Program_Collection")
                .document("Listeners_Document")
        recordingVariable.addSnapshotListener { snapshot, e ->
            if(snapshot!=null && snapshot.exists() && snapshot.get("ProgramStopField") == null){
                recordingVariable.update("ProgramStopField", false)
            }
        }


    }

    fun backButtonClicked(view: View){
        val intent = Intent(this, OptionsList::class.java)
        startActivity(intent)
        finish()
    }
    fun stopClicked(view: View){
        val stopButton = findViewById(R.id.StopButton) as Button
        val buttonText = stopButton.getText() as String
        if(buttonText == "Stop") {
            Toast.makeText(this, buttonText, Toast.LENGTH_LONG).show()
            db.collection("Users_Collection")
                .document(auth.currentUser.uid).collection("Program_Collection")
                .document("Listeners_Document")
                .update("ProgramStopField", true)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Successfully registered", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Data not registered",
                        Toast.LENGTH_LONG
                    ).show()
                }
            stopButton.setText("Start")
            stopButton.setBackgroundColor(Color.GREEN)
        }else if(buttonText == "Start"){
            db.collection("Users_Collection")
                .document(auth.currentUser.uid).collection("Program_Collection")
                .document("Listeners_Document")
                .update("ProgramStopField", false)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Successfully registered", Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Data not registered",
                        Toast.LENGTH_LONG
                    ).show()
                }
            stopButton.setText("Stop")
            stopButton.setBackgroundColor(Color.RED)
        }
    }
}