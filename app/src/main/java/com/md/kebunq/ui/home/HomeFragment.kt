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

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referensi FloatingActionButton
        val fab: FloatingActionButton = view.findViewById(R.id.floating_action_button)

        // Set klik listener untuk navigasi
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_ambilGambarFragment)
        }
    }
}