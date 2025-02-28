package com.dicoding.mystoryapp.ui.detail

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.api.response.ListStoryItem
import com.dicoding.mystoryapp.databinding.ActivityDetailBinding
import com.squareup.picasso.Picasso

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailBinding
    private var defaultName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val storyId = intent.getStringExtra(EXTRA_STORY_ID)
        val storyName = intent.getStringExtra(EXTRA_STORY)

        if (storyId != null && defaultName != storyName) {
            viewModel.fetchStoryDetail(storyId)
        }

        viewModel.storyDetail.observe(this) { storyDetail ->
            showStoryDetail(storyDetail)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }
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

    private fun showStoryDetail(storyDetail: ListStoryItem?) {
        storyDetail?.let {
            Picasso.get().load(storyDetail.photoUrl).into(binding.imageStory)
            binding.textName.text = storyDetail.name
            binding.textDescription.text = storyDetail.description
        } ?: run {
            Toast.makeText(this, "Tidak ada cerita yang ditemukan", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val EXTRA_STORY = "extra_story"
        const val EXTRA_STORY_ID = "extra_story_id"
    }
}