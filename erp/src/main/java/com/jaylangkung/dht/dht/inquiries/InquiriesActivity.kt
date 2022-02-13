package com.jaylangkung.dht.dht.inquiries

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityInquiriesBinding
import com.jaylangkung.dht.databinding.BottomSheetInquiriesDetailBinding
import com.jaylangkung.dht.databinding.BottomSheetInquiriesScBinding
import com.jaylangkung.dht.databinding.BottomSheetInquiriesWoBinding
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.InquiriesResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class InquiriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInquiriesBinding
    private lateinit var detailBinding: BottomSheetInquiriesDetailBinding
    private lateinit var scBinding: BottomSheetInquiriesScBinding
    private lateinit var woBinding: BottomSheetInquiriesWoBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var inquiriesAdapter: InquiriesAdapter
    private lateinit var inquiriesDetailAdapter: InquiriesDetailAdapter
    private var listData: ArrayList<InquiriesEntity> = arrayListOf()
    private var listDataDetail: ArrayList<InquiriesDetailEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInquiriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@InquiriesActivity)
        inquiriesAdapter = InquiriesAdapter()
        inquiriesDetailAdapter = InquiriesDetailAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getInquiries(tokenAuth)

        inquiriesAdapter.setOnItemClickCallback(object : InquiriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int) {
                detailBinding = BottomSheetInquiriesDetailBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@InquiriesActivity)

                listDataDetail = data[position].detail
                inquiriesDetailAdapter.setItem(listDataDetail)
                inquiriesDetailAdapter.notifyItemRangeChanged(0, listDataDetail.size)
                val total = listDataDetail.sumOf { it.sub_total.toInt() }
                val formatter = DecimalFormat("#,###")

                with(detailBinding.rvDetailProduct) {
                    layoutManager = LinearLayoutManager(this@InquiriesActivity)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = inquiriesDetailAdapter
                }

                with(detailBinding) {
                    tvInquiriesDetailTitle.text = getString(R.string.inquiries_detail_title, data[position].kode)
                    tvShipmentNameType.text = data[position].shipment
                    tvShipmentSent.text = data[position].shipment_send
                    tvShipmentEstimate.text = data[position].shipment_estimasi
                    tvDetailProductTotal.text = getString(R.string.inquiries_product_total, formatter.format(total).toString())
                }

                dialog.behavior.peekHeight = 600
                dialog.behavior.maxHeight = 1200
                dialog.setCancelable(true)
                dialog.setContentView(detailBinding.root)
                dialog.show()
            }
        })

        inquiriesAdapter.setOnShowSCClick(object : InquiriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int) {
                scBinding = BottomSheetInquiriesScBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@InquiriesActivity)

                listDataDetail = data[position].detail
                inquiriesDetailAdapter.setItem(listDataDetail)
                inquiriesDetailAdapter.notifyItemRangeChanged(0, listDataDetail.size)
                val total = listDataDetail.sumOf { it.sub_total.toInt() }
                val formatter = DecimalFormat("#,###")

                with(scBinding.rvDetailProduct) {
                    layoutManager = LinearLayoutManager(this@InquiriesActivity)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = inquiriesDetailAdapter
                }

                with(scBinding) {
                    tvInquiriesCode.text = data[position].kode
                    tvScCode.text = data[position].sales_contract[0].idsc
                    tvScCreatedDate.text = data[position].createddate
                    tvScCustomer.text = getString(R.string.inquiries_name_phone, data[position].nama, data[position].telp)
                    tvScCustomerAddress.text = data[position].alamat
                    tvDetailProductTotal.text = getString(R.string.inquiries_product_total, formatter.format(total).toString())
                    tvProductNetGross.text = getString(
                        R.string.tv_sc_net_gross,
                        data[position].sales_contract[0].netto,
                        data[position].sales_contract[0].bruto
                    )
                }

                dialog.behavior.peekHeight = 800
                dialog.behavior.maxHeight = 1600
                dialog.setCancelable(true)
                dialog.setContentView(scBinding.root)
                dialog.show()
            }
        })

        inquiriesAdapter.setOnShowWOClick(object : InquiriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int) {
                woBinding = BottomSheetInquiriesWoBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@InquiriesActivity)

                listDataDetail = data[position].detail
                inquiriesDetailAdapter.setItem(listDataDetail)
                inquiriesDetailAdapter.notifyItemRangeChanged(0, listDataDetail.size)
                val total = listDataDetail.sumOf { it.sub_total.toInt() }
                val formatter = DecimalFormat("#,###")

                with(woBinding.rvDetailProduct) {
                    layoutManager = LinearLayoutManager(this@InquiriesActivity)
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
        startActivity(Intent(this@InquiriesActivity, MainActivity::class.java))
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
                        inquiriesAdapter.setItem(listData)
                        inquiriesAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvInquiries) {
                            layoutManager = LinearLayoutManager(this@InquiriesActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = inquiriesAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        inquiriesAdapter.setItem(listData)
                        inquiriesAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@InquiriesActivity,
                        "getInquiries | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<InquiriesResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@InquiriesActivity,
                    "getInquiries | onFailure", t.message.toString()
                )
            }
        })
    }

}