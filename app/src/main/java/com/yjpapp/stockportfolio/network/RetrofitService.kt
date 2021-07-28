package com.yjpapp.stockportfolio.network

import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import com.yjpapp.stockportfolio.localdb.sqlte.data.MyStockInfo
import com.yjpapp.stockportfolio.model.IncomeNoteModel
import com.yjpapp.stockportfolio.model.LoginUserInfo
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    /**
     * User API
     */
    @POST("/api/user/regist_user")
    suspend fun requestRegUser(@Body snsLoginRequest: SNSLoginRequest): Response<LoginUserInfo>

//    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/api/user/user_info")
    suspend fun requestUserInfo(@QueryMap params: HashMap<String, String>): Response<LoginUserInfo>

    @GET("/v1/nid/me")
    suspend fun requestNaverUserInfo(@QueryMap params: HashMap<String, String>): Response<LoginUserInfo>

    /**
     * 수익노트 API
     */
    @POST("/api/income_note/list")
    suspend fun requestPostIncomeNote(@Body incomeNoteModel: IncomeNoteModel)

    @GET("/api/income_note/list")
    suspend fun requestGetIncomeNote(@QueryMap params: HashMap<String, String>): Response<ArrayList<IncomeNoteModel>>

    @PUT("/api/income_note/list")
    suspend fun requestPutIncomeNote(@Body incomeNoteModel: IncomeNoteModel)

    @DELETE("/api/income_note/list")
    suspend fun requestDeleteIncomeNote(@Body id: Int)
}