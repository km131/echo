package com.example.echo_kt.data

class ProductBean {
    var image: Int? = null
    var text:String? =null
    constructor (i: Int) {
        this.image=i
    }
    constructor (t: String){
        this.text=t
    }
}