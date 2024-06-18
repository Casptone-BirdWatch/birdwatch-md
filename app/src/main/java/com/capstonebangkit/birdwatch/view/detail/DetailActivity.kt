package com.capstonebangkit.birdwatch.view.detail

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val jenisBurung = intent.getStringExtra("JENIS_BURUNG_KEY")
        val imageUrl = intent.getStringExtra("IMAGE_KEY")
        val genus = intent.getStringExtra("GENUS_KEY")
        val famili = intent.getStringExtra("FAMILI_KEY")
        val deskripsi = intent.getStringExtra("DESKRIPSI_KEY")

        binding.tvGenus.text = genus
        binding.tvBird.text = jenisBurung
        binding.tvFamili.text = famili
        binding.tvDescription.text = deskripsi
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.baseline_image_search_24) // Gambar placeholder
            .error(R.drawable.baseline_image_search_24) // Gambar error
            .into(binding.ivBird)
    }
}