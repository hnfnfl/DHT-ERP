package com.jaylangkung.dht.master_design.product

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
import com.jaylangkung.dht.databinding.ActivityProductBinding
import com.jaylangkung.dht.databinding.BottomSheetProductDetailBinding
import com.jaylangkung.dht.master_design.AdditionalEntity
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.ProductResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding
    private lateinit var detailBinding: BottomSheetProductDetailBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var productAdapter: ProductAdapter
    private lateinit var specificationAdapter: SpecificationAdapter
    private lateinit var compositionAdapter: CompositionAdapter
    private var listData: ArrayList<ProductEntity> = arrayListOf()
    private var listSpesifikasi: ArrayList<AdditionalEntity> = arrayListOf()
    private var listKomposisi: ArrayList<AdditionalEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@ProductActivity)
        productAdapter = ProductAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getProduct(tokenAuth)

        productAdapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<ProductEntity>, position: Int) {
                detailBinding = BottomSheetProductDetailBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@ProductActivity)
                specificationAdapter = SpecificationAdapter()
                compositionAdapter = CompositionAdapter()

                if (!data[position].spesifikasi.isNullOrEmpty()) {
                    listSpesifikasi = data[position].spesifikasi
                    specificationAdapter.setItem(listSpesifikasi)
                    specificationAdapter.notifyItemRangeChanged(0, listData.size)

                    with(detailBinding.rvSpecification) {
                        layoutManager = LinearLayoutManager(this@ProductActivity)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = specificationAdapter
                    }
                }

                if (!data[position].komposisi.isNullOrEmpty()) {
                    listKomposisi = data[position].komposisi
                    compositionAdapter.setItem(listKomposisi)
                    compositionAdapter.notifyItemRangeChanged(0, listData.size)

                    with(detailBinding.rvComposition) {
                        layoutManager = LinearLayoutManager(this@ProductActivity)
                        itemAnimator = DefaultItemAnimator()
                        setHasFixedSize(true)
                        adapter = compositionAdapter
                    }
                }

                dialog.setCancelable(true)
                dialog.setContentView(detailBinding.root)
                dialog.show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@ProductActivity, MainActivity::class.java))
        finish()
    }

    private fun getProduct(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getProduct(tokenAuth).enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        productAdapter.setItem(listData)
                        productAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvProduct) {
                            layoutManager = LinearLayoutManager(this@ProductActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = productAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        productAdapter.setItem(listData)
                        productAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@ProductActivity,
                        "getProduct | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@ProductActivity,
                    "getProduct | onFailure", t.message.toString()
                )
            }
        })
    }
}