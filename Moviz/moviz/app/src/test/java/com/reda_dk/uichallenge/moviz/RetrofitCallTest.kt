package com.reda_dk.uichallenge.moviz

import android.util.Log
import com.reda_dk.uichallenge.moviz.data.api.TmbdEndPoints
import com.reda_dk.uichallenge.moviz.data.model.genres.Genres
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitCallTest {



         val baseUrl = "https://api.themoviedb.org/3/"
         val api_key = "2e27645e1938878aee2b80d8a00e81a1"

         val client = OkHttpClient
            .Builder()
            .build()

         val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()

         val apiservices = retrofit.create(TmbdEndPoints::class.java)


         val compositeDisposable = CompositeDisposable()


    
    @Before
    fun prepare(){

        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setNewThreadSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    @Test
    fun testGenresCall(){

        compositeDisposable.add(
            apiservices.getGenres(api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    assert(true)
                },{
                   assert(false)
                })
        )
    }




    @Test
    fun testMoviesCall(){

        compositeDisposable.add(
            apiservices.getLatestMovies(api_key)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    assert(true)
                },{
                    assert(false)
                })
        )
    }
}