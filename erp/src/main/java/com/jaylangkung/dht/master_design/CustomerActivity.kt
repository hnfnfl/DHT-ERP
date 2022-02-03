package com.jaylangkung.dht.master_design

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jaylangkung.dht.databinding.ActivityCustomerBinding

class CustomerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}