package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.dht.packing.PackingEntity

class PackingResponse(
    var status: String = "",
    var data: ArrayList<PackingEntity>,
)

