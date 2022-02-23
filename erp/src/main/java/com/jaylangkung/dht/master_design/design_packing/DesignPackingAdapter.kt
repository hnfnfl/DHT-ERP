package com.jaylangkung.dht.master_design.design_packing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemDesignPackingBinding
import com.jaylangkung.dht.master_design.AdditionalAdapter
import com.jaylangkung.dht.master_design.AdditionalEntity

class DesignPackingAdapter : RecyclerView.Adapter<DesignPackingAdapter.ItemHolder>() {

    private var list = ArrayList<DesignPackingEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setItem(item: List<DesignPackingEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<DesignPackingEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemDesignPackingBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var additionalAdapter: AdditionalAdapter
        private var listData: ArrayList<AdditionalEntity> = arrayListOf()

        fun bind(item: DesignPackingEntity) {
            additionalAdapter = AdditionalAdapter()
            with(binding) {
                tvPackingKode.text = item.kode
                tvPackingName.text = item.packing

                if (!item.additional.isNullOrEmpty()) {
                    tvAdditionalText.visibility = View.VISIBLE
                    rvAdditional.visibility = View.VISIBLE
                    listData = item.additional
                    additionalAdapter.setItem(listData)
                    additionalAdapter.notifyItemRangeChanged(0, listData.size)

                    with(binding.rvAdditional) {
                        layoutManager = LinearLayoutManager(itemView.context)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = additionalAdapter
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemDesignPackingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(list, position)
        }
    }

    override fun getItemCount(): Int = list.size
}



