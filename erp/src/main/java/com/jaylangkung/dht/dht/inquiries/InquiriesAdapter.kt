package com.jaylangkung.dht.dht.inquiries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.dht.R
import com.jaylangkung.dht.administrator.admin.AdminAdapter
import com.jaylangkung.dht.administrator.admin.AdminEntity
import com.jaylangkung.dht.databinding.ItemInquiriesBinding
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.MySharedPreferences

class InquiriesAdapter : RecyclerView.Adapter<InquiriesAdapter.ItemHolder>() {

    private var list = ArrayList<InquiriesEntity>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setItem(item: List<InquiriesEntity>?) {
        if (item == null) return
        list.clear()
        list.addAll(item)
        notifyItemRangeChanged(0, item.size)
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemInquiriesBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var myPreferences: MySharedPreferences

        fun bind(item: InquiriesEntity) {
            with(binding) {
                myPreferences = MySharedPreferences(itemView.context)
                val iddepartemen = myPreferences.getValue(Constants.USER_IDDEPARTEMEN).toString()

                tvInquiriesCode.text = item.kode
                tvInquiriesNamePhone.text = itemView.context.getString(R.string.inquiries_name_phone, item.nama, item.telp)
                tvInquiriesAddress.text = item.alamat
                tvInquiriesCreatedate.text = itemView.context.getString(R.string.inquiries_createddate, item.createddate)
                tvInquiriesUpdatedate.text = itemView.context.getString(R.string.inquiries_lastupdate, item.lastupdate)
                tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, item.status)

                when (item.status) {
                    "drafted" -> {
                        when (iddepartemen) {
                            "1" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                            "3" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                            else -> {
                                tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status_drafted)
                            }
                        }
                    }
                    "waiting_approval_customer" -> {
                        when (iddepartemen) {
                            "1" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                            "3" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                            else -> {
                                tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status_waiting_approval_customer)
                            }
                        }
                    }
                    "waiting_approval" -> {
                        tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status_waiting_approval)
                    }
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemInquiriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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



