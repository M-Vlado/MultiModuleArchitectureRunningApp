package sk.vmproject.virtualocr.navigation

sealed class Screen(val route: String) {
    data object MainRunsScreen : Screen(route = "main_runs_screen")
    data object ActiveRunScreen : Screen(route = "active_run_screen/{run_id}") {
        fun routeWithParams(runId: Long): String {
            return "active_run_screen/$runId"
        }
    }
}
