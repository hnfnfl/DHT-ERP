package com.jaylangkung.dht.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.brainnet_staff.retrofit.response.DefaultResponse
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityAdminChangePassBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class AdminChangePassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminChangePassBinding
    private lateinit var myPreferences: MySharedPreferences

    companion object {
        const val idadmin = "idadmin"
        const val name = "name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChangePassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@AdminChangePassActivity)

        val idadmin = intent.getStringExtra(idadmin).toString()
        val name = intent.getStringExtra(name).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.tvAdminName.text = getString(R.string.change_pass_name, name)

        binding.btnSavePassword.setOnClickListener {
            if (validate()) {
                val pass = binding.tvAdminChangePass.text.toString()
                binding.btnSavePassword.startAnimation()
                updatePassword(idadmin, pass, tokenAuth)
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@AdminChangePassActivity, AdminActivity::class.java))
        finish()
    }

    private fun validate(): Boolean {
        val pass = binding.tvAdminChangePass.text.toString()
        val passConfirm = binding.tvAdminChangePassConfirm.text.toString()

        val passwordREGEX = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$"
        )

        when {
            pass == "" -> {
                binding.tvAdminError.visibility = View.GONE
                binding.tvAdminChangePass.error = getString(R.string.password_cant_empty)
                binding.tvAdminChangePass.requestFocus()
                return false
            }
            passConfirm == "" -> {
                binding.tvAdminError.visibility = View.GONE
                binding.tvAdminChangePassConfirm.error = getString(R.string.password_cant_empty)
                binding.tvAdminChangePassConfirm.requestFocus()
                return false
            }
            !passwordREGEX.matcher(pass).matches() -> {
                binding.tvAdminError.visibility = View.VISIBLE
                binding.tvAdminError.text = getString(R.string.pass_not_validated_desc)
                return false
            }
            pass != passConfirm -> {
                binding.tvAdminError.visibility = View.VISIBLE
                binding.tvAdminError.text = getString(R.string.pass_not_match)
                return false
            }
            else -> {
                binding.tvAdminError.visibility = View.GONE
                return true
            }
        }
    }

    private fun updatePassword(idadmin: String, password: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.updatePassword(idadmin, password, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        Toasty.success(this@AdminChangePassActivity, response.body()!!.message, Toasty.LENGTH_SHORT).show()
                        startActivity(Intent(this@AdminChangePassActivity, AdminActivity::class.java))
                        finish()
                    }
                } else {
                    binding.btnSavePassword.endAnimation()
                    ErrorHandler().responseHandler(
                        this@AdminChangePassActivity,
                        "loginProcess | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.btnSavePassword.endAnimation()
                ErrorHandler().responseHandler(
                    this@AdminChangePassActivity,
                    "loginProcess | onResponse", t.message.toString()
                )
            }
        })
    }
}