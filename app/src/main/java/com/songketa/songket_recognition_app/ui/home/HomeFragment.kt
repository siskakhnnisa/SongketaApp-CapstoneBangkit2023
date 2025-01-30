package com.songketa.songket_recognition_app.ui.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import android.Manifest
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.adapter.HomeSongketAdapter
import com.songketa.songket_recognition_app.adapter.MenuAdapter
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.data.response.DatasetItem
import com.songketa.songket_recognition_app.databinding.FragmentHomeBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.ui.camera.CameraFragment
import com.songketa.songket_recognition_app.ui.listsongket.ListSongketActivity
import com.songketa.songket_recognition_app.ui.signin.SignInActivity
import com.songketa.songket_recognition_app.utils.Constant.LOCATION_PERMISSION_REQUEST_CODE

class HomeFragment : Fragment(), MenuAdapter.OnMenuItemClickListener {

    private lateinit var binding: FragmentHomeBinding
    private val TAG = "YourFragment"
    private val mNames = ArrayList<String>()
    private val mImageUrls = ArrayList<String>()

    private val params = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    ).apply {
        setMargins(8, 0, 8, 0)
    }

    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSession()
        getImages()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root

        val linearLayoutManager = LinearLayoutManager(requireContext())
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSongket.layoutManager = linearLayoutManager

        binding.tvExplore.setOnClickListener {
            startActivity(Intent(requireContext(), ListSongketActivity::class.java))
        }

        return view
    }

    private fun observeSession() {
        viewModel.getSession().observe(viewLifecycleOwner) { user ->
            if (!user.isLogin) {
                startActivity(Intent(requireContext(), SignInActivity::class.java))
                requireActivity().finish()
            } else {
                getStory()
                binding.tvUsername.text = user.name
            }
        }
    }

    private fun getImages() {
        Log.d(TAG, "initImageBitmaps: preparing bitmaps.")

        mImageUrls.clear()
        mNames.clear()

        // Menggunakan resource string
        mImageUrls.add("android.resource://${requireContext().packageName}/${R.drawable.icon_home_maps2}")
        mNames.add(getString(R.string.market_finder))

        mImageUrls.add("android.resource://${requireContext().packageName}/${R.drawable.icon_home_scanner2}")
        mNames.add(getString(R.string.scanner))

        mImageUrls.add("android.resource://${requireContext().packageName}/${R.drawable.icon_home_list}")
        mNames.add(getString(R.string.list_songket))

        mImageUrls.add("android.resource://${requireContext().packageName}/${R.drawable.icon_home_article}")
        mNames.add(getString(R.string.article))

        initRecyclerView()
    }
    
    private fun initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview")

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val recyclerView: RecyclerView = binding.root.findViewById(R.id.cardview_menu)
        recyclerView.layoutManager = layoutManager
        val adapter = MenuAdapter(requireContext(), mNames, mImageUrls, this)
        recyclerView.adapter = adapter
    }

    private fun getStory() {
        viewModel.getSongket().observe(viewLifecycleOwner) { story ->
            if (story != null) {
                when (story) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        val listStory = story.data
                        songketAdapter(listStory)
                        showLoading(false)
                    }

                    is Result.Error -> {
                        showLoading(false)
                        showToast(story.error)
                    }
                }
            }
        }
    }

    private fun songketAdapter(listStory: List<DatasetItem>) {
        val randomList = listStory.shuffled()
        val limitedList = randomList.take(5)
        val adapter = HomeSongketAdapter(requireContext())
        adapter.submitList(limitedList)
        binding.rvSongket.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun onButtonClicked() {
        val tfFragment = CameraFragment()
        val fragmentManager = activity?.supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager?.beginTransaction() ?: return
        fragmentTransaction.replace(R.id.frame_layout, tfFragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showToast(getString(R.string.location_permission_granted))
            } else {
                showToast(getString(R.string.location_permission_denied))
            }
        }
    }


    override fun onItemClick(position: Int) {
        when (position) {
            0 -> {
                if (checkLocationPermission()) {
                    val keyword = getString(R.string.map_search_keyword)
                    val gmmIntentUri = Uri.parse("geo:0,0?q=$keyword")

                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")

                    if (mapIntent.resolveActivity(requireContext().packageManager) != null) {
                        startActivity(mapIntent)
                    } else {
                        showToast(getString(R.string.google_maps_not_installed))
                    }
                } else {
                    requestLocationPermission()
                }
            }
            1 -> {
                onButtonClicked()
            }
            2 -> {
                startActivity(Intent(requireContext(), ListSongketActivity::class.java))
            }
            3 -> {
                showToast(getString(R.string.feature_not_available_yet))
            }
        }
    }
    companion object {
    }
}
