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
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.data.retrofit.ApiService
import com.md.kebunq.databinding.FragmentDetailAnalisisBinding

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

//        val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
//        sectionsPagerAdapter.setArguments(args)
//        val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
//        viewPager.adapter = sectionsPagerAdapter
//        val tabs: TabLayout = view.findViewById(R.id.tabs)
//        TabLayoutMediator(tabs, viewPager) { tab, position ->
//            tab.text = resources.getString(TAB_TITLES[position])
//        }.attach()

        val predictionId = arguments?.getString("PREDICTION_ID") ?: ""
        viewModel.getDetailPrediction(predictionId)
        viewModel.detailPrediction.observe(viewLifecycleOwner){ detail ->
            val args = Bundle().apply {
                putString("ANALYSIS", detail.analysis)
                putString("TREATMENT", detail.treatment)
            }
            val sectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
            sectionsPagerAdapter.setArguments(args)

            val viewPager: ViewPager2 = view.findViewById(R.id.view_pager)
            viewPager.adapter = sectionsPagerAdapter

            val tabs: TabLayout = view.findViewById(R.id.tabs)
            TabLayoutMediator(tabs, viewPager) { tab, position ->
                tab.text = resources.getString(TAB_TITLES[position])
            }.attach()

            binding.tvHasilAnalisis.text = detail.analysis
            binding.tvCsTanggal.text = detail.createdAt
            binding.tvJenisTanaman.text = detail.plantIndex.toString()

            Glide.with(this)
                .load(detail.temporaryImageUrl)
                .into(binding.imgHasilAnalis)

        }

//        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
//            binding.progressBar.isVisible = isLoading
//        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}