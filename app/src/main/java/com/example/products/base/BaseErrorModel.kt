package com.example.products.base

data class BaseErrorModel (val code : Int?, val errorMessage : String?){
    var serverErrorText : String = ""
    var serverErrorCode : String = ""
}

