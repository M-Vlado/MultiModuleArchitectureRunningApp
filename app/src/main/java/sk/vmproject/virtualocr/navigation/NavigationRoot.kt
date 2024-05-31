package sk.vmproject.virtualocr.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import sk.vmproject.run.presentation.active_run.ActiveRunScreenRoot
import sk.vmproject.run.presentation.active_run.service.ActiveRunService
import sk.vmproject.run.presentation.main_runs.MainRunsScreenRoot
import sk.vmproject.virtualocr.MainActivity

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = RUN_GRAPH
    ) {
        runGraph(navController = navController)
    }
}

private fun NavGraphBuilder.runGraph(navController: NavHostController) {
    navigation(
        startDestination = Screen.MainRunsScreen.route,
        route = RUN_GRAPH
    ) {
        composable(
            route = Screen.MainRunsScreen.route
        ) {
            MainRunsScreenRoot(
                onMenuItemClick = { },
                onRunSelected = { runId ->
                    navController.navigate(Screen.ActiveRunScreen.routeWithParams(runId = runId))
                }
            )
        }

        composable(
            route = Screen.ActiveRunScreen.route,
            arguments = listOf(
                navArgument("run_id") {
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "virtualocr://active_run_screen/{run_id}"
                }
            )
        ) {
            val context = LocalContext.current
            val runId = it.arguments?.getString("run_id")
            ActiveRunScreenRoot(
                onBack = {
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                },
                onFinish = {
                    if (navController.previousBackStackEntry != null) {
                        navController.navigateUp()
                    }
                },
                onServiceToggle = { shouldServiceRun ->
                    if (shouldServiceRun) {
                        context.startService(
                            ActiveRunService.createStartIntent(
                                context = context,
                                activityClass = MainActivity::class.java,
                                runId = runId ?: ""
                            )
                        )
                    } else {
                        context.startService(
                            ActiveRunService.createStopIntent(context = context)
                        )
                    }
                }
            )
        }
    }
}