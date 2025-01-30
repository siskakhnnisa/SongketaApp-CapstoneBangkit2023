package com.songketa.songket_recognition_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.songketa.songket_recognition_app.data.response.DatasetItem
import com.songketa.songket_recognition_app.databinding.SongketHomeItemBinding
import com.songketa.songket_recognition_app.ui.detailsongket.DetailSongketActivity

class HomeSongketAdapter(private val context: Context) : ListAdapter<DatasetItem, HomeSongketAdapter.MyViewHolder>(
    DIFF_CALLBACK
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = SongketHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val songket = getItem(position)
        holder.bind(songket)
        holder.itemView.setOnClickListener {
            val moveDataUserIntent = Intent(holder.itemView.context, DetailSongketActivity::class.java)
            moveDataUserIntent.putExtra(DetailSongketActivity.ID, songket.idfabric)
            holder.itemView.context.startActivity(moveDataUserIntent)
        }

    }
    class MyViewHolder(val binding: SongketHomeItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(songket: DatasetItem){
            binding.tvItemSongket.text = songket.fabricname
            binding.tvItemOrigin.text = songket.origin
            binding.tvItemDesc.text = songket.description
            Glide.with(binding.root.context).load(songket.imgUrl)
                .into(binding.ivItemSongketPicture)
        }
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DatasetItem>() {
            override fun areItemsTheSame(oldItem: DatasetItem, newItem: DatasetItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DatasetItem, newItem: DatasetItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}