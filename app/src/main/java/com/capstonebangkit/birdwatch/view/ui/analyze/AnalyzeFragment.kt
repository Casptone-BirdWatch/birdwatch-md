package com.capstonebangkit.birdwatch.view.ui.analyze

import AnalyzeViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.databinding.FragmentAnalyzeBinding
import com.capstonebangkit.birdwatch.helper.getImageUri
import com.capstonebangkit.birdwatch.helper.uriToFile
import com.capstonebangkit.birdwatch.view.detail.DetailActivity

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private val binding get() = _binding!!

    private val analyzeViewModel: AnalyzeViewModel by viewModels()
    private var currentImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAnalyzeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.apply {
            cameraButton.setOnClickListener { startCamera() }
            intentButton.setOnClickListener { startGallery() }
            analyzeButton.setOnClickListener { uploadImage() }
        }
        observePrediction()
        return root
    }

    private fun observePrediction() {
        analyzeViewModel.predictResponse.observe(viewLifecycleOwner){predictResponse ->
            predictResponse?.let {
                val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                    putExtra(DetailActivity.EXTRA_DETAIL, predictResponse)
                }
                startActivity(intent)
            }
        }
        analyzeViewModel.loading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun startGallery() {
        launcherGallery.launch("image/*")
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(requireContext())
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, requireContext())
            analyzeViewModel.uploadImage(imageFile)
        } ?: showToast(getString(R.string.empty_image_warning))
    }


    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
