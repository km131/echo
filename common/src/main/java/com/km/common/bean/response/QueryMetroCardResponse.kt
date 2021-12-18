package com.km.common.bean.response

data class QueryMetroCardResponse(
    val code: Int,
    val data: Data,
    val msg: String
){
    data class Data(
        val cardNum: String,
        val id: Int,
        val idCard: String,
        val phonenumber: String,
        val userId: Int,
        val userName: String
    )
}

