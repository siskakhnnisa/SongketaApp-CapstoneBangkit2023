package com.songketa.songket_recognition_app.ui.welcome

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import com.songketa.songket_recognition_app.MainActivity
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.databinding.ActivityMainBinding
import com.songketa.songket_recognition_app.databinding.ActivityWelcomeBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.ui.signin.SignInActivity
import com.songketa.songket_recognition_app.ui.signup.SignUpActivity
import com.songketa.songket_recognition_app.ui.splashscreen.SplashScreenActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        setupView()
        setupAction()

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

    private fun setupAction(){
        binding.signInButton.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()

        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(300)
        val subtitle = ObjectAnimator.ofFloat(binding.tvSubtitle, View.ALPHA, 1f).setDuration(300)
        val btnsignin = ObjectAnimator.ofFloat(binding.signInButton, View.ALPHA, 1f).setDuration(300)
        val btnsignup = ObjectAnimator.ofFloat(binding.signUpButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title,subtitle,btnsignin,btnsignup)
            start()
        }
    }
}