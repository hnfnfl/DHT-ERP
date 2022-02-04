package com.jaylangkung.dht.administrator.level

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.brainnet_staff.retrofit.response.DefaultResponse
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
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface
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

        val dialog = BottomSheetDialog(this@LevelActivity)
        val dialogAddLevel = BottomSheetDialog(this@LevelActivity)
        val btnSave = addlevelbinding.btnSaveLevel
        dialog.setContentView(actionBinding.root)
        dialogAddLevel.setContentView(addlevelbinding.root)

        binding.fabAddLevel.setOnClickListener {
            addlevelbinding.inputLevelName.setText("")

            btnSave.setOnClickListener {
                val level = addlevelbinding.inputLevelName.text.toString()
                if (validate()) {
                    binding.loadingAnim.visibility = View.VISIBLE
                    insertLevel(level, tokenAuth, dialogAddLevel)
                }
            }

            dialogAddLevel.setCancelable(true)
            dialogAddLevel.show()
        }

        levelAdapter.setOnItemClickCallback(object : LevelAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<LevelEntity>, position: Int) {
                actionBinding.llEdit.setOnClickListener {
                    addlevelbinding.inputLevelName.setText(data[position].level)

                    btnSave.setOnClickListener {
                        val idlevel = data[position].idlevel
                        val level = addlevelbinding.inputLevelName.text.toString()
                        if (validate()) {
                            binding.loadingAnim.visibility = View.VISIBLE
                            updateLevel(idlevel, level, tokenAuth, dialogAddLevel)
                        }
                        dialogAddLevel.dismiss()
                    }

                    dialog.dismiss()
                    dialogAddLevel.setCancelable(true)
                    dialogAddLevel.show()
                }

                actionBinding.llShowAccess.setOnClickListener {
                    val idlevel = data[position].idlevel
                    startActivity(Intent(this@LevelActivity, ViewAccessActivity::class.java)
                        .apply {
                            putExtra(ViewAccessActivity.idlevel, idlevel)
                        })
                    finish()
                    dialog.dismiss()
                }

                actionBinding.llDelete.setOnClickListener {
                    val idlevel = data[position].idlevel
                    val deleteDialog = MaterialDialog.Builder(this@LevelActivity)
                        .setTitle(getString(R.string.confirm_delete_level_title))
                        .setMessage(getString(R.string.confirm_delete_level_desc, data[position].level))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.no), R.drawable.ic_close)
                        { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton(getString(R.string.yes), R.drawable.ic_trash)
                        { dialogInterface, _ ->
                            deleteLevel(idlevel, tokenAuth, dialogInterface, position)
                            dialogInterface.dismiss()
                        }
                        .build()
                    deleteDialog.show()
                    dialog.dismiss()
                }

                dialog.setCancelable(true)
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
                        levelAdapter.setItem(listData)
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
                        levelAdapter.setItem(listData)
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

    private fun validate(): Boolean {
        return if (addlevelbinding.inputLevelName.text.toString() == "") {
            addlevelbinding.inputLevelName.error = getString(R.string.level_cant_empty)
            addlevelbinding.inputLevelName.requestFocus()
            false
        } else true
    }

    private fun insertLevel(level: String, tokenAuth: String, dialog: BottomSheetDialog) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.insertLevel(level, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        getLevel(tokenAuth)
                        dialog.dismiss()
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@LevelActivity,
                        "insertLevel | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@LevelActivity,
                    "insertLevel | onFailure", t.message.toString()
                )
            }
        })
    }

    private fun updateLevel(idlevel: String, level: String, tokenAuth: String, dialog: BottomSheetDialog) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.updateLevel(idlevel, level, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        getLevel(tokenAuth)
                        dialog.dismiss()
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@LevelActivity,
                        "updateLevel | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@LevelActivity,
                    "updateLevel | onFailure", t.message.toString()
                )
            }
        })
    }

    private fun deleteLevel(idlevel: String, tokenAuth: String, dialogInterface: DialogInterface, position: Int) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.deleteLevel(idlevel, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        listData.removeAt(position)
                        levelAdapter.setItem(listData)
                        levelAdapter.notifyItemRemoved(position)
                        dialogInterface.dismiss()
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@LevelActivity,
                        "deleteLevel | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@LevelActivity,
                    "deleteLevel | onFailure", t.message.toString()
                )
            }
        })
    }

}