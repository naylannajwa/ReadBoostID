// File: di/AppContainer.kt
package com.readboost.id.di

import android.content.Context
import android.util.Log
import com.readboost.id.data.local.database.AppDatabase
import com.readboost.id.domain.repository.ArticleRepository
import com.readboost.id.domain.repository.UserDataRepository

class AppContainer(private val context: Context) {

    // Database - simplified initialization
    private val database: AppDatabase by lazy {
        Log.d("AppContainer", "Initializing database...")
        val db = AppDatabase.getDatabase(context)
        Log.d("AppContainer", "Database initialized successfully")
        db
    }

    // Repositories - simplified initialization
    val articleRepository: ArticleRepository by lazy {
        Log.d("AppContainer", "Initializing articleRepository...")
        val repo = ArticleRepository(database.articleDao())
        Log.d("AppContainer", "ArticleRepository initialized successfully")
        repo
    }

    val userDataRepository: UserDataRepository by lazy {
        Log.d("AppContainer", "Initializing userDataRepository...")
        val repo = UserDataRepository(
            database.notesDao(),
            database.userProgressDao(),
            database.leaderboardDao()
        )
        Log.d("AppContainer", "UserDataRepository initialized successfully")
        repo
    }
}
