package com.jaylangkung.dht.dht.inquiries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ItemInquiriesBinding
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.DefaultResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InquiriesAdapter : RecyclerView.Adapter<InquiriesAdapter.ItemHolder>() {

    private var list = ArrayList<InquiriesEntity>()
    private lateinit var showDetailCallBack: OnItemClickCallback
    private lateinit var showSCCallBack: OnItemClickCallback
    private lateinit var showWOCallBack: OnItemClickCallback

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

    fun setOnShowWOClick(onItemClickCallback: OnItemClickCallback) {
        this.showWOCallBack = onItemClickCallback
    }

    class ItemHolder(private val binding: ItemInquiriesBinding) : RecyclerView.ViewHolder(binding.root) {
        private lateinit var myPreferences: MySharedPreferences

        fun bind(item: InquiriesEntity) {
            with(binding) {
                myPreferences = MySharedPreferences(itemView.context)
                val iddepartemen = myPreferences.getValue(Constants.USER_IDDEPARTEMEN).toString()
                val tokenAuth = itemView.context.getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

                tvInquiriesCode.text = item.kode
                tvInquiriesNamePhone.text = itemView.context.getString(R.string.inquiries_name_phone, item.nama, item.telp)
                tvInquiriesAddress.text = item.alamat
                tvInquiriesCreatedate.text = itemView.context.getString(R.string.inquiries_createddate, item.createddate)
                tvInquiriesUpdatedate.text = itemView.context.getString(R.string.inquiries_lastupdate, item.lastupdate)
                var status = ""
                when (item.status) {
                    "drafted" -> {
                        status = itemView.context.getString(R.string.inquiries_status_drafted)
                        when (iddepartemen) {
                            "1" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                            "3" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                        }
                    }
                    "waiting_approval" -> {
                        status = itemView.context.getString(R.string.inquiries_status_waiting_approval)
                    }
                    "waiting_approval_customer" -> {
                        status = itemView.context.getString(R.string.inquiries_status_waiting_approval_customer)
                        when (iddepartemen) {
                            "1" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                            "3" -> {
                                btnMakeIt.visibility = View.VISIBLE
                            }
                        }
                    }
                }
                tvInquiriesStatus.text = itemView.context.getString(R.string.inquiries_status, status)
                tvInquiriesPayment.text = itemView.context.getString(R.string.inquiries_payment, item.payment_detail)

                btnMakeIt.setOnClickListener {
                    btnMakeIt.startAnimation()
                    updateInquiries(item.idinquiries, tokenAuth)
                }
            }
        }

        val btnShowDetail = binding.btnShowInquiriesDetail
        val btnShowSC = binding.btnShowInquiriesSc
        val btnShowWO = binding.btnShowInquiriesWo

        private fun updateInquiries(idinquiries: String, tokenAuth: String) {
            val service = RetrofitClient().apiRequest().create(DhtService::class.java)
            service.updateInquiries(idinquiries, "waiting_approval", tokenAuth).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    binding.btnMakeIt.endAnimation()
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "success") {
                            binding.btnMakeIt.visibility = View.GONE
                            binding.tvInquiriesStatus.text = itemView.context.getString(
                                R.string.inquiries_status, itemView.context.getString(
                                    R.string.inquiries_status_waiting_approval
                                )
                            )
                            Toasty.success(itemView.context, "Inquiries berhasil diperbarui", Toasty.LENGTH_LONG).show()
                        }
                    } else {
                        ErrorHandler().responseHandler(
                            itemView.context,
                            "updateInquiries | onResponse", response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    binding.btnMakeIt.endAnimation()
                    ErrorHandler().responseHandler(
                        itemView.context,
                        "updateInquiries | onFailure", t.message.toString()
                    )
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemBinding = ItemInquiriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        holder.btnShowWO.setOnClickListener {
            showWOCallBack.onItemClicked(list, position)
        }
    }

    override fun getItemCount(): Int = list.size
}



