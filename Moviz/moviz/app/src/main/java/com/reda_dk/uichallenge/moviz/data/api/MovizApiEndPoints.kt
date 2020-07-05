package com.reda_dk.uichallenge.moviz.data.api

import com.reda_dk.uichallenge.moviz.data.model.ServerResponse
import com.reda_dk.uichallenge.moviz.data.model.User
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface MovizApiEndPoints {

        @GET("users/{id}")
        fun getUser(@Path("id") id:String) : Observable<User>


        @POST("users/")
        fun createUser(@Body user: User) : Observable<ServerResponse>

        @Multipart
        @POST("users/uploadimg/{id}")
        fun uploadUserImage(@Path("id") id:String,
                            @Part  file: MultipartBody.Part
        ) : Observable<ServerResponse>


}