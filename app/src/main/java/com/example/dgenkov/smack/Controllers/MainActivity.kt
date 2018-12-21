package com.example.dgenkov.smack.Controllers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.dgenkov.smack.R
import com.example.dgenkov.smack.Services.AuthService
import com.example.dgenkov.smack.Services.UserDataService
import com.example.dgenkov.smack.Utilities.BROADCAST_USER_DATA_CHANGE
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {

    val regex = Regex("(\\d+)(\\.?)(\\d*)")

    private val userDataChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            if(AuthService.isLoggedIn) {
                when (intent?.action) {
                    BROADCAST_USER_DATA_CHANGE -> handleUserDataChange()
                }
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        LocalBroadcastManager.getInstance(this).registerReceiver(userDataChangeReceiver, IntentFilter(
            BROADCAST_USER_DATA_CHANGE))

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    fun handleUserDataChange() {
        userEmailNavHeader.text = UserDataService.email
        userNameNavHeader.text = UserDataService.name
        val resource = resources.getIdentifier(UserDataService.avatarName,"drawable",packageName)
        userImageNavHeader.setImageResource(resource)
//        val colors = regex.findAll(UserDataService.avatarColor)
//        val r = colors.elementAt(0)?.value?.toFloat()
//        val g = colors.elementAt(1)?.value?.toFloat()
//        val b = colors.elementAt(2)?.value?.toFloat()
//        if(Build.VERSION.SDK_INT > 26) {
//            userImageNavHeader.setBackgroundColor(Color.rgb(r,g,b))
//        }

        userImageNavHeader.setBackgroundColor(UserDataService.returnAvatarColor(UserDataService.avatarColor))
        loginButtonNavHeader.text = "Logout"
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun loginButtonNavClicked(view: View) {

        if (AuthService.isLoggedIn) {
            // Log out
            UserDataService.logout()
            userNameNavHeader.text = "Login"
            userEmailNavHeader.text = ""
            userImageNavHeader.setImageResource(R.drawable.profiledefault)
            userImageNavHeader.setBackgroundColor(Color.TRANSPARENT)
            loginButtonNavHeader.text = "login"


        } else {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    fun addChannelClicked(view: View) {

    }

    fun sendMessageButtonClicked(view: View) {

    }
}
