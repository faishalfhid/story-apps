package com.dicoding.mystoryapp.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.mystoryapp.api.response.ListStoryItem
import com.dicoding.mystoryapp.data.StoryRepository
import com.dicoding.mystoryapp.data.pref.UserModel
import kotlinx.coroutines.launch

class StoryViewModel(
    private val storyRepository: StoryRepository
) : ViewModel() {


    fun getSession(): LiveData<UserModel> {
        return storyRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            storyRepository.logout()
        }
    }

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getStories().cachedIn(viewModelScope)
    }
}