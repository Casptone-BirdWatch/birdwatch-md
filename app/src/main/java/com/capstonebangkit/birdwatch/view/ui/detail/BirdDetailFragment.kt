package com.capstonebangkit.birdwatch.view.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.databinding.FragmentBirdDetailBinding

class BirdDetailFragment : Fragment() {

    private var _binding: FragmentBirdDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBirdDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val args = arguments
        val genus = args?.getString("genus")
        val jenisBurung = args?.getString("jenisBurung")
        val imageUrl = args?.getString("imageUrl")
        val deskripsi = args?.getString("deskripsi")
        val famili = args?.getString("famili")

        binding.tvGenus.text = genus
        binding.tvBird.text = jenisBurung
        binding.tvDescription.text = deskripsi
        binding.tvFamili.text = famili
        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.baseline_image_search_24) // Gambar placeholder
            .error(R.drawable.baseline_image_search_24) // Gambar error
            .into(binding.ivBird)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
