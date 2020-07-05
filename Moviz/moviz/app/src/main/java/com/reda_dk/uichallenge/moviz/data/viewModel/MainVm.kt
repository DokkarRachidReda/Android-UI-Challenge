package com.reda_dk.uichallenge.moviz.data.viewModel

import androidx.lifecycle.ViewModel
import com.reda_dk.uichallenge.moviz.data.repository.MainRepo

class MainVm : ViewModel() {

    private val mainRepo = MainRepo()

    private val movieslv = mainRepo.getMoviesLv()

    private val genreslv = mainRepo.getGenres()


    
}