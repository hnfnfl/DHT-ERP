package com.jaylangkung.dht.dht.department

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.administrator.level.LevelEntity
import com.jaylangkung.dht.databinding.ActivityDepartmentBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.DepartmentResponse
import com.jaylangkung.dht.retrofit.response.LevelResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DepartmentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDepartmentBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var departmentAdapter: DepartmentAdapter
    private var listData: ArrayList<DepartmentEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDepartmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@DepartmentActivity)
        departmentAdapter = DepartmentAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getDepartemen(tokenAuth)
    }

    override fun onBackPressed() {
        startActivity(Intent(this@DepartmentActivity, MainActivity::class.java))
        finish()
    }

    private fun getDepartemen(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.getDepartemen(tokenAuth).enqueue(object : Callback<DepartmentResponse> {
            override fun onResponse(call: Call<DepartmentResponse>, response: Response<DepartmentResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        departmentAdapter.setItem(listData)
                        departmentAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvDepartment) {
                            layoutManager = LinearLayoutManager(this@DepartmentActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = departmentAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        departmentAdapter.setItem(listData)
                        departmentAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@DepartmentActivity,
                        "getDepartemen | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DepartmentResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@DepartmentActivity,
                    "getDepartemen | onFailure", t.message.toString()
                )
            }
        })
    }
}