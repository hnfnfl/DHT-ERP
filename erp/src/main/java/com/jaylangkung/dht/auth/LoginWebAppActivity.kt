package com.jaylangkung.dht.auth

import android.content.Intent
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.retrofit.response.DefaultResponse
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityLoginWebAppBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginWebAppActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginWebAppBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var scanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginWebAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@LoginWebAppActivity)

        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        scanner = CodeScanner(this@LoginWebAppActivity, binding.scannerView)
        // Parameters (default values)
        scanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        scanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        scanner.autoFocusMode = AutoFocusMode.CONTINUOUS // or CONTINUOUS
        scanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        scanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        scanner.isFlashEnabled = false // Whether to enable flash or not
        scanner.startPreview()

        // Callbacks
        scanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                binding.loadingAnim.visibility = View.VISIBLE
                if (it.text.contains("webapp", ignoreCase = true)) {
                    insertWebApp(idadmin, it.text, tokenAuth)
                } else {
                    Toasty.warning(this@LoginWebAppActivity, getString(R.string.qrcode_not_match), Toasty.LENGTH_LONG).show()
                    onBackPressed()
                }
            }
        }
        scanner.errorCallback = ErrorCallback.SUPPRESS

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        scanner.startPreview()
    }

    override fun onPause() {
        scanner.releaseResources()
        super.onPause()
    }

    override fun onBackPressed() {
        startActivity(Intent(this@LoginWebAppActivity, MainActivity::class.java))
        finish()
    }

    private fun vibrate() {
        val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }

    }

    private fun insertWebApp(idadmin: String, device_id: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.insertWebApp(idadmin, device_id, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                binding.loadingAnim.visibility = View.GONE
                if (response.isSuccessful) {
                    vibrate()
                    if (response.body()!!.status == "success") {
                        Toasty.success(this@LoginWebAppActivity, getString(R.string.login_webapp_success), Toasty.LENGTH_LONG).show()
                        onBackPressed()
                    }
                } else {
                    ErrorHandler().responseHandler(
                        this@LoginWebAppActivity,
                        "insertWebApp | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@LoginWebAppActivity,
                    "insertWebApp | onResponse", t.message.toString()
                )
            }
        })
    }
}