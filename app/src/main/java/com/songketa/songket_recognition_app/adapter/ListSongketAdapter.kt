package com.songketa.songket_recognition_app.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.songketa.songket_recognition_app.data.database.SongketEntity
import com.songketa.songket_recognition_app.databinding.SongketListItemBinding
import com.songketa.songket_recognition_app.ui.detailsongket.DetailSongketActivity

class ListSongketAdapter(
    private val context: Context,
) : ListAdapter<SongketEntity, ListSongketAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            SongketListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

    class MyViewHolder(val binding: SongketListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(songket: SongketEntity) {
            binding.tvSongketName.text = songket.fabricname
            binding.tvAsalSogket.text = songket.origin
            Glide.with(binding.root.context).load(songket.imgUrl)
                .into(binding.ivSongketImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SongketEntity>() {
            override fun areItemsTheSame(oldItem: SongketEntity, newItem: SongketEntity): Boolean {
                return oldItem.idfabric == newItem.idfabric
            }

            override fun areContentsTheSame(
                oldItem: SongketEntity,
                newItem: SongketEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

