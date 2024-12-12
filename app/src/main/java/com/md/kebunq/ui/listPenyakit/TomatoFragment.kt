package com.md.kebunq.ui.listPenyakit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.md.kebunq.R
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentTomatoBinding

class TomatoFragment : Fragment() {

    private var _binding: FragmentTomatoBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ListPenyakitViewModel
    private lateinit var adapter: PenyakitAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Tomat"
        }
        setHasOptionsMenu(true)
        _binding = FragmentTomatoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PenyakitAdapter { diseaseItem ->
            val navController = findNavController()
            val bundle = Bundle().apply {
                putParcelable("disease_item", diseaseItem)
                putString("plant_id", "2")
            }
            navController.navigate(R.id.action_tomatoFragment_to_detailPenyakitFragment, bundle)
        }

        setupRecyclerView()
        setupViewModel()
    }

    private fun setupRecyclerView() {

        binding.rvPlants.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TomatoFragment.adapter
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

        viewModel.fetchDiseasesByPlant("2")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp() // Navigasi kembali
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
