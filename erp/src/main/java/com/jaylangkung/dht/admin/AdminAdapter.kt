package com.jaylangkung.dht.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemAdminBinding

class AdminAdapter : RecyclerView.Adapter<AdminAdapter.AdminItemHolder>() {

    private var listTodo = ArrayList<AdminEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setAdminItem(adminItem: List<AdminEntity>?) {
        if (adminItem == null) return
        listTodo.clear()
        listTodo.addAll(adminItem)
        notifyItemRangeChanged(0, adminItem.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<AdminEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class AdminItemHolder(private val binding: ItemAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(adminItem: AdminEntity) {
            with(binding) {
                tvAdminNameLevel.text = itemView.context.getString(
                    R.string.admin_name_level, adminItem.nama, adminItem.level
                )
                tvProfileEmail.text = adminItem.email
                tvProfilePhone.text = adminItem.telp
                Glide.with(itemView.context)
                    .load(adminItem.img)
                    .apply(RequestOptions().override(150))
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.imgAdmin)
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
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listTodo, position)
        }
    }

    override fun getItemCount(): Int = listTodo.size
}



