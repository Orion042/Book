package com.example.book

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.room.Room
import com.example.book.database.BookDatabase
import com.example.book.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var navView: BottomNavigationView

    val ipAddress = "xxx.xxx.x.xx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initDatabase()

        setContentView(R.layout.activity_main)

        navView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_gemini, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun initDatabase() {
        Room.databaseBuilder(
            applicationContext,
            BookDatabase::class.java, "book-db"
        ).build()
    }
}