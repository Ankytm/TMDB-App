package com.app.tmdbtask.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.tmdbtask.ui.component.DetailsScreen
import com.app.tmdbtask.ui.component.HomeScreen
import com.app.tmdbtask.ui.component.SavedScreen
import com.app.tmdbtask.ui.viewmodel.MoviesViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Details : Screen("details/{movieId}") {
        fun createRoute(movieId: Long) = "details/$movieId"
    }

    object Saved : Screen("saved")
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
                onSavedClick = { navController.navigate(Screen.Saved.route) }
            )
        }
        composable(Screen.Details.route) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getString("movieId")?.toLongOrNull()
            movieId?.let {
                DetailsScreen(movieId = it, viewModel = viewModel) {
                    navController.navigate(Screen.Saved.route)
                }
            }
        }
        composable(Screen.Saved.route) {
            SavedScreen(viewModel = viewModel) { movieId ->
                navController.navigate(Screen.Details.createRoute(movieId))
            }
        }
    }
}