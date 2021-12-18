package com.km.common.bean.response

data class NearSellerResponse(
    val code: Int,
    val msg: String,
    val rows: List<Row>,
    val total: Int
)

data class Row(
    val address: String,
    val avgCost: Int,
    val deliveryTime: Int,
    val distance: Int,
    val id: Int,
    val imgUrl: String,
    val introduction: String,
    val name: String,
    val other: String?,
    val recommend: String,
    val saleNum3hour: Int,
    val saleQuantity: Int,
    val score: Double,
    val themeId: Int
)