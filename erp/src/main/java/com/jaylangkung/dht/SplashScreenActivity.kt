package com.jaylangkung.dht

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.dht.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            //Ketika user sudah login tidak perlu ke halaman login lagi
//            if (myPreferences.getValue(Constants.USER).equals(Constants.LOGIN)) {
//                val email = myPreferences.getValue(Constants.USER_EMAIL).toString()
//                val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
//                val deviceID = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
//                refreshAuthToken(email, idadmin, "hp.$deviceID")
//            } else {
//                startActivity(Intent(this@SplashScreenActivity, LoginActivity::class.java))
//                finish()
//            }
            startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        }, 500)
    }
}