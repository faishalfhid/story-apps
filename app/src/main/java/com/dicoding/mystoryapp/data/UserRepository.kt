package com.dicoding.mystoryapp.data

import androidx.lifecycle.liveData
import com.dicoding.mystoryapp.api.ApiService
import com.dicoding.mystoryapp.api.response.RegisterResponse
import com.dicoding.mystoryapp.data.pref.UserModel
import com.dicoding.mystoryapp.data.pref.UserPreference
import com.google.gson.Gson
import com.dicoding.mystoryapp.api.response.LoginResponse
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }


    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    fun login(name: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.login(name, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    companion object {
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService
        ) = UserRepository(userPreference, apiService)
    }
}