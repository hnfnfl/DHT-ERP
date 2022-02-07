package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.master_design.product.ProductEntity

class ProductResponse(
    var status: String = "",
    var data: ArrayList<ProductEntity>
)