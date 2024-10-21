package com.brebet.bakarcontroller.screen

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brebet.bakarcontroller.viewmodel.SelectDeviceViewModel

@SuppressLint("MissingPermission")
@Composable
fun SelectDeviceScreen(
    viewmodel: SelectDeviceViewModel = hiltViewModel(),
    onNextButtonClicked: (device: BluetoothDevice) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (viewmodel.failText == null) {
        Column(
            modifier = modifier
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            ) {
                items(
                    viewmodel.pairedDevices
                ) { device ->
                    Text(
                        text = device.name ?: (device.address),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onNextButtonClicked(device)
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
    else {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewmodel.failText!!
            )
        }
    }
}