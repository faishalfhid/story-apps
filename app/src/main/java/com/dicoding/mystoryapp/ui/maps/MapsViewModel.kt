package com.dicoding.mystoryapp.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mystoryapp.api.response.ListStoryItem
import com.dicoding.mystoryapp.data.ResultState
import com.dicoding.mystoryapp.data.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {
    fun getStoriesWithLocation(location: Int = 1): LiveData<ResultState<List<ListStoryItem>>> {
        return repository.getStoriesWithLocation(location)
    }
}
