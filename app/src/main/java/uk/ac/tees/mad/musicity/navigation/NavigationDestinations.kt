package uk.ac.tees.mad.musicity.navigation

object SplashDestination : NavigationDestination {
    override val route = "splash"
    override val title = "Splash Screen"
}

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val title = "Login"
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val title = "Home"
}

object MusicPlayerDestination : NavigationDestination {
    override val route = "music_player"
    override val title = "Music Player"
}

object FavoritesDestination : NavigationDestination {
    override val route = "favorites"
    override val title = "Favorites"
}

object ProfileDestination : NavigationDestination {
    override val route = "profile"
    override val title = "Profile"
}
