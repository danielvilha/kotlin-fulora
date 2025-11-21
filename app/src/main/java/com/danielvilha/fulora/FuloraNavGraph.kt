package com.danielvilha.fulora

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danielvilha.fulora.data.Plant
import com.danielvilha.fulora.ui.route.CreatePlantRoute
import com.danielvilha.fulora.ui.route.DetailPlantRoute
import com.danielvilha.fulora.ui.route.HomePlantRoute

object Destinations {
    const val HOME_PLANT = "homePlant"
    const val CREATE_PLANT = "createPlant"
    const val DETAIL_PLANT = "detailPlant"
}

@Composable
fun FuloraNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.HOME_PLANT
    ) {
        composable(Destinations.HOME_PLANT) {
            HomePlantRoute(
                onPlantClick = { plant: Plant ->
                    navController.navigate("${Destinations.DETAIL_PLANT}/${plant.id}")
                },
                onCreatePlantClick = {
                    navController.navigate(Destinations.CREATE_PLANT)
                }
            )
        }

        composable(Destinations.CREATE_PLANT) {
            CreatePlantRoute(
                onBackClick = { navController.popBackStack() },
                onSavedSuccess = { navController.popBackStack() },
            )
        }

        composable(
            route = "${Destinations.DETAIL_PLANT}/{plantId}",
            arguments = listOf(navArgument("plantId") { type = NavType.IntType })
        ) {
            DetailPlantRoute(
                onBackClick = { navController.popBackStack() },
            )
        }
    }
}