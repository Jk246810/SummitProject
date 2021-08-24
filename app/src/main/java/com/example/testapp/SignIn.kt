package com.example.testapp

import android.os.Bundle
import android.provider.ContactsContract
import android.content.Intent
import android.nfc.Tag
import android.util.Log
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.Toast
import android.widget.EditText
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

class SignIn : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
// ...
// Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();

        // Enables Always-on
        setAmbientEnabled()
    }
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this, OptionsList::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signInClicked(view: View){
        val emailField = findViewById<EditText>(R.id.emailTextField)
        val email = emailField.text.toString()

        val passwordField = findViewById<EditText>(R.id.passwordTextField)
        val password = passwordField.text.toString()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                // If sign in fails, display a message to the user.
                Toast.makeText(this, email, Toast.LENGTH_LONG).show()
            }
        }

    }
    fun createAccountClicked(view: View){
        val emailField = findViewById<EditText>(R.id.emailTextField)
        val email = emailField.text.toString()

        val passwordField = findViewById<EditText>(R.id.passwordTextField)
        val password = passwordField.text.toString()

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Registration Failed", Toast.LENGTH_LONG).show()
                    }
                }

    }

}