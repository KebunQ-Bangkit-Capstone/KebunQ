package com.md.kebunq.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.md.kebunq.databinding.FragmentHasilAnalisisBinding

class HasilAnalisisFragment : Fragment() {
    private var _binding: FragmentHasilAnalisisBinding? = null
    private val binding get() = _binding!!

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}