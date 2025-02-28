package com.dicoding.mystoryapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.api.response.ListStoryItem
import com.dicoding.mystoryapp.data.StoryRepository
import kotlinx.coroutines.launch


class DetailViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _storyDetail = MutableLiveData<ListStoryItem>()
    val storyDetail: LiveData<ListStoryItem>
        get() = _storyDetail

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    fun fetchStoryDetail(id: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getDetailStory(id)
                _storyDetail.value = response.story?.let {
                    it.id?.let { it1 ->
                        ListStoryItem(
                            id = it1,
                            name = it.name,
                            description = it.description,
                            photoUrl = it.photoUrl
                        )
                    }
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}