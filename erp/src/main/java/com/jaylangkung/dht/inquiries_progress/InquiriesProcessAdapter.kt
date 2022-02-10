package com.jaylangkung.dht.inquiries_progress

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemInquiriesProcessBinding

class InquiriesProcessAdapter : RecyclerView.Adapter<InquiriesProcessAdapter.ItemHolder>() {

    private var list = ArrayList<InquiriesProcessEntity>()

    fun setItem(item: List<InquiriesProcessEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemInquiriesProcessBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: InquiriesProcessEntity) {
            with(binding) {
                tvInquiriesName.text = itemView.context.getString(
                    R.string.inquiries_nama_kode_process, item.nama, item.kode, item.proses
                )
                progressBar.progress = item.prosentase.toFloat()
                progressBar.progressText = item.prosentase + "%"
                when {
                    item.prosentase.toInt() in 0..25 -> {
                        progressBar.progressColor = itemView.context.getColor(R.color.md_red_500)
                    }
                    item.prosentase.toInt() in 26..50 -> {
                        progressBar.progressColor = itemView.context.getColor(R.color.md_amber_500)
                    }
                    item.prosentase.toInt() in 51..75 -> {
                        progressBar.progressColor = itemView.context.getColor(R.color.md_light_green_500)
                    }
                    item.prosentase.toInt() in 76..100 -> {
                        progressBar.progressColor = itemView.context.getColor(R.color.md_green_500)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemInquiriesProcessBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



