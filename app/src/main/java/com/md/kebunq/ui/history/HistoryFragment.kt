package com.md.kebunq.ui.history

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.md.kebunq.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: PredictionsAdapter

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
//        val userId = FirebaseAuth.getInstance().currentUser?.uid
        setupViewModel()
        setupRecyclerViews()
        observeViewModel()
        viewModel.getPredictionsByUserId("1")
    }

    private fun observeViewModel() {
        viewModel.predictionsHistory.observe(viewLifecycleOwner){ predictions ->
            adapter.setList(predictions)
        }

        viewModel.isLoading.observe(viewLifecycleOwner){ isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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

    private fun setupRecyclerViews(){
        adapter = PredictionsAdapter { item ->
            val intent = Intent(requireContext(), DetailAnalisisFragment::class.java)
            intent.putExtra("PREDICTION_ID", item.predictionId)
            startActivity(intent)
        }

        binding.rvHistoryAnalisis.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}