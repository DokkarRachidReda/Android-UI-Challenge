package com.reda_dk.uichallenge.moviz.data.repository

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.reda_dk.uichallenge.moviz.data.api.TmbdEndPoints
import com.reda_dk.uichallenge.moviz.data.model.genres.Genres
import com.reda_dk.uichallenge.moviz.data.model.movies.Movies
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainRepo {

    //should be fetched at startup and passed with constructor
    private val baseUrl = "https://api.themoviedb.org/3/"
    private val api_key = "2e27645e1938878aee2b80d8a00e81a1"


    private val moviesLiveData :MutableLiveData<Movies> = MutableLiveData()
        get() = field

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



    fun getGenres(){

        compositeDisposable.add(
            apiservices.getGenres(api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                . subscribe(this::onGenresResponse, this::onGenresFailure)
        )
    }

    private fun onGenresFailure(t: Throwable) {

        Log.e("Tmdb-api-genres","api call failed  : "+t.toString())
    }

    private fun onGenresResponse(response: Genres){

        Log.e("Tmdb-api-genres","api call OK")

        genresLiveData.value = response
    }



    fun getMovies(){

        compositeDisposable.add(
            apiservices.getLatestMovies(api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                . subscribe(this::onMoviesResponse, this::onMoviesFailure)
        )
    }


    private fun onMoviesFailure(t: Throwable) {

        Log.e("Tmdb-api-movies","api call failed  : "+t.toString())

    }


    private fun onMoviesResponse(response: Movies){

        Log.e("Tmdb-api-movies","api call OK")

        moviesLiveData.value = response

    }
}