package com.md.kebunq.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.md.kebunq.R
import com.md.kebunq.data.UserRepository
import com.md.kebunq.data.UserViewModel
import com.md.kebunq.data.UserViewModelFactory
import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate layout dengan ViewBinding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val factory = UserViewModelFactory(UserRepository(ApiConfig.getApiService()))
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            userViewModel.getUserById(userId)
        }
        userViewModel.detailUser.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                val selamatDatang = "Selamat Datang, ${response.name}"
                binding.selamatDatang.text = selamatDatang
            }.onFailure { exception ->
                binding.selamatDatang.text = "Selamat Datang!"
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


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Hindari memory leaks
    }
}
