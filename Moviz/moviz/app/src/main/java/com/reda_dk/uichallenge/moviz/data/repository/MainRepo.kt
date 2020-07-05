package com.reda_dk.uichallenge.moviz.data.repository

import androidx.lifecycle.MutableLiveData
import com.reda_dk.uichallenge.moviz.data.api.TmbdEndPoints
import com.reda_dk.uichallenge.moviz.data.model.genres.Genres
import com.reda_dk.uichallenge.moviz.data.model.movies.Movies
import io.reactivex.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepo {

    private val baseUrl = "https://api.themoviedb.org/3/"
    private val movizBaseUrl = "http://192.168.1.3:3000/"
    private val api_key = "2e27645e1938878aee2b80d8a00e81a1"
    private val imgBaseUrl = "https://image.tmdb.org/t/p/w500"


    private val moviesLiveData :MutableLiveData<Movies> = MutableLiveData()

    private val genresLiveData :MutableLiveData<Genres> = MutableLiveData()


    private val client = OkHttpClient
                            .Builder()
                            .build()

    private val retrofit = Retrofit.Builder()
                                .baseUrl(baseUrl)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .client(client)
                                .build()

    private val apiservices = retrofit.create(TmbdEndPoints::class.java)


    private val compositeDisposable = CompositeDisposable()

    
}