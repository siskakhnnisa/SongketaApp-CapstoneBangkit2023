package com.songketa.songket_recognition_app.ui.detailsongket

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.songketa.songket_recognition_app.MainActivity
import com.songketa.songket_recognition_app.R
import com.songketa.songket_recognition_app.databinding.ActivityDetailSongketBinding
import com.songketa.songket_recognition_app.ui.ViewModelFactory
import com.songketa.songket_recognition_app.data.Result
import com.songketa.songket_recognition_app.data.database.SongketEntity
import com.songketa.songket_recognition_app.data.response.DetailSongketResponse

class DetailSongketActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailSongketBinding
    private val viewModel by viewModels<DetailSongketViewModel>(){
        ViewModelFactory.getInstance(applicationContext)
    }

    private var isFavorite: Boolean = false
    private lateinit var name: String
    private lateinit var avtimg:String

    private var id: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSongketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        name = intent.getStringExtra(USER_NAME).toString()
        avtimg = intent.getStringExtra(avatarUrl).toString()

        id = intent.getStringExtra(ID)

        viewModel.getDetailStory(id.toString()).observe(this){item ->
            if(item != null){
                when(item){
                    is Result.Loading ->{
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        val detailStory = item.data
                        setDetailStory(detailStory)

                        viewModel.isFavorite(item.data.datasetItem.idfabric).observe(this){
                            if (it == null){
                                isFavorite=false
                                binding.btnBookmark.setImageDrawable(
                                    ContextCompat.getDrawable(binding.btnBookmark.context,
                                        R.drawable.ic_bookmark
                                    )
                                )

                            }else{
                                isFavorite=true
                                binding.btnBookmark.setImageDrawable(
                                    ContextCompat.getDrawable(binding.btnBookmark.context,
                                        R.drawable.ic_bookmark_red)
                                )
                            }
                        }
                        binding.btnBookmark.setOnClickListener {
                            val favSongket = SongketEntity(
                                item.data.datasetItem.idfabric,
                                item.data.datasetItem.fabricname,
                                item.data.datasetItem.imgUrl,
                                item.data.datasetItem.origin,
                            )
                            if (isFavorite) {
                                viewModel.deleteFavorite(favSongket)
                                Toast.makeText(this, getString(R.string.deleted), Toast.LENGTH_SHORT).show()
                            } else {
                                viewModel.saveFavorite(favSongket)
                                Toast.makeText(this, getString(R.string.favorite), Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showToast(item.error)
                    }
                }
            }
        }

        binding.homebutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setDetailStory(detailstory: DetailSongketResponse){
        binding.ivSongket.visibility = View.VISIBLE
        binding.apply {
            Glide.with(ivSongket.context)
                .load(detailstory.datasetItem.imgUrl)
                .into(ivSongket)
        }

        binding.tvSongket.visibility = View.VISIBLE
        binding.tvSongket.text = detailstory.datasetItem.fabricname
        binding.tvOrigin.visibility = View.VISIBLE
        binding.tvOrigin.text = detailstory.datasetItem.origin
        binding.tvMotif.visibility = View.VISIBLE
        binding.tvMotif.text = detailstory.datasetItem.pattern
        binding.tvDesc.visibility = View.VISIBLE
        binding.tvDesc.text = detailstory.datasetItem.description
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val ID = ""
        const val USER_NAME = ""
        const val avatarUrl = "EXTRA_AVATAR"
    }

}