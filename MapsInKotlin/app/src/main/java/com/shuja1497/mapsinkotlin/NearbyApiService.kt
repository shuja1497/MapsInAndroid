package com.shuja1497.mapsinkotlin

import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyApiService {

    @GET("maps/api/place/nearbysearch/json")
        fun getNearbyLocations(
                               @Query("location") location: String,
                               @Query("radius") radius: Int,
                               @Query("type") type: String,
                               @Query("key") key: String) : Observable<Response>

    companion object {
        fun create(): NearbyApiService{
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://maps.googleapis.com/")
                    .build()

            return retrofit.create(NearbyApiService::class.java)
        }
    }


}