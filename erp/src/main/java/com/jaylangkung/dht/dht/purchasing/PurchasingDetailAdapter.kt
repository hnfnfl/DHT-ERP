package com.jaylangkung.dht.dht.purchasing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemInquiriesProductBinding
import com.jaylangkung.dht.dht.inquiries.InquiriesDetailEntity
import java.text.DecimalFormat

class PurchasingDetailAdapter : RecyclerView.Adapter<PurchasingDetailAdapter.ItemHolder>() {

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
                tvProductName.text = item.barang
                tvProductBasePrice.text = itemView.context.getString(R.string.inquiries_product_price, item.harga)
                tvProductQty.text = itemView.context.getString(R.string.inquiries_product_qty, item.jumlah)
                tvProductSubtotal.text = itemView.context.getString(R.string.inquiries_product_subtotal, item.sub_total)
                tvProductPacking.visibility = View.GONE
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



