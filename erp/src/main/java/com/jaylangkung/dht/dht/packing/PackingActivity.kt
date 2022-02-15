package com.jaylangkung.dht.dht.packing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityPackingBinding
import com.jaylangkung.dht.dht.inquiries.InquiriesEntity
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.InquiriesResponse
import com.jaylangkung.dht.retrofit.response.PackingResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackingBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var packingAdapter: PackingAdapter
    private var listData: ArrayList<PackingEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@PackingActivity)
        packingAdapter = PackingAdapter()

        val iddepartemen = myPreferences.getValue(Constants.USER_IDDEPARTEMEN).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.fabScanner.setOnClickListener {
            startActivity(Intent(this@PackingActivity, PackingScannerActivity::class.java))
            finish()
        }

        when (iddepartemen) {
            "1" -> {
                binding.fabScanner.visibility = View.VISIBLE
            }
            "7" -> {
                binding.fabScanner.visibility = View.VISIBLE
            }
            "8" -> {
                binding.fabScanner.visibility = View.VISIBLE
            }
        }

        getPackingBarcode(tokenAuth)

    }

    override fun onBackPressed() {
        startActivity(Intent(this@PackingActivity, MainActivity::class.java))
        finish()
    }

    private fun getPackingBarcode(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.getPackingBarcode(tokenAuth).enqueue(object : Callback<PackingResponse> {
            override fun onResponse(call: Call<PackingResponse>, response: Response<PackingResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        packingAdapter.setItem(listData)
                        packingAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvPacking) {
                            layoutManager = LinearLayoutManager(this@PackingActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = packingAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        packingAdapter.setItem(listData)
                        packingAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@PackingActivity,
                        "getPackingBarcode | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<PackingResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@PackingActivity,
                    "getPackingBarcode | onFailure", t.message.toString()
                )
            }
        })
    }

}