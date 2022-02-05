package com.jaylangkung.dht.master_design.customer

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityCustomerBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.CustomerResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var customerAdapter: CustomerAdapter
    private var listData: ArrayList<CustomerEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@CustomerActivity)
        customerAdapter = CustomerAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getCustomer(tokenAuth)

    }

    override fun onBackPressed() {
        startActivity(Intent(this@CustomerActivity, MainActivity::class.java))
        finish()
    }

    private fun getCustomer(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getCustomer(tokenAuth).enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        customerAdapter.setItem(listData)
                        customerAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvCustomer) {
                            layoutManager = LinearLayoutManager(this@CustomerActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = customerAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        customerAdapter.setItem(listData)
                        customerAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@CustomerActivity,
                        "getCustomer | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@CustomerActivity,
                    "getCustomer | onFailure", t.message.toString()
                )
            }
        })
    }
}