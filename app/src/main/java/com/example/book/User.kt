package com.example.book

class User(private val userId: String, private val nickname: String) {
    private val mUserId = userId
    private val mNickname = nickname

    fun getUserId(): String {
        return mUserId
    }
    fun getNickname(): String {
        return mNickname
    }
}