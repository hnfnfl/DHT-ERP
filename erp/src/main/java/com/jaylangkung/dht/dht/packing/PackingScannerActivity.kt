package com.jaylangkung.dht.dht.packing

import android.content.Intent
import android.os.*
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityPackingScannerBinding
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.DefaultResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PackingScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackingScannerBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var scanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPackingScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@PackingScannerActivity)

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        scanner = CodeScanner(this@PackingScannerActivity, binding.scannerView)
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
                updatePackingBarcode(it.text, tokenAuth)
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
        startActivity(Intent(this@PackingScannerActivity, PackingActivity::class.java))
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

    private fun updatePackingBarcode(kode: String, tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.updatePackingBarcode(kode, tokenAuth).enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.isSuccessful) {
                    binding.loadingAnim.visibility = View.GONE
                    vibrate()
                    if (response.body()!!.status == "success") {
                        Toasty.success(this@PackingScannerActivity, "Data berhasil diupdate", Toasty.LENGTH_SHORT).show()
                        scanner.startPreview()
                    }
                } else {
                    binding.loadingAnim.visibility = View.GONE
                    ErrorHandler().responseHandler(
                        this@PackingScannerActivity,
                        "updatePackingBarcode | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                binding.loadingAnim.visibility = View.GONE
                ErrorHandler().responseHandler(
                    this@PackingScannerActivity,
                    "updatePackingBarcode | onFailure", t.message.toString()
                )
            }
        })
    }
}