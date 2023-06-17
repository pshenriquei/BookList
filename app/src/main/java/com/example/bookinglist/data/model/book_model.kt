package com.example.bookinglist.data.model

import com.google.gson.annotations.SerializedName

data class BooksModel(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("authors") val authors: List<AuthorsModel> = arrayListOf(),
    @SerializedName("subjects") val subjects: List<String> = arrayListOf(),
)
