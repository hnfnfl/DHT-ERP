package com.jaylangkung.dht.dht.purchasing

import com.jaylangkung.dht.dht.inquiries.InquiriesDetailEntity

class PurchasingEntity(
    var idbarang_po: String = "",
    var kode: String = "",
    var detail: ArrayList<InquiriesDetailEntity>,
    var total: String = "",
    var status: String = "",
    var nama: String = "",
    var estimasi_delivery: String = "",
    var createddate: String = "",
)