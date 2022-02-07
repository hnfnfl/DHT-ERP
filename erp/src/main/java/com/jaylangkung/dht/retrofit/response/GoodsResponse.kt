package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.master_design.goods.GoodsEntity

class GoodsResponse(
    var status: String = "",
    var data: ArrayList<GoodsEntity>
)