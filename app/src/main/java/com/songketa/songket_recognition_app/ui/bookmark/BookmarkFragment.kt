package com.songketa.songket_recognition_app.ui.bookmark

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.adapter.BookmarkAdapter
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.data.database.SongketEntity
import com.songketa.songket_recognition_app.databinding.FragmentBookmarkBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.ui.detailsongket.DetailSongketActivity
import com.songketa.songket_recognition_app.ui.home.HomeFragment

class BookmarkFragment : Fragment() {

    private lateinit var binding: FragmentBookmarkBinding
    private lateinit var viewModel: BookmarkViewModel
    private val bookmarkAdapter: BookmarkAdapter by lazy {
        BookmarkAdapter(
            context = requireContext(),
            onBookmarkClick = { }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        val view = binding.root

        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvListSongket.layoutManager = linearLayoutManager
        binding.rvListSongket.adapter = bookmarkAdapter

        setupSearchView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireContext()))
            .get(BookmarkViewModel::class.java)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().supportFragmentManager.replaceFragment(
                    HomeFragment(),
                    R.id.frame_layout
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        viewModel.getSongket().observe(viewLifecycleOwner, Observer {
            bookmarkAdapter.submitList(it)
        })

        bookmarkAdapter.onBookmarkClick = { songket ->
            val moveDataUserIntent =
                Intent(requireContext(), DetailSongketActivity::class.java)
            moveDataUserIntent.putExtra(DetailSongketActivity.ID, songket.idfabric)
            requireContext().startActivity(moveDataUserIntent)
        }
    }

    private fun setupSearchView() {
        val searchView = binding.searchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { performSearch(it) }
                return false
            }
        })
    }

    private fun performSearch(query: String) {
        viewModel.searchSongket(query).observe(this@BookmarkFragment) { result ->
            when (result) {
                is Result.Loading -> {
                }
                is Result.Success -> {
                    val filteredList = result.data.filter { item ->
                        item.fabricname.contains(query, true) || item.origin.contains(query, true)
                    }

                    if (filteredList.isEmpty()) {
                        showEmptySearchNotification()
                        bookmarkAdapter.submitList(emptyList())
                    } else {
                        val songketEntities = filteredList.map { datasetItem ->
                            SongketEntity(
                                idfabric = datasetItem.idfabric,
                                fabricname = datasetItem.fabricname,
                                origin = datasetItem.origin,
                                imgUrl = datasetItem.imgUrl
                            )
                        }
                        bookmarkAdapter.submitList(songketEntities)
                    }
                }
                is Result.Error -> {
                    showToast(result.error)
                }
            }
        }
    }

    private fun showEmptySearchNotification() {
        Snackbar.make(
            binding.root,
            getString(R.string.empty_search_notification),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun FragmentManager.replaceFragment(fragment: Fragment, containerId: Int) {
        beginTransaction()
            .replace(containerId, fragment)
            .commit()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}