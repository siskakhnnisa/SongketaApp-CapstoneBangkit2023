package com.songketa.songket_recognition_app.ui.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.databinding.FragmentProfileBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.ui.home.HomeFragment
import com.songketa.songket_recognition_app.ui.signin.SignInActivity
import com.songketa.songket_recognition_app.utils.SettingPreferences
import com.songketa.songket_recognition_app.utils.ThemeModelFactory
import com.songketa.songket_recognition_app.utils.ThemeViewModel



class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel by viewModels<ProfileViewModel>{
        ViewModelFactory.getInstance(requireContext())
    }
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSession()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                replaceFragment(HomeFragment())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        playAnimation()

        val pref = SettingPreferences.getInstance(requireActivity().dataStore)

        val themeViewModel = ViewModelProvider(this, ThemeModelFactory(pref)).get(
            ThemeViewModel::class.java
        )

        themeViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            setTheme(isDarkModeActive)
            binding.btnSwitch.isChecked = isDarkModeActive
        }

        binding.btnSwitch.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            themeViewModel.saveThemeSetting(isChecked)
        }

        binding.btnLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btnLogout.setOnClickListener {
            val title = getString(R.string.warning_notif)
            val message = getString(R.string.question_notif)
            val next = getString(R.string.logout_notif)
            val back = getString(R.string.return_notif)
            showOptionDialogWithIntent(title,message,next,back)
        }
        binding.btnEdit.setOnClickListener {
            val intent = Intent(requireContext(), ProfileChangeActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
    private fun showOptionDialogWithIntent(title: String, message: String, next: String, back: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(next) { _, _ ->
                logout()
            }
            setNegativeButton(back){ _, _ ->
                requireActivity().supportFragmentManager.popBackStack()
            }
            create()
            show()
        }
    }

    private fun setTheme(isDarkModeActive: Boolean) {
        val mode = if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    private fun observeSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                requireActivity().finish()
            } else {
                binding.tvUsername.text = user.name
                binding.tvEmail.text = user.email
                binding.tvPhone.text = user.phone
            }
        }
    }
    private fun playAnimation() {
        val ivimage = ObjectAnimator.ofFloat(binding.ivImageProfile, View.ALPHA, 1f).setDuration(200)
        val titleusername = ObjectAnimator.ofFloat(binding.tvUsername, View.ALPHA, 1f).setDuration(200)
        val titleemail = ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(200)
        val titlephone = ObjectAnimator.ofFloat(binding.tvPhone, View.ALPHA, 1f).setDuration(200)

        val card1 = ObjectAnimator.ofFloat(binding.card1, View.ALPHA, 1f).setDuration(100)
        val card2 = ObjectAnimator.ofFloat(binding.card2, View.ALPHA, 1f).setDuration(100)
        val card3 = ObjectAnimator.ofFloat(binding.card3, View.ALPHA, 1f).setDuration(100)
        val card4 = ObjectAnimator.ofFloat(binding.card4, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(ivimage,titleusername,titleemail,titlephone,card1,card2,card4,card3)
            start()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }

    private fun logout() {
        viewModel.logout()
    }

    companion object {

    }
}