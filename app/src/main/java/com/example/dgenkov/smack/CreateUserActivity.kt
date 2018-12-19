package com.example.dgenkov.smack

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_create_user.*
import java.util.*

class CreateUserActivity : AppCompatActivity() {

    var userAvatar = "profileDefault"
    var avatarColor = "[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
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

    fun createUserClicked(view:View) {

    }
}
