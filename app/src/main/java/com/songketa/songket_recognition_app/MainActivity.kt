package com.songketa.songket_recognition_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.songketa.songket_recognition_app.databinding.ActivityMainBinding
import com.songketa.songket_recognition_app.ui.bookmark.BookmarkFragment
import com.songketa.songket_recognition_app.ui.camera.CameraFragment
import com.songketa.songket_recognition_app.ui.home.HomeFragment
import com.songketa.songket_recognition_app.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_scan -> replaceFragment(CameraFragment())
                R.id.nav_bookmark -> replaceFragment(BookmarkFragment())
                R.id.nav_account -> replaceFragment(ProfileFragment())
            }
            true
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}