package com.example.dgenkov.smack.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.dgenkov.smack.Controllers.App
import com.example.dgenkov.smack.Utilities.*
import org.json.JSONException
import org.json.JSONObject


object AuthService {

//    var isLoggedIn = false
//    var userEmail = ""
//    var authToken = ""

    fun registerUser(context: Context, email: String, password: String, complete: (Boolean) -> Unit){
        val url = URL_REGISTER

        val jsonBody = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        val requestBody = jsonBody.toString()

        val registerRequest = object : StringRequest(Request.Method.POST, url, Response.Listener { _ ->
            complete(true)
            }, Response.ErrorListener { error ->
            Log.d("ERROR","Could not register user: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(registerRequest)
    }

    fun loginUser(context: Context,email: String,password: String,complete: (Boolean) -> Unit) {

        val url = URL_LOGIN

        val jsonBody = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        var requestBody = jsonBody.toString()

        val loginRequest = object: JsonObjectRequest(Request.Method.POST, url,null, Response.Listener { response ->


            try {
                App.prefs.userEmail = response.getString("user")
                App.prefs.authToken = response.getString("token")
                App.prefs.isLoggedIn = true
            } catch (e: JSONException) {
                Log.d("JSON", "EXC: ${e.localizedMessage}")
            }


            complete(true)

        }, Response.ErrorListener { error ->
            complete(false)
            Log.d("ERROR", "Could not login user: $error")
        } ) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        App.prefs.requestQueue.add(loginRequest)
    }

    fun createUser(context: Context,name: String, email: String, avatarName: String, avatarColor: String, complete: (Boolean) -> Unit) {
        val url = URL_CREATE_USER

        val jsonBody = JSONObject()
        jsonBody.put("name", name)
        jsonBody.put("email", email)
        jsonBody.put("avatarName", avatarName)
        jsonBody.put("avatarColor", avatarColor)
        val requestBody = jsonBody.toString()

        val createUserRequest = object: JsonObjectRequest(Request.Method.POST, url, null, Response.Listener { createUserSuccess ->


            try {
                UserDataService.name = createUserSuccess.getString("name")
                UserDataService.email = createUserSuccess.getString("email")
                UserDataService.avatarName = createUserSuccess.getString("avatarName")
                UserDataService.avatarColor = createUserSuccess.getString("avatarColor")
                UserDataService.id = createUserSuccess.getString("_id")
                complete(true)

            } catch (e: JSONException) {
                complete(false)
                Log.d("JSON", "EXC ${e.localizedMessage}")
            }
        }, Response.ErrorListener { error ->
            complete(false)
            Log.d("ERROR", "Could not create user: $error")
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }
        App.prefs.requestQueue.add(createUserRequest)
    }

    fun findUserByEmail(context: Context,complete: (Boolean) -> Unit) {
        val url = URL_USER_BY_EMAIL

        val findUserRequest = object: JsonObjectRequest(Request.Method.GET, "$url${App.prefs.userEmail}", null, Response.Listener { response ->

            try {
                UserDataService.email = response.getString("email")
                UserDataService.name = response.getString("name")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                val userDataChange = Intent(BROADCAST_USER_DATA_CHANGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)

                complete(true)

            } catch(e: JSONException) {
                complete(false)
                Log.d("JSON", "Error parsing the json body from the finduserbyemail :$e")
            }
        }, Response.ErrorListener { error ->

            Log.d("Error", "error finding user by email: $error")
            complete(false)
        }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(findUserRequest)
    }
}