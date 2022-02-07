package com.jaylangkung.dht.master_design.product

import com.jaylangkung.dht.master_design.AdditionalEntity

class ProductEntity(
    var idproduk: String = "",
    var kode: String = "",
    var produk: String = "",
    var harga: String = "",
    var kategori: String = "",
    var additional: ArrayList<AdditionalEntity>,
    var komposisi: ArrayList<AdditionalEntity>,
    var spesifikasi: ArrayList<AdditionalEntity>,
)