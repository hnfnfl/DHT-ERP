package com.jaylangkung.dht.master_design.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemCustomerBinding
import com.jaylangkung.dht.master_design.AdditionalEntity
import com.jaylangkung.dht.master_design.AdditionalAdapter

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.ItemHolder>() {

    private var list = ArrayList<CustomerEntity>()

    fun setItem(item: List<CustomerEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var additionalAdapter: AdditionalAdapter
        private var listData: ArrayList<AdditionalEntity> = arrayListOf()

        fun bind(item: CustomerEntity) {
            additionalAdapter = AdditionalAdapter()
            with(binding) {
                tvCustomerName.text = item.nama
                tvCustomerAddress.text = itemView.context.getString(R.string.customer_address, item.alamat)
                tvCustomerPhone.text = itemView.context.getString(R.string.customer_phone, item.telp)

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
        val itemBinding = ItemCustomerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



