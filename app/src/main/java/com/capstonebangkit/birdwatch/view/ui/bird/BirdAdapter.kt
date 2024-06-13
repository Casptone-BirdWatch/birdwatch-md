package com.capstonebangkit.birdwatch.view.ui.bird

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstonebangkit.birdwatch.R
import com.capstonebangkit.birdwatch.data.remote.response.PredictResponse

class BirdAdapter(private var birdList: MutableList<PredictResponse>) :
    RecyclerView.Adapter<BirdAdapter.BirdViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(items: List<PredictResponse>) {
        birdList.clear()
        birdList.addAll(items)
        notifyDataSetChanged()
    }

    inner class BirdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val birdImage: ImageView = itemView.findViewById(R.id.bird_image)
        val birdName: TextView = itemView.findViewById(R.id.bird_name)
        val birdDescription: TextView = itemView.findViewById(R.id.bird_description)

        fun bind(bird: PredictResponse) {
            Glide.with(itemView.context)
                .load(bird.imageUrl)
                .into(birdImage)
            birdName.text = bird.jenisBurung
            birdDescription.text = bird.deskripsi

            itemView.setOnClickListener {
                onItemClickCallback?.onItemClicked(bird)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BirdViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bird, parent, false)
        return BirdViewHolder(view)
    }

    override fun getItemCount(): Int = birdList.size

    override fun onBindViewHolder(holder: BirdViewHolder, position: Int) {
        holder.bind(birdList[position])
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: PredictResponse)
    }
}
