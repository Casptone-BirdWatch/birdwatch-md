package com.capstonebangkit.birdwatch.view.detailbookmark

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.databinding.ActivityDetailBookmarkBinding
import kotlinx.coroutines.launch

class DetailBookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupDetail()
        setupDeleteButton()
    }

    private fun setupDetail() {
        val bookmark: BookmarkResponseItem? = intent.getParcelableExtra(EXTRA_BOOKMARK)
        bookmark?.let { birdDetail ->
            binding.apply {
                tvGenus.text = birdDetail.genus
                tvBird.text = birdDetail.jenisBurung
                tvFamili.text = birdDetail.famili
                tvDescription.text = birdDetail.deskripsi
                Glide.with(this@DetailBookmarkActivity).load(birdDetail.imageUrl).into(ivBird)
            }
        }
    }

    private fun setupDeleteButton() {
        binding.btnDeleteBookmark.setOnClickListener {
            val bookmark: BookmarkResponseItem? = intent.getParcelableExtra(EXTRA_BOOKMARK)
            bookmark?.id?.let { bookmarkId ->
                deleteBookmark(bookmarkId)
            }
        }
    }

    private fun deleteBookmark(bookmarkId: String) {
        lifecycleScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.deleteBookmark(bookmarkId)
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DetailBookmarkActivity,
                        "Bookmark berhasil dihapus",
                        Toast.LENGTH_SHORT
                    ).show()
                    val resultIntent = Intent().apply {
                        putExtra(EXTRA_BOOKMARK_ID, bookmarkId)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                } else {
                    Toast.makeText(
                        this@DetailBookmarkActivity,
                        "Gagal menghapus bookmark: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@DetailBookmarkActivity,
                    "Exception: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    companion object {
        const val EXTRA_BOOKMARK = "extra_bookmark"
        const val EXTRA_BOOKMARK_ID = "extra_bookmark_id"
    }
}
