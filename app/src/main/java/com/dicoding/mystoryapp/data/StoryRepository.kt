package com.dicoding.mystoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.mystoryapp.api.ApiService
import com.dicoding.mystoryapp.api.response.DetailStoryResponse
import com.dicoding.mystoryapp.api.response.FileUploadResponse
import com.dicoding.mystoryapp.api.response.ListStoryItem
import com.dicoding.mystoryapp.api.response.StoryDatabase
import com.dicoding.mystoryapp.api.response.StoryResponse
import com.dicoding.mystoryapp.data.pref.UserModel
import com.dicoding.mystoryapp.data.pref.UserPreference
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val database:StoryDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10, // Adjust the page size as needed
            ),
            remoteMediator = StoryRemoteMediator(database, apiService)
        ) {
            database.storyDao().getAllStory()
        }.liveData
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    fun getStoriesWithLocation(location: Int = 1): LiveData<ResultState<List<ListStoryItem>>> = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.getStoriesWithLocation(location)
            emit(ResultState.Success(successResponse.listStory))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(ResultState.Error(errorResponse.message ?: "Unknown Error"))
        }
    }


    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun uploadImage(
        imageFile: File,
        description: String,
        lat: Double?,
        lon: Double?
    ): ResultState<FileUploadResponse> {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )

        val latRequestBody = lat?.toString()?.toRequestBody("text/plain".toMediaType())
        val lonRequestBody = lon?.toString()?.toRequestBody("text/plain".toMediaType())

        return try {
            val response = apiService.uploadImage(
                multipartBody,
                requestBody,
                latRequestBody ?: "".toRequestBody("text/plain".toMediaType()), // Handle null values
                lonRequestBody ?: "".toRequestBody("text/plain".toMediaType()) // Handle null values
            )
            ResultState.Success(response)
        } catch (e: Exception) {
            ResultState.Error("Error Upload Image")
        }
    }

    companion object {
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            database:StoryDatabase
        ) = StoryRepository(apiService, userPreference, database)
    }
}