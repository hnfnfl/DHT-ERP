package com.jaylangkung.dht.master_design.shipment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemShipmentBinding
import com.jaylangkung.dht.master_design.AdditionalAdapter
import com.jaylangkung.dht.master_design.AdditionalEntity

class ShipmentAdapter : RecyclerView.Adapter<ShipmentAdapter.ItemHolder>() {

    private var list = ArrayList<ShipmentEntity>()

    fun setItem(item: List<ShipmentEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemShipmentBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var additionalAdapter: AdditionalAdapter
        private var listData: ArrayList<AdditionalEntity> = arrayListOf()

        fun bind(item: ShipmentEntity) {
            additionalAdapter = AdditionalAdapter()
            with(binding) {
                tvShipmentNameType.text = itemView.context.getString(
                    R.string.product_name_category, item.shipment, item.jenis
                )

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
        val itemBinding = ItemShipmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



