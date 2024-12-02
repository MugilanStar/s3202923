package uk.ac.tees.mad.musicity.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.musicity.screens.home.HomeScreen
import uk.ac.tees.mad.musicity.screens.login.LoginScreen
import uk.ac.tees.mad.musicity.screens.splash.SplashScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = SplashDestination.route) {
        composable(SplashDestination.route) {
            SplashScreen(navController = navController)
        }
        composable(LoginDestination.route) {
            LoginScreen(navController = navController)
        }
        composable(HomeDestination.route) {
            HomeScreen(navController = navController)
        }
        composable(FavoritesDestination.route) {
            // FavoritesScreen()
        }
        composable(ProfileDestination.route) {
            // ProfileScreen()
        }
    }
}

interface NavigationDestination {
    val route: String
    val title: String
}

