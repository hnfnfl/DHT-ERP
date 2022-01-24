package com.jaylangkung.dht.administrator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemAdminBinding

class AdminAdapter : RecyclerView.Adapter<AdminAdapter.AdminItemHolder>() {

    private var listTodo = ArrayList<AdminEntity>()

    fun setAdminItem(adminItem: List<AdminEntity>?) {
        if (adminItem == null) return
        listTodo.clear()
        listTodo.addAll(adminItem)
        notifyItemRangeChanged(0, adminItem.size)
    }

    class AdminItemHolder(private val binding: ItemAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adminItem: AdminEntity) {
            with(binding) {
                tvAdminNameLevel.text = itemView.context.getString(
                    R.string.admin_name_level, adminItem.nama, adminItem.level
                )
                tvProfileEmail.text = adminItem.email
                tvProfilePhone.text = adminItem.telp
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminItemHolder {
        val itemAdminBinding = ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdminItemHolder(itemAdminBinding)
    }

    override fun onBindViewHolder(holder: AdminItemHolder, position: Int) {
        val vendorItem = listTodo[position]
        holder.bind(vendorItem)
    }

    override fun getItemCount(): Int = listTodo.size
}



