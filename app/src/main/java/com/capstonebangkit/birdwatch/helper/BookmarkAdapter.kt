package com.capstonebangkit.birdwatch.helper

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.databinding.ItemBookmarkBinding
import com.capstonebangkit.birdwatch.view.detail.DetailActivity
import com.capstonebangkit.birdwatch.view.detailbookmark.DetailBookmarkActivity

class BookmarkAdapter(private var bookmarks: List<BookmarkResponseItem?>) : RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>() {
    inner class BookmarkViewHolder(private val binding: ItemBookmarkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookmark: BookmarkResponseItem?) {
            bookmark?.let {
                binding.apply {
                    tvBird.text = bookmark.jenisBurung
                    Glide.with(itemView.context).load(bookmark.imageUrl).into(ivBird)
                }
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailBookmarkActivity::class.java).apply {
                    putExtra(DetailBookmarkActivity.EXTRA_BOOKMARK, bookmark)
                }
                itemView.context.startActivity(intent)
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

    fun updateList(newBookmarks: List<BookmarkResponseItem?>) {
        bookmarks = newBookmarks
        notifyDataSetChanged()
    }
}
