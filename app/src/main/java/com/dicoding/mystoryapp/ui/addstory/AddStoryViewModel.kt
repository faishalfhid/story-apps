package com.dicoding.mystoryapp.ui.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.mystoryapp.data.ResultState
import com.dicoding.mystoryapp.data.StoryRepository
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun uploadImage(file: File, description: String, lat: Double?, lon: Double?) = liveData {
        emit(ResultState.Loading)
        val result = if (lat != null && lon != null) {
            repository.uploadImage(file, description, lat, lon)
        } else {
            repository.uploadImage(file, description, null, null)
        }
        emit(result)
    }
}