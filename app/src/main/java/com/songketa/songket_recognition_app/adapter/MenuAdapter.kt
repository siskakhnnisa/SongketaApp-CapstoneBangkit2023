package com.songketa.songket_recognition_app.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.songketa.songket_recognition_app.R

class MenuAdapter (
    private val context: Context?,
    private val names: ArrayList<String>,
    private val imageUrls: ArrayList<String>,
    private val itemClickListener: OnMenuItemClickListener
) : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private val TAG = "RecyclerViewAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.menu_home_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder: called.")
        Glide.with(context!!)
            .asBitmap()
            .load(imageUrls[position])
            .into(holder.image)
        holder.name.text = names[position]
        holder.image.setOnClickListener {
            itemClickListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.image_view)
        var name: TextView = itemView.findViewById(R.id.name)
    }

    interface OnMenuItemClickListener {
        fun onItemClick(position: Int)
    }


}
