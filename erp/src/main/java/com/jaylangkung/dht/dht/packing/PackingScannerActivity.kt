package com.jaylangkung.dht.dht.packing

import android.content.Intent
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import com.budiyev.android.codescanner.*
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityPackingScannerBinding
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty

class PackingScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackingScannerBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var scanner: CodeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPackingScannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@PackingScannerActivity)

        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        scanner = CodeScanner(this@PackingScannerActivity, binding.scannerView)
        // Parameters (default values)
        scanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        scanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        scanner.autoFocusMode = AutoFocusMode.CONTINUOUS // or CONTINUOUS
        scanner.scanMode = ScanMode.CONTINUOUS // or CONTINUOUS or PREVIEW
        scanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        scanner.isFlashEnabled = false // Whether to enable flash or not
        scanner.startPreview()

        // Callbacks
        scanner.decodeCallback = DecodeCallback {
            runOnUiThread {
//                binding.loadingAnim.visibility = View.VISIBLE
                Toasty.success(this@PackingScannerActivity, it.text, Toasty.LENGTH_SHORT).show()
                scanner.stopPreview()
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
}