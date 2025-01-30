package com.songketa.songket_recognition_app.ui.signup

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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import com.songketa.songket_recognition_app.MainActivity
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.databinding.ActivitySignUpBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.ui.signin.SignInActivity
import com.songketa.songket_recognition_app.ui.welcome.WelcomeActivity
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.data.model.User
import com.songketa.songket_recognition_app.utils.disable
import com.songketa.songket_recognition_app.utils.enable
import io.reactivex.Observable
import com.songketa.songket_recognition_app.utils.softkeyboard


class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel by viewModels<SignUpViewModel>{
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSigninHere.setOnClickListener {
            navigateToSignInActivity()
        }

        val tvSignInHere = binding.tvSigninHere
        tvSignInHere.paintFlags = tvSignInHere.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        playAnimation()
        setupView()
        processingSignup()
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
        showPassword()

    }
    private fun showPassword() {
        binding.showPassword.setOnCheckedChangeListener { _, isChecked ->
            val editText = binding.passwordField
            editText.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            editText.text?.let { editText.setSelection(it.length) }
        }
    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleregister = ObjectAnimator.ofFloat(binding.tvSignup, View.ALPHA, 1f).setDuration(200)
        val titleusername = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(200)
        val usernameedit = ObjectAnimator.ofFloat(binding.tilUsername, View.ALPHA, 1f).setDuration(300)
        val titlemail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(200)
        val emailedit = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 1f).setDuration(300)
        val titlphone = ObjectAnimator.ofFloat(binding.tvPhone, View.ALPHA, 1f).setDuration(200)
        val phoneedit = ObjectAnimator.ofFloat(binding.tilPhone, View.ALPHA, 1f).setDuration(300)
        val titlepass = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(200)
        val passedit = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.btnSignUp, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(titleregister,titleusername,usernameedit,titlemail,
                emailedit,titlphone,phoneedit,titlepass,passedit,login)
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToSignInActivity() {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun confirmAccount(): Boolean {
        val valid: Boolean?
        val username = binding.usernameField.text?.trim().toString()
        val email = binding.emailField.text?.trim().toString()
        val phone = binding.phoneField.text?.trim().toString()
        val password = binding.passwordField.text?.trim().toString()
        when {
            username.isEmpty() ->{
                binding.tilUsername.error = getString(R.string.enter_username)
                valid = java.lang.Boolean.FALSE
            }
            email.isEmpty() -> {
                binding.tilEmail.error = getString(R.string.enter_email)
                valid = java.lang.Boolean.FALSE
            }
            phone.isEmpty() -> {
                binding.tilPhone.error = getString(R.string.enter_your_phone_number)
                valid = java.lang.Boolean.FALSE
            }
            password.isEmpty() -> {
                binding.tilPassword.error = getString(R.string.enter_your_password)
                valid = java.lang.Boolean.FALSE
            }
            else -> {
                valid = java.lang.Boolean.TRUE
                binding.tilUsername.error = null
                binding.tilEmail.error = null
                binding.tilPhone.error = null
                binding.tilPassword.error = null
            }
        }
        return valid
    }

    @SuppressLint("CheckResult")
    private fun processingSignup() {
        val usernameStream = RxTextView.textChanges(binding.usernameField)
            .skipInitialValue()
            .map { it.isNotEmpty() && binding.tilUsername.error == null }

        val emailStream = RxTextView.textChanges(binding.emailField)
            .skipInitialValue()
            .map { it.isNotEmpty() && binding.tilEmail.error == null }

        val phoneStream = RxTextView.textChanges(binding.phoneField)
            .skipInitialValue()
            .map { it.isNotEmpty() && binding.tilPhone.error == null }

        val passwordStream = RxTextView.textChanges(binding.passwordField)
            .skipInitialValue()
            .map { it.isNotEmpty() && isValidPassword(it.toString()) && binding.tilPassword.error == null }

        val invalidFieldStream = Observable.combineLatest(
            usernameStream,
            emailStream,
            phoneStream,
            passwordStream
        ) { usernameValid, emailValid, phoneValid, passwordValid ->
            usernameValid && emailValid && phoneValid && passwordValid
        }

        invalidFieldStream.subscribe { isValid ->
            if (isValid) {
                binding.btnSignUp.enable()
            } else {
                binding.btnSignUp.disable()
            }
        }

        binding.tvSigninHere.setOnClickListener {
            navigateToSignInActivity()
        }

        binding.btnSignUp.setOnClickListener {
            if (confirmAccount()) {
                val email = binding.emailField.text.toString()
                val name = binding.usernameField.text.toString()
                val phone = binding.phoneField.text.toString()
                val pass = binding.passwordField.text.toString()

                viewModel.register(email = email, name = name, phone = phone, password = pass)
                    .observe(this) { result ->
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                showLoading(false)
                                val title = getString(R.string.head_notif_succes)
                                val message = getString(R.string.register_succes_notif)
                                val next = getString(R.string.next_notif)
                                showSuccessDialog(title, message, next)
                            }

                            is Result.Error -> {
                                showLoading(false)
                                val title = getString(R.string.head_notif_failed)
                                val message = result.error
                                val next = getString(R.string.failed_notif)
                                showFailedDialog(title, message, next)
                            }
                        }
                    }
            }
        }
    }

    private fun showSuccessDialog(title: String, message: String, next: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(next) { _, _ ->
                val intent = Intent(context, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }

    private fun showFailedDialog(title: String, message: String, next: String) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(next) { _, _ ->
                val intent = Intent(context, SignUpActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
            create()
            show()
        }
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

}

