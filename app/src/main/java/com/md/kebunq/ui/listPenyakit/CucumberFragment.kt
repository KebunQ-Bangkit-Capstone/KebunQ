package com.md.kebunq.ui.listPenyakit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.md.kebunq.R
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentCucumberBinding
import com.md.kebunq.viewmodel.PenyakitViewModelFactory

class CucumberFragment : Fragment() {

    private var _binding: FragmentCucumberBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListPenyakitViewModel
    private lateinit var adapter: PenyakitAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCucumberBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PenyakitAdapter { diseaseItem ->
            val navController = findNavController()
            val bundle = Bundle().apply {
                putParcelable("disease_item", diseaseItem)
                putString("plant_id", "0")
            }
            navController.navigate(R.id.action_cucumberFragment_to_detailPenyakitFragment, bundle)
        }

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {

        binding.rvPlants.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@CucumberFragment.adapter
        }
    }

    private fun setupViewModel() {
        val apiService = ApiConfig.getApiService()
        viewModel = ViewModelProvider(
            this,
            PenyakitViewModelFactory(apiService)
        )[ListPenyakitViewModel::class.java]

        viewModel.diseases.observe(viewLifecycleOwner) { diseases ->
            if (diseases.isEmpty()) {
                binding.tvEmptyMessage.visibility = View.VISIBLE
            } else {
                binding.tvEmptyMessage.visibility = View.GONE
                adapter.submitList(diseases)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.fetchDiseasesByPlant("0")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
