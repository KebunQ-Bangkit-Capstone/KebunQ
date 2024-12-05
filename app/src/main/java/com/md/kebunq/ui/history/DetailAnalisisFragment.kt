package com.md.kebunq.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.md.kebunq.R
import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentDetailAnalisisBinding
import java.text.SimpleDateFormat
import java.util.Locale

class DetailAnalisisFragment : Fragment() {
    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_hasil_analisis,
            R.string.tab_saran_pengobatan
        )
    }

    private var _binding: FragmentDetailAnalisisBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAnalisisBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val viewModel: DetailAnalisisViewModel by viewModels {
        ViewModelFactory.getInstance(ApiConfig.getApiService())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val predictionId = arguments?.getString("PREDICTION_ID") ?: ""
        if (predictionId.isEmpty()) {
            Toast.makeText(context, "Prediction ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.getDetailPrediction(predictionId)
        viewModel.detailPrediction.observe(viewLifecycleOwner) { detail ->
            if (detail != null) {
                updateUI(detail)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
        val date = inputFormat.parse(dateString)

        val outputFormat = SimpleDateFormat("dd MMMM yyyy HH:mm", Locale("id", "ID"))
        return outputFormat.format(date)
    }
    private fun updateUI(detail: DetailPredictionResponse) {
        binding.tvHasilAnalisis.text = detail.diseaseName
        val createAt = formatDate(detail.createdAt)
        binding.tvCsTanggal.text = "${createAt} - CS ${detail.confidenceScore}%"
        binding.tvJenisTanaman.text = detail.plantName

        Glide.with(this)
            .load(detail.temporaryImageUrl)
            .into(binding.imgHasilAnalis)

        val args = Bundle().apply {
            putString("ANALYSIS", detail.analysis)
            putString("TREATMENT", detail.treatment)
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        sectionsPagerAdapter.setArguments(args)

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}