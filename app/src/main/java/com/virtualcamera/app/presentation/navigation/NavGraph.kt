package com.virtualcamera.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.virtualcamera.app.presentation.screen.camera.CameraPreviewScreen
import com.virtualcamera.app.presentation.screen.camera.CameraSettingsScreen
import com.virtualcamera.app.presentation.screen.home.HomeScreen
import com.virtualcamera.app.presentation.screen.media.MediaPickerScreen
import com.virtualcamera.app.presentation.screen.media.MediaPreviewScreen
import com.virtualcamera.app.presentation.screen.settings.SettingsScreen
import com.virtualcamera.app.presentation.screen.stream.StreamInputScreen

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToMediaPicker = {
                    navController.navigate(Screen.MediaPicker.route)
                },
                onNavigateToStreamInput = {
                    navController.navigate(Screen.StreamInput.route)
                },
                onNavigateToCameraSettings = {
                    navController.navigate(Screen.CameraSettings.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                }
            )
        }

        composable(Screen.MediaPicker.route) {
            MediaPickerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMediaSelected = { mediaId ->
                    navController.navigate(Screen.MediaPreview.createRoute(mediaId))
                }
            )
        }

        composable(
            route = Screen.MediaPreview.route,
            arguments = listOf(
                navArgument("mediaId") { type = NavType.LongType }
            )
        ) {
            MediaPreviewScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.StreamInput.route) {
            StreamInputScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.CameraSettings.route) {
            CameraSettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToCameraPreview = {
                    navController.navigate(Screen.CameraPreview.route)
                }
            )
        }

        composable(Screen.CameraPreview.route) {
            CameraPreviewScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
