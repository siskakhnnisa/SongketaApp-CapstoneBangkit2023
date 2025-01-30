package com.songketa.songket_recognition_app.ui.splashscreen

import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.songketa.songket_recognition_app.MainActivity
import com.songketa.songket_recognition_app.databinding.ActivitySplashScreenBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.ui.welcome.WelcomeActivity
import com.songketa.songket_recognition_app.utils.Constant
import com.songketa.songket_recognition_app.utils.SettingPreferences
import com.songketa.songket_recognition_app.utils.ThemeModelFactory
import com.songketa.songket_recognition_app.utils.ThemeViewModel

class SplashScreenActivity : AppCompatActivity() {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel by viewModels<SplashViewModel>{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                playAnimation()
                val countdownTimer = object : CountDownTimer(Constant.TIMER_1, Constant.TIMER_2) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        navigateToActivity()
                    }
                }
                countdownTimer.start()
            } else {
                playAnimation()
                val countdownTimer = object : CountDownTimer(Constant.TIMER_1, Constant.TIMER_2) {
                    override fun onTick(millisUntilFinished: Long) {
                    }

                    override fun onFinish() {
                        navigateToMainActivity()
                    }
                }
                countdownTimer.start()

            }
        }

        val pref = SettingPreferences.getInstance(application.dataStore)

        val themeViewModel = ViewModelProvider(this, ThemeModelFactory(pref)).get(
            ThemeViewModel::class.java
        )
        themeViewModel.getThemeSettings().observe(this) {isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToActivity() {
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = Constant.SPLASH_SCREEN_TIMER
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

}