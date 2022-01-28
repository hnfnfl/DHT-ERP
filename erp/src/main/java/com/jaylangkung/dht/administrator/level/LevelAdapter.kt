package com.jaylangkung.dht.administrator.level

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemLevelBinding

class LevelAdapter : RecyclerView.Adapter<LevelAdapter.LevelItemHolder>() {

    private var listData = ArrayList<LevelEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setLevelItem(levelItem: List<LevelEntity>?) {
        if (levelItem == null) return
        listData.clear()
        listData.addAll(levelItem)
        notifyItemRangeChanged(0, levelItem.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<LevelEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class LevelItemHolder(private val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(levelItem: LevelEntity) {
            with(binding) {
                tvLevel.text = levelItem.level
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelItemHolder {
        val itemLevelBinding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LevelItemHolder(itemLevelBinding)
    }

    override fun onBindViewHolder(holder: LevelItemHolder, position: Int) {
        val vendorItem = listData[position]
        holder.bind(vendorItem)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listData, position)
        }
    }

    override fun getItemCount(): Int = listData.size
}



