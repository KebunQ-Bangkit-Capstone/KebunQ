package com.md.kebunq.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.md.kebunq.R
import com.md.kebunq.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingViewModel = ViewModelProvider(requireActivity()).get(SettingsViewModel::class.java)

        // Observe the dark mode setting and update the switch state
        settingViewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            binding.toggleDarkmode.isChecked = isDarkMode
        }

        // Set up the switch listener to update dark mode setting
        binding.toggleDarkmode.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveDarkModeSetting(isChecked)
        }

        binding.btnAccountPriv.setOnClickListener {
            findNavController().navigate(R.id.accPrivacyFragment)
        }

        binding.btnTermsCons.setOnClickListener {
            findNavController().navigate(R.id.termsConsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}