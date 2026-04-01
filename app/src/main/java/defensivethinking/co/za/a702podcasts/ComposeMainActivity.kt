package defensivethinking.co.za.a702podcasts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import defensivethinking.co.za.a702podcasts.ui.screens.HomeScreen
import defensivethinking.co.za.a702podcasts.ui.screens.SearchScreen
import defensivethinking.co.za.a702podcasts.ui.screens.DetailsScreen
import defensivethinking.co.za.a702podcasts.ui.screens.SettingsScreen
import defensivethinking.co.za.a702podcasts.ui.theme.PodcastsTheme
import defensivethinking.co.za.a702podcasts.ui.BottomNavigationBar

class ComposeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PodcastsTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavigationBar(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(navController) }
                        composable("search") { SearchScreen(navController) }
                        composable("library") { SettingsScreen(navController) } // Mapping library to settings for now or create library
                        composable("details/{podcastId}") { backStackEntry ->
                            DetailsScreen(
                                podcastId = backStackEntry.arguments?.getString("podcastId"),
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
