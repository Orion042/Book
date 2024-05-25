package com.example.book.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase: RoomDatabase(){
    abstract fun bookDao(): BookDao

    companion object {

        private val dbName = "book.db"
        private var instance: BookDatabase? = null

        fun getInstance(context: Context): BookDatabase? {
            if(instance == null) {
                instance = Room.databaseBuilder(context, BookDatabase::class.java, dbName).build()
            }
            return instance
        }
    }
}