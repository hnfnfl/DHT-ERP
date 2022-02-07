package com.jaylangkung.dht.master_design.goods

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityGoodsBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.GoodsResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoodsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoodsBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var goodsAdapter: GoodsAdapter
    private var listData: ArrayList<GoodsEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoodsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@GoodsActivity)
        goodsAdapter = GoodsAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getBarang(tokenAuth)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@GoodsActivity, MainActivity::class.java))
        finish()
    }

    private fun getBarang(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getBarang(tokenAuth).enqueue(object : Callback<GoodsResponse> {
            override fun onResponse(call: Call<GoodsResponse>, response: Response<GoodsResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        goodsAdapter.setItem(listData)
                        goodsAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvGoods) {
                            layoutManager = LinearLayoutManager(this@GoodsActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = goodsAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        goodsAdapter.setItem(listData)
                        goodsAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@GoodsActivity,
                        "getBarang | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<GoodsResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@GoodsActivity,
                    "getBarang | onFailure", t.message.toString()
                )
            }
        })
    }
}