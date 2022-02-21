package com.jaylangkung.dht.dht.purchasing

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
import com.jaylangkung.dht.databinding.ActivityPurchasingBinding
import com.jaylangkung.dht.databinding.BottomSheetPurchasingDetailBinding
import com.jaylangkung.dht.dht.inquiries.InquiriesDetailEntity
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.PurchasingResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class PurchasingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchasingBinding
    private lateinit var detailBinding: BottomSheetPurchasingDetailBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var purchasingAdapter: PurchasingAdapter
    private lateinit var purchasingDetailAdapter: PurchasingDetailAdapter
    private var listData: ArrayList<PurchasingEntity> = arrayListOf()
    private var listDataDetail: ArrayList<InquiriesDetailEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@PurchasingActivity)
        purchasingAdapter = PurchasingAdapter()
        purchasingDetailAdapter = PurchasingDetailAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getBarangPo(tokenAuth)

        purchasingAdapter.setOnItemClickCallback(object : PurchasingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<PurchasingEntity>, position: Int) {
                detailBinding = BottomSheetPurchasingDetailBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@PurchasingActivity)

                listDataDetail = data[position].detail
                purchasingDetailAdapter.setItem(listDataDetail)
                purchasingDetailAdapter.notifyItemRangeChanged(0, listDataDetail.size)

                with(detailBinding.rvDetailProduct) {
                    layoutManager = LinearLayoutManager(this@PurchasingActivity)
                    itemAnimator = DefaultItemAnimator()
                    setHasFixedSize(true)
                    adapter = purchasingDetailAdapter
                }

                with(detailBinding) {
                    tvPurchasingDetailTitle.text = data[position].kode
                    tvDetailGoodsTotal.text = getString(R.string.inquiries_product_total, data[position].total)
                }

                dialog.behavior.peekHeight = 600
                dialog.behavior.maxHeight = 1200
                dialog.setCancelable(true)
                dialog.setContentView(detailBinding.root)
                dialog.show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@PurchasingActivity, MainActivity::class.java))
        finish()
    }

    private fun getBarangPo(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.getBarangPo(tokenAuth).enqueue(object : Callback<PurchasingResponse> {
            override fun onResponse(call: Call<PurchasingResponse>, response: Response<PurchasingResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        purchasingAdapter.setItem(listData)
                        purchasingAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvBarangPo) {
                            layoutManager = LinearLayoutManager(this@PurchasingActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = purchasingAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        purchasingAdapter.setItem(listData)
                        purchasingAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@PurchasingActivity,
                        "getBarangPo | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<PurchasingResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@PurchasingActivity,
                    "getBarangPo | onFailure", t.message.toString()
                )
            }
        })
    }

}