package com.reda_dk.uichallenge.moviz.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.reda_dk.uichallenge.moviz.data.model.genres.Genres
import com.reda_dk.uichallenge.moviz.data.model.movies.Movies
import com.reda_dk.uichallenge.moviz.data.repository.MainRepo

class MainVm : ViewModel() {

    private val mainRepo = MainRepo()


    fun getGenres(){
        mainRepo.getGenres()
    }


    fun getMovies(){
        mainRepo.getMovies()
    }


    fun getGenresLiveData():LiveData<Genres>{return mainRepo.getGenresLv()}

    fun getMoviesLiveData():LiveData<Movies>{return mainRepo.getMoviesLv()}
}