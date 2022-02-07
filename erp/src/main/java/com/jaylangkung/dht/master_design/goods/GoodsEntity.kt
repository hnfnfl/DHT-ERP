package com.jaylangkung.dht.master_design.goods

import com.jaylangkung.dht.master_design.AdditionalEntity

class GoodsEntity(
    var idbarang: String = "",
    var kode: String = "",
    var barang: String = "",
    var stok: String = "",
    var jenis: String = "",
    var additional: ArrayList<AdditionalEntity>,
)