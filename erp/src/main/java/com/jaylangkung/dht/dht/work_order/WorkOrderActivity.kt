package com.jaylangkung.dht.dht.work_order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityWorkOrderBinding
import com.jaylangkung.dht.databinding.BottomSheetInquiriesWoBinding
import com.jaylangkung.dht.dht.inquiries.InquiriesDetailAdapter
import com.jaylangkung.dht.dht.inquiries.InquiriesDetailEntity
import com.jaylangkung.dht.dht.inquiries.InquiriesEntity
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.InquiriesResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.Serializable
import java.text.DecimalFormat

class WorkOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkOrderBinding
    private lateinit var woBinding: BottomSheetInquiriesWoBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var workOrderAdapter: WorkOrderAdapter
    private lateinit var inquiriesDetailAdapter: InquiriesDetailAdapter
    private var listData: ArrayList<InquiriesEntity> = arrayListOf()
    private var listDataDetail: ArrayList<InquiriesDetailEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@WorkOrderActivity)
        workOrderAdapter = WorkOrderAdapter()
        inquiriesDetailAdapter = InquiriesDetailAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getInquiries(tokenAuth)

        workOrderAdapter.setOnShowBreakdownClick(object : WorkOrderAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int) {
                val size = data[position].detail.size
                val listData: MutableList<JSONObject> = ArrayList()
                for (i in 0 until size) {
                    val obj = JSONObject()
                    obj.put("idproduk", data[position].detail[i].idproduk)
                    obj.put("produk", data[position].detail[i].produk)
                    obj.put("qty", data[position].detail[i].jumlah)
                    listData.add(obj)
                }
                val a = Gson().toJson(listData)
                startActivity(Intent(this@WorkOrderActivity, BreakdownActivity::class.java)
                    .apply {
                        putExtra("dataProduct", listData.toString())
                    })
                finish()

            }
        })

        workOrderAdapter.setOnShowWOClick(object : WorkOrderAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int) {
                woBinding = BottomSheetInquiriesWoBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@WorkOrderActivity)

                listDataDetail = data[position].detail
                inquiriesDetailAdapter.setItem(listDataDetail)
                inquiriesDetailAdapter.notifyItemRangeChanged(0, listDataDetail.size)
                val total = listDataDetail.sumOf { it.sub_total.toInt() }
                val formatter = DecimalFormat("#,###")

                with(woBinding.rvDetailProduct) {
                    layoutManager = LinearLayoutManager(this@WorkOrderActivity)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = inquiriesDetailAdapter
                }

                with(woBinding) {
                    tvInquiriesCode.text = data[position].kode
                    tvWoCode.text = data[position].work_order[0].idwo
                    tvWoCreatedDate.text = data[position].createddate
                    tvWoCustomer.text = getString(R.string.inquiries_name_phone, data[position].nama, data[position].telp)
                    tvWoCustomerAddress.text = data[position].alamat
                    tvDetailProductTotal.text = getString(R.string.inquiries_product_total, formatter.format(total).toString())
                    tvProductNetGross.text = getString(
                        R.string.tv_sc_net_gross,
                        data[position].work_order[0].netto,
                        data[position].work_order[0].bruto
                    )
                }

                dialog.behavior.peekHeight = 800
                dialog.behavior.maxHeight = 1600
                dialog.setCancelable(true)
                dialog.setContentView(woBinding.root)
                dialog.show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@WorkOrderActivity, MainActivity::class.java))
        finish()
    }

    private fun getInquiries(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.getInquiries(tokenAuth).enqueue(object : Callback<InquiriesResponse> {
            override fun onResponse(call: Call<InquiriesResponse>, response: Response<InquiriesResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        workOrderAdapter.setItem(listData)
                        workOrderAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvWorkOrder) {
                            layoutManager = LinearLayoutManager(this@WorkOrderActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = workOrderAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        workOrderAdapter.setItem(listData)
                        workOrderAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@WorkOrderActivity,
                        "getInquiries | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<InquiriesResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@WorkOrderActivity,
                    "getInquiries | onFailure", t.message.toString()
                )
            }
        })
    }

}