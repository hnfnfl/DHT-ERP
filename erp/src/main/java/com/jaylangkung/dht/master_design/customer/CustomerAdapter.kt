package com.jaylangkung.dht.master_design.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemCustomerBinding

class CustomerAdapter : RecyclerView.Adapter<CustomerAdapter.ItemHolder>() {

    private var list = ArrayList<CustomerEntity>()

    fun setItem(item: List<CustomerEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemCustomerBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var customerAdditionalAdapter: CustomerAdditionalAdapter
        private var listData: ArrayList<AdditionalEntity> = arrayListOf()

        fun bind(item: CustomerEntity) {
            customerAdditionalAdapter = CustomerAdditionalAdapter()
            with(binding) {
                tvCustomerName.text = item.nama
                tvCustomerAddress.text = itemView.context.getString(R.string.customer_address, item.alamat)
                tvCustomerPhone.text = itemView.context.getString(R.string.customer_phone, item.telp)

                if (!item.additional.isNullOrEmpty()) {
                    tvAdditionalText.visibility = View.VISIBLE
                    rvCustomerAdditional.visibility = View.VISIBLE
                    listData = item.additional
                    customerAdditionalAdapter.setItem(listData)
                    customerAdditionalAdapter.notifyItemRangeChanged(0, listData.size)

                    with(binding.rvCustomerAdditional) {
                        layoutManager = LinearLayoutManager(itemView.context)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = customerAdditionalAdapter
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



