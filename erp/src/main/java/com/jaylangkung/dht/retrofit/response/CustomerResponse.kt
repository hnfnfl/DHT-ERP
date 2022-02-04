package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.master_design.customer.CustomerEntity

class CustomerResponse(
    var status: String = "",
    var data: ArrayList<CustomerEntity>
)