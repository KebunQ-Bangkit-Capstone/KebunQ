package com.md.kebunq.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.md.kebunq.R
import com.md.kebunq.data.response.PredictionResponse
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentPredictionBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class PredictCucumberFragment : Fragment(R.layout.fragment_prediction) {

    private var _binding: FragmentPredictionBinding? = null
    private val binding get() = _binding!!

    private var currentImageUri: Uri? = null
    private var currentImageFile: File? = null

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK && currentImageUri != null) {
            binding.ivFotoDeteksi.setImageURI(currentImageUri)
            currentImageFile = uriToFile(currentImageUri!!, requireContext())
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                currentImageFile = uriToFile(uri, requireContext())
                binding.ivFotoDeteksi.setImageURI(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPredictionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupActions()
    }

    private fun setupActions() {
        binding.btnGaleri.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnAnalisisGambar.setOnClickListener { analyzeImage() }
    }

    private fun startCamera() {
        if (checkCameraPermission()) {
            currentImageUri = getImageUri(requireContext())
            if (this.currentImageUri != null) {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                    putExtra(MediaStore.EXTRA_OUTPUT, currentImageUri)
                }
                launcherIntentCamera.launch(intent)
            } else {
                Toast.makeText(requireContext(), "Gagal mengakses penyimpanan", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestCameraPermission()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Pilih Gambar")
        launcherIntentGallery.launch(chooser)
    }

    private fun analyzeImage() {
        if (currentImageFile == null) {
            Toast.makeText(requireContext(), "Silakan pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            return
        }

        val file = currentImageFile!!
        val userId = FirebaseAuth.getInstance().currentUser?.uid
//        val userId = "111"  // Ubah setelah fitur autentikasi
        val plantIndex = "0"  // Index tanaman timun

        // Menampilkan loading
        binding.progressBar.visibility = View.VISIBLE

        // Siapkan API Service
        val apiService = ApiConfig.getApiService()
        val imagePart = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody("multipart/form-data".toMediaType())
        )

        // Panggil API prediksi
        apiService.predict(userId.toString(), plantIndex, imagePart).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful && response.body() != null) {
                    val predictionResponse = response.body()!!

                    // Ambil data prediksi
                    val predictionId = predictionResponse.predictionId
                    val predictedPlantIndex = predictionResponse.plantIndex
                    val confidenceScore = predictionResponse.confidenceScore
                    val diseaseName = predictionResponse.diseaseName

                    // Debugging
                    println("Prediction ID: $predictionId")
                    println("Plant Index: $predictedPlantIndex")
                    println("Disease Name: $diseaseName")
                    println("Confidence Score: $confidenceScore")

                    // Navigasi ke DetailAnalisisFragment
                    navigateToDetailFragment(predictionId)
                } else {
                    Toast.makeText(requireContext(), "Gagal melakukan analisis: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(requireContext(), "Kesalahan: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun navigateToDetailFragment(predictionResult: String) {
        val bundle = Bundle().apply {
            putString("PREDICTION_ID", predictionResult)
        }
        findNavController().navigate(R.id.actionPredictCucumberFragmentToDetailAnalisisFragment, bundle)

    }

    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
                Toast.makeText(requireContext(), "Izin kamera diperlukan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
