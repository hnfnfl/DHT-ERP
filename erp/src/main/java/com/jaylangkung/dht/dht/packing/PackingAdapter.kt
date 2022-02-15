package com.jaylangkung.dht.dht.packing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemInquiriesProcessBinding
import com.jaylangkung.dht.databinding.ItemPackingBinding

class PackingAdapter : RecyclerView.Adapter<PackingAdapter.ItemHolder>() {

    private var list = ArrayList<PackingEntity>()

    fun setItem(item: List<PackingEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemPackingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PackingEntity) {
            with(binding) {
                tvPackingKode.text = item.code
                tvPackingName.text = item.packing
                tvPackingMustPrint.text = item.harus_dicetak
                tvPackingAlreadyPrint.text = item.sudah_dicetak
                tvPackingScanned.text = item.sudah_discan
                tvPackingLoadContainer.text = item.sudah_masuk_kontainer
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemPackingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



