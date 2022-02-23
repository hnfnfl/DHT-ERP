package com.jaylangkung.dht.master_design.design_packing

import com.jaylangkung.dht.master_design.AdditionalEntity

class DesignPackingEntity(
    var idpacking: String = "",
    var kode: String = "",
    var packing: String = "",
    var detail_muat: ArrayList<AdditionalEntity>,
    var komposisi: ArrayList<AdditionalEntity>,
    var additional: ArrayList<AdditionalEntity>,
)