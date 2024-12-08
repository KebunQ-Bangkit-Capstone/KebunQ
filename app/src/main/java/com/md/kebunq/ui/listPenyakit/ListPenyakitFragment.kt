package com.md.kebunq.ui.listPenyakit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.md.kebunq.R

class ListPenyakitFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_penyakit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Temukan RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.rv_plants)

        // Set RecyclerView dan adapter
        val plantAdapter = PlantAdapter { plantName ->
            when (plantName) {
                "Timun" -> {
                    val navController = findNavController()
                    navController.navigate(R.id.action_listPenyakitFragment_to_cucumberFragment)
                }
                "Anggur" -> {
                    val navController = findNavController()
                    navController.navigate(R.id.action_listPenyakitFragment_to_grapeFragment)
                }
                "Tomat" -> {
                    val navController = findNavController()
                    navController.navigate(R.id.action_listPenyakitFragment_to_tomatoFragment)
                }
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = plantAdapter
    }
}
