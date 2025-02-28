package com.dicoding.mystoryapp.ui.story

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.mystoryapp.R
import com.dicoding.mystoryapp.ViewModelFactory
import com.dicoding.mystoryapp.databinding.ActivityMainBinding
import com.dicoding.mystoryapp.ui.addstory.AddStoryActivity
import com.dicoding.mystoryapp.ui.maps.MapsActivity
import com.dicoding.mystoryapp.ui.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val storyViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestLocationPermissionIfNeeded()

        setupView()
        setupAction()
        observeSession()
        getData()

        // Setup swipe to refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun setupView() {
        storyAdapter = StoryAdapter()
        binding.recyclerViewStories.adapter = storyAdapter
        val layoutManager = LinearLayoutManager(this)
        binding.recyclerViewStories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.recyclerViewStories.addItemDecoration(itemDecoration)
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.peringatan))
                setMessage(getString(R.string.logoutmsg))
                setPositiveButton(R.string.logout) { _, _ ->
                    storyViewModel.logout()
                }
                setNegativeButton(getString(R.string.no), null)
                create()
                show()
            }
        }

        binding.fabAddstory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        binding.fabMap.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun observeSession() {
        storyViewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                storyViewModel.getStories().observe(this) { pagingData ->
                    storyAdapter.submitData(lifecycle, pagingData)
                }
            } else {
                startActivity(Intent(this, WelcomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
            }
        }
    }

    private fun requestLocationPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun getData() {
        val adapter = StoryAdapter()
        binding.recyclerViewStories.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )

        binding.progressBar.visibility = View.VISIBLE

        storyViewModel.getStories().observe(this) { pagingData ->
            binding.progressBar.visibility = View.GONE
            adapter.submitData(lifecycle, pagingData)
        }
    }

    private fun refreshData() {
        lifecycleScope.launch {
            storyAdapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123
    }
}
