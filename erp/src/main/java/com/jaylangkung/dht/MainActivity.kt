package com.jaylangkung.dht

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.retrofit.response.DefaultResponse
import com.jaylangkung.dht.administrator.admin.AdminActivity
import com.jaylangkung.dht.administrator.admin.EditProfileActivity
import com.jaylangkung.dht.administrator.level.LevelActivity
import com.jaylangkung.dht.auth.LoginActivity
import com.jaylangkung.dht.auth.LoginWebAppActivity
import com.jaylangkung.dht.databinding.ActivityMainBinding
import com.jaylangkung.dht.dht.inquiries.InquiriesActivity
import com.jaylangkung.dht.inquiries_progress.InquiriesProcessAdapter
import com.jaylangkung.dht.inquiries_progress.InquiriesProcessEntity
import com.jaylangkung.dht.master_design.customer.CustomerActivity
import com.jaylangkung.dht.master_design.goods.GoodsActivity
import com.jaylangkung.dht.master_design.product.ProductActivity
import com.jaylangkung.dht.master_design.shipment.ShipmentActivity
import com.jaylangkung.dht.master_design.supplier.SupplierActivity
import com.jaylangkung.dht.retrofit.AuthService
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.DhtService
import com.jaylangkung.dht.retrofit.response.InquiriesProcessResponse
import com.jaylangkung.dht.retrofit.response.MenuResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.*
import dev.shreyaspatil.MaterialDialog.MaterialDialog
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var inquiriesProcessAdapter: InquiriesProcessAdapter
    private var listData: ArrayList<InquiriesProcessEntity> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@MainActivity)
        inquiriesProcessAdapter = InquiriesProcessAdapter()

        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA),
                100
            )
        }

        val idlevel = myPreferences.getValue(Constants.USER_IDLEVEL).toString()
        val imgProfile = myPreferences.getValue(Constants.FOTO_PATH).toString()
        val name = myPreferences.getValue(Constants.USER_NAMA).toString()
        val idadmin = myPreferences.getValue(Constants.USER_IDADMIN).toString()
        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())

        binding.show.setOnClickListener {
            binding.root.openDrawer(binding.slider)
        }

        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        binding.tvGreetings.text = when (currentHour) {
            in 4..11 -> getString(R.string.greetings, getString(R.string.greeting_morning), name)
            in 12..14 -> getString(R.string.greetings, getString(R.string.greeting_afternoonA), name)
            in 15..17 -> getString(R.string.greetings, getString(R.string.greeting_afternoonB), name)
            else -> getString(R.string.greetings, getString(R.string.greeting_evening), name)
        }

        Glide.with(this@MainActivity)
            .load(imgProfile)
            .apply(RequestOptions().override(100))
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(binding.imgProfile)

        getMenu(idadmin, idlevel, tokenAuth)
        getPersentaseProses(tokenAuth)

        binding.fabWebApp.setOnClickListener {
            startActivity(Intent(this@MainActivity, LoginWebAppActivity::class.java))
            finish()
        }

        binding.llBody.setOnRefreshListener {
            getPersentaseProses(tokenAuth)
        }
    }

    override fun onBackPressed() {
        if (binding.root.isDrawerOpen(binding.slider)) {
            binding.root.closeDrawer(binding.slider)
        } else {
            super.onBackPressed()
        }
    }

    private fun getMenu(idadmin: String, idlevel: String, tokenAuth: String) {

        val iconMenu: ArrayList<Int> = arrayListOf(
            R.drawable.ic_dashboard, //0
            R.drawable.ic_dashboard, //1
            R.drawable.ic_setting, //2
            R.drawable.ic_administrator, //3
            R.drawable.ic_admin, //4
            R.drawable.ic_sticky_note, //5
            R.drawable.ic_sitemap, //6
            R.drawable.ic_building, //7
            R.drawable.ic_clipboard, //8
            R.drawable.ic_profile, //9
            R.drawable.ic_building, //10
            R.drawable.ic_administrator, //11
            R.drawable.ic_wrench, //12
            R.drawable.ic_setting, //13
            R.drawable.ic_folder, //14
            R.drawable.ic_process, //15
            R.drawable.ic_bug, //16
            R.drawable.ic_administrator, //17
            R.drawable.ic_product, //18

        )

        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getMenu(idlevel, tokenAuth).enqueue(object : Callback<MenuResponse> {
            override fun onResponse(call: Call<MenuResponse>, response: Response<MenuResponse>) {
                if (response.isSuccessful) {
                    if (response.body()!!.status == "success") {
                        val menu = response.body()!!.data
                        for (i in 0 until menu.size) {
                            val idmodul = menu[i].idmodul.toInt()
                            binding.slider.apply {
                                val subMenu = menu[i].sub_menu
                                if (!subMenu.isNullOrEmpty()) {
                                    val item = ExpandableDrawerItem().apply {

                                        try { //index out of bound exception handler
                                            nameText = menu[i].menu; identifier = idmodul.toLong(); isSelectable = false; iconRes =
                                                iconMenu[idmodul]; icon
                                        } catch (exception: IndexOutOfBoundsException) {
                                            nameText = menu[i].menu; identifier = idmodul.toLong(); isSelectable = false; iconicsIcon =
                                                GoogleMaterial.Icon.gmd_dashboard
                                        }

                                        for (j in 0 until subMenu.size) {
                                            val idmodulSub = subMenu[j].idmodul.toInt()
                                            subItems.add(SecondaryDrawerItem().apply {

                                                try { //index out of bound exception handler
                                                    nameText = subMenu[j].menu; level = 2; identifier = idmodulSub.toLong(); iconRes =
                                                        iconMenu[idmodulSub]
                                                } catch (exception: IndexOutOfBoundsException) {
                                                    nameText = subMenu[j].menu; level = 2; identifier = idmodulSub.toLong(); iconicsIcon =
                                                        GoogleMaterial.Icon.gmd_dashboard
                                                }

                                            })
                                        }
                                    }
                                    binding.slider.itemAdapter.add(item)

                                } else {
                                    addItems(

                                        try { //index out of bound exception handler
                                            PrimaryDrawerItem().apply {
                                                nameText = menu[i].menu; identifier = idmodul.toLong(); iconRes = iconMenu[idmodul]
                                            }
                                        } catch (exception: IndexOutOfBoundsException) {
                                            PrimaryDrawerItem().apply {
                                                nameText = menu[i].menu; identifier = idmodul.toLong(); iconicsIcon =
                                                GoogleMaterial.Icon.gmd_dashboard
                                            }
                                        }

                                    )
                                }
                            }
                        }

                        binding.slider.apply {
                            addStickyDrawerItems(
                                SecondaryDrawerItem().apply {
                                    nameText = context.getString(R.string.about_app); identifier = 98; iconRes = R.drawable.ic_setting
                                },
                                PrimaryDrawerItem().apply {
                                    nameText = context.getString(R.string.logout); identifier = 99; iconRes = R.drawable.ic_logout
                                }
                            )
                        }

                        binding.slider.onDrawerItemClickListener = { _, drawerItem, _ ->
                            var intent: Intent? = null
                            val context: Context = this@MainActivity
                            when (drawerItem.identifier) {
                                1L -> intent = Intent(context, MainActivity::class.java)
                                2L -> intent = Intent(context, MainActivity::class.java)
                                3L -> Log.d("menu", drawerItem.identifier.toString())
                                4L -> intent = Intent(context, AdminActivity::class.java)
                                5L -> intent = Intent(context, MainActivity::class.java)
                                6L -> intent = Intent(context, LevelActivity::class.java)
                                7L -> intent = Intent(context, MainActivity::class.java)
                                8L -> intent = Intent(context, MainActivity::class.java)
                                9L -> intent = Intent(context, EditProfileActivity::class.java)
                                    .apply {
                                        putExtra(EditProfileActivity.idadmin, myPreferences.getValue(Constants.USER_IDADMIN).toString())
                                        putExtra(EditProfileActivity.level, myPreferences.getValue(Constants.USER_LEVEL).toString())
                                        putExtra(EditProfileActivity.departemen, myPreferences.getValue(Constants.USER_DEPARTEMEN).toString())
                                        putExtra(EditProfileActivity.nama, myPreferences.getValue(Constants.USER_NAMA).toString())
                                        putExtra(EditProfileActivity.email, myPreferences.getValue(Constants.USER_EMAIL).toString())
                                        putExtra(EditProfileActivity.alamat, myPreferences.getValue(Constants.USER_ALAMAT).toString())
                                        putExtra(EditProfileActivity.telp, myPreferences.getValue(Constants.USER_TELP).toString())
                                        putExtra(EditProfileActivity.img, myPreferences.getValue(Constants.FOTO_PATH).toString())
                                        putExtra(EditProfileActivity.from, "MainActivity")
                                    }
                                10L -> Log.d("menu", drawerItem.identifier.toString())
                                11L -> intent = Intent(context, CustomerActivity::class.java)
                                12L -> Log.d("menu", drawerItem.identifier.toString())
                                13L -> intent = Intent(context, MainActivity::class.java)
                                14L -> intent = Intent(context, MainActivity::class.java)
                                15L -> intent = Intent(context, MainActivity::class.java)
                                16L -> intent = Intent(context, MainActivity::class.java)
                                17L -> intent = Intent(context, SupplierActivity::class.java)
                                18L -> intent = Intent(context, ProductActivity::class.java)

                                20L -> intent = Intent(context, InquiriesActivity::class.java)

                                24L -> intent = Intent(context, ShipmentActivity::class.java)
                                25L -> intent = Intent(context, GoodsActivity::class.java)

                                99L -> logout(idadmin)
                                else -> Toasty.warning(context, getString(R.string.menu_not_avail), Toasty.LENGTH_SHORT).show()
                            }

                            if (intent != null) {
                                this@MainActivity.startActivity(intent)
                            }

                            false
                        }
                    }
                } else {
                    ErrorHandler().responseHandler(
                        this@MainActivity,
                        "getMenu | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<MenuResponse>, t: Throwable) {
                ErrorHandler().responseHandler(
                    this@MainActivity,
                    "getMenu | onFailure", t.message.toString()
                )
            }
        })
    }

    private fun logout(idadmin: String) {
        val service = RetrofitClient().apiRequest().create(AuthService::class.java)
        val mDialog = MaterialDialog.Builder(this@MainActivity)
            .setTitle(getString(R.string.logout))
            .setMessage(getString(R.string.confirm_logout))
            .setCancelable(true)
            .setPositiveButton(
                getString(R.string.no), R.drawable.ic_close
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
            .setNegativeButton(
                getString(R.string.yes), R.drawable.ic_logout
            ) { dialogInterface, _ ->
                myPreferences.setValue(Constants.TokenAuth, "")
                myPreferences.setValue(Constants.USER, "")
                myPreferences.setValue(Constants.USER_IDADMIN, "")
                myPreferences.setValue(Constants.USER_EMAIL, "")
                myPreferences.setValue(Constants.USER_NAMA, "")
                myPreferences.setValue(Constants.USER_ALAMAT, "")
                myPreferences.setValue(Constants.USER_TELP, "")
                myPreferences.setValue(Constants.FOTO_PATH, "")
                myPreferences.setValue(Constants.DEVICE_TOKEN, "")
                myPreferences.setValue(Constants.USER_IDLEVEL, "")
                myPreferences.setValue(Constants.USER_LEVEL, "")
                myPreferences.setValue(Constants.USER_IDDEPARTEMEN, "")
                myPreferences.setValue(Constants.USER_DEPARTEMEN, "")

                service.logout(idadmin).enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if (response.isSuccessful) {
                            if (response.body()!!.status == "success") {
                                Toasty.success(this@MainActivity, getString(R.string.logout_success), Toasty.LENGTH_LONG).show()
                                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                                finish()
                                dialogInterface.dismiss()
                            }
                        } else {
                            ErrorHandler().responseHandler(
                                this@MainActivity,
                                "logout | onResponse", response.message()
                            )
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        ErrorHandler().responseHandler(
                            this@MainActivity,
                            "logout | onFailure", t.message.toString()
                        )
                    }
                })

            }
            .build()
        mDialog.show()
    }

    private fun getPersentaseProses(tokenAuth: String) {
        val service = RetrofitClient().apiRequest().create(DhtService::class.java)
        service.getPersentaseProses(tokenAuth).enqueue(object : Callback<InquiriesProcessResponse> {
            override fun onResponse(call: Call<InquiriesProcessResponse>, response: Response<InquiriesProcessResponse>) {
                if (response.isSuccessful) {
                    binding.llBody.isRefreshing = false
                    if (response.body()!!.status == "success") {
                        binding.loadingAnim.visibility = View.GONE
                        listData = response.body()!!.data
                        inquiriesProcessAdapter.setItem(listData)
                        inquiriesProcessAdapter.notifyItemRangeChanged(0, listData.size)

                        with(binding.rvInquiriesProcess) {
                            layoutManager = LinearLayoutManager(this@MainActivity)
                            itemAnimator = DefaultItemAnimator()
                            setHasFixedSize(true)
                            adapter = inquiriesProcessAdapter
                        }
                    }
                } else {
                    ErrorHandler().responseHandler(
                        this@MainActivity,
                        "getPersentaseProses | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<InquiriesProcessResponse>, t: Throwable) {
                binding.llBody.isRefreshing = false
                ErrorHandler().responseHandler(
                    this@MainActivity,
                    "getPersentaseProses | onFailure", t.message.toString()
                )
            }
        })
    }

//    private fun insertJsonObject() {
//        val data: MutableList<JSONObject> = ArrayList<JSONObject>()
//        for (i in 0..1) {
//            val obj = JSONObject()
//            obj.put("id", i)
//            obj.put("parameter", "test")
//            obj.put("value", "anjay")
//            data.add(obj)
//        }
//        Log.e("json data", data.toString())
//    }
}