package com.jaylangkung.dht.dht.packing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.dht.MainActivity
import com.jaylangkung.dht.databinding.ActivityPackingBinding

class PackingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabScanner.setOnClickListener {
            startActivity(Intent(this@PackingActivity, PackingScannerActivity::class.java))
            finish()
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@PackingActivity, MainActivity::class.java))
        finish()
    }
}