package com.example.bookinglist.data.infra

import com.example.bookinglist.data.model.ListBookModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ListOfBooksApi {

    @GET("books")
    fun getBooks(): Call<ListBookModel>

    @GET("books")
    fun getBooksById(@Query("ids") id: String?): Call<ListBookModel>

}

fun retroFit(): Retrofit = Retrofit.Builder()
    .baseUrl("https://gutendex.com/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()