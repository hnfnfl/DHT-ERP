package com.jaylangkung.dht.master_design.shipment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityShipmentBinding
import com.jaylangkung.dht.master_design.supplier.SupplierEntity
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.ShipmentResponse
import com.jaylangkung.dht.retrofit.response.SupplierResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShipmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShipmentBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var shipmentAdapter: ShipmentAdapter
    private var listData: ArrayList<ShipmentEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShipmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@ShipmentActivity)
        shipmentAdapter = ShipmentAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getShipment(tokenAuth)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ShipmentActivity, MainActivity::class.java))
        finish()
    }

    private fun getShipment(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getShipment(tokenAuth).enqueue(object : Callback<ShipmentResponse> {
            override fun onResponse(call: Call<ShipmentResponse>, response: Response<ShipmentResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        shipmentAdapter.setItem(listData)
                        shipmentAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvShipment) {
                            layoutManager = LinearLayoutManager(this@ShipmentActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = shipmentAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        shipmentAdapter.setItem(listData)
                        shipmentAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@ShipmentActivity,
                        "getShipment | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<ShipmentResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@ShipmentActivity,
                    "getShipment | onFailure", t.message.toString()
                )
            }
        })
    }

}