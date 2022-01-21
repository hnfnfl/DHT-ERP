package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.auth.UserEntity

class LoginResponse(
    var status: String = "",
    var message: String = "",
    var data: ArrayList<UserEntity>,
    var tokenAuth: String = ""
)

