package com.jaylangkung.dht.dht.work_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemBreakdownBinding

class BreakdownAdapter(product: String, code: String) : RecyclerView.Adapter<BreakdownAdapter.ItemHolder>() {

    private var list = ArrayList<BreakdownEntity>()

    fun setItem(item: List<BreakdownEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemBreakdownBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BreakdownEntity) {
            with(binding) {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBreakdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



