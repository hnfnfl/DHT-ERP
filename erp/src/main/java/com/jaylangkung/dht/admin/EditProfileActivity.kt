package com.jaylangkung.dht.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_kan.utils.FileUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import com.jaylangkung.brainnet_staff.retrofit.RetrofitClient
import com.jaylangkung.brainnet_staff.retrofit.response.DefaultResponse
import com.jaylangkung.dht.R
import com.jaylangkung.dht.databinding.ActivityEditProfileBinding
import com.jaylangkung.dht.retrofit.DataService
import com.jaylangkung.dht.retrofit.response.DataSpinnerResponse
import com.jaylangkung.dht.retrofit.spinnerData.DataSpinnerEntity
import com.jaylangkung.dht.utils.Constants
import com.jaylangkung.dht.utils.ErrorHandler
import com.jaylangkung.dht.utils.MySharedPreferences
import es.dmoral.toasty.Toasty
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var myPreferences: MySharedPreferences
    var photoUri: Uri? = null

    private var listLevel: ArrayList<DataSpinnerEntity> = arrayListOf()
    private var listDepartment: ArrayList<DataSpinnerEntity> = arrayListOf()

    private var idlevel: String = ""
    private var iddepartment: String = ""

    companion object {
        const val idadmin = "idadmin"
        const val level = "level"
        const val departemen = "departemen"
        const val nama = "nama"
        const val alamat = "alamat"
        const val telp = "telp"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myPreferences = MySharedPreferences(this@EditProfileActivity)

        val tokenAuth = getString(R.string.token_auth, myPreferences.getValue(Constants.TokenAuth).toString())
        val idadmin = intent.getStringExtra(idadmin).toString()
        val level = intent.getStringExtra(level).toString()
        val departemen = intent.getStringExtra(departemen).toString()
        val nama = intent.getStringExtra(nama).toString()
        val alamat = intent.getStringExtra(alamat).toString()
        val telp = intent.getStringExtra(telp).toString()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        getSpinnerData(tokenAuth, level, departemen)

        binding.tvEditAdminName.setText(nama)
        binding.tvEditAdminAddress.setText(alamat)
        binding.tvEditAdminPhone.setText(telp)

        binding.btnChangeImg.setOnClickListener {
            ImagePicker.with(this)
                .cropSquare()
                .compress(1024)
                .maxResultSize(720, 720)
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val fileUri = data?.data
                            this.photoUri = fileUri
                            binding.imgProfile.setImageURI(fileUri)
                            ImagePicker.getFile(data)
                            ImagePicker.getFilePath(data).toString()
                        }
                        ImagePicker.RESULT_ERROR -> {
                            Toasty.warning(this@EditProfileActivity, ImagePicker.getError(data), Toasty.LENGTH_LONG).show()
                        }
                        else -> {
                            Toasty.normal(this@EditProfileActivity, "Task Cancelled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

        binding.btnSaveEditProfile.setOnClickListener {
            Log.e("debug", "$idlevel, $iddepartment")

            val idadminMP = idadmin.toRequestBody(MultipartBody.FORM)
            var photo: MultipartBody.Part? = null
            photoUri?.let {
                val file = FileUtils.getFile(this, photoUri)
                val requestBodyPhoto = file?.asRequestBody(contentResolver.getType(it).toString().toMediaTypeOrNull())
                if (file != null) {
                    photo = requestBodyPhoto?.let { it1 -> MultipartBody.Part.createFormData("filefoto", file.name, it1) }
                }
            }

            val service = RetrofitClient().apiRequest().create(DataService::class.java)
            service.updateProfile(idadminMP, photo, tokenAuth).enqueue(object : Callback<DefaultResponse> {
                override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.status == "success") {
                            Toasty.success(this@EditProfileActivity, getString(R.string.success_update_profile), Toasty.LENGTH_LONG).show()
                        } else {
                            ErrorHandler().responseHandler(
                                this@EditProfileActivity,
                                "updateProfile | onResponse", response.message()
                            )
                        }
                    } else {
                        ErrorHandler().responseHandler(
                            this@EditProfileActivity,
                            "updateProfile | onResponse", response.message()
                        )
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    ErrorHandler().responseHandler(
                        this@EditProfileActivity,
                        "updateProfile | onFailure", t.message.toString()
                    )
                }

            })
        }

    }

    override fun onBackPressed() {
        startActivity(Intent(this@EditProfileActivity, AdminActivity::class.java))
        finish()
    }

    private fun getSpinnerData(tokenAuth: String, level: String, departemen: String) {
        val service = RetrofitClient().apiRequest().create(DataService::class.java)
        service.getSpinnerData(tokenAuth).enqueue(object : Callback<DataSpinnerResponse> {
            override fun onResponse(call: Call<DataSpinnerResponse>, response: Response<DataSpinnerResponse>) {
                if (response.isSuccessful) {
                    listLevel.clear()
                    listDepartment.clear()

                    listLevel = response.body()!!.level
                    listDepartment = response.body()!!.departemen

                    val listA = ArrayList<String>()
                    val listB = ArrayList<String>()
                    for (i in 0 until listLevel.size) {
                        listA.add(response.body()!!.level[i].level)
                        if (listLevel[i].level == level) {
                            binding.spinnerLevel.setSelection(i)
                        }
                    }
                    for (i in 0 until listDepartment.size) {
                        listB.add(response.body()!!.departemen[i].departemen)
                        if (listDepartment[i].departemen == departemen) {
                            binding.spinnerDepartemen.setSelection(i)
                        }
                    }
                    binding.spinnerLevel.item = listA as List<Any>?
                    binding.spinnerDepartemen.item = listB as List<Any>?

                    binding.spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            idlevel = listLevel[p2].idlevel
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}

                    }

                    binding.spinnerDepartemen.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            iddepartment = listDepartment[p2].iddepartemen
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {}

                    }
                } else {
                    ErrorHandler().responseHandler(
                        this@EditProfileActivity,
                        "getSpinnerData | onResponse", response.message()
                    )
                }
            }

            override fun onFailure(call: Call<DataSpinnerResponse>, t: Throwable) {
                ErrorHandler().responseHandler(
                    this@EditProfileActivity,
                    "getSpinnerData | onFailure", t.message.toString()
                )
            }
        })
    }

    private fun savebitmap(bmp: Uri?): File? {
        val extStorageDirectory = Environment.getExternalStorageDirectory().toString()
        var outStream: OutputStream? = null
        // String temp = null;
        var file = File(extStorageDirectory, "temp.png")
        if (file.exists()) {
            file.delete()
            file = File(extStorageDirectory, "temp.png")
        }
        try {
            outStream = FileOutputStream(file)
            outStream.flush()
            outStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return file
    }

}