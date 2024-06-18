package com.capstonebangkit.birdwatch.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse
import com.capstonebangkit.birdwatch.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        setupDetail()
    }

    private fun setupDetail() {
        val response: PredictResponse? = intent.getParcelableExtra(EXTRA_DETAIL)
        response?.let { birdDetail ->
            binding.apply {
                tvGenus.text = birdDetail.genus
                tvBird.text = birdDetail.jenisBurung
                tvFamili.text = birdDetail.famili
                tvDescription.text = birdDetail.deskripsi
                Glide.with(this@DetailActivity)
                    .load(birdDetail.imageUrl)
                    .into(ivBird)
            }
        }
    }
    companion object{
        const val EXTRA_DETAIL = "extra_detail"
    }
}