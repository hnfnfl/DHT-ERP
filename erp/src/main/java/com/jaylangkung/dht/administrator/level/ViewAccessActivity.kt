package com.jaylangkung.dht.administrator.level

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityViewAccessBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.LevelResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAccessActivity : AppCompatActivity() {

    private lateinit var binding: ActivityViewAccessBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var viewAccessAdapter: ViewAccessAdapter
    private var listData: ArrayList<LevelEntity> = arrayListOf()

    companion object {
        const val idlevel = "idlevel"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewAccessBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewAccessAdapter = ViewAccessAdapter()
        myPreferences = MySharedPreferences(this@ViewAccessActivity)

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())
        val idlevel = intent.getStringExtra(idlevel).toString()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getHakAkses(idlevel, tokenAuth)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ViewAccessActivity, LevelActivity::class.java))
        finish()
    }

    private fun getHakAkses(idlevel: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getHakAkses(idlevel, tokenAuth).enqueue(object : Callback<LevelResponse> {
            override fun onResponse(call: Call<LevelResponse>, response: Response<LevelResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        viewAccessAdapter.setItem(listData)
                        viewAccessAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvViewAccess) {
                            layoutManager = LinearLayoutManager(this@ViewAccessActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = viewAccessAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        viewAccessAdapter.setItem(listData)
                        viewAccessAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@ViewAccessActivity,
                        "getHakAkses | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<LevelResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@ViewAccessActivity,
                    "getHakAkses | onFailure", t.message.toString()
                )
            }
        })
    }
}