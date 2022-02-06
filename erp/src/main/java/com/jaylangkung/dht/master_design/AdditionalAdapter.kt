package com.jaylangkung.dht.master_design

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemAdditionalBinding

class AdditionalAdapter : RecyclerView.Adapter<AdditionalAdapter.ItemHolder>() {

    private var list = ArrayList<AdditionalEntity>()

    fun setItem(item: List<AdditionalEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemAdditionalBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdditionalEntity) {
            with(binding) {
                tvAdditional.text = itemView.context.getString(
                    R.string.customer_additional_holder, item.parameter, item.value
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemAdditionalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



