package com.jaylangkung.dht.dht.purchasing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityPurchasingBinding
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.PurchasingResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchasingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchasingBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var purchasingAdapter: PurchasingAdapter
    private var listData: ArrayList<PurchasingEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchasingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@PurchasingActivity)
        purchasingAdapter = PurchasingAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getBarangPo(tokenAuth)

        purchasingAdapter.setOnItemClickCallback(object : PurchasingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<PurchasingEntity>, position: Int) {

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