package com.jaylangkung.dht

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.dht.databinding.ActivityMainBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.MenuResponse
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import com.mikepenz.iconics.typeface.library.googlematerial.GoogleMaterial
import com.mikepenz.materialdrawer.iconics.iconicsIcon
import com.mikepenz.materialdrawer.model.*
import com.mikepenz.materialdrawer.model.interfaces.*
import com.mikepenz.materialdrawer.util.*
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import es.dmoral.toasty.Toasty
import okio.ArrayIndexOutOfBoundsException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var myPreferences: MySharedPreferences
    private lateinit var headerView: AccountHeaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@MainActivity)

        val idlevel = myPreferences.getValue(Constants.USER_IDLEVEL).toString()
        val imgProfile = myPreferences.getValue(Constants.FOTO_PATH).toString()
        val name = myPreferences.getValue(Constants.USER_NAMA).toString()
        val email = myPreferences.getValue(Constants.USER_EMAIL).toString()
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

        val profile = ProfileDrawerItem().apply {
            nameText = name; descriptionText = email; iconUrl = imgProfile; identifier = 1
        }

        // Create the AccountHeader
        headerView = AccountHeaderView(this).apply {
            attachToSliderView(binding.slider)
            addProfiles(profile)
            withSavedInstance(savedInstanceState)
        }

        getMenu(idlevel, tokenAuth)

    }

    override fun onBackPressed() {
        if (binding.root.isDrawerOpen(binding.slider)) {
            binding.root.closeDrawer(binding.slider)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        private const val PROFILE_SETTING = 100000
    }

    private fun getMenu(idlevel: String, tokenAuth: String) {

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
                                            nameText = menu[i].menu; identifier = idmodul.toLong(); isSelectable = false; iconRes = iconMenu[idmodul]
                                        } catch (exception: ArrayIndexOutOfBoundsException) {
                                            nameText = menu[i].menu; identifier = idmodul.toLong(); isSelectable = false; iconicsIcon =
                                                GoogleMaterial.Icon.gmd_favorite
                                        }

                                        for (j in 0 until subMenu.size) {
                                            val idmodulSub = subMenu[j].idmodul.toInt()
                                            subItems.add(SecondaryDrawerItem().apply {

                                                try { //index out of bound exception handler
                                                    nameText = subMenu[j].menu; level = 2; identifier = idmodulSub.toLong(); iconRes =
                                                        iconMenu[idmodulSub]
                                                } catch (exception: ArrayIndexOutOfBoundsException) {
                                                    nameText = subMenu[j].menu; level = 2; identifier = idmodulSub.toLong(); iconicsIcon =
                                                        GoogleMaterial.Icon.gmd_favorite
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
                                        } catch (exception: ArrayIndexOutOfBoundsException) {
                                            PrimaryDrawerItem().apply {
                                                nameText = menu[i].menu; identifier = idmodul.toLong(); iconicsIcon =
                                                GoogleMaterial.Icon.gmd_favorite
                                            }
                                        }

                                    )
                                }
                            }
                        }

                        binding.slider.apply {
                            addItems(
                                DividerDrawerItem(),
                                PrimaryDrawerItem().apply {
                                    nameText = context.getString(R.string.setting); identifier = 97; iconRes = R.drawable.ic_setting
                                },
                                PrimaryDrawerItem().apply {
                                    nameText = context.getString(R.string.about_app); identifier = 98; iconRes = R.drawable.ic_setting
                                },
                            )
                            addStickyFooterItem(
                                PrimaryDrawerItem().apply {
                                    nameText = context.getString(R.string.logout); identifier = 99; iconRes = R.drawable.ic_logout
                                }
                            )
                        }

                        binding.slider.onDrawerItemClickListener = { _, drawerItem, _ ->
                            var intent: Intent? = null
                            when (drawerItem is Nameable) {
                                else -> Toasty.warning(this@MainActivity, drawerItem.identifier.toString(), Toasty.LENGTH_SHORT).show()
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
}