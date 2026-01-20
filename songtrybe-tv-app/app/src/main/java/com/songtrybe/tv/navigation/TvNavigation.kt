package com.songtrybe.tv.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.songtrybe.tv.ui.screens.*

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Discover : Screen("discover")
    object Search : Screen("search")
    object Library : Screen("library")
    object Settings : Screen("settings")
    object MusicianDetail : Screen("musician/{musicianId}") {
        fun createRoute(musicianId: String) = "musician/$musicianId"
    }
    object GenreList : Screen("genre/{genre}") {
        fun createRoute(genre: String) = "genre/$genre"
    }
    object CategoryList : Screen("category/{category}") {
        fun createRoute(category: String) = "category/$category"
    }
}

@Composable
fun TvNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onMusicianClick = { musicianId ->
                    navController.navigate(Screen.MusicianDetail.createRoute(musicianId))
                },
                onNavigateToDiscover = {
                    navController.navigate(Screen.Discover.route)
                },
                onNavigateToSearch = {
                    navController.navigate(Screen.Search.route)
                },
                onNavigateToLibrary = {
                    navController.navigate(Screen.Library.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }
        
        composable(Screen.Discover.route) {
            DiscoverScreen(
                onGenreClick = { genre ->
                    navController.navigate(Screen.GenreList.createRoute(genre))
                },
                onCategoryClick = { category ->
                    navController.navigate(Screen.CategoryList.createRoute(category))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Search.route) {
            SearchScreen(
                onMusicianClick = { musicianId ->
                    navController.navigate(Screen.MusicianDetail.createRoute(musicianId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Library.route) {
            LibraryScreen(
                onMusicianClick = { musicianId ->
                    navController.navigate(Screen.MusicianDetail.createRoute(musicianId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            Screen.MusicianDetail.route,
            arguments = listOf(
                navArgument("musicianId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val musicianId = backStackEntry.arguments?.getString("musicianId") ?: ""
            MusicianDetailScreen(
                musicianId = musicianId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            Screen.GenreList.route,
            arguments = listOf(
                navArgument("genre") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val genre = backStackEntry.arguments?.getString("genre") ?: ""
            GenreListScreen(
                genre = genre,
                onMusicianClick = { musicianId ->
                    navController.navigate(Screen.MusicianDetail.createRoute(musicianId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(
            Screen.CategoryList.route,
            arguments = listOf(
                navArgument("category") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            CategoryListScreen(
                category = category,
                onMusicianClick = { musicianId ->
                    navController.navigate(Screen.MusicianDetail.createRoute(musicianId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}