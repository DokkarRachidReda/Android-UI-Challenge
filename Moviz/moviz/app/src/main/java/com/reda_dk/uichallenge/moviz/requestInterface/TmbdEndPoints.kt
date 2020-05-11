package com.reda_dk.uichallenge.moviz.requestInterface

import com.reda_dk.uichallenge.moviz.model.castcrew.CastCrew
import com.reda_dk.uichallenge.moviz.model.genres.Genres
import com.reda_dk.uichallenge.moviz.model.movies.Movies
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmbdEndPoints {

    @GET("genre/movie/list")
    fun getGenres(@Query("api_key")api_key:String,@Query("language")language:String="en-US"): Observable<Genres>


    @GET("discover/movie")
    fun getLatestMovies(@Query("api_key")api_key:String,
                        @Query("language")language:String="en-US",
                        @Query("sort_by")sort_by:String="vote_count.desc",
                        @Query("include_adult")include_adult:String="false",
                        @Query("include_video")include_video:String="false",
                        @Query("page")page:String="3"): Observable<Movies>



    @GET("movie/{movie_id}/credits")
    fun getCastCrew(@Query("api_key")api_key:String, @Path("movie_id") movie_id:String): Observable<CastCrew>
}