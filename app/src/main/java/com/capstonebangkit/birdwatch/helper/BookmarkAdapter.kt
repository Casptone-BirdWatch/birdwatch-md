package com.capstonebangkit.birdwatch.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.databinding.ItemBookmarkBinding

class BookmarkAdapter(
    private var bookmarks: List<BookmarkResponseItem?>,
    private val onItemClick: (BookmarkResponseItem) -> Unit
) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {

    inner class BookmarkViewHolder(private val binding: ItemBookmarkBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: BookmarkResponseItem?) {
            bookmark?.let {
                binding.apply {
                    tvBird.text = bookmark.jenisBurung
                    Glide.with(itemView.context).load(bookmark.imageUrl).into(ivBird)
                }
                itemView.setOnClickListener {
                    onItemClick(bookmark)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookmarkViewHolder {
        val binding =
            ItemBookmarkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookmarkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookmarkViewHolder, position: Int) {
        holder.bind(bookmarks[position])
    }

    override fun getItemCount(): Int = bookmarks.size

    fun updateList(newBookmarks: List<BookmarkResponseItem?>) {
        bookmarks = newBookmarks
        notifyDataSetChanged()
    }
}
