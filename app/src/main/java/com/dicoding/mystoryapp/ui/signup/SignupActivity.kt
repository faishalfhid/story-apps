package com.dicoding.mystoryapp.ui.signup

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.databinding.ActivitySignupBinding
import com.dicoding.mystoryapp.ui.login.LoginActivity
import com.dicoding.mystoryapp.user.AuthViewModelFactory

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        AuthViewModelFactory.getAuthInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()

        observeLoadingState()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            viewModel.register(name, email, password) { response ->
                runOnUiThread {
                    if (!response.error!!) {
                        AlertDialog.Builder(this).apply {
                            setTitle(getString(R.string.yeah))
                            setMessage(getString(R.string.registermessage, email))
                            setPositiveButton(getString(R.string.next)) { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        val errorMessage = getString(R.string.failedregister)
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun observeLoadingState() {
        viewModel.isLoading.observe(this@SignupActivity) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
    }
}