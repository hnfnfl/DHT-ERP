package com.jaylangkung.dht.dht.work_order

class BreakdownEntity(
    var idproduk: String = "",
    var produk: String = "",
    var barang: ArrayList<BreakdownDetailEntity>,
    var packing: ArrayList<BreakdownDetailEntity>,
)