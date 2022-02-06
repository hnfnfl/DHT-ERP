package com.jaylangkung.dht.master_design.supplier

import com.jaylangkung.dht.master_design.AdditionalEntity

class SupplierEntity(
    var idsupplier: String = "",
    var nama: String = "",
    var alamat: String = "",
    var telp: String = "",
    var additional: ArrayList<AdditionalEntity>,
    var jenis: String = "",
)