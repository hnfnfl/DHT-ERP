package com.jaylangkung.dht.administrator.level

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.databinding.ItemAccessRightBinding
import com.jaylangkung.dht.databinding.ItemLevelBinding

class ViewAccessAdapter : RecyclerView.Adapter<ViewAccessAdapter.ItemHolder>() {

    private var list = ArrayList<LevelEntity>()

    fun setItem(item: List<LevelEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    class ItemHolder(private val binding: ItemAccessRightBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LevelEntity) {
            with(binding) {
                tvModulName.text = item.menu
                if (item.isread == "1") {
                    readCheck.visibility = View.VISIBLE
                }

                if (item.isinsert == "1") {
                    insertCheck.visibility = View.VISIBLE
                }

                if (item.isupdate == "1") {
                    updateCheck.visibility = View.VISIBLE
                }

                if (item.isdelete == "1") {
                    deleteCheck.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemAccessRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = list.size
}



