package com.jaylangkung.dht.admin

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
import com.jaylangkung.dht.databinding.ActivityAdminBinding
import com.jaylangkung.dht.databinding.BottomSheetAdminActionBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.AdminResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var bsBinding: BottomSheetAdminActionBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var adminAdapter: AdminAdapter
    private var listData: ArrayList<AdminEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@AdminActivity)
        adminAdapter = AdminAdapter()

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getAllAdmin(tokenAuth)

        adminAdapter.setOnItemClickCallback(object : AdminAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ArrayList<AdminEntity>, position: Int) {
                bsBinding = BottomSheetAdminActionBinding.inflate(layoutInflater)
                val dialog = BottomSheetDialog(this@AdminActivity)
                val idadmin = data[position].idadmin
                val name = data[position].nama
                val email = data[position].email
                val level = data[position].level
                val departemen = data[position].departemen
                val alamat = data[position].alamat
                val telp = data[position].telp
                val img = data[position].img

                bsBinding.llEdit.setOnClickListener {
                    startActivity(Intent(this@AdminActivity, EditProfileActivity::class.java)
                        .apply {
                            putExtra(EditProfileActivity.idadmin, idadmin)
                            putExtra(EditProfileActivity.level, level)
                            putExtra(EditProfileActivity.departemen, departemen)
                            putExtra(EditProfileActivity.nama, name)
                            putExtra(EditProfileActivity.email, email)
                            putExtra(EditProfileActivity.alamat, alamat)
                            putExtra(EditProfileActivity.telp, telp)
                            putExtra(EditProfileActivity.img, img)
                        })
                    finish()
                    dialog.dismiss()
                }

                bsBinding.llChangePass.setOnClickListener {
                    startActivity(Intent(this@AdminActivity, ChangePassActivity::class.java)
                        .apply {
                            putExtra(ChangePassActivity.idadmin, idadmin)
                            putExtra(ChangePassActivity.nama, name)
                        })
                    finish()
                    dialog.dismiss()
                }

                bsBinding.llResetLogin.setOnClickListener {
                    val mDialog = MaterialDialog.Builder(this@AdminActivity)
                        .setTitle(getString(R.string.confirm_reset_login_title))
                        .setMessage(getString(R.string.confirm_reset_login_desc, name))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.yes), R.drawable.ic_restart)
                        { dialogInterface, _ ->
                            deleteSecurity(idadmin, tokenAuth, dialogInterface)
                            Toasty.info(this@AdminActivity, getString(R.string.please_wait), Toasty.LENGTH_LONG).show()
                        }
                        .setNegativeButton(getString(R.string.no), R.drawable.ic_close)
                        { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .build()
                    mDialog.show()
                    dialog.dismiss()
                }

                bsBinding.llDelete.setOnClickListener {
                    val mDialog = MaterialDialog.Builder(this@AdminActivity)
                        .setTitle(getString(R.string.confirm_delete_title))
                        .setMessage(getString(R.string.confirm_delete_desc, name))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.no), R.drawable.ic_close)
                        { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        .setNegativeButton(getString(R.string.yes), R.drawable.ic_trash)
                        { dialogInterface, _ ->
                            deleteAdmin(idadmin, tokenAuth, dialogInterface, position)
                            Toasty.info(this@AdminActivity, getString(R.string.please_wait), Toasty.LENGTH_SHORT).show()
                        }
                        .build()
                    mDialog.show()
                    dialog.dismiss()
                }
                dialog.setCancelable(true)
                dialog.setContentView(bsBinding.root)
                dialog.show()
            }
        })
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminActivity, MainActivity::class.java))
        finish()
    }

    private fun getAllAdmin(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getAllAdmin(tokenAuth).enqueue(object : Callback<AdminResponse> {
            override fun onResponse(call: Call<AdminResponse>, response: Response<AdminResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        binding.empty.visibility = View.GONE
                        listData = response.body()!!.data
                        adminAdapter.setAdminItem(listData)
                        adminAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvAdmin) {
                            layoutManager = LinearLayoutManager(this@AdminActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = adminAdapter
                        }
                    } else if (response.body()!!.status == "empty") {
                        binding.empty.visibility = View.VISIBLE
                        binding.loadingAnim.visibility = View.GONE
                        listData.clear()
                        adminAdapter.setAdminItem(listData)
                        adminAdapter.notifyItemRangeChanged(0, listData.size)
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@AdminActivity,
                        "getAllAdmin | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<AdminResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@AdminActivity,
                    "getAllAdmin | onFailure", t.message.toString()
                )
            }
        })
    }

    private fun deleteSecurity(idadmin: String, tokenAuth: String, dialogInterface: DialogInterface) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.deleteSecurity(idadmin, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        dialogInterface.dismiss()
                        Toasty.success(this@AdminActivity, getString(R.string.login_reset_success), Toasty.LENGTH_LONG).show()
                    } else {
                        binding.loadingAnim.visibility = View.GONE
                        ErrorHandler().responseHandler(
                            this@AdminActivity,
                            "deleteSecurity | onResponse", response.message()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@AdminActivity,
                    "deleteSecurity | onFailure", t.message.toString()
                )
            }
        })
    }

    private fun deleteAdmin(idadmin: String, tokenAuth: String, dialogInterface: DialogInterface, position: Int) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.deleteAdmin(idadmin, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        listData.removeAt(position)
                        adminAdapter.setAdminItem(listData)
                        adminAdapter.notifyItemRemoved(position)
                        binding.loadingAnim.visibility = View.GONE
                        dialogInterface.dismiss()
                        Toasty.success(this@AdminActivity, getString(R.string.delete_user_success), Toasty.LENGTH_LONG).show()
                    } else {
                        binding.loadingAnim.visibility = View.GONE
                        ErrorHandler().responseHandler(
                            this@AdminActivity,
                            "deleteSecurity | onResponse", response.message()
                        )
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@AdminActivity,
                    "deleteSecurity | onFailure", t.message.toString()
                )
            }
        })
    }

}

