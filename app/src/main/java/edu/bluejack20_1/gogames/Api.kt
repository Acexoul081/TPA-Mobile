package edu.bluejack20_1.gogames

import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("posts")
    fun getPosts():Call<ArrayList<PostResponse>>
}