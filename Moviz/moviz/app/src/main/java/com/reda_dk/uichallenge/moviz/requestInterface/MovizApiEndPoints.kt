package com.reda_dk.uichallenge.moviz.requestInterface

import com.reda_dk.uichallenge.moviz.model.User
import io.reactivex.Observable
import retrofit2.http.*

interface MovizApiEndPoints {

        @GET("users/{id}")
        fun getUser(@Path("id") id:String) : Observable<User>


        @POST("users/")
        fun createUser(@Body user: User) : Observable<Any>


}