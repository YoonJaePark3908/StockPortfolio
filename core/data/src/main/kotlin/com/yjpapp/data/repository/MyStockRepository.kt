package com.yjpapp.data.repository

import com.gadget.GadgetDate
import com.yjpapp.data.APICall
import com.yjpapp.data.datasource.MyStockRoomDataSource
import com.yjpapp.data.mapper.mapping
import com.yjpapp.data.model.MyStockData
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqStockPriceInfo
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.network.datasource.DataPortalDataSource
import java.text.DecimalFormat
import javax.inject.Inject

interface MyStockRepository {
    suspend fun addMyStock(myStockData: MyStockData): ResponseResult<Boolean>
    suspend fun updateMyStock(myStockData: MyStockData): ResponseResult<Boolean>
    suspend fun deleteMyStock(myStockData: MyStockData): ResponseResult<Boolean>
    suspend fun getAllMyStock(): ResponseResult<List<MyStockData>>
    suspend fun searchStockInfo(request: ReqStockPriceInfo): ResponseResult<List<StockPriceData>>
    suspend fun refreshMyStock(): ResponseResult<Boolean>
}

class MyStockRepositoryImpl @Inject constructor(
    private val myStockLocalDataSource: MyStockRoomDataSource,
    private val stockInfoDataSource: DataPortalDataSource
) : MyStockRepository {

    override suspend fun addMyStock(myStockData: MyStockData) =
        try {
            myStockLocalDataSource.requestInsertMyStock(myStockData.mapping())
            ResponseResult.Success(
                data = true,
                resultCode = "200",
                resultMessage = "SUCCESS"
            )
        } catch (e: Exception) {
            ResponseResult.Error(
                data = false,
                errorCode = "700",
                errorMessage = e.message?: "Unknown Message"
            )
        }

    override suspend fun updateMyStock(myStockData: MyStockData) =
        try {
            myStockLocalDataSource.requestUpdateMyStock(myStockData.mapping())
            ResponseResult.Success(
                data = true,
                resultCode = "200",
                resultMessage = "SUCCESS"
            )
        } catch (e: Exception) {
            ResponseResult.Error(
                errorCode = "700",
                errorMessage = e.message?: "Unknown Message"
            )
        }

    override suspend fun deleteMyStock(myStockData: MyStockData) =
        try {
            myStockLocalDataSource.requestDeleteMyStock(myStockData.mapping())
            ResponseResult.Success(
                data = true,
                resultCode = "200",
                resultMessage = "SUCCESS"
            )
        } catch (e: Exception) {
            ResponseResult.Error(
                errorCode = "700",
                errorMessage = e.message?: "Unknown Message"
            )
        }

    override suspend fun getAllMyStock() =
        try {
            val data = myStockLocalDataSource.requestGetAllMyStock().map { it.mapping() }
            ResponseResult.Success(
                data = data,
                resultCode = "200",
                resultMessage = "SUCCESS"
            )
        } catch (e: Exception) {
            ResponseResult.Error(
                errorCode = "700",
                errorMessage = e.message?: "Unknown Message"
            )
        }
    override suspend fun searchStockInfo(request: ReqStockPriceInfo): ResponseResult<List<StockPriceData>> {
        val hashMap = HashMap<String, String>().apply {
            this["serviceKey"] = request.serviceKey
            this["numOfRows"] = request.numOfRows
            this["pageNo"] = request.pageNo
            this["resultType"] = request.resultType
            this["basDt"] = ""
            this["likeItmsNm"] = request.likeItmsNm
        }
        val response = APICall.requestApi { stockInfoDataSource.getStockPriceInfo(hashMap) }
        return if (response is ResponseResult.Success) {
            val data = response.data?.response?.body?.items?.item?.map {
                it.mapping()
            }?.distinctBy {
                it.srtnCd
            }?: listOf()
            ResponseResult.Success(
                data = data,
                resultCode = response.resultCode,
                resultMessage = response.resultMessage
            )
        } else {
            ResponseResult.Error(
                errorMessage = response.resultMessage,
                errorCode = response.resultCode
            )
        }
    }

    override suspend fun refreshMyStock(): ResponseResult<Boolean> {
        val allMyStock = getAllMyStock()
        if (allMyStock is ResponseResult.Success) {
            allMyStock.data?.forEach { myStockEntity ->
                val reqStockPriceInfo = ReqStockPriceInfo(
                    numOfRows = "20",
                    pageNo = "1",
                    isinCd = myStockEntity.subjectCode,
                    likeItmsNm = myStockEntity.subjectName,
                    basDt = GadgetDate.getTodayKorea(GadgetDate.DateFormat4)
                )
                val result = searchStockInfo(reqStockPriceInfo)
                if (result is ResponseResult.Success) {
                    val item = result.data ?: listOf()
                    val resultItem = item.firstOrNull()
                    if (resultItem != null) {
                        updateMyStock(
                            myStockEntity.copy(
                                currentPrice = getNumInsertComma(resultItem.clpr),
                                dayToDayPrice = resultItem.vs,
                                dayToDayPercent = resultItem.fltRt,
                                basDt = resultItem.basDt
                            )
                        )
                    }
                } else {
                    return ResponseResult.Error(
                        data = false,
                        errorCode = "700",
                        errorMessage = "resultItem is null"
                    )
                }
            }
            return ResponseResult.Success(
                data = true,
                resultCode = "200",
                resultMessage = "SUCCESS"
            )
        } else {
            return ResponseResult.Error(
                data = false,
                errorCode = "700",
                errorMessage = "resultItem is null"
            )
        }
    }

    //5000000 => 5,000,000 변환
    private fun getNumInsertComma(num: String): String {
        if (num.isEmpty()) return "0"
        val decimalFormat = DecimalFormat("###,###")
        return decimalFormat.format(num.replace(",", "").toDouble())
    }

}