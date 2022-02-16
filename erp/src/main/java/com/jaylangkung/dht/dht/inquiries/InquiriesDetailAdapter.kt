package com.jaylangkung.dht.dht.inquiries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemInquiriesProductBinding
import okhttp3.internal.format
import java.text.DecimalFormat

class InquiriesDetailAdapter : RecyclerView.Adapter<InquiriesDetailAdapter.ItemHolder>() {

    private var list = ArrayList<InquiriesDetailEntity>()

    fun setItem(item: List<InquiriesDetailEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemInquiriesProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InquiriesDetailEntity) {
            with(binding) {
                val formatter = DecimalFormat("#,###")
                val subTotal = formatter.format(item.sub_total.toInt()).toString()
                val qty = formatter.format(item.jumlah.toInt()).toString()
                tvProductName.text = item.produk
                tvProductBasePrice.text = itemView.context.getString(R.string.inquiries_product_price, item.harga)
                tvProductQty.text = itemView.context.getString(R.string.inquiries_product_qty, qty)
                tvProductSubtotal.text = itemView.context.getString(R.string.inquiries_product_subtotal, subTotal)
                tvProductPacking.text = itemView.context.getString(R.string.inquiries_product_packing, item.packing)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemInquiriesProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



