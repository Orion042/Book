package com.example.book.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BookDao {
    @Query("SELECT * FROM book")
    fun getAllBooksInfo(): List<BookEntity>

    @Query("SELECT title FROM book")
    fun getAllBookTitles(): List<String>

    @Query("SELECT author FROM book")
    fun getAllBookAuthors(): List<String>

    @Query("SELECT * FROM book WHERE title = :title")
    fun getBookInfoByTitle(title: String): BookEntity

    @Query("SELECT * FROM book WHERE author = :author")
    fun getBookInfoByAuthor(author: String): BookEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(vararg book: BookEntity)

    @Delete
    fun deleteBook(vararg book: BookEntity)
}