package com.capstonebangkit.birdwatch.view.detailbookmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.data.remote.response.BookmarkResponseItem
import com.capstonebangkit.birdwatch.databinding.ActivityDetailBookmarkBinding

class DetailBookmarkActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBookmarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookmarkBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupDetail()
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
    companion object {
        const val EXTRA_BOOKMARK = "extra_bookmark"
    }
}
