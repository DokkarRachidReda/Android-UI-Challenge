package com.reda_dk.uichallenge.moviz.data.model.movies

data class Movies(
    val page: Int,
    val results: ArrayList<SingleMovie>,
    val total_pages: Int,
    val total_results: Int
)