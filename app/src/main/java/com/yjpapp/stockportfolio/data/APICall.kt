package com.yjpapp.stockportfolio.data

import com.yjpapp.stockportfolio.data.model.ResponseResult
import com.yjpapp.stockportfolio.data.network.ServerRespCode
import com.yjpapp.stockportfolio.data.network.ServerRespMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * 공용 API Call 함수가 구현 된 base class
 */
object APICall {
    private val ioDispatcher = Dispatchers.IO
    suspend fun <T : Any> handleApi(
        execute: suspend () -> Response<T>
    ): ResponseResult<T> {
        return withContext(ioDispatcher) {
            try {
                val response = execute()
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    ResponseResult.Success(body, ServerRespCode.OK, ServerRespMessage.Success)
                } else {
                    ResponseResult.DataError(ServerRespCode.BadRequest, ServerRespMessage.Fail)
                }
            } catch (e: HttpException) {
                ResponseResult.DataError(ServerRespCode.InternalServerError, "${e.message}")
            } catch (e: Throwable) {
                ResponseResult.DataError(ServerRespCode.Exception, "${e.message}")
            }
        }
    }
}