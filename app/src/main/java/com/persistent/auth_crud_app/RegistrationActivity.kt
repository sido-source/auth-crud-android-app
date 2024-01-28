package com.persistent.auth_crud_app

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.persistent.auth_crud_app.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = Firebase.auth
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root) // Use binding.root to set the content view

        // when clicked "Log in ", user is forwarded to LoginActivity
        binding.registrationBtnLogIn.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        // when clicked "Register" , there is a validation and if everything is correct, user is being added to firebase db
        binding.registrationBtnRegister.setOnClickListener{

            val username = binding.registrationEtUsername.text.toString()
            val password = binding.registrationEtPassword.text.toString()
            val repeatPassword = binding.registrationEtRepeatPassword.text.toString()

            val areValuesOK = validation(username, password, repeatPassword)

            if (areValuesOK) {
                firebaseAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
                    if ( it.isSuccessful) {
                        Toast.makeText(this, "User Registered successfully", Toast.LENGTH_LONG).show()
                        val i = Intent(this, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(this, "User can't be register", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validation(username: String, password: String, repeatPassword: String): Boolean {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeatPassword)) {
            Toast.makeText(this, "All the fields are required and can't be empty", Toast.LENGTH_LONG).show()
            return false
        }

        if (password.length < 6 ) {
            Toast.makeText(this, "Password needs to be longer than 6 chars", Toast.LENGTH_LONG).show()
            return false
        }

        if (!TextUtils.equals(password, repeatPassword)) {
            Toast.makeText(this, "Password and repeatPassword are not the same", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }
}
