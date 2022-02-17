package com.jaylangkung.dht.dht.work_order

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.dht.databinding.ActivityBreakdownBinding
import com.jaylangkung.dht.utils.MySharedPreferences
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable

class BreakdownActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBreakdownBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var breakdownAdapter: BreakdownAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreakdownBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        val obj = JSONObject(intent.getStringExtra("dataProduct"))
        for (i in 0 until obj.length()){
            val dataProduct = obj.get(i.toString()).toString()
            dataProduct[0].toString()
            Log.e("debug", dataProduct)
        }
    }

    override fun onBackPressed() {
        startActivity(Intent(this@BreakdownActivity, WorkOrderActivity::class.java))
        finish()
    }
}