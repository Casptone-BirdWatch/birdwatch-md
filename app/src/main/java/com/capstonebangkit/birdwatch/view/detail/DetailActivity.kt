package com.capstonebangkit.birdwatch.view.detail

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse
import com.capstonebangkit.birdwatch.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var isBookmark = false
    private var isClicked = false

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        setupDetail()

        binding.btnBookmark.setOnClickListener {
            if (!isClicked) {
                isBookmark = !isBookmark
                setupBookmark()
                isClicked = true
            } else {
                showToast("Bookmark sudah ditambahkan")
            }
        }

        detailViewModel.toastMessage.observe(this) { message ->
            showToast(message)
        }
    }

    private fun setupDetail() {
        val response: PredictResponse? = intent.getParcelableExtra(EXTRA_DETAIL)
        response?.let { birdDetail ->
            binding.apply {
                tvGenus.text = birdDetail.genus
                tvBird.text = birdDetail.jenisBurung
                tvFamili.text = birdDetail.famili
                tvDescription.text = birdDetail.deskripsi
                Glide.with(this@DetailActivity).load(birdDetail.imageUrl).into(ivBird)
            }
        }
    }

    private fun setupBookmark() {
        val predictResponse: PredictResponse? = intent.getParcelableExtra(EXTRA_DETAIL)
        if (isBookmark) {
            binding.btnBookmark.setImageResource(R.drawable.ic_bookmark)
            predictResponse?.id?.let { predictionId ->
                detailViewModel.addBookmark(predictionId)
            }
        } else {
            binding.btnBookmark.setImageResource(R.drawable.ic_bookmark_border)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }
}