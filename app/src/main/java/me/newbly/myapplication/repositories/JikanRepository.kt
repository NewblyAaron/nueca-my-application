package me.newbly.myapplication.repositories

import me.newbly.myapplication.models.JikanDataModel
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class JikanRepository {
    object RetrofitInstance {
        val api: JikanApi by lazy {
            Retrofit.Builder()
                .baseUrl("https://api.jikan.moe/v4/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(JikanApi::class.java)
        }
    }

    interface JikanApi {
        @GET("anime")
        fun getAnimes() : Call<JikanDataModel>
    }
}

