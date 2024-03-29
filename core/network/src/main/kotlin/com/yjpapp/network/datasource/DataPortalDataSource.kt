package com.yjpapp.network.datasource

import com.yjpapp.network.model.RespStockPriceInfo
import retrofit2.Response
interface DataPortalDataSource {
    suspend fun getStockPriceInfo(params: HashMap<String, String>): Response<RespStockPriceInfo>
}