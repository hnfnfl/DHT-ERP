package com.jaylangkung.dht.dht.work_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemBreakdownPackingBinding

class BreakdownPackingAdapter : RecyclerView.Adapter<BreakdownPackingAdapter.ItemHolder>() {

    private var list = ArrayList<BreakdownDetailEntity>()

    fun setItem(item: List<BreakdownDetailEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemBreakdownPackingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BreakdownDetailEntity) {
            with(binding) {
                tvPackingNameCode.text = itemView.context.getString(R.string.product_name_category, item.packing, item.kode_packing)
                tvPackingLoad.text = itemView.context.getString(R.string.packing_load, item.muat)
                tvPackingQty.text = itemView.context.getString(R.string.inquiries_product_qty, item.jumlah)
                tvPackingTotal.text = itemView.context.getString(R.string.breakdown_total, item.total)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBreakdownPackingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



