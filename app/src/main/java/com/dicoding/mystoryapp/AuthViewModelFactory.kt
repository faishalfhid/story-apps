package com.dicoding.mystoryapp.user

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.mystoryapp.data.UserRepository
import com.dicoding.mystoryapp.di.Injection
import com.dicoding.mystoryapp.ui.login.LoginViewModel
import com.dicoding.mystoryapp.ui.signup.SignupViewModel

class AuthViewModelFactory(private val userRepository: UserRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignupViewModel::class.java) -> {
                SignupViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getAuthInstance(context: Context) = AuthViewModelFactory(Injection.provideUserRepository(context))
    }
}