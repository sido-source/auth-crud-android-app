package com.persistent.auth_crud_app

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.persistent.auth_crud_app.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var bindingLoginBinding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        bindingLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(bindingLoginBinding.root)

        bindingLoginBinding.loginBtnRegister.setOnClickListener {
            val i = Intent(this, RegistrationActivity::class.java)
            startActivity(i)
        }


        bindingLoginBinding.loginBtnLogin.setOnClickListener{
            val username = bindingLoginBinding.loginEtEmail.text.toString()
            val password = bindingLoginBinding.loginEtPassword.text.toString()

            val validation = validation(username, password)

            if (validation) {
                firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener {
                    if ( it.isSuccessful) {
                        Toast.makeText(this, "User Logged In successfully", Toast.LENGTH_LONG).show()
                        val i = Intent(this, LoginActivity::class.java)
                        startActivity(i)
                        finish()
                    } else {
                        it.exception
                        Log.w(TAG, "loginUserWithEmail:failure", it.exception)
                        Toast.makeText(this, "Incorrect credentials", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun validation(username: String, password: String): Boolean {

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "All the fields are required and can't be empty", Toast.LENGTH_LONG).show()
            return false
        }

        if (password.length < 6 ) {
            Toast.makeText(this, "Password needs to be longer than 6 chars", Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    override fun onStart() {
        super.onStart()
        // in on start method checking if
        // the user is already sign in.
        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null) {
            // if the user is not null then we are
            // opening a main activity on below line.
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }
    }
}