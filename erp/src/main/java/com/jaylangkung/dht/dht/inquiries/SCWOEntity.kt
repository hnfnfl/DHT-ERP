package com.jaylangkung.dht.dht.inquiries

class SCWOEntity(
    var idinquiries: String = "",
    var idsc: String = "",
    var idwo: String = "",
    var customer: String = "",
    var telp: String = "",
    var alamat: String = "",
    var sales_contract_detail: ArrayList<InquiriesDetailEntity>,
    var work_order_detail: ArrayList<InquiriesDetailEntity>,
    var netto: String = "",
    var bruto: String = "",
)