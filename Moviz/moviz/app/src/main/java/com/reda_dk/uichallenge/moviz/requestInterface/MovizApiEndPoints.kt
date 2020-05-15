package com.reda_dk.uichallenge.moviz.requestInterface

import com.reda_dk.uichallenge.moviz.model.ServerResponse
import com.reda_dk.uichallenge.moviz.model.User
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
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