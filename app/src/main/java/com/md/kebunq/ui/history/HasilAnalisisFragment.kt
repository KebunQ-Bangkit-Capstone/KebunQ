package com.md.kebunq.ui.history

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.md.kebunq.R
import com.md.kebunq.data.response.DetailPredictionResponse
import com.md.kebunq.databinding.FragmentHasilAnalisisBinding

class HasilAnalisisFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentHasilAnalisisBinding? = null
    private val binding get() = _binding!!
    private val detailPredictionResponse: DetailPredictionResponse? = null

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


    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_artikel ->{
                val articleUrl = detailPredictionResponse?.article
                if (!articleUrl.isNullOrEmpty()){
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(articleUrl)
                    }
                    v.context.startActivity(intent)
                }else{
                    Toast.makeText(v.context, "URL artikel tidak tersedia",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    }
