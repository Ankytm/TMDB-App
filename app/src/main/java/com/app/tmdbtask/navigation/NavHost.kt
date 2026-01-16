package com.app.tmdbtask.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.tmdbtask.ui.component.DetailsScreen
import com.app.tmdbtask.ui.component.HomeScreen
import com.app.tmdbtask.ui.component.SavedScreen
import com.app.tmdbtask.ui.component.SearchScreen
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel
import com.app.tmdbtask.ui.viewmodel.SearchViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Long) = "details/$movieId"
    }

    object Saved : Screen("saved")

    object Search : Screen("search")
}

@Composable
fun AppNavHost(navController: NavHostController, viewModel: MoviesViewModel) {

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = viewModel,
                onMovieClick = { movieId ->
                    navController.navigate(Screen.Details.createRoute(movieId))
                },
                onSavedClick = { navController.navigate(Screen.Saved.route) },
                onSearch = { navController.navigate(Screen.Search.route) }
            )
        }
        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
            movieId?.let {
                DetailsScreen(movieId = it, viewModel = viewModel,
                    onBackClick = {
                        navController.popBackStack()
                    }) {
                    navController.navigate(Screen.Saved.route)
                }
            }
        }
        composable(Screen.Saved.route) {
            SavedScreen(viewModel = viewModel, onBackClick = {
                navController.popBackStack()
            }) { movieId ->
                navController.navigate(Screen.Details.createRoute(movieId))
            }
        }
        composable(Screen.Search.route) {
            val vm: SearchViewModel = hiltViewModel()
            SearchScreen(
                viewModel = vm,
                onMovieClick = { movieId -> navController.navigate(Screen.Details.createRoute(movieId)) }
            )
        }
    }
}