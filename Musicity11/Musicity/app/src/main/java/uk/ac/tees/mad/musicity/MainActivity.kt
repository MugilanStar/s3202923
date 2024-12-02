package uk.ac.tees.mad.musicity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.musicity.navigation.NavGraph
import uk.ac.tees.mad.musicity.ui.theme.MusicityTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MusicityTheme {
                Surface(color = MaterialTheme.colorScheme.background){
                    val navController = rememberNavController()
                    NavGraph(navController = navController)
                }
            }
        }
    }
}