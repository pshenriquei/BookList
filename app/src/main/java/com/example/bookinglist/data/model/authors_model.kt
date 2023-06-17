package com.example.bookinglist.data.model

import com.google.gson.annotations.SerializedName

data class AuthorsModel(
    @SerializedName("name") val name: String = "",
    @SerializedName("birth_year") val birth_year: Int = 0,
    @SerializedName("death_year") val death_year: Int = 0,
)