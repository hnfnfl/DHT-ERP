package com.jaylangkung.dht.master_design.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemProductBinding
import com.jaylangkung.dht.master_design.AdditionalAdapter
import com.jaylangkung.dht.master_design.AdditionalEntity

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ItemHolder>() {

    private var list = ArrayList<ProductEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setItem(item: List<ProductEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<ProductEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var additionalAdapter: AdditionalAdapter
        private var listData: ArrayList<AdditionalEntity> = arrayListOf()

        fun bind(item: ProductEntity) {
            additionalAdapter = AdditionalAdapter()
            with(binding) {
                tvProductKode.text = item.kode
                tvProductNameCategory.text = itemView.context.getString(
                    R.string.product_name_category, item.produk, item.kategori
                )
                tvProductPrice.text = itemView.context.getString(R.string.product_price, item.harga)

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
        val itemBinding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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



