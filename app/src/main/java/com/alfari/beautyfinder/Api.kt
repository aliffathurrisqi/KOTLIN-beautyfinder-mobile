package com.alfari.beautyfinder

import retrofit2.Call
import retrofit2.http.*

interface Api {

    @GET("auth/login.php")
    fun getUser(
        @Query("id") id: String?
    ): Call<ArrayList<LoginResponse>>

    @POST("auth/regist.php")
    fun userRegister(
        @Query("username") username: String?,
        @Query("email") email: String?,
        @Query("password") password: String?,
        @Query("konfirmasi") konfirmasi: String?
    ): Call<ArrayList<RegisterResponse>>

    @POST("auth/forget.php")
    fun userForget(
        @Query("id") id: String?,
    ): Call<ArrayList<ForgetResponse>>

}