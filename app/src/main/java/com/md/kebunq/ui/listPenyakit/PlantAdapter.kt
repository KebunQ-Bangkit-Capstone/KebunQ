package com.md.kebunq.ui.listPenyakit

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.md.kebunq.R

class PlantAdapter(private val onItemClickListener: (String) -> Unit) : RecyclerView.Adapter<PlantAdapter.ViewHolder>() {

    private val plantList = listOf(
        Plant("Timun", R.drawable.cucumber_image),
        Plant("Anggur", R.drawable.grape_image),
        Plant("Tomat", R.drawable.tomato_image)
    )

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val plantImage: ImageView = view.findViewById(R.id.iv_plant)
        val plantName: TextView = view.findViewById(R.id.tv_nama_penyakit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list_penyakit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plant = plantList[position]
        holder.plantImage.setImageResource(plant.imageResId)
        holder.plantName.text = plant.name

        // Set onClickListener untuk setiap item
        holder.itemView.setOnClickListener {
            onItemClickListener(plant.name)
        }
    }

    override fun getItemCount(): Int {
        return plantList.size
    }
}

data class Plant(
    val name: String,
    val imageResId: Int
)
