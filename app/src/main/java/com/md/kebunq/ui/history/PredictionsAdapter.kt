package com.md.kebunq.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.md.kebunq.data.response.PredictionsItem
import com.md.kebunq.databinding.ItemHistoryBinding

class PredictionsAdapter(
    private val onItemClickListener: (PredictionsItem) -> Unit
) : RecyclerView.Adapter<PredictionsAdapter.PredictionViewHolder>(){

    class PredictionViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: PredictionsItem){
            binding.apply {
                tvItemHasilPrediksi.text = item.analysis
                tvItemTanggal.text = item.createdAt
                Glide.with(itemView.context)
                    .load(item.temporaryImageUrl)
                    .into(imgItemHistory)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PredictionViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PredictionViewHolder(binding)
    }

    private val predictions = ArrayList<PredictionsItem>()
    fun setList(items: List<PredictionsItem>) {
        predictions.clear()
        predictions.addAll(items)
    }

    override fun onBindViewHolder(holder: PredictionViewHolder, position: Int) {
        val prediction = predictions[position]
        holder.bind(prediction)
        holder.itemView.setOnClickListener{
            onItemClickListener(prediction)
        }

    }

    override fun getItemCount(): Int = predictions.size
}