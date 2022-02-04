package com.jaylangkung.dht.administrator.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemAdminBinding

class AdminAdapter : RecyclerView.Adapter<AdminAdapter.ItemHolder>() {

    private var list = ArrayList<AdminEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setItem(item: List<AdminEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<AdminEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemAdminBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AdminEntity) {
            with(binding) {
                tvAdminNameLevel.text = itemView.context.getString(
                    R.string.admin_name_level, item.nama, item.level
                )
                tvProfileEmail.text = item.email
                tvProfilePhone.text = item.telp
                Glide.with(itemView.context)
                    .load(item.img)
                    .apply(RequestOptions().override(150))
                    .placeholder(R.drawable.ic_profile)
                    .error(R.drawable.ic_profile)
                    .into(binding.imgAdmin)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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



