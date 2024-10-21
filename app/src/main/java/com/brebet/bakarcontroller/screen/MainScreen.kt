package com.brebet.bakarcontroller.screen

import android.bluetooth.BluetoothDevice
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.brebet.bakarcontroller.R
import com.brebet.bakarcontroller.viewmodel.SelectDeviceViewModel

// Define the different routes/screens in the app.
sealed class Screen(val route: String, @StringRes val title: Int) {
    object SelectDevice : Screen("select_device", R.string.select_device)
    object Controller : Screen("controller/{deviceAddress}", R.string.controller)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenBar(
    currentScreen: Screen,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(currentScreen.title)) },
        modifier = modifier,
    )
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = when (backStackEntry?.destination?.route) {
        Screen.SelectDevice.route -> Screen.SelectDevice
        Screen.Controller.route -> Screen.Controller
        else -> Screen.SelectDevice
    }

    val selectDeviceViewModel: SelectDeviceViewModel = hiltViewModel()

    Scaffold(
        topBar = {
            MainScreenBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null
            )
        },
        floatingActionButton = {
            if (currentScreen is Screen.SelectDevice) {
                FloatingActionButton(
                    onClick = selectDeviceViewModel::onRefresh
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }
        }
    ) { innerPadding ->
        // Create the NavGraph here
        NavGraph(
            navController = navController,
            selectDeviceViewModel = selectDeviceViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

// Separate composable function to define the navigation graph
@Composable
fun NavGraph(
    navController: NavHostController,
    selectDeviceViewModel: SelectDeviceViewModel,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SelectDevice.route,
        modifier = modifier
    ) {
        // Composable for SelectDeviceScreen
        composable(route = Screen.SelectDevice.route) {
            SelectDeviceScreen(
                viewmodel = selectDeviceViewModel,
                onNextButtonClicked = { device ->
                    navController.currentBackStackEntry?.savedStateHandle?.set("device", device)
                    navController.navigate(Screen.Controller.route)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
        // Composable for ControllerScreen
        composable(route = Screen.Controller.route) { backStackEntry ->
            val device = navController.previousBackStackEntry?.savedStateHandle?.get<BluetoothDevice>("device")
            ControllerScreen(
                selectedDevice = device,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
