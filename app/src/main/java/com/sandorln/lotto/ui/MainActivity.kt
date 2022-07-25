package com.sandorln.lotto.ui

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sandorln.lotto.R
import com.sandorln.lotto.ui.scene.HomeScreen
import com.sandorln.lotto.ui.scene.PrizeMapScreen
import com.sandorln.lotto.ui.scene.PullNumberScreen
import com.sandorln.lotto.ui.scene.SettingScreen
import com.sandorln.lotto.ui.theme.Blue00
import com.sandorln.lotto.ui.theme.Blue01
import com.sandorln.lotto.ui.theme.LottoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LottoTheme {
                NavigationGraph()
            }
        }
    }
}

val LocalNavAction = staticCompositionLocalOf { NavActions() }

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()
    val navActions = NavActions(navController)
    CompositionLocalProvider(
        LocalNavAction provides navActions
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            backgroundColor = Color.White,
            bottomBar = {
                BottomNavigation(backgroundColor = Blue00) {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    bottomNavItems.forEach { item ->
                        BottomNavigationItem(
                            icon = { Icon(painter = painterResource(id = item.iconId), contentDescription = item.route) },
                            label = { Text(text = item.name) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                            onClick = { navActions.navigateTo(item.route) },
                            selectedContentColor = Blue01,
                            unselectedContentColor = Color.DarkGray
                        )
                    }
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = NavDestination.HOME_SCREEN,
                modifier = Modifier.padding(it)
            ) {
                composable(NavDestination.HOME_SCREEN) {
                    HomeScreen()
                }
                composable(NavDestination.PULL_NUMBER_SCREEN) {
                    PullNumberScreen()
                }
                composable(NavDestination.PRIZE_MAP_SCREEN) {
                    PrizeMapScreen()
                }
                composable(NavDestination.SETTING_SCREEN) {
                    SettingScreen()
                }
            }
        }
    }
}

private val bottomNavItems = mutableListOf<BottomNavItem>().apply {
    add(BottomNavItem(name = "홈", route = NavDestination.HOME_SCREEN, iconId = R.drawable.ic_home))
    add(BottomNavItem(name = "뽑기", route = NavDestination.PULL_NUMBER_SCREEN, iconId = R.drawable.ic_number))
//    add(BottomNavItem(name = "당첨지도", route = NavDestination.PRIZE_MAP_SCREEN, iconId = R.drawable.ic_map))
//    add(BottomNavItem(name = "설정", route = NavDestination.SETTING_SCREEN, iconId = R.drawable.ic_settings))
}

data class BottomNavItem(
    val name: String = "",
    val route: String = "",
    val iconId: Int,
)

object NavDestination {
    const val HOME_SCREEN = "home_screen"
    const val PRIZE_MAP_SCREEN = "prize_map_screen"
    const val PULL_NUMBER_SCREEN = "pull_number_screen"
    const val SETTING_SCREEN = "setting_screen"
}

class NavActions(navController: NavController? = null) {
    private fun NavOptionsBuilder.init(navController: NavController) {
        popUpTo(navController.graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

    val navigateTo: (router: String) -> Unit = { router -> navController?.navigate(route = router, builder = { init(navController) }) }
}
