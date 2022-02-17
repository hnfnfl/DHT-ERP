package com.jaylangkung.dht.dht.work_order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityBreakdownBinding
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.BreakdownResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreakdownBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var breakdownAdapter: BreakdownAdapter
    private var listData: ArrayList<BreakdownEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@BreakdownActivity)
        breakdownAdapter = BreakdownAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val idinquiries = intent.getStringExtra("idinquiries").toString()

        getWorkOrderBreakdown(idinquiries, tokenAuth)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@BreakdownActivity, WorkOrderActivity::class.java))
        finish()
    }

    private fun getWorkOrderBreakdown(idinquiries: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.getWorkOrderBreakdown(idinquiries, tokenAuth).enqueue(object : Callback<BreakdownResponse> {
            override fun onResponse(call: Call<BreakdownResponse>, response: Response<BreakdownResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        breakdownAdapter.setItem(listData)
                        breakdownAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvBreakdown) {
                            layoutManager = LinearLayoutManager(this@BreakdownActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = breakdownAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        breakdownAdapter.setItem(listData)
                        breakdownAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@BreakdownActivity,
                        "getWorkOrderBreakdown | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<BreakdownResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@BreakdownActivity,
                    "getWorkOrderBreakdown | onFailure", t.message.toString()
                )
            }
        })
    }
}