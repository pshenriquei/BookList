package com.example.bookinglist.data.model

import com.google.gson.annotations.SerializedName

data class ListBookModel(

    @SerializedName("results") var results: List<BooksModel> = arrayListOf()
)
