package com.example.book

import android.util.Log
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

fun makeHttpPost(ipAddress: String?, title: String, author: String, callback: (String?, String?) -> Unit) {

    val url = "http://${ipAddress}:8080/book/"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val JSON_TYPE = "application/json; charset=utf-8".toMediaType()

    val jsonBody = """
        {
            "title": "$title",
            "author": "$author"
        }"""

    val request = Request.Builder()
        .url(url)
        .post(jsonBody.toRequestBody(JSON_TYPE))
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if(response.isSuccessful) {
                val responseData = response.body?.string()

                callback(responseData, null)
            }
            else {
                callback(null, response.message)
            }
        }

        override fun onFailure(call: Call, e: IOException) {
            callback(null, e.message)
        }
    })
}