package com.melih.kmptemplate.features.movies.internal.list

import com.melih.kmptemplate.core.shared.data.api.TestMoviesRepository
import com.melih.kmptemplate.core.shared.model.Movie
import com.melih.kmptemplate.core.shared.model.fakeMovie
import com.melih.kmptemplate.core.shared.test.utils.launchInBackground
import com.melih.kmptemplate.core.shared.test.utils.withKoin
import com.melih.kmptemplate.core.shared.test.utils.withMainTestDispatcher
import de.infix.testBalloon.framework.core.TestConfig
import de.infix.testBalloon.framework.core.testSuite
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.advanceUntilIdle
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals

internal val MovieListViewModelTest by testSuite(
    testConfig = TestConfig.withMainTestDispatcher().withKoin(),
) {
    testFixture {
        object : KoinTest {
            val repository: TestMoviesRepository by inject()
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
            val movie1 = fakeMovie(id = 1L, title = "Movie 1")
            val movie2 = fakeMovie(id = 2L, title = "Movie 2")
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
            val movie1 = fakeMovie(id = 1L, title = "Movie 1")
            val movie2 = fakeMovie(id = 2L, title = "Movie 2")

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
