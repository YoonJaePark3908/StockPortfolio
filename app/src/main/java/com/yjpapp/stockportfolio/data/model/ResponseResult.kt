package com.yjpapp.stockportfolio.data.model

sealed class ResponseResult<T>(
    val data: T? = null,
    val resultCode: String = "",
    val resultMessage: String = "",
    val isSuccessful: Boolean = true
) {
    class Success<T>(data: T, resultCode: String, resultMessage: String): ResponseResult<T>(data, isSuccessful = true, resultCode = resultCode, resultMessage = resultMessage)
    class DataError<T>(errorCode: String, errorMessage: String, data: T? = null): ResponseResult<T>(data, isSuccessful = false, resultCode = errorCode, resultMessage = errorMessage)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is DataError -> "Error[exception=$resultCode]"
        }
    }
}