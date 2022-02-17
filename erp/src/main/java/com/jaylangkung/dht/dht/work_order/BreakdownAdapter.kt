package com.jaylangkung.dht.dht.work_order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemBreakdownBinding

class BreakdownAdapter() : RecyclerView.Adapter<BreakdownAdapter.ItemHolder>() {

    private var list = ArrayList<BreakdownEntity>()

    fun setItem(item: List<BreakdownEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemBreakdownBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var breakdownGoodsAdapter: BreakdownGoodsAdapter
        private lateinit var breakdownPackingAdapter: BreakdownPackingAdapter
        private var listGoods: ArrayList<BreakdownDetailEntity> = arrayListOf()
        private var listPacking: ArrayList<BreakdownDetailEntity> = arrayListOf()

        fun bind(item: BreakdownEntity) {
            breakdownGoodsAdapter = BreakdownGoodsAdapter()
            breakdownPackingAdapter = BreakdownPackingAdapter()

            with(binding) {
                tvProductNameCode.text = item.produk

                listGoods = item.barang
                listPacking = item.packing

                breakdownGoodsAdapter.setItem(listGoods)
                breakdownPackingAdapter.setItem(listPacking)

                breakdownGoodsAdapter.notifyItemRangeChanged(0, listGoods.size)
                breakdownPackingAdapter.notifyItemRangeChanged(0, listPacking.size)

                with(rvBreakdownGoods) {
                    layoutManager = LinearLayoutManager(itemView.context)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = breakdownGoodsAdapter
                }

                with(rvBreakdownPacking) {
                    layoutManager = LinearLayoutManager(itemView.context)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = breakdownPackingAdapter
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemBreakdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



