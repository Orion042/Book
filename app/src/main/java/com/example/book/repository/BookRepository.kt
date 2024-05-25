package com.example.book.repository

import android.content.Context
import com.example.book.database.BookDao
import com.example.book.database.BookDatabase
import com.example.book.database.BookEntity

class BookRepository(val context: Context) {
    private val bookDao: BookDao by lazy { BookDatabase.getInstance(context)!!.bookDao() }

    suspend fun getAllBooks(): List<BookEntity> {
        return bookDao.getAllBooksInfo()
    }

    suspend fun getAllBookTitles(): List<String> {
        return bookDao.getAllBookTitles()
    }

    suspend fun getAllBookAuthors(): List<String> {
        return bookDao.getAllBookAuthors()
    }

    suspend fun getBookInfoByTitle(title: String): BookEntity {
        return bookDao.getBookInfoByTitle(title)
    }

    suspend fun getBookInfoByAuthor(author: String): BookEntity {
        return bookDao.getBookInfoByAuthor(author)
    }

    suspend fun insertBook(book: BookEntity) {
        bookDao.insertBook(book)
    }

    suspend fun deleteBook(book: BookEntity) {
        bookDao.deleteBook(book)
    }
}