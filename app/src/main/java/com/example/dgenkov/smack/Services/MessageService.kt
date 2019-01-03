package com.example.dgenkov.smack.Services

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.dgenkov.smack.Controllers.App
import com.example.dgenkov.smack.Model.Channel
import com.example.dgenkov.smack.Utilities.URL_GET_CHANNELS
import org.json.JSONArray
import org.json.JSONException

object MessageService {
    val channels = ArrayList<Channel>()

    fun getChannels(complete: (Boolean) -> Unit) {
        val channelsRequest = object: JsonArrayRequest(Method.GET, URL_GET_CHANNELS,null, Response.Listener {response ->
            try {
                if(channels.size > 0) {
                    channels.clear()
                }
                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val channelDesc = channel.getString("description")
                    val channelId = channel.getString("_id")

                    val newChannel = Channel(name,channelDesc, channelId)
                    this.channels.add(newChannel)
                }
                complete(true)

            }catch(e: JSONException) {
                Log.d("JSON", "EXC:" + e.localizedMessage)
                complete(false)
            }

        }, Response.ErrorListener {error ->
            Log.d("ERROR", "Could not retrieve channels")
            complete(false)
        })  {
            override fun getBodyContentType(): String {
                return "application/json; charset=urf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization", "Bearer ${App.prefs.authToken}")
                return headers
            }
        }

        App.prefs.requestQueue.add(channelsRequest)
    }

}