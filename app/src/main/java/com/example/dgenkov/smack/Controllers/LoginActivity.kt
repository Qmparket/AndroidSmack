package com.example.dgenkov.smack.Controllers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.dgenkov.smack.R
import com.example.dgenkov.smack.Services.AuthService

import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginSpinner.visibility = View.INVISIBLE
    }

    fun onLoginButtonClicked(view: View) {
        enableSpinner(true)


        val email = loginEmailText.text.toString()
        val password = loginPasswordText.text.toString()
        hideKeyboard()
        if(email.isNotEmpty() && password.isNotEmpty()) {
            AuthService.loginUser(email,password) {loginSuccess ->
                if(loginSuccess) {
                    AuthService.findUserByEmail(this) {findUserByEmailSuccess ->
                        if(findUserByEmailSuccess) {
                            enableSpinner(false)
                            finish()
                        } else {
                            enableSpinner(false)
                            errorToast()
                        }
                    }
                } else {
                    enableSpinner(false)
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this,"Please enter your email and password",Toast.LENGTH_LONG).show()
            enableSpinner(false)
        }
    }

    fun errorToast() {
        Toast.makeText(this,"Something went wrong",Toast.LENGTH_LONG).show()
    }

    fun enableSpinner(enable: Boolean) {
        if (enable) {
            loginSpinner.visibility = View.VISIBLE
        } else {
            loginSpinner.visibility = View.INVISIBLE
        }
        loginEmailText.isEnabled = !enable
        loginPasswordText.isEnabled = !enable
        loginLoginButton.isEnabled = !enable
        loginSignUpButton.isEnabled = !enable
    }

    fun onSignupButtonClicked(view: View) {
        val signupIntent = Intent(this, CreateUserActivity::class.java)
        startActivity(signupIntent)
        finish()
    }

    fun hideKeyboard() {
        val view: View = if (currentFocus == null) View(this) else currentFocus
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
