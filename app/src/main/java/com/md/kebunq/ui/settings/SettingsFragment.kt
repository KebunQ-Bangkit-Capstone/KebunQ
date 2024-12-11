package com.md.kebunq.ui.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.md.kebunq.DataStoreManager
import com.md.kebunq.R
import com.md.kebunq.data.SettingsViewModelFactory
import com.md.kebunq.data.UserRepository
import com.md.kebunq.data.UserViewModel
import com.md.kebunq.data.UserViewModelFactory
import com.md.kebunq.data.retrofit.ApiConfig
import com.md.kebunq.databinding.FragmentSettingsBinding
import com.md.kebunq.ui.welcome.WelcomeActivity

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingViewModel: SettingsViewModel
    private lateinit var userViewModel: UserViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStoreManager = DataStoreManager.getInstance(requireContext())
        // Inisialisasi ViewModel
        settingViewModel = ViewModelProvider(
            requireActivity(),
            SettingsViewModelFactory(requireActivity().application, dataStoreManager)
        )[SettingsViewModel::class.java]

        val factory = UserViewModelFactory(
            UserRepository(ApiConfig.getApiService()),
            dataStoreManager
        )
        userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            userViewModel.getUserById(userId)
        }

        userViewModel.detailUser.observe(viewLifecycleOwner) { result ->
            result.onSuccess { response ->
                binding.tvUserName.text = response.name
                binding.ivUserEmail.text = response.email
            }.onFailure { exception ->
                binding.tvUserName.text = "Username"
                binding.ivUserEmail.text = "Email"
            }
        }
        // Observe the dark mode setting and update the switch state
        settingViewModel.isDarkMode.observe(viewLifecycleOwner) { isDarkMode ->
            binding.toggleDarkmode.isChecked = isDarkMode
        }

        binding.btnLogout.setOnClickListener {
            // Tampilkan dialog konfirmasi
            android.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    // Proses logout
                    userViewModel.signOut()

                    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)


                    // Hapus semua data di back stack
                    val intent = Intent(requireContext(), WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
                    Log.d("LogoutIntent", "Navigating to WelcomeActivity")

                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("No", null)
                .show()
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