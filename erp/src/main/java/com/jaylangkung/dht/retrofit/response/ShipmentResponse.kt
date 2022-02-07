package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.master_design.shipment.ShipmentEntity

class ShipmentResponse(
    var status: String = "",
    var data: ArrayList<ShipmentEntity>
)