package com.jaylangkung.dht.dht.inquiries

class InquiriesEntity(
    var idinquiries: String = "",
    var kode: String = "",
    var nama: String = "",
    var telp: String = "",
    var alamat: String = "",
    var status: String = "",
    var detail: ArrayList<InquiriesDetailEntity>,
    var payment_detail: String = "",
    var createddate: String = "",
    var lastupdate: String = "",
    var shipment: String = "",
    var shipment_send: String = "",
    var shipment_estimasi: String = "",
    var sales_contract: ArrayList<InquiriesDetailEntity>,
    var work_order: ArrayList<InquiriesDetailEntity>,
)