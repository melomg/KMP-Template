package com.melih.kmptemplate.shared.network

import com.melih.kmptemplate.shared.model.Movies

interface MoviesApi {

    suspend fun getPopularMovies(page: Int): Movies?
}
