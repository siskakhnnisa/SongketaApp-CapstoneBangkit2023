package com.songketa.songket_recognition_app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.songketa.songket_recognition_app.data.database.SongketEntity
import com.songketa.songket_recognition_app.databinding.SongketListItemBinding

class BookmarkAdapter(
    private val context: Context,
    var onBookmarkClick: (SongketEntity) -> Unit
) : ListAdapter<SongketEntity, BookmarkAdapter.MyViewHolder>(UserEntityDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            SongketListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onBookmarkClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val songket = getItem(position)
        holder.bind(songket)
    }

    class MyViewHolder(
        val binding: SongketListItemBinding,
        private val onBookmarkClick: (SongketEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(songket: SongketEntity) {
            binding.tvSongketName.text = songket.fabricname
            binding.tvAsalSogket.text = songket.origin
            Glide.with(binding.root.context).load(songket.imgUrl)
                .into(binding.ivSongketImage)

            itemView.setOnClickListener {
                onBookmarkClick.invoke(songket)
            }
        }
    }

    object UserEntityDiffCallback : DiffUtil.ItemCallback<SongketEntity>() {
        override fun areItemsTheSame(oldItem: SongketEntity, newItem: SongketEntity): Boolean {
            return oldItem.idfabric == newItem.idfabric
        }

        override fun areContentsTheSame(oldItem: SongketEntity, newItem: SongketEntity): Boolean {
            return oldItem == newItem
        }
    }
}
