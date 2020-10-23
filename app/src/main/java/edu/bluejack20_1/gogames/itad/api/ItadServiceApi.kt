package edu.bluejack20_1.gogames.itad.api

import edu.bluejack20_1.gogames.itad.api.entity.Deal
import edu.bluejack20_1.gogames.itad.api.entity.ItadResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItadServiceApi {
    @GET("/v01/deals/list")
    fun getDeals(
        @Query("key") key: String = "2f1613592974ad7d11a240845e097cf069b15ae3",
        @Query("offset") offset: Int? = null,
        @Query("limit") limit: Int? = null,
        @Query("region") region: String? = null,
        @Query("country") country: String? = null,
        @Query("shops") shops: String? = null,
        @Query("sort") sort: String? = null
    ): Call<ItadResult>

}