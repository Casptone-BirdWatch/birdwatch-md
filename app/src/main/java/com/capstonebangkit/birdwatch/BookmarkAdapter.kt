package com.capstonebangkit.birdwatch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.databinding.ItemBookmarkBinding

class BookmarkAdapter(private val bookmarks: List<BookmarkResponseItem?>) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: BookmarkResponseItem?) {
            bookmark?.let {
                binding.apply {
                    tvGenus.text = bookmark.genus
                    tvBird.text = bookmark.jenisBurung
                    tvFamili.text = bookmark.famili
                    Glide.with(itemView.context).load(bookmark.imageUrl).into(ivBird)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding = ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarks[position])
    }

    override fun getItemCount(): Int = bookmarks.size
}
