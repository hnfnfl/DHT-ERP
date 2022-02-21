package com.jaylangkung.dht.dht.purchasing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemPurchasingBinding

class PurchasingAdapter : RecyclerView.Adapter<PurchasingAdapter.ItemHolder>() {

    private var list = ArrayList<PurchasingEntity>()
    private lateinit var showDetailCallBack: OnItemClickCallback

    fun setItem(item: List<PurchasingEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<PurchasingEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.showDetailCallBack = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemPurchasingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PurchasingEntity) {
            with(binding) {
                tvPurchasingCode.text = item.kode
                tvPurchasingSupplier.text = item.nama
                tvPurchasingCreatedate.text = itemView.context.getString(R.string.inquiries_createddate, item.createddate)
                tvPurchasingEstimated.text = itemView.context.getString(R.string.purchasing_estimated_delivery, item.estimasi_delivery)
                when (item.status) {
                    "check_list" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Check List")
                    }
                    "rejected" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Rejected")
                    }
                    "waiting_approval" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Waiting Approval")
                    }
                    "waiting_delivery" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Waiting Delivery")
                    }
                    "completed" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Completed")
                    }
                    "draft" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Draft")
                    }
                    "waiting_for_edit" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, "Waiting For Edit")
                    }
                    else -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, item.status)
                    }
                }
            }
        }

        val btnShowDetail = binding.btnShowPurchasingDetail

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemPurchasingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.btnShowDetail.setOnClickListener {
            showDetailCallBack.onItemClicked(list, position)
        }
    }

    override fun getItemCount(): Int = list.size
}



