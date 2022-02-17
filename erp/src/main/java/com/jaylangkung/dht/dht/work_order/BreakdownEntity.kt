package com.jaylangkung.dht.dht.work_order

class BreakdownEntity(
    var idproduk: String = "",
    var barang: ArrayList<BreakdownDetailEntity>,
    var kebutuhan_packing: ArrayList<BreakdownDetailEntity>,
)