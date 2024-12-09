package com.md.kebunq.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.md.kebunq.R
import com.md.kebunq.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var predictionAdapter: PredictionsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "111"

        binding.swipeRefreshLayout.setOnRefreshListener {
            // Lakukan aksi refresh, misalnya panggil ulang API
            viewModel.getPredictionsByUserId(userId)

            // Matikan animasi loading setelah data selesai dimuat
            viewModel.predictionsHistory.observe(viewLifecycleOwner) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        setupViewModel()
        setupRecyclerViews()
        observeViewModel()
        if (userId != null) {
            viewModel.getPredictionsByUserId(userId)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.predictionsHistory.observe(viewLifecycleOwner) { predictions ->
            if (predictions.isNullOrEmpty()) {
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.rvHistoryAnalisis.visibility = View.GONE
            } else {
                binding.tvEmptyMessage.visibility = View.GONE
                binding.rvHistoryAnalisis.visibility = View.VISIBLE
                predictionAdapter.setList(predictions)
            }
        }




        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            if (errorMsg != null) {
                Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupViewModel() {
        viewModel = ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private fun setupRecyclerViews() {
        predictionAdapter = PredictionsAdapter { item ->
            // Navigasi menggunakan NavController
            val bundle = Bundle().apply {
                putString("PREDICTION_ID", item.predictionId)
            }
            findNavController().navigate(R.id.action_historyFragment_to_detailAnalisisFragment, bundle)
        }

        binding.rvHistoryAnalisis.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = predictionAdapter
            setHasFixedSize(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}