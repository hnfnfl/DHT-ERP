package com.jaylangkung.dht.master_design.shipment

import com.jaylangkung.dht.master_design.AdditionalEntity

class ShipmentEntity(
    var shipment: String = "",
    var jenis: String = "",
    var additional: ArrayList<AdditionalEntity>,
)