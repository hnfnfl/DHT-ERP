package com.jaylangkung.dht

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.dht.databinding.ActivityMainBinding
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.descriptionText
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.widget.AccountHeaderView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        // memunculkan tombol burger menu
//        supportActionBar?.setHomeButtonEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//
//        // untuk toggle open dan close navigation
//        mToggle = ActionBarDrawerToggle(this@MainActivity, binding.drawerLayout, R.string.open, R.string.close)
//        // tambahkan mToggle ke drawer_layout sebagai pengendali open dan close drawer
//        binding.drawerLayout.addDrawerListener(mToggle)
//        mToggle.syncState()

        binding.slider.onDrawerItemClickListener = { v, drawerItem, position ->
            // do something with the clicked item :D
            false
        }

//        var headerView = AccountHeaderView(this).apply {
//            attachToSliderView(binding.slider) // attach to the slider
//            addProfiles(
//                ProfileDrawerItem().apply { nameText = "Mike Penz"; descriptionText = "mikepenz@gmail.com"; iconRes = R.drawable.ic_profile; identifier = 102 }
//            )
//            onAccountHeaderListener = { view, profile, current ->
//                // react to profile changes
//                false
//            }
//            withSavedInstance(savedInstanceState)
//        }
    }
}