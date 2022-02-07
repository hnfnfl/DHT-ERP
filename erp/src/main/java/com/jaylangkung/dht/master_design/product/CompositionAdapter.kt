package com.jaylangkung.dht.master_design.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemProductCompositionBinding
import com.jaylangkung.dht.master_design.AdditionalEntity

class CompositionAdapter : RecyclerView.Adapter<CompositionAdapter.ItemHolder>() {

    private var list = ArrayList<AdditionalEntity>()

    fun setItem(item: List<AdditionalEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemProductCompositionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdditionalEntity) {
            with(binding) {
                tvProductDetailParameter.text = item.parameter
                tvProductDetailValue.text = item.value
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemProductCompositionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



