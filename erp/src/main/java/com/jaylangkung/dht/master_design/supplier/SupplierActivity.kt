package com.jaylangkung.dht.master_design.supplier

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivitySupplierBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.SupplierResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SupplierActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySupplierBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var supplierAdapter: SupplierAdapter
    private var listData: ArrayList<SupplierEntity> = arrayListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySupplierBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@SupplierActivity)
        supplierAdapter = SupplierAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getSupplier(tokenAuth)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@SupplierActivity, MainActivity::class.java))
        finish()
    }

    private fun getSupplier(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getSupplier(tokenAuth).enqueue(object : Callback<SupplierResponse> {
            override fun onResponse(call: Call<SupplierResponse>, response: Response<SupplierResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        supplierAdapter.setItem(listData)
                        supplierAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvSupplier) {
                            layoutManager = LinearLayoutManager(this@SupplierActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = supplierAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        supplierAdapter.setItem(listData)
                        supplierAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@SupplierActivity,
                        "getSupplier | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<SupplierResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@SupplierActivity,
                    "getSupplier | onFailure", t.message.toString()
                )
            }
        })
    }

}