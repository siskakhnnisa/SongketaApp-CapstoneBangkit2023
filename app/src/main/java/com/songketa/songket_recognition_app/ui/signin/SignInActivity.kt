package com.songketa.songket_recognition_app.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.songketa.songket_recognition_app.MainActivity
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.data.model.User
import com.songketa.songket_recognition_app.databinding.ActivitySignInBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.ui.welcome.WelcomeActivity
import com.songketa.songket_recognition_app.utils.disable
import com.songketa.songket_recognition_app.utils.enable
import com.jakewharton.rxbinding2.widget.RxTextView
import com.songketa.songket_recognition_app.ui.signup.SignUpActivity
import io.reactivex.Observable

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding

    private val viewModel by viewModels<SignInViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tvSignUpHere = binding.tvSignupHere
        tvSignUpHere.paintFlags = tvSignUpHere.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        playAnimation()
        setupView()
        processingLogin()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent)
        finish()
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

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titlemail = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailedit = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val titlepass = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passedit = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.signInButton, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(titlemail,emailedit,titlepass,passedit,login)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("CheckResult")
    private fun processingLogin() {
        val emailStream = RxTextView.textChanges(binding.emailEditText)
            .skipInitialValue()
            .map {
                binding.emailEditText.error != null
            }

        val passwordStream = RxTextView.textChanges(binding.passwordEditText)
            .skipInitialValue()
            .map {
                binding.passwordEditText.error != null
            }

        val invalidFieldStream = Observable.combineLatest(
            emailStream,
            passwordStream
        ) { emailInvalid, passwordInvalid ->
            !emailInvalid && !passwordInvalid
        }

        invalidFieldStream.subscribe { isValid ->
            if (isValid) binding.signInButton.enable() else binding.signInButton.disable()
        }
        binding.tvSignupHere.setOnClickListener{
            navigateToSignUpActivity()
        }

        binding.seePassword.setOnCheckedChangeListener { _, isChecked ->
            binding.passwordEditText.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            binding.passwordEditText.text?.let { binding.passwordEditText.setSelection(it.length) }
        }

        binding.signInButton.setOnClickListener {
            if (confirmAccount()) {
                showLoading(true)
                val email = binding.emailEditText.text.toString()
                val pass = binding.passwordEditText.text.toString()

                viewModel.login(email = email, password = pass).observe(this) { hoho ->
                    when (hoho) {
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            val user = User(
                                userId = hoho.data.loginResult.userId,
                                name = hoho.data.loginResult.name,
                                token = hoho.data.loginResult.token,
                                email = email,
                                phone = hoho.data.loginResult.phone,
                            )
                            viewModel.saveSession(user)
                            val title = getString(R.string.head_notif_succes)
                            val message = getString(R.string.login_succes_notif)
                            val next = getString(R.string.next_notif)
                            showSuccessDialogWithIntent(title, message, next)
                        }
                        is Result.Error -> {
                            showLoading(false)
                            val title = getString(R.string.head_notif_failed)
                            val message = hoho.error
                            val next = getString(R.string.failed_notif)
                            showSuccessDialogWithIntent(title, message, next)
                        }
                    }
                }
            }
        }
    }

    private fun showSuccessDialogWithIntent(title: String, message: String, next: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(next) { _, _ ->
                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }


    private fun confirmAccount(): Boolean {
        val valid: Boolean?
        val email = binding.emailEditText.text?.trim().toString()
        val password = binding.passwordEditText.text?.trim().toString()
        when {
            email.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.enter_your_email)
                valid = java.lang.Boolean.FALSE
            }
            password.isEmpty() -> {
                binding.emailEditTextLayout.error = getString(R.string.enter_your_password)
                valid = java.lang.Boolean.FALSE
            }
            else -> {
                valid = java.lang.Boolean.TRUE
                binding.emailEditTextLayout.error = null
                binding.emailEditTextLayout.error = null
            }
        }
        return valid
    }

    private fun navigateToSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}
