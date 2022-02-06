package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.master_design.supplier.SupplierEntity

class SupplierResponse(
    var status: String = "",
    var data: ArrayList<SupplierEntity>
)