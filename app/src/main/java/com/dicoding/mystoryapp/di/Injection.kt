package com.dicoding.mystoryapp.di

import android.content.Context
import com.dicoding.mystoryapp.api.ApiConfig
import com.dicoding.mystoryapp.api.response.StoryDatabase
import com.dicoding.mystoryapp.data.StoryRepository
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.mystoryapp.data.pref.UserPreference
import com.dicoding.mystoryapp.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideStoryRepository(context: Context): StoryRepository {
        val preference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { preference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(apiService, preference, database)
    }

    fun provideUserRepository(context: Context): UserRepository {
        val preference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { preference.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(preference, apiService)
    }
}