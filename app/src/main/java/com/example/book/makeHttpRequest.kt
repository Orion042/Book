package com.example.book

import android.util.Log
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

fun makeHttpRequest(ipAddress: String?, searchOption: String, searchValue: String, callback: (String?, String?) -> Unit) {

    val url = "http://${ipAddress}:8080/book/${searchOption}/${searchValue}"

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            if(response.isSuccessful) {
                val responseData = response.body?.string()

                val result = Gson().fromJson(responseData, BookDataClass::class.java)

                if(result.ID != 0) {
                    callback(responseData, null)
                }
                else {
                    callback(null, "Book not found")
                }
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