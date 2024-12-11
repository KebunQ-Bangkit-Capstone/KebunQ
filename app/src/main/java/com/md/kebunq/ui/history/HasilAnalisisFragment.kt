package com.md.kebunq.ui.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.md.kebunq.R
import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentHasilAnalisisBinding
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HasilAnalisisFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHasilAnalisisBinding? = null
    private val binding get() = _binding!!
    private var detailPredictionResponse: DetailPredictionResponse? = null
    private var predictionId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHasilAnalisisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val analysis = arguments?.getString("ANALYSIS")
        binding.sectionHasilAnalisis.text = analysis

        predictionId = arguments?.getString("PREDICTION_ID")

        predictionId?.let {
            fetchDetailPrediction(predictionId!!)
        } ?: Toast.makeText(requireContext(), "ID prediksi tidak ditemukan.", Toast.LENGTH_SHORT).show()
        binding.btnArtikel.setOnClickListener(this)
    }

    private fun fetchDetailPrediction(predictionId: String) {
        lifecycleScope.launch {
            binding.btnArtikel.isEnabled = false // Disable tombol sementara
            try {
                // Panggil API
                val response = ApiConfig.getApiService().getDetailPrediction(predictionId)
                detailPredictionResponse = response
                binding.btnArtikel.isEnabled = true // Enable tombol jika sukses
                Toast.makeText(requireContext(), "Data berhasil dimuat", Toast.LENGTH_SHORT).show()
            } catch (e: HttpException) {
                when (e.code()) {
                    404 -> Toast.makeText(requireContext(), "Data tidak ditemukan. Periksa ID Anda.", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(requireContext(), "Kesalahan HTTP: ${e.code()}", Toast.LENGTH_SHORT).show()
                }
                Log.e("API Error", "HTTP ${e.code()}: ${e.response()?.errorBody()?.string()}")
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Kesalahan jaringan: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("API Error", "Error: ${e.message}")
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_artikel -> {
                val articleUrl = detailPredictionResponse?.article
                if (!articleUrl.isNullOrEmpty() && articleUrl.startsWith("http")) {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(articleUrl)
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "URL artikel tidak tersedia atau tidak valid", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


//    override fun onClick(v: View?) {
//        when(v?.id){
//            R.id.btn_artikel ->{
//                val articleUrl = detailPredictionResponse?.article
//                if (!articleUrl.isNullOrEmpty()){
//                    val intent = Intent(Intent.ACTION_VIEW).apply {
//                        data = Uri.parse(articleUrl)
//                    }
//                    v.context.startActivity(intent)
//                }else{
//                    Toast.makeText(v.context, "URL artikel tidak tersedia",Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    }
