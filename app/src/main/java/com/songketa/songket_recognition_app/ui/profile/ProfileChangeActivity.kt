package com.songketa.songket_recognition_app.ui.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding2.widget.RxTextView
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.databinding.ActivityProfileChangeBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import io.reactivex.Observable

class ProfileChangeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileChangeBinding
    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        playAnimation()
        setupEditButton()
        showPassword()
    }

    private fun setupEditButton() {
        binding.btnEdit.setOnClickListener {
            showEditConfirmationDialog()
        }
        processingEdit()
    }

    @SuppressLint("CheckResult")
    private fun processingEdit() {
        val usernameStream = RxTextView.textChanges(binding.usernameField)
            .skipInitialValue()
            .map { it.isNotEmpty() && binding.tilUsername.error == null }

        val phoneStream = RxTextView.textChanges(binding.phoneField)
            .skipInitialValue()
            .map { it.isNotEmpty() && binding.tilPhone.error == null }

        val passwordStream = RxTextView.textChanges(binding.passwordField)
            .skipInitialValue()
            .map { it.isNotEmpty() && isValidPassword(it.toString()) && binding.tilPassword.error == null }

        val invalidFieldStream = Observable.combineLatest(
            usernameStream,
            phoneStream,
            passwordStream
        ) { usernameValid, phoneValid, passwordValid ->
            usernameValid && phoneValid && passwordValid
        }

        invalidFieldStream.subscribe { isValid ->
            binding.btnEdit.isEnabled = isValid
        }
    }

    private fun showEditConfirmationDialog() {
        val title = getString(R.string.warning_notif)
        val message = getString(R.string.question_notif)
        val next = getString(R.string.logout_notif)
        val back = getString(R.string.return_notif)

        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(next) { _, _ ->
                editUser()
                logout()
            }
            setNegativeButton(back) { _, _ ->
                supportFragmentManager.popBackStack()
            }
            create()
            show()
        }
    }

    private fun editUser() {
        viewModel.getSession().observe(this) { session ->
            val id = session.userId
            val name = binding.usernameField.text.toString()
            val phone = binding.phoneField.text.toString()
            val pass = binding.passwordField.text.toString()

            viewModel.editUser(id, name, phone, pass).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        val errorMessage = result.error
                        showToast(errorMessage)
                    }
                }
            }
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleusername = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(200)
        val usernameedit = ObjectAnimator.ofFloat(binding.tilUsername, View.ALPHA, 1f).setDuration(300)
        val titlphone = ObjectAnimator.ofFloat(binding.tvPhone, View.ALPHA, 1f).setDuration(200)
        val phoneedit = ObjectAnimator.ofFloat(binding.tilPhone, View.ALPHA, 1f).setDuration(300)
        val titlepass = ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(200)
        val passedit = ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 1f).setDuration(300)
        val edit = ObjectAnimator.ofFloat(binding.btnEdit, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(titleusername,usernameedit,
                titlphone,phoneedit,titlepass,passedit,edit)
            start()
        }
    }

    private fun showPassword() {
        binding.showPassword.setOnCheckedChangeListener { _, isChecked ->
            val editText = binding.passwordField // Assuming password_field is the id of your EditText inside TextInputLayout
            editText.transformationMethod = if (isChecked) {
                HideReturnsTransformationMethod.getInstance()
            } else {
                PasswordTransformationMethod.getInstance()
            }
            editText.text?.let { editText.setSelection(it.length) }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun logout() {
        viewModel.logout()
    }
    private fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}