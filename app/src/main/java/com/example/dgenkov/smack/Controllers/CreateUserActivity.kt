package com.example.dgenkov.smack.Controllers

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import com.example.dgenkov.smack.R
import com.example.dgenkov.smack.Services.AuthService
import com.example.dgenkov.smack.Services.UserDataService
import com.example.dgenkov.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        createSpinner.visibility = View.INVISIBLE
    }

    fun generateUserAvatar(view: View) {

        val random = Random()
        val color = random.nextInt(2)
        val avatarNumber = random.nextInt(28)

        if(color == 0) {
            userAvatar = "light$avatarNumber"
        } else {
            userAvatar = "dark$avatarNumber"

        }

        val resourceId = resources.getIdentifier(userAvatar, "drawable", packageName)
        createAvatarImageView.setImageResource(resourceId)
    }

    fun generateColorClicked(view: View) {
        val random = Random()
        val red = random.nextInt(255)
        val green = random.nextInt(255)
        val blue = random.nextInt(255)

        createAvatarImageView.setBackgroundColor(Color.rgb(red,green,blue))

        val savedR = red.toDouble() / 255
        val savedG = green.toDouble() / 255
        val savedB = blue.toDouble() / 255

        avatarColor = "[$savedR, $savedG, $savedB, 1]"
        println("@avatarColor $avatarColor")

    }

    fun createUserClicked (view:View) {
        enableSpinner(true)
        val userName = createUsernameText.text.toString()
        val email = createEmailText.text.toString()
        val password = createPasswordText.text.toString()

        if (userName.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty()) {
            AuthService.registerUser(email, password) { registerSuccess ->
                if (registerSuccess) {
                    AuthService.loginUser(email,password) { loginSuccess ->
                        if(loginSuccess) {
                            AuthService.createUser(userName,email,userAvatar,avatarColor) {createUserSuccess ->
                                if(createUserSuccess) {

                                    val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                                    LocalBroadcastManager.getInstance(this).sendBroadcast(userDataChange)

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
                    enableSpinner(false)
                    errorToast()
                }
            }
        } else {
            Toast.makeText(this,"Make sure all the fields are filled", Toast.LENGTH_SHORT)
            enableSpinner(false)
        }



    }


    fun errorToast() {
        Toast.makeText(this,"Something went wrong, plase try again.",Toast.LENGTH_LONG).show()
        enableSpinner(false)
    }
    fun enableSpinner(enable: Boolean) {
        if(enable) {
            createSpinner.visibility = View.VISIBLE
        } else {
            createSpinner.visibility = View.INVISIBLE

        }
        createUserButton.isEnabled = !enable
        createAvatarImageView.isEnabled = !enable
        createBackgroundColorButton.isEnabled = !enable
    }

}

