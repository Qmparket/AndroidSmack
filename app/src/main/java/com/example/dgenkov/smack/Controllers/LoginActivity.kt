package com.example.dgenkov.smack.Controllers

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.dgenkov.smack.R
import com.example.dgenkov.smack.Services.AuthService
import kotlinx.android.synthetic.main.activity_create_user.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun onLoginButtonClicked(view: View) {

        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        val userName = createUsernameText.text.toString()

        AuthService.loginUser(this,email,password) {loginSuccess ->

        }
    }

    fun onSignupButtonClicked(view: View) {
        val signupIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signupIntent)
    }
}
