package com.melih.kmptemplate.features.movies.internal.list

import com.melih.kmptemplate.core.shared.data.api.TestMoviesRepository
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.core.shared.test.utils.launchInBackground
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.koin.java.KoinJavaComponent.inject
import kotlin.test.assertEquals

internal val MovieListViewModelTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin(),
) {
    testFixture {
        object {
            val repository: TestMoviesRepository by inject(TestMoviesRepository::class.java)
            val viewModel = MovieListViewModel(repository)
        }
    } asContextForEach {
        test("initial state is empty list") {
            assertEquals(
                expected = emptyList(),
                actual = viewModel.movies.value,
            )
        }

        test("emits movies when repository has movies") {
            val movie1 = createFakeMovie(id = 1L, title = "Movie 1")
            val movie2 = createFakeMovie(id = 2L, title = "Movie 2")
            val movieList = listOf(movie1, movie2)
            repository.setMovies(movieList)

            val states = mutableListOf<List<Movie>>()
            it.testScope.launchInBackground {
                viewModel.movies.toList(states)
            }

            assertEquals(
                expected = listOf(
                    emptyList(),
                    movieList,
                ),
                actual = states,
            )
            assertEquals(
                expected = movieList,
                actual = viewModel.movies.value,
            )
        }

        test("updates state dynamically when repository emits new values") {
            val movie1 = createFakeMovie(id = 1L, title = "Movie 1")
            val movie2 = createFakeMovie(id = 2L, title = "Movie 2")

            val states = mutableListOf<List<Movie>>()
            it.testScope.launchInBackground {
                viewModel.movies.toList(states)
            }

            repository.addMovie(movie1)
            it.testScope.advanceUntilIdle()
            repository.addMovie(movie2)
            it.testScope.advanceUntilIdle()

            assertEquals(
                expected = listOf(
                    emptyList(),
                    listOf(movie1),
                    listOf(movie1, movie2),
                ),
                actual = states,
            )
        }
    } closeWith {
        repository.setMovies(emptyList())
    }
}

private fun createFakeMovie(
    id: Long = 1L,
    title: String = "Test Movie",
    overview: String = "Test Overview",
    backdropUrl: String = "https://image.tmdb.org/t/p/w500/backdrop.jpg",
    posterUrl: String = "https://image.tmdb.org/t/p/w500/poster.jpg",
    adult: Boolean = false,
    originalLanguage: String = "en",
    originalTitle: String = "Test Movie",
    popularity: Float = 8.5f,
    releaseDate: String = "2024-01-01",
    video: Boolean = false,
    voteAverage: String = "8.5",
    voteCount: Int = 100,
) = Movie(
    adult = adult,
    backdropUrl = backdropUrl,
    id = id,
    originalLanguage = originalLanguage,
    originalTitle = originalTitle,
    overview = overview,
    popularity = popularity,
    posterUrl = posterUrl,
    releaseDate = releaseDate,
    title = title,
    video = video,
    voteAverage = voteAverage,
    voteCount = voteCount,
)
