package com.jaylangkung.dht.administrator.level

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
import com.jaylangkung.dht.databinding.ActivityLevelBinding
import com.jaylangkung.dht.databinding.BottomSheetLevelActionBinding
import com.jaylangkung.dht.databinding.BottomSheetLevelAddBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.LevelResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LevelActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelBinding
    private lateinit var addlevelbinding: BottomSheetLevelAddBinding
    private lateinit var actionBinding: BottomSheetLevelActionBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var levelAdapter: LevelAdapter
    private var listData: ArrayList<LevelEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLevelBinding.inflate(layoutInflater)
        addlevelbinding = BottomSheetLevelAddBinding.inflate(layoutInflater)
        actionBinding = BottomSheetLevelActionBinding.inflate(layoutInflater)

        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@LevelActivity)
        levelAdapter = LevelAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getLevel(tokenAuth)

        binding.fabAddLevel.setOnClickListener {
            val dialog = BottomSheetDialog(this@LevelActivity)
            val btnSave = addlevelbinding.btnSaveLevel

            btnSave.setOnClickListener {

                dialog.dismiss()
            }
            dialog.setCancelable(true)
            dialog.setContentView(addlevelbinding.root)
            dialog.show()
        }

        levelAdapter.setOnItemClickCallback(object : LevelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<LevelEntity>, position: Int) {
                val dialog = BottomSheetDialog(this@LevelActivity)

                actionBinding.llEdit.setOnClickListener {
                    addlevelbinding.inputLevelName.setText(data[position].level)
                    dialog.dismiss()
                }

                actionBinding.llEditAccess.setOnClickListener {

                    dialog.dismiss()
                }

                actionBinding.llDelete.setOnClickListener {

                    dialog.dismiss()
                }

                dialog.setCancelable(true)
                dialog.setContentView(actionBinding.root)
                dialog.show()
            }

        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@LevelActivity, MainActivity::class.java))
        finish()
    }

    private fun getLevel(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getLevel(tokenAuth).enqueue(object : Callback<LevelResponse> {
            override fun onResponse(call: Call<LevelResponse>, response: Response<LevelResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        levelAdapter.setLevelItem(listData)
                        levelAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvLevel) {
                            layoutManager = LinearLayoutManager(this@LevelActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = levelAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        levelAdapter.setLevelItem(listData)
                        levelAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@LevelActivity,
                        "getLevel | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<LevelResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@LevelActivity,
                    "getLevel | onFailure", t.message.toString()
                )
            }
        })
    }
}