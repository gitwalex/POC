package com.gerwalex.batteryguard


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.camera.CAMERA
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.PermissionsControllerFactory
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import qrscanner.CameraLens
import qrscanner.QrScanner

@Composable
fun QRScanner() {
    var openImagePicker by remember { mutableStateOf(value = false) }
    var qrCodeURL by remember { mutableStateOf("") }
    val factory: PermissionsControllerFactory = rememberPermissionsControllerFactory()
    val controller: PermissionsController =
        remember(factory) { factory.createPermissionsController() }
    BindEffect(controller)
    var permissionState by mutableStateOf(PermissionState.NotDetermined)
    LaunchedEffect(Unit) {
        try {
            controller.providePermission(Permission.CAMERA)
            permissionState = PermissionState.Granted
            // Permission has been granted successfully.
        } catch (deniedAlways: DeniedAlwaysException) {
            // Permission is always denied.
            permissionState = PermissionState.DeniedAlways
        } catch (denied: DeniedException) {
            // Permission was denied.
            permissionState = PermissionState.Denied
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            Modifier
                .size(200.dp)
                .clip(shape = RoundedCornerShape(size = 14.dp))
                .clipToBounds()
                .border(2.dp, Color.Gray, RoundedCornerShape(size = 14.dp)),
            contentAlignment = Alignment.Center
        ) {
            QrScanner(
                modifier = Modifier
                    .clipToBounds()
                    .clip(shape = RoundedCornerShape(size = 14.dp)),
                flashlightOn = false,
                openImagePicker = openImagePicker,
                onCompletion = {
                    qrCodeURL = it
                },
                imagePickerHandler = {
                    openImagePicker = it
                },
                cameraLens = CameraLens.Back,
                onFailure = {}

            )
        }

        if (qrCodeURL.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 14.dp)
                    .padding(bottom = 22.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = qrCodeURL,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .weight(1f),
                    fontSize = 12.sp,
                    color = Color.White,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

            }
        }
    }
}