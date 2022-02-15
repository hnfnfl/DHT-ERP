package com.jaylangkung.dht.dht.department

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemLevelBinding

class DepartmentAdapter : RecyclerView.Adapter<DepartmentAdapter.ItemHolder>() {

    private var list = ArrayList<DepartmentEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setItem(item: List<DepartmentEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<DepartmentEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemLevelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DepartmentEntity) {
            with(binding) {
                imgDropdown.visibility = View.GONE
                tvLevel.text = item.departemen
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemLevelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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



