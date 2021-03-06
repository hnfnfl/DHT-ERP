package com.jaylangkung.dht.dht.sales_contract

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemSalesContractBinding
import com.jaylangkung.dht.dht.inquiries.InquiriesEntity
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.DefaultResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalesContractAdapter : RecyclerView.Adapter<SalesContractAdapter.ItemHolder>() {

    private var list = ArrayList<InquiriesEntity>()
    private lateinit var showDetailCallBack: OnItemClickCallback
    private lateinit var showSCCallBack: OnItemClickCallback

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
        this.showDetailCallBack = onItemClickCallback
    }

    fun setOnShowSCClick(onItemClickCallback: OnItemClickCallback) {
        this.showSCCallBack = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemSalesContractBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var myPreferences: MySharedPreferences

        fun bind(item: InquiriesEntity) {
            with(binding) {
                myPreferences = MySharedPreferences(itemView.context)
                val iddepartemen = myPreferences.getValue(Constants.USER_IDDEPARTEMEN).toString()
                val tokenAuth = itemView.context.getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

                tvScCode.text = item.kode
                tvScNamePhone.text = itemView.context.getString(R.string.inquiries_name_phone, item.nama, item.telp)
                tvScAddress.text = item.alamat
                tvScCreatedate.text = itemView.context.getString(R.string.inquiries_createddate, item.createddate)
                tvScUpdatedate.text = itemView.context.getString(R.string.inquiries_lastupdate, item.lastupdate)
                val status: String
                when (item.status) {
                    "waiting_approval" -> {
                        status = itemView.context.getString(R.string.inquiries_status_waiting_approval)
                        when (iddepartemen) {
                            "1" -> {
                                btnApprove.visibility = View.VISIBLE
                                btnReject.visibility = View.VISIBLE
                            }
                            "2" -> {
                                btnApprove.visibility = View.VISIBLE
                                btnReject.visibility = View.VISIBLE
                            }
                        }
                    }
                    "waiting_approval_customer" -> {
                        status = itemView.context.getString(R.string.inquiries_status_waiting_approval_customer)
                        when (iddepartemen) {
                            "1" -> {
                                btnCustomerApproved.visibility = View.VISIBLE
                            }
                            "2" -> {
                                btnCustomerApproved.visibility = View.VISIBLE
                            }
                            "3" -> {
                                btnCustomerApproved.visibility = View.VISIBLE
                            }
                        }
                    }
                    "rejected" -> {
                        status = itemView.context.getString(R.string.inquiries_status_rejected)
                    }
                    else -> {
                        status = itemView.context.getString(R.string.inquiries_status_process)
                    }
                }
                tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, status)

                btnApprove.setOnClickListener {
                    updateInquiries(item.idinquiries, "waiting_approval_customer", tokenAuth)
                }

                btnReject.setOnClickListener {
                    updateInquiries(item.idinquiries, "rejected", tokenAuth)
                }

                btnCustomerApproved.setOnClickListener {
                    updateInquiries(item.idinquiries, "waiting_approval_production", tokenAuth)
                }
            }
        }

        val btnShowDetail = binding.btnShowInquiriesDetail
        val btnShowSC = binding.btnShowInquiriesSc

        private fun updateInquiries(idinquiries: String, status: String, tokenAuth: String) {
            val service = RetrofitClient().apiRequest().create(DhtService::class.java)
            service.updateInquiries(idinquiries, status, tokenAuth).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "success") {
                            binding.btnApprove.visibility = View.GONE
                            binding.btnReject.visibility = View.GONE
                            binding.btnCustomerApproved.visibility = View.GONE
                            val statusText: String
                            when (status) {
                                "waiting_approval_customer" -> {
                                    statusText = itemView.context.getString(R.string.inquiries_status_waiting_approval_customer)
                                    binding.btnCustomerApproved.visibility = View.VISIBLE
                                }
                                "rejected" -> {
                                    statusText = itemView.context.getString(R.string.inquiries_status_rejected)
                                }
                                else -> {
                                    statusText = itemView.context.getString(R.string.inquiries_status_process)
                                }
                            }
                            binding.tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, statusText)
                            Toasty.success(itemView.context, "Inquiries berhasil diperbarui", Toasty.LENGTH_SHORT).show()
                        }
                    } else {
                        ErrorHandler().responseHandler(
                            itemView.context,
                            "updateInquiries | onResponse", response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    binding.btnApprove.endAnimation()
                    ErrorHandler().responseHandler(
                        itemView.context,
                        "updateInquiries | onFailure", t.message.toString()
                    )
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemSalesContractBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
        holder.btnShowDetail.setOnClickListener {
            showDetailCallBack.onItemClicked(list, position)
        }

        holder.btnShowSC.setOnClickListener {
            showSCCallBack.onItemClicked(list, position)
        }
    }

    override fun getItemCount(): Int = list.size
}



