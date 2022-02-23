package com.jaylangkung.dht.master_design.design_packing

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityDesignPackingBinding
import com.jaylangkung.dht.databinding.BottomSheetDesignPackingDetailBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.DesignPackingResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DesignPackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDesignPackingBinding
    private lateinit var detailBinding: BottomSheetDesignPackingDetailBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var designPackingAdapter: DesignPackingAdapter
    private lateinit var compositionDPAdapter: CompositionDPAdapter
    private lateinit var usedForDPAdapter: UsedForDPAdapter
    private var listData: ArrayList<DesignPackingEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDesignPackingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@DesignPackingActivity)
        designPackingAdapter = DesignPackingAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getPacking(tokenAuth)

        designPackingAdapter.setOnItemClickCallback(object : DesignPackingAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<DesignPackingEntity>, position: Int) {
                detailBinding = BottomSheetDesignPackingDetailBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@DesignPackingActivity)
                compositionDPAdapter = CompositionDPAdapter()
                usedForDPAdapter = UsedForDPAdapter()

                if (!data[position].komposisi.isNullOrEmpty()) {
                    val listData = data[position].komposisi
                    compositionDPAdapter.setItem(listData)
                    compositionDPAdapter.notifyItemRangeChanged(0, listData.size)

                    with(detailBinding.rvComposition) {
                        layoutManager = LinearLayoutManager(this@DesignPackingActivity)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = compositionDPAdapter
                    }
                }

                if (!data[position].detail_muat.isNullOrEmpty()) {
                    val listData = data[position].detail_muat
                    usedForDPAdapter.setItem(listData)
                    usedForDPAdapter.notifyItemRangeChanged(0, listData.size)

                    with(detailBinding.rvUsedFor) {
                        layoutManager = LinearLayoutManager(this@DesignPackingActivity)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = usedForDPAdapter
                    }
                }

                dialog.behavior.peekHeight = 600
                dialog.behavior.maxHeight = 1200
                dialog.setCancelable(true)
                dialog.setContentView(detailBinding.root)
                dialog.show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@DesignPackingActivity, MainActivity::class.java))
        finish()
    }

    private fun getPacking(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getPacking(tokenAuth).enqueue(object : Callback<DesignPackingResponse> {
            override fun onResponse(call: Call<DesignPackingResponse>, response: Response<DesignPackingResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        designPackingAdapter.setItem(listData)
                        designPackingAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvDesignProduct) {
                            layoutManager = LinearLayoutManager(this@DesignPackingActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = designPackingAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        designPackingAdapter.setItem(listData)
                        designPackingAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@DesignPackingActivity,
                        "getPacking | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DesignPackingResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@DesignPackingActivity,
                    "getPacking | onFailure", t.message.toString()
                )
            }
        })
    }
}