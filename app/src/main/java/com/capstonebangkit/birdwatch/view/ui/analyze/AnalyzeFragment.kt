package com.capstonebangkit.birdwatch.view.ui.analyze

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import com.capstonebangkit.birdwatch.databinding.FragmentAnalyzeBinding
import com.capstonebangkit.birdwatch.helper.getImageUri
import com.capstonebangkit.birdwatch.helper.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class AnalyzeFragment : Fragment() {

    private var _binding: FragmentAnalyzeBinding? = null
    private var currentImageUri: Uri? = null

    // onDestroyView.
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
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
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
                "photo",
                imageFile.name,
                requestImageFile
            )

            val apiService = ApiConfig.getApiService()
            val token = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjMzMDUxMThiZTBmNTZkYzA4NGE0NmExN2RiNzU1NjVkNzY4YmE2ZmUiLCJ0eXAiOiJKV1QifQ.eyJuYW1lIjoiTmFiaWlsYSBXaWR5YSBLaG9pcnVuaXNzYSIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NJdHhTNHRtWkEyUk1KWnJxLXpjS19WWERGZXVxbGZHNVgxN1RxZ054T21QaTIyNGc9czk2LWMiLCJpc3MiOiJodHRwczovL3NlY3VyZXRva2VuLmdvb2dsZS5jb20vY2Fwc3RvbmUtYmlyZHdhdGNoIiwiYXVkIjoiY2Fwc3RvbmUtYmlyZHdhdGNoIiwiYXV0aF90aW1lIjoxNzE3NjcxNDIyLCJ1c2VyX2lkIjoidGxnVG9GNXdUZ2RKWktwVTZZSnlUNm5SdEhXMiIsInN1YiI6InRsZ1RvRjV3VGdkSlpLcFU2WUp5VDZuUnRIVzIiLCJpYXQiOjE3MTc2NzE0MjUsImV4cCI6MTcxNzY3NTAyNSwiZW1haWwiOiJuYWJpaWxhd2lkeWE3NzAyQGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJmaXJlYmFzZSI6eyJpZGVudGl0aWVzIjp7Imdvb2dsZS5jb20iOlsiMTE0MTczNDAxMTkzMTk1ODI4MjAwIl0sImVtYWlsIjpbIm5hYmlpbGF3aWR5YTc3MDJAZ21haWwuY29tIl19LCJzaWduX2luX3Byb3ZpZGVyIjoiZ29vZ2xlLmNvbSJ9fQ.Ap5YTNq07PKWCGGa6x8d8JsQIGkh9zJjG697Ykbxjl2qYjER0TELKwqPgVTlQq5CoP8YlnPfkECQXDDcT4K4X1Tx05ojMZE7SQprIxYA0UV1JvBwi-LQMgC-AZUJKxnp09aJCXGfKYxSQZpzNrn_hD1Pd-wM8zNA02dsUCh8osif-yha1fqlC2f4dN6Ki5FPlKlRZWo0CCsDdZ7RlPKRBKaQEFXm7zq19p_5jbh1a6buExUQN-mNC5V0yjTBSo2T6k4Up03pr8I0BZSUIK83nwGU1QwhJv8DaNwlGfOVw-l-LGgvDtda1hkI0EcpZQGzlL--ARmeblx0GybLU5Dg"
            lifecycleScope.launch {
                try {
                    val response = apiService.uploadImage(token, multipartBody)
                    if (response.isSuccessful) {
                        val predictResponse = response.body()
                        Log.d("Response", "Genus: ${predictResponse?.genus}, JenisBurung: ${predictResponse?.jenisBurung}")
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
