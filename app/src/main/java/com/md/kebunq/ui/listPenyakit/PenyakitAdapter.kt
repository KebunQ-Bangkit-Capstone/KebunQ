package com.md.kebunq.ui.listPenyakit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.md.kebunq.data.response.DiseasesItem
import com.md.kebunq.databinding.ItemListPenyakitBinding

class PenyakitAdapter(
    private val onItemClickListener: (DiseasesItem) -> Unit
) : ListAdapter<DiseasesItem, PenyakitAdapter.PenyakitViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PenyakitViewHolder {
        val binding = ItemListPenyakitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PenyakitViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PenyakitViewHolder, position: Int) {
        val disease = getItem(position)
        holder.bind(disease)
        holder.itemView.setOnClickListener {
            onItemClickListener(disease)
        }
    }

    class PenyakitViewHolder(private val binding: ItemListPenyakitBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(disease: DiseasesItem) {
            binding.tvNamaPenyakit.text = disease.diseaseName // Tampilkan deskripsi atau nama penyakit
            Glide.with(binding.ivPlant.context)
                .load(disease.temporaryImageUrl) // Ganti dengan URL gambar
                .into(binding.ivPlant)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiseasesItem>() {
            override fun areItemsTheSame(oldItem: DiseasesItem, newItem: DiseasesItem): Boolean {
                return oldItem.diseaseId == newItem.diseaseId
            }

            override fun areContentsTheSame(oldItem: DiseasesItem, newItem: DiseasesItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
