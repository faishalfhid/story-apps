package com.dicoding.mystoryapp.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.mystoryapp.api.response.RegisterResponse
import com.dicoding.mystoryapp.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    init {
        _isLoading.value = false
    }

    fun register(name: String, email: String, password: String, onResult: (RegisterResponse) -> Unit) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.register(name, email, password)
                onResult(response)
            } catch (e: Exception) {
                onResult(RegisterResponse(error = true, message = e.message ?: "Unknown error"))
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}