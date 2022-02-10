package com.jaylangkung.dht.dht.inquiries

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityInquiriesBinding
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.InquiriesResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InquiriesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInquiriesBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var inquiriesAdapter: InquiriesAdapter
    private var listData: ArrayList<InquiriesEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInquiriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@InquiriesActivity)
        inquiriesAdapter = InquiriesAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getInquiries(tokenAuth)

        inquiriesAdapter.setOnItemClickCallback(object : InquiriesAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<InquiriesEntity>, position: Int) {

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