package com.md.kebunq.ui.home

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.md.kebunq.R
import com.md.kebunq.data.response.PredictionsItem
import com.md.kebunq.databinding.FragmentHomeBinding
import com.md.kebunq.databinding.ItemHistoryBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "111"
        viewModel.getLatestPredictionsByUserId(userId)

        // Inflate layout dengan ViewBinding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: "111"
        viewModel.getLatestPredictionsByUserId(userId)

        // Menginisialisasi SwipeRefreshLayout
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Lakukan aksi refresh, misalnya panggil ulang API
            viewModel.getLatestPredictionsByUserId(userId)

            // Matikan animasi loading setelah data selesai dimuat
            viewModel.predictionsHistory.observe(viewLifecycleOwner) {
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }

        // Implementasi aksi untuk FloatingActionButton
        binding.btnScanTimun.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_predictCucumberFragment)
        }

        binding.btnScanAnggur.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_predictGrapeFragment)
        }

        binding.btnScanTomat.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_predictTomatoFragment)
        }

        setupViewModel()
        observeViewModel()
        setupRecyclerView()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.rvHistoryAnalisis.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.latestPredictions.observe(viewLifecycleOwner) { predictions ->
            Log.d("HomeFragment", "Received predictions: ${predictions?.size}")
            if (predictions.isNullOrEmpty()) {
                Log.d("HomeFragment", "Predictions is empty")
                binding.tvEmptyMessage.visibility = View.VISIBLE
                binding.rvHistoryAnalisis.visibility = View.GONE
            } else {
                Log.d("HomeFragment", "Setting up adapter with ${predictions.size} items")
                binding.tvEmptyMessage.visibility = View.GONE
                binding.rvHistoryAnalisis.visibility = View.VISIBLE
                binding.rvHistoryAnalisis.adapter = QuickAccessAdapter(predictions)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hindari memory leaks
    }

    inner class QuickAccessAdapter(private val predictions: List<PredictionsItem>) : RecyclerView.Adapter<QuickAccessAdapter.ViewHolder>() {

        inner class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
            @RequiresApi(Build.VERSION_CODES.O)
            fun bind(item: PredictionsItem) {
                with(binding) {
                    tvItemHasilPrediksi.text = item.diseaseName

                    val createAt = item.createdAt
                    val date = ZonedDateTime.parse(createAt, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                    val formattedDate = date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale("id", "ID")))
                    tvItemTanggal.text = formattedDate

                    Glide.with(itemView.context)
                        .load(item.temporaryImageUrl)
                        .into(imgItemHistory)
                }

                itemView.setOnClickListener {
                    // Membuat Bundle untuk mengirim data ke DetailAnalisisFragment
                    val bundle = Bundle().apply {
                        putString("PREDICTION_ID", item.predictionId)  // Passing predictionId
                    }
                    // Navigasi ke DetailAnalisisFragment dengan bundle data
                    findNavController().navigate(R.id.action_homeFragment_to_detailAnalisisFragment, bundle)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ViewHolder(binding)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(predictions[position])
        }

        override fun getItemCount(): Int = predictions.size
    }
}
