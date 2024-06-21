import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse
import com.capstonebangkit.birdwatch.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AnalyzeViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean>
        get() = _loading

    private val _predictResponse = MutableLiveData<PredictResponse?>()
    val predictResponse: LiveData<PredictResponse?>
        get() = _predictResponse

    fun uploadImage(imageFile: File) {
        _loading.value = true

        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "image",
            imageFile.name,
            requestImageFile
        )

        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.uploadImage(multipartBody)
                if (response.isSuccessful) {
                    _predictResponse.value = response.body()
                } else {
                    Log.e("UploadImage", "Gagal mengunggah gambar: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("UploadImage", "Exception: ${e.message}")
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetPredictResponse() {
        _predictResponse.value = null
    }
}
