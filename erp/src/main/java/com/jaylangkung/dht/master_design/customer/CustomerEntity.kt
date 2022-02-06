package com.jaylangkung.dht.master_design.customer

import com.jaylangkung.dht.master_design.AdditionalEntity

class CustomerEntity(
    var idcustomer: String = "",
    var nama: String = "",
    var alamat: String = "",
    var telp: String = "",
    var additional: ArrayList<AdditionalEntity>,
)