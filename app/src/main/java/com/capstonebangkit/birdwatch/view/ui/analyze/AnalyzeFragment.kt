package com.capstonebangkit.birdwatch.view.ui.analyze

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
import androidx.lifecycle.lifecycleScope
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import com.capstonebangkit.birdwatch.databinding.FragmentAnalyzeBinding
import com.capstonebangkit.birdwatch.helper.getImageUri
import com.capstonebangkit.birdwatch.helper.uriToFile
import com.capstonebangkit.birdwatch.view.detail.DetailActivity
import com.capstonebangkit.birdwatch.view.ui.detail.BirdDetailFragment
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private var currentImageUri: Uri? = null

    private val binding get() = _binding!!

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
        return root
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
            Log.d("Image Classification File", "showImage: ${imageFile.path}")
            showLoading(true)

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val apiService = ApiConfig.getApiService()
                    val response = apiService.uploadImage(multipartBody)
                    if (response.isSuccessful) {
                        val predictResponse = response.body()
                        Log.d("Response", "Genus: ${predictResponse?.genus}, JenisBurung: ${predictResponse?.jenisBurung}")

                        val intent = Intent(requireContext(), DetailActivity::class.java)
                        intent.putExtra("JENIS_BURUNG_KEY", predictResponse?.jenisBurung)
                        intent.putExtra("IMAGE_KEY", uri.toString())
                        intent.putExtra("GENUS_KEY", predictResponse?.genus)
                        intent.putExtra("FAMILI_KEY", predictResponse?.famili)
                        intent.putExtra("DESKRIPSI_KEY", predictResponse?.deskripsi)
                        startActivity(intent)

                    } else {
                        Log.e("UploadImage", "Gagal mengunggah gambar: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("UploadImage", "Exception: ${e.message}")
                } finally {
                    showLoading(false)
                }
            }
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
