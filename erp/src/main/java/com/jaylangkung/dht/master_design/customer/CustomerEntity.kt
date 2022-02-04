package com.jaylangkung.dht.master_design.customer

class CustomerEntity(
    var idcustomer: String = "",
    var nama: String = "",
    var alamat: String = "",
    var telp: String = "",
    var additional: ArrayList<AdditionalEntity>,
)