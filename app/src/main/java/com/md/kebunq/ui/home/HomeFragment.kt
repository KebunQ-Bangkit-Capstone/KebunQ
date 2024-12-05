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
import com.md.kebunq.R
import com.md.kebunq.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
