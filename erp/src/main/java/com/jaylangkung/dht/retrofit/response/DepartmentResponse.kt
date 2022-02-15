package com.jaylangkung.dht.retrofit.response

import com.jaylangkung.dht.dht.department.DepartmentEntity

class DepartmentResponse(
    var status: String = "",
    var data: ArrayList<DepartmentEntity>
)