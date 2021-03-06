package com.jaylangkung.dht.dht.work_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemBreakdownGoodsBinding

class BreakdownGoodsAdapter : RecyclerView.Adapter<BreakdownGoodsAdapter.ItemHolder>() {

    private var list = ArrayList<BreakdownDetailEntity>()

    fun setItem(item: List<BreakdownDetailEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemBreakdownGoodsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BreakdownDetailEntity) {
            with(binding) {
                tvGoodsNameCode.text = itemView.context.getString(R.string.product_name_category, item.barang, item.kode_barang)
                tvGoodsNeeds.text = itemView.context.getString(R.string.goods_needs_each, item.kebutuhan)
                tvGoodsQty.text = itemView.context.getString(R.string.inquiries_product_qty, item.jumlah)
                tvGoodsTotal.text = itemView.context.getString(R.string.breakdown_total, item.total)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBreakdownGoodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



