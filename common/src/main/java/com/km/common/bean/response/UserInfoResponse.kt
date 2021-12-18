package com.km.common.bean.response

class UserInfoResponse(
    val code: Int,
    val msg: String,
    val user: User
){
    data class User(
        val avatar: String,
        val balance: Int,
        val email: String,
        val idCard: String,
        val nickName: String,
        val phonenumber: String,
        val score: Int,
        val sex: String,
        val userId: Int,
        val userName: String
    )
}

