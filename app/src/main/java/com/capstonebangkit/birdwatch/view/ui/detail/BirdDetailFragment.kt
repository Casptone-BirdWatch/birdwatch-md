package com.capstonebangkit.birdwatch.view.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.R

class BirdDetailFragment : Fragment() {

    companion object {
        const val ARG_BIRD_NAME = "bird_name"
        const val ARG_BIRD_DESCRIPTION = "bird_description"
        const val ARG_BIRD_IMAGE_URL = "bird_image_url"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bird_detail, container, false)

        val birdName = view.findViewById<TextView>(R.id.bird_name)
        val birdDescription = view.findViewById<TextView>(R.id.bird_description)
        val birdImage = view.findViewById<ImageView>(R.id.bird_image)

        val name = arguments?.getString(ARG_BIRD_NAME)
        val description = arguments?.getString(ARG_BIRD_DESCRIPTION)
        val imageUrl = arguments?.getString(ARG_BIRD_IMAGE_URL)

        birdName.text = name
        birdDescription.text = description

        Glide.with(this)
            .load(imageUrl)
            .into(birdImage)

        return view
    }
}