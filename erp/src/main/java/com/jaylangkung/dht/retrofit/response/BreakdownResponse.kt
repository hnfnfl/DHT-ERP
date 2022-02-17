package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.dht.work_order.BreakdownEntity

class BreakdownResponse(
    var status: String = "",
    var data: ArrayList<BreakdownEntity>
)